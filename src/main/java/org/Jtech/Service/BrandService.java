package org.Jtech.Service;


import org.Jtech.Constant.BrandStatus;
import org.Jtech.DTO.BrandSummaryResponse;
import org.Jtech.DTO.PageResponse;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;



@Service
public class BrandService {
    public PageResponse<BrandSummaryResponse> getBrands(BrandStatus status, Pageable pageable) {

    }
}
