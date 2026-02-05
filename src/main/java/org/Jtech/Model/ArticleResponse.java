package org.Jtech.Model;

import org.Jtech.Entity.Article;
import org.Jtech.Repository.ArticleRepository;

import java.util.List;

public class ArticleResponse {

    private int statusCode;
    private String message;
    private List<Article> article;

    public ArticleResponse(int statusCode,String message,List<Article> article){

        this.statusCode=statusCode;
        this.message=message;
        this.article=article;


    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Article> getArticle() {
        return article;
    }

    public void setArticle(List<Article> article) {
        this.article = article;
    }
}
