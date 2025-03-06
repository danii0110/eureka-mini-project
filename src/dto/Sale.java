package dto;

import java.sql.Timestamp;

public class Sale {
    private int id;
    private int userId;
    private int phoneId;
    private Timestamp saleTime;

    public Sale() {}
    public Sale(int id, int userId, int phoneId, Timestamp saleTime) {
        this.id = id;
        this.userId = userId;
        this.phoneId = phoneId;
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
                ", saleTime=" + saleTime +
                '}';
    }
}