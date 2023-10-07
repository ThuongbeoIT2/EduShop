package com.example.ttversion1.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductTypeDTO {
    private String producttypename;
    private String producttypeimg;
    List<ProductDTO> productDTOS;

}
