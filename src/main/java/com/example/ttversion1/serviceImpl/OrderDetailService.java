package com.example.ttversion1.serviceImpl;


import com.example.ttversion1.dto.OrderDetailDTO;
import com.example.ttversion1.entity.*;
import com.example.ttversion1.login.entity.Account;
import com.example.ttversion1.login.repository.AccountRepo;
import com.example.ttversion1.repository.*;
import com.example.ttversion1.service.IOrderDetailService;
import com.example.ttversion1.shareds.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class OrderDetailService implements IOrderDetailService {
    @Autowired
    private PaymentRepo paymentRepo;
    @Autowired
    private OrderStatusRepo orderStatusRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private OrderdetailRepo orderdetailRepo;
    @Autowired
    private CartItemRepo cartItemRepo;
    @Autowired
    private AccountRepo accountRepo;
    @Override
    public void Insert(OrderDetailDTO orderDetailDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account account = accountRepo.findByUsername(auth.getName()).get();
        String email = account.getUser().getEmail();
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setCreatedAt(Constants.getCurrentDay());
        orderDetail.setUpdatedAt(Constants.getCurrentDay());
        orderDetail.setQuantity(orderDetailDTO.getQuantity());
        Payment payment= paymentRepo.GetByMethod(orderDetailDTO.getPaymentmethod()).get();
        orderDetail.setPayment(payment);
        OrderStatus orderStatus = orderStatusRepo.findById(1).get();
        orderDetail.setOrderstatus(orderStatus);
        Product product= productRepo.findProductByName(orderDetailDTO.getProductname().trim()).get();
        orderDetail.setProduct(product);
        orderDetail.setPricetotal(priceTotal(orderDetailDTO));
        Order order = orderRepo.GetOrderTblByUser(email.trim()).get();
        orderDetail.setOrder(order);
        orderdetailRepo.save(orderDetail);
        CartItem cartItem = cartItemRepo.GetCartItemByProductnameEmail(orderDetailDTO.getProductname().trim(),email.trim()).get();
        cartItemRepo.delete(cartItem);
        product.setQuantity(product.getQuantity()-orderDetailDTO.getQuantity());
        productRepo.save(product);
    }

    @Override
    public void Update(OrderDetailDTO orderDetailDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account account = accountRepo.findByUsername(auth.getName()).get();
        String email = account.getUser().getEmail();
        OrderDetail orderDetail = orderdetailRepo.GetDetailByUserProduct(email,orderDetailDTO.getProductname(),orderDetailDTO.getPaymentmethod()).get();
        orderDetail.setUpdatedAt(Constants.getCurrentDay());
        orderDetail.setQuantity(orderDetailDTO.getQuantity());
        orderdetailRepo.save(orderDetail);
    }

    @Override
    public List<OrderDetailDTO> GetOrderDetailByUser(String email) {

        List<OrderDetailDTO> RS = orderdetailRepo.GetDetailByUser(email.trim()).stream().map(
                orderDetail -> {
                    OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
                    orderDetailDTO.setProductname(orderDetail.getProduct().getProductname());
                    orderDetailDTO.setEmail(orderDetail.getOrder().getUser().getEmail());
                    orderDetailDTO.setStatus(orderDetail.getOrderstatus().getStatusname());
                    orderDetailDTO.setPaymentmethod(orderDetail.getPayment().getPaymentmethod());
                    orderDetailDTO.setPricetotal(orderDetail.getPricetotal());
                    orderDetailDTO.setQuantity(orderDetail.getQuantity());
                    return orderDetailDTO;
                }
        ).collect(Collectors.toList());
        return RS;
    }

    @Override
    public List<OrderDetailDTO> GetOrderDetailByProduct(String productname) {
       List<OrderDetailDTO> RS = orderdetailRepo.GetDetailByProduct(productname.trim()).stream().map(
               orderDetail -> {
                   OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
                   orderDetailDTO.setProductname(orderDetail.getProduct().getProductname());
                   orderDetailDTO.setEmail(orderDetail.getOrder().getUser().getEmail());
                   orderDetailDTO.setStatus(orderDetail.getOrderstatus().getStatusname());
                   orderDetailDTO.setPaymentmethod(orderDetail.getPayment().getPaymentmethod());
                   orderDetailDTO.setPricetotal(orderDetail.getPricetotal());
                   orderDetailDTO.setQuantity(orderDetail.getQuantity());
                   return orderDetailDTO;
               }
       ).collect(Collectors.toList());
       return RS;
    }

    @Override
    public List<OrderDetailDTO> GetOrderDetailByPaymentMethod(String method) {
        List<OrderDetailDTO> RS = orderdetailRepo.GetDetailByPayMentMethod(method.trim()).stream().map(
                orderDetail -> {
                    OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
                    orderDetailDTO.setProductname(orderDetail.getProduct().getProductname());
                    orderDetailDTO.setEmail(orderDetail.getOrder().getUser().getEmail());
                    orderDetailDTO.setStatus(orderDetail.getOrderstatus().getStatusname());
                    orderDetailDTO.setPaymentmethod(orderDetail.getPayment().getPaymentmethod());
                    orderDetailDTO.setPricetotal(orderDetail.getPricetotal());
                    orderDetailDTO.setQuantity(orderDetail.getQuantity());
                    return orderDetailDTO;
                }
        ).collect(Collectors.toList());
        return RS;
    }

    @Override
    public List<OrderDetailDTO> GetOrderDetailByOrderStatus(int idStatus) {
        List<OrderDetailDTO> RS = orderdetailRepo.GetDetailByOrderDetail(idStatus).stream().map(
                orderDetail -> {
                    OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
                    orderDetailDTO.setProductname(orderDetail.getProduct().getProductname());
                    orderDetailDTO.setEmail(orderDetail.getOrder().getUser().getEmail());
                    orderDetailDTO.setStatus(orderDetail.getOrderstatus().getStatusname());
                    orderDetailDTO.setPaymentmethod(orderDetail.getPayment().getPaymentmethod());
                    orderDetailDTO.setPricetotal(orderDetail.getPricetotal());
                    orderDetailDTO.setQuantity(orderDetail.getQuantity());
                    return orderDetailDTO;
                }
        ).collect(Collectors.toList());
        return RS;
    }

    @Override
    public double priceTotal(OrderDetailDTO orderDetailDTO) {
        Product product= productRepo.findProductByName(orderDetailDTO.getProductname().trim()).get();
        double priceTotal= product.getPrice()*(100-product.getDiscount())* orderDetailDTO.getQuantity()/100;
        return priceTotal;
    }
}
