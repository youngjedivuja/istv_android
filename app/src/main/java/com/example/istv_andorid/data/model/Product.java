package com.example.istv_andorid.data.model;

public class Product {
    private Integer id;
    private String productCode;
    private String fullName;
    private String countryOfOrigin;
    private String storageQuantity;
    private String price;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public String getStorageQuantity() {
        return storageQuantity;
    }

    public void setStorageQuantity(String storageQuantity) {
        this.storageQuantity = storageQuantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
