package org.Jtech.Entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.Jtech.Constant.BrandStatus;

@Entity
@Table(name="brand")
public class Brand extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long brandId;

    @Column(name="brand_name",nullable = false,unique = true)
    @NotBlank
    private String brandName;

    @Column(name="brand_website",nullable = true)
    private String brandWebsite;

    @Column(name="logo_url")
    private String logoUrl;

    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private BrandStatus brandStatus;

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandWebsite() {
        return brandWebsite;
    }

    public void setBrandWebsite(String brandWebsite) {
        this.brandWebsite = brandWebsite;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public BrandStatus getBrandStatus() {
        return brandStatus;
    }

    public void setBrandStatus(BrandStatus brandStatus) {
        this.brandStatus = brandStatus;
    }
}
