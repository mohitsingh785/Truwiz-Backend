package org.Jtech.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "article")
public class Article {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;


    @Column(name = "title",columnDefinition = "VAR")
    @JsonProperty("title")
    private String title;

    @Column(name = "image_url", columnDefinition = "TEXT")
    @JsonProperty("image_url")
    private String imageUrl;

    @Lob
    @Column(name = "content", columnDefinition = "LONGTEXT")
    @JsonProperty("content")
    private String content;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
