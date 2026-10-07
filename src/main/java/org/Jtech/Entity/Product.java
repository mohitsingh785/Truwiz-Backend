package org.Jtech.Entity;


import jakarta.persistence.*;
import org.Jtech.Constant.ProductStatus;

import java.util.Set;

@Entity
@Table(name="product")
public class Product extends BaseEntity{

    @Id
    private String barcode;

    @Column(name="product_name",nullable = false)
    private String productName;

    @ManyToOne
    @JoinColumn(name="brand_id",nullable = false)
    private Brand brand;

    @ManyToOne
    @JoinColumn(name="category_id",nullable = false)
    private Category category;

    @Column(name="variant")
    private String variant;

    @Column(name="description")
    private String description;

    @Column(name="image_url")
    private String imageUrl;

    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @OneToMany(mappedBy = "product")
    private Set<ProductIngredient> productIngredients;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public ProductStatus getProductStatus() {
        return status;
    }

    public void setProductStatus(ProductStatus status) {
        this.status = status;
    }

    public String getImage_url() {
        return imageUrl;
    }

    public void setImage_url(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }
}
