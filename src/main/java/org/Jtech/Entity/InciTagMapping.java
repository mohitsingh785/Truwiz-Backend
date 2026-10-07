package org.Jtech.Entity;


import jakarta.persistence.*;
/*
id (P.K)
inci_id (FK)
tag_id (FK)
UNIQUE (inci_id, tag_id)

 */

@Entity
@Table(name = "inci_tag_mapping")
public class InciTagMapping extends BaseEntity{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "inci_id", nullable = false)
    private InciIngredient inciIngredient;

    @ManyToOne
    @JoinColumn(name = "tag_id", nullable = false)
    private InciTag inciTag;


    public InciTag getInciTag() {
        return inciTag;
    }

    public void setInciTag(InciTag inciTag) {
        this.inciTag = inciTag;
    }

    public InciIngredient getInciIngredient() {
        return inciIngredient;
    }

    public void setInciIngredient(InciIngredient inciIngredient) {
        this.inciIngredient = inciIngredient;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
