package org.Jtech.Model;

import org.Jtech.Entity.Category;

import java.util.List;

public class CategoryResponse {


    private int statuscode;
    private String message;
    private List<Category> category;

    public CategoryResponse(int statuscode,String message,List<Category> category){
        this.statuscode=statuscode;
        this.message=message;
        this.category=category;
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

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }
}
