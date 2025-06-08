package org.Jtech.Model;

import org.Jtech.Entity.Article;
import org.Jtech.Repository.ArticleRepository;

import java.util.List;

public class ArticleResponse {

    private int statuscode;
    private String message;
    private List<Article> article;

    public ArticleResponse(int statuscode,String message,List<Article> article){

        this.statuscode=statuscode;
        this.message=message;
        this.article=article;


    }

    public int getStatuscode() {
        return statuscode;
    }

    public void setStatuscode(int statuscode) {
        this.statuscode = statuscode;
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
