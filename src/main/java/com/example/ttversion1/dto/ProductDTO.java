package com.example.ttversion1.dto;


import lombok.Data;

import java.util.List;

@Data
public class ProductDTO {
    private String productname;
    private double price;
    private String avatarproduct;
    private String title;
    private int discount;
    private int quantity;
    private List<ProductImageDTO> productImageDTOS;
    private List<ProductReviewDTO> productReviewDTOS;
    private List<CartItemDTO> cartItemDTOS;
    private List<OrderDetailDTO> orderDetailDTOS;
    private ProductTypeDTO productTypeDTO;

}
