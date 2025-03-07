package dto;

import java.sql.Timestamp;

public class Sale {
    private int id;
    private int userId;
    private int phoneId;
    private String brand;
    private String model;
    private int price;
    private Timestamp saleTime;

    public Sale() {}

    public Sale(int id, int userId, int phoneId, String brand, String model, int price, Timestamp saleTime) {
        this.id = id;
        this.userId = userId;
        this.phoneId = phoneId;
        this.brand = brand;
        this.model = model;
        this.price = price;
        this.saleTime = saleTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(int phoneId) {
        this.phoneId = phoneId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Timestamp getSaleTime() {
        return saleTime;
    }

    public void setSaleTime(Timestamp saleTime) {
        this.saleTime = saleTime;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "id=" + id +
                ", userId=" + userId +
                ", phoneId=" + phoneId +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", price=" + price +
                ", saleTime=" + saleTime +
                '}';
    }
}