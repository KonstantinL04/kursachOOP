package com.example.kursachoop.Model;

public class Cart {
    private String id;
    private String name;
    private String price;
    private String image;
    private String availableQuantity;
    private int quantity;

    public Cart() {
    }

    public Cart(String id, String name, String price, String image, String availableQuantity, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.availableQuantity = availableQuantity;
        this.quantity = quantity;
    }

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

    public String getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(String availableQuantity) {
        this.availableQuantity = availableQuantity;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
