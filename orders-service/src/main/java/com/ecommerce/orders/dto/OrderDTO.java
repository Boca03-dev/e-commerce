package com.ecommerce.orders.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class OrderDTO {

    private Long id;

    @NotNull(message = "User ID je obavezan")
    private Long userId;

    @NotNull(message = "Naziv proizvoda je obavezan")
    private String productName;

    @NotNull(message = "Kolicina je obavezna")
    @Positive(message = "Kolicina mora biti veca od 0")
    private Integer quantity;

    @NotNull(message = "Cena je obavezna")
    @Positive(message = "Cena mora biti veca od 0")
    private Double price;

    private String status;

    public OrderDTO() {
    }

    public OrderDTO(Long id, Long userId, String productName, Integer quantity, Double price, String status) {
        this.id = id;
        this.userId = userId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "id=" + id +
                ", userId=" + userId +
                ", productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", status='" + status + '\'' +
                '}';
    }
}
