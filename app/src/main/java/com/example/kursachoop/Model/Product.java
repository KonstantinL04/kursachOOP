package com.example.kursachoop.Model;

public class Product {
    private String id;
    private String name;
    private String price;
    private String image;
    private String description;
    private String brand;
    private String availability;  // Поле для количества товара

    // Конструктор
    public Product() {
    }

    public Product(String id, String name, String price, String image, String description, String brand, String availability) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.description = description;
        this.brand = brand;
        this.availability = availability;
    }


    // Геттеры и сеттеры
    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }
    
}
