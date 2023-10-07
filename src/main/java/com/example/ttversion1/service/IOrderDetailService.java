package com.example.ttversion1.service;


import com.example.ttversion1.dto.OrderDetailDTO;

import java.util.List;


public interface IOrderDetailService {

  void Insert(OrderDetailDTO orderDetailDTO);
  void Update(OrderDetailDTO orderDetailDTO);

  List<OrderDetailDTO> GetOrderDetailByUser(String email);
  List<OrderDetailDTO> GetOrderDetailByProduct(String productname);
  List<OrderDetailDTO> GetOrderDetailByPaymentMethod(String method);
  List<OrderDetailDTO> GetOrderDetailByOrderStatus(int idStatus);
  double priceTotal(OrderDetailDTO orderDetailDTO);
}
