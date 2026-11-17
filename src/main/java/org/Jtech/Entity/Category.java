package org.Jtech.Entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.Jtech.Constant.ConcernArea;
import org.Jtech.Constant.ContactType;

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

    // SKIN or HAIR. Decides which match bar and which concerns scoring uses.
    @Enumerated(EnumType.STRING)
    @Column(name = "area", nullable = false, columnDefinition = "varchar(8) not null default 'SKIN'")
    private ConcernArea area = ConcernArea.SKIN;

    // LEAVE_ON or RINSE_OFF. Rinse-off products count less exposure.
    @Enumerated(EnumType.STRING)
    @Column(name = "contact_type", nullable = false, columnDefinition = "varchar(16) not null default 'LEAVE_ON'")
    private ContactType contactType = ContactType.LEAVE_ON;

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

    public ConcernArea getArea() {
        return area;
    }

    public void setArea(ConcernArea area) {
        this.area = area;
    }

    public ContactType getContactType() {
        return contactType;
    }

    public void setContactType(ContactType contactType) {
        this.contactType = contactType;
    }
}
