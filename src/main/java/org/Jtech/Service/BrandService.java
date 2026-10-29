package org.Jtech.Service;


import org.Jtech.Constant.BrandStatus;
import org.Jtech.DTO.BrandSummaryResponse;
import org.Jtech.DTO.PageResponse;
import org.Jtech.Entity.Brand;
import org.Jtech.Exception.InvalidSortFieldException;
import org.Jtech.Repository.BrandRepository;
import org.Jtech.mapper.BrandMapper;
import org.Jtech.specification.BrandSpecifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Set;


@Service
public class BrandService {

    private static final String DEFAULT_SORT_FIELD = "name";
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(DEFAULT_SORT_FIELD);
    // Entity field names: what Hibernate needs                    // FIX 1
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

}
