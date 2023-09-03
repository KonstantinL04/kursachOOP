package com.example.kursachoop.Model;

public class Brands {
    private String brandId;
    private String brandName;
    private String brandImage;

    public Brands(){}

    public Brands(String brandId, String brandName, String brandImage) {
        this.brandId = brandId;
        this.brandName = brandName;
        this.brandImage = brandImage;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandImage() {
        return brandImage;
    }

    public void setBrandImage(String brandImage) {
        this.brandImage = brandImage;
    }
}
