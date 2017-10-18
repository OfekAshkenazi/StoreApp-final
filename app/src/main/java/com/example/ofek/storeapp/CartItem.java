package com.example.ofek.storeapp;

/**
 * Created by Ofek on 18-Oct-17.
 */

public class CartItem{
    long id;
    String title;
    double price;
    int count;

    public CartItem(String title, double price, int count) {
        this.title = title;
        this.price = price;
        this.count = count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    public double getTotalPrice(){
        return price*count;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
