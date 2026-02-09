package com.ecommerce.notification.dto;

import java.time.LocalDateTime;

/**
 * DTO za WebSocket notifikacije koje se šalju browser-u
 */
public class WebSocketNotificationDTO {

    private String type;  // EMAIL, SMS, ORDER_CREATED
    private String title;
    private String message;
    private Long orderId;
    private Long userId;
    private String productName;
    private Double totalAmount;
    private LocalDateTime timestamp;

    // Constructors
    public WebSocketNotificationDTO() {
        this.timestamp = LocalDateTime.now();
    }

    public WebSocketNotificationDTO(String type, String title, String message) {
        this();
        this.type = type;
        this.title = title;
        this.message = message;
    }

    // Getters and Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
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

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
