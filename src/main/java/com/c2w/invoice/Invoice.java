package com.c2w.invoice;

import java.time.LocalDate;
import java.util.List;

public class Invoice {
    private List<InvoiceItem> items;
    private String customerName;
    private String invoiceNumber;
    private LocalDate date;
    private String company;
    private String model;
    private String vehicleNumber;

    public Invoice(List<InvoiceItem> items, String customerName, String invoiceNumber, 
                   LocalDate date, String company, String model, String vehicleNumber) {
        this.items = items;
        this.customerName = customerName;
        this.invoiceNumber = invoiceNumber;
        this.date = date;
        this.company = company;
        this.model = model;
        this.vehicleNumber = vehicleNumber;
    }

    // Existing getters and setters

    public List<InvoiceItem> getItems() {
        return items;
    }

    public void setItems(List<InvoiceItem> items) {
        this.items = items;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    // New getters and setters for model and vehicleNumber

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    // Existing methods

    public double getTotal() {
        return items.stream()
                    .mapToDouble(item -> item.getPrice() * item.getQuantity())
                    .sum();
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "invoiceNumber='" + invoiceNumber + '\'' +
                ", customerName='" + customerName + '\'' +
                ", date=" + date +
                ", company='" + company + '\'' +
                ", model='" + model + '\'' +
                ", vehicleNumber='" + vehicleNumber + '\'' +
                ", items=" + items +
                ", total=" + getTotal() +
                '}';
    }
}