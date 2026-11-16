package org.Jtech.Service;

import org.Jtech.Constant.ProductStatus;
import org.Jtech.Constant.VerificationStatus;
import org.Jtech.DTO.*;
import org.Jtech.Entity.InciIngredient;
import org.Jtech.Entity.Product;
import org.Jtech.Entity.ProductIngredient;
import org.Jtech.Exception.ProductNotActiveException;
import org.Jtech.Exception.ProductNotFoundException;
import org.Jtech.Repository.InciIngredientRepository;
import org.Jtech.Repository.ProductRepository;
import org.Jtech.mapper.ScanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class ScanService {


    private static final Logger logger = LoggerFactory.getLogger(ScanService.class);

    private final ProductService productService;
    private final InciService inciService;
    private final ScanMapper scanMapper;
    private final ScoreService scoreService;

    public ScanService(ProductService productService, InciService inciService, ScanMapper scanMapper, ScoreService scoreService) {
        this.productService = productService;
        this.inciService = inciService;
        this.scanMapper = scanMapper;
        this.scoreService = scoreService;
    }


    public ScanResponse scan(String barcode, Long userId) {
        long start = System.currentTimeMillis();
        logger.info("Scan requested: barcode={}, userId={}", barcode, userId);

        // 1–2. Product with ordered lines; hidden products look exactly like missing ones
        ProductDetailsDto product = productService.getProductDetails(barcode)
                .filter(this::isScannable)
                .orElseThrow(() -> new ProductNotFoundException(barcode));

        // 3. Distinct INCI ids of matched lines
        Set<Long> inciIds = product.ingredients().stream()
                .map(ProductIngredientLineDto::inciId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());


        // 4. If ids not empty → InciService.findSummariesByIds(ids) → Map<Long, InciSummaryDto>
        //    else use an empty map
        // 4. Display data for those ids (InciService returns an empty map for no ids)
        Map<Long, InciSummaryDto> inciById = inciService.findSummariesByIds(inciIds);
        if (inciById.size() != inciIds.size()) {
            logger.warn("INCI rows missing for barcode={}: requested={}, found={}",
                    barcode, inciIds, inciById.keySet());
        }

        // 5. ScoreService.score(product.ingredients(), userId) → ScoreResult

        ScoreResult scoreResult = scoreService.getProductScore(product, inciIds, userId);

        // 6. ScanMapper.toResponse(product, summaries /*, scoreResult later */) → ScanResponse

        // 7. Log barcode, userId, ingredient count, matched count, time taken → return
        return new ScanResponse();
    }

    private boolean isScannable(ProductDetailsDto product) {
        return product.status() != ProductStatus.INACTIVE
                && product.verificationStatus() != VerificationStatus.REJECTED;
    }
}
