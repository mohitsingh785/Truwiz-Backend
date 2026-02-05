package org.Jtech.Model;

import org.Jtech.Entity.Category;

import java.util.List;

public class CategoryResponse {


    private int statusCode;
    private String message;
    private List<Category> category;

    public CategoryResponse(int statusCode,String message,List<Category> category){
        this.statusCode=statusCode;
        this.message=message;
        this.category=category;
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

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }
}
