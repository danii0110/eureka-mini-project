package dto;

import java.sql.Timestamp;

public class User {
    private int userId;
    private String userName;
    private String contact;
    private String email;
    private String password;
    private Timestamp createdAt;
    private String role;

    public User() {}

    public User(int userId, String userName, String contact, String email, String password, Timestamp createdAt, String role) {
        this.userId = userId;
        this.userName = userName;
        this.contact = contact;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", contact='" + contact + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", createdAt=" + createdAt +
                ", role='" + role + '\'' +
                '}';
    }
}