package edu.du.mypage.model;

import lombok.Data;

@Data
public class OrderCompleteMessage {
    private Long orderId;
    private String productName;
    private Integer quantity;
    private String customerName;
    private String orderDate;
} 