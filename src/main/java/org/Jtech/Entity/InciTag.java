package org.Jtech.Entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

@Entity
@Table(name="inci_tag")
public class InciTag extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="tag_id")
    private Long tagId;

    @Column(name="tag_name",unique = true,length = 255)
    @NotBlank
    private String tagName;

    @OneToMany(mappedBy = "inciTag")
    private Set<InciTagMapping> inciTags;

    public Long getId() {
        return tagId;
    }

    public void setId(Long tagId) {
        tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public Set<InciTagMapping> getInciTags() {
        return inciTags;
    }

    public void setInciTags(Set<InciTagMapping> inciTags) {
        this.inciTags = inciTags;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
