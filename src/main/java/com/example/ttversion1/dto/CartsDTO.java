package com.example.ttversion1.dto;


import com.example.ttversion1.login.dto.UserDTO;
import lombok.Data;

import java.util.List;

@Data
public class CartsDTO {
    private UserDTO userDTO;
    private List<CartItemDTO> cartItemDTOS;

}
