package org.Jtech.Entity;

import jakarta.persistence.*;

@Entity
@Table(name="product_ingredient")
public class ProductIngredient extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="inci_id",nullable = false)
    private InciIngredient inciIngredient;

    @ManyToOne
    @JoinColumn(name="barcode",nullable = false)
    private Product product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InciIngredient getInciIngredient() {
        return inciIngredient;
    }

    public void setInciIngredient(InciIngredient inciIngredient) {
        this.inciIngredient = inciIngredient;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
