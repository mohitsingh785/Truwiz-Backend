package org.Jtech.Entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.Jtech.Constant.BrandStatus;

@Entity
@Table(name="brand")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("brand_id")
    private Long brandId;

    @Column(name="brand_name",nullable = false)
    @NotBlank
    private String brandName;

    @Column(name="brand_website",nullable = true)
    private String brandWebsite;

    @Column(name="logo_url")
    private String logoUrl;

    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private BrandStatus brandStatus;

}
