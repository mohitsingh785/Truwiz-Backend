package org.Jtech.Entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.Jtech.Constant.ProductSource;
import org.Jtech.Constant.ProductStatus;
import org.Jtech.Constant.VerificationStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="product")
public class Product extends BaseEntity{

    @Id
    private String barcode;

    @Column(name="product_name",nullable = false)
    private String productName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="brand_id")
    private Brand brand;

    @Size(max = 150)
    @Column(name = "submitted_brand_name", length = 150)
    private String submittedBrandName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status", nullable = false)
    private VerificationStatus verificationStatus = VerificationStatus.PENDING;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "source", nullable = false)
    private ProductSource source;

    @Column(name = "raw_ingredient_text", columnDefinition = "TEXT")
    private String rawIngredientText;

    @ManyToOne(fetch = FetchType.LAZY)
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

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("ingredientOrder ASC")
    private List<ProductIngredient> productIngredients = new ArrayList<>();



    @AssertTrue(message = "Either brand or submittedBrandName must be provided")
    private boolean isBrandProvided() {
        return brand != null
                || (submittedBrandName != null && !submittedBrandName.isBlank());
    }
    public void addIngredient(ProductIngredient ingredient) {
        productIngredients.add(ingredient);
        ingredient.setProduct(this);
    }
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

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public VerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(VerificationStatus verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public String getSubmittedBrandName() {
        return submittedBrandName;
    }

    public void setSubmittedBrandName(String submittedBrandName) {
        this.submittedBrandName = submittedBrandName;
    }

    public ProductSource getSource() {
        return source;
    }

    public void setSource(ProductSource source) {
        this.source = source;
    }

    public String getRawIngredientText() {
        return rawIngredientText;
    }

    public void setRawIngredientText(String rawIngredientText) {
        this.rawIngredientText = rawIngredientText;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public List<ProductIngredient> getProductIngredients() {
        return productIngredients;
    }

    public void setProductIngredients(List<ProductIngredient> productIngredients) {
        this.productIngredients = productIngredients;
    }
}
