package org.Jtech.Service;


import jakarta.validation.Valid;
import org.Jtech.Constant.BrandStatus;
import org.Jtech.DTO.BrandCreateRequest;
import org.Jtech.DTO.BrandSummaryResponse;
import org.Jtech.DTO.BrandUpdateRequest;
import org.Jtech.DTO.PageResponse;
import org.Jtech.Entity.Brand;
import org.Jtech.Exception.BrandNotFoundException;
import org.Jtech.Exception.DuplicateBrandException;
import org.Jtech.Exception.InvalidSortFieldException;
import org.Jtech.Repository.BrandRepository;
import org.Jtech.mapper.BrandMapper;
import org.Jtech.specification.BrandSpecifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
import java.util.Set;


@Service
public class BrandService {

    private static final String DEFAULT_SORT_FIELD = "name";
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(DEFAULT_SORT_FIELD);
    // Entity field names: what Hibernate needs
    private static final String NAME_ENTITY_FIELD = "brandName";
    private static final String TIE_BREAKER_FIELD = "brandId";
    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private BrandMapper brandMapper;

    private static final Logger logger = LoggerFactory.getLogger(BrandService.class);
    @Transactional(readOnly = true)
    public PageResponse<BrandSummaryResponse> getBrands(BrandStatus status, Pageable pageable) {

        logger.debug("Fetching brands: page={}, size={}, sort={}, status={}",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort(), status);
        // Step 1: reject any sort field we don't allow
        validateSortFields(pageable.getSort());



        Pageable pageRequest = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                buildSort(pageable.getSort()));

        // Step 3: build the WHERE conditions
        Specification<Brand> spec = (root, query, cb) -> cb.conjunction();   // "1 = 1", no filter
        if (status != null) {
            spec = spec.and(BrandSpecifications.hasStatus(status));
        }

        // Step 4: runs the page query (LIMIT/OFFSET) and the COUNT query
        Page<Brand> brandPage = brandRepository.findAll(spec, pageRequest);

        // Step 5: convert entities to DTOs, keeping the page info, then wrap
        return PageResponse.from(
                brandPage.map(brandMapper::toBrandSummaryResponse),
                pageable.getSort());
    }

    private void validateSortFields(Sort sort) {
        for (Sort.Order order : sort) {
            if (!ALLOWED_SORT_FIELDS.contains(order.getProperty())) {
                throw new InvalidSortFieldException(order.getProperty(), ALLOWED_SORT_FIELDS);
            }
        }
    }

    private Sort buildSort(Sort requested) {
        Sort.Order nameOrder = requested.getOrderFor(DEFAULT_SORT_FIELD);
        Sort.Direction direction = (nameOrder != null) ? nameOrder.getDirection() : Sort.Direction.ASC;

        return Sort.by(direction, NAME_ENTITY_FIELD)
                .and(Sort.by(Sort.Direction.ASC, TIE_BREAKER_FIELD));
    }

    // Get brand by brand Id
    @Transactional(readOnly = true)
    public BrandSummaryResponse getBrandById(Long brandId) {

        logger.debug("Fetching brand by id={}", brandId);

        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new BrandNotFoundException(brandId));

        return brandMapper.toBrandSummaryResponse(brand);
    }

    @Transactional
    public BrandSummaryResponse createBrand(BrandCreateRequest request) {
        String brandName = request.name().trim();
        logger.info("Creating brand name={}", brandName);

        // Step 1: duplicate check, case-insensitive ("dove" == "Dove")
        if (brandRepository.existsByBrandNameIgnoreCase(brandName)) {
            throw new DuplicateBrandException(brandName);
        }

        // Step 2: build the entity with cleaned values and the default status
        Brand brand = brandMapper.toEntity(request);
        brand.setBrandName(brandName);
        brand.setBrandWebsite(blankToNull(request.websiteUrl()));
        brand.setLogoUrl(blankToNull(request.logoUrl()));
        brand.setBrandStatus(BrandStatus.ACTIVE);

        // Step 3: save; the DB unique constraint is the final guard against races
        Brand saved;
        try {
            saved = brandRepository.saveAndFlush(brand);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateBrandException(brandName);
        }

        logger.info("Brand created id={}, name={}", saved.getBrandId(), saved.getBrandName());
        return brandMapper.toBrandSummaryResponse(saved);
    }

    private String blankToNull(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }

    @Transactional
    public BrandSummaryResponse updateBrand(Long brandId, BrandUpdateRequest request) {
        String brandName = request.name().trim();
        logger.info("Updating brand id={}, name={}", brandId, brandName);

        // Step 1: the brand must exist
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new BrandNotFoundException(brandId));

        // Step 2: name must not belong to ANOTHER brand
        if (brandRepository.existsByBrandNameIgnoreCaseAndBrandIdNot(brandName, brandId)) {
            throw new DuplicateBrandException(brandName);
        }

        // Step 3: PUT = full replacement; status is not touched here
        brand.setBrandName(brandName);
        brand.setBrandWebsite(blankToNull(request.websiteUrl()));
        brand.setLogoUrl(blankToNull(request.logoUrl()));

        // Step 4: flush now so a race on the unique constraint is caught here
        Brand saved;
        try {
            saved = brandRepository.saveAndFlush(brand);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateBrandException(brandName);
        }

        logger.info("Brand updated id={}", saved.getBrandId());
        return brandMapper.toBrandSummaryResponse(saved);
    }
    @Transactional
    public BrandSummaryResponse updateBrandStatus(Long brandId, BrandStatus newStatus) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new BrandNotFoundException(brandId));

        BrandStatus oldStatus = brand.getBrandStatus();

        // Same status: nothing to do, safe to call repeatedly
        if (oldStatus == newStatus) {
            logger.debug("Brand id={} already {}", brandId, newStatus);
            return brandMapper.toBrandSummaryResponse(brand);
        }

        brand.setBrandStatus(newStatus);
        Brand saved = brandRepository.save(brand);

        logger.info("Brand id={} status changed {} -> {}", brandId, oldStatus, newStatus);
        return brandMapper.toBrandSummaryResponse(saved);
    }
}
