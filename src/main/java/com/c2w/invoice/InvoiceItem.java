package com.c2w.invoice;

public class InvoiceItem {
    private String description;
    private double price;
    private int quantity;

    // Constructor
    public InvoiceItem(String description, double price, int quantity) {
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    // Getter for description
    public String getDescription() {
        return description;
    }

    // Setter for description
    public void setDescription(String description) {
        this.description = description;
    }

    // Getter for price
    public double getPrice() {
        return price;
    }

    // Setter for price
    public void setPrice(double price) {
        this.price = price;
    }

    // Getter for quantity
    public int getQuantity() {
        return quantity;
    }

    // Setter for quantity
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Override toString() for better representation in ListView
    @Override
    public String toString() {
        return String.format("%s - rs%.2f x %d", description, price, quantity);
    }
}
