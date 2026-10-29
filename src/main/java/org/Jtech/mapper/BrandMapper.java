package org.Jtech.mapper;


import org.Jtech.DTO.BrandSummaryResponse;
import org.Jtech.Entity.Brand;
import org.springframework.stereotype.Component;

@Component
public class BrandMapper {

    public BrandSummaryResponse toBrandSummaryResponse(Brand brand){
        return new BrandSummaryResponse(
                brand.getBrandId(),
                brand.getBrandName(),
                brand.getBrandWebsite(),
                brand.getLogoUrl(),
                brand.getBrandStatus()
        );
    }
}
