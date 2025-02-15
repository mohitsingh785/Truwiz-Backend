package org.Jtech.Entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import javax.naming.Name;

@Entity
@Table(name="category")
public class Category {

    @Id
    @Column(name = "category_id",nullable = false)
    @JsonProperty("category_id")
    private Integer categoryId;

    @Column(name="Category_name",nullable = false)
    @JsonProperty("Category_name")
    private String categoryName;


    @Column(name="Category_img",nullable = false)
    @JsonProperty("Category_img")
    private String categoryImg;

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
