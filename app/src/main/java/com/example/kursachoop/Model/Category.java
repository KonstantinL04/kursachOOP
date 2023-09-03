package com.example.kursachoop.Model;

public class Category {
    private String categoryId;
    private String brandId;
    private String nameCategory;
    private String image;

    public Category() {}

    public Category(String categoryId, String brandId, String nameCategory, String image) {
        this.categoryId = categoryId;
        this.brandId = brandId;
        this.nameCategory = nameCategory;
        this.image = image;
    }

    public Category(String newCategoryId, String nameCategory) {

    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public String getImageCategory() {
        return image;
    }

    public void setImageCategory(String image) {
        this.image = image;
    }
}