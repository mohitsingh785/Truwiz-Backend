package org.Jtech.Entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import javax.naming.Name;
import java.util.Set;

@Entity
@Table(name="category")
public class Category extends BaseEntity{

    @Id
    @Column(name = "category_id",nullable = false)
    @JsonProperty("category_id")
    private Integer categoryId;

    @Column(name="category_name",nullable = false)
    @JsonProperty("category_name")
    private String categoryName;


    @Column(name="category_img",nullable = false)
    @JsonProperty("category_img")
    private String categoryImg;

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    @OneToMany(mappedBy = "category")
    private Set<Product> products;

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryImg() {
        return categoryImg;
    }

    public void setCategoryImg(String categoryImg) {
        this.categoryImg = categoryImg;
    }
}
