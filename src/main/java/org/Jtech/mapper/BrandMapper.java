package org.Jtech.mapper;


import org.Jtech.DTO.BrandCreateRequest;
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

    public Brand toEntity(BrandCreateRequest request) {
        Brand brand = new Brand();
        brand.setBrandName(request.name());
        brand.setBrandWebsite(request.websiteUrl());
        brand.setLogoUrl(request.logoUrl());
        return brand;
    }
}
