package com.example.ttversion1.dto;


import lombok.Data;

@Data
public class CartItemDTO {
    private int quantity;
    private ProductDTO productDTO;
    private CartsDTO cartsDTO;

}
