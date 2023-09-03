package com.example.kursachoop.Model;

public class Catalog {
    private String categoryId;
    private String categoryName;
    private Integer count;

    public Catalog(){}

    public Catalog(String categoryId, String categoryName, Integer count) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.count = count;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
