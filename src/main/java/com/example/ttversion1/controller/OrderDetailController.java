package com.example.ttversion1.controller;

import com.example.ttversion1.ResponseObject;
import com.example.ttversion1.dto.OrderDetailDTO;
import com.example.ttversion1.entity.*;
import com.example.ttversion1.login.dto.UserDTO;
import com.example.ttversion1.login.entity.Account;
import com.example.ttversion1.login.repository.AccountRepo;
import com.example.ttversion1.login.service.IUserService;
import com.example.ttversion1.repository.*;
import com.example.ttversion1.service.IOrderDetailService;
import com.example.ttversion1.shareds.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class OrderDetailController {
    @Autowired
    private IOrderDetailService orderDetailService;
    @Autowired
    private IUserService userService;
    @Autowired
    private PaymentRepo paymentRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private OrderdetailRepo orderdetailRepo;
    @Autowired
    private OrderStatusRepo orderStatusRepo;
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private CartItemRepo cartItemRepo;
    @Autowired
    private OrderRepo orderRepo;

    @GetMapping(value = "/user/ordertbl")
    ResponseEntity<ResponseObject> GetOrderByUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account account = accountRepo.findByUsername(auth.getName()).get();
        System.out.println(account.getUser().getEmail());
        System.out.println(account);
        Order order =orderRepo.GetOrderTblByUser(account.getUser().getEmail()).get();
        System.out.println(order);
        double Actualprice=0;
        for (int i=0;i<order.getOrdersDetail().size();i++){
            if (order.getOrdersDetail().get(i).getOrderstatus().getOrderstatusID()==1){
                Actualprice+=order.getOrdersDetail().get(i).getPricetotal();
            }
        }
        System.out.println(Actualprice);
        order.setActualprice(Actualprice);
        double Originalprice=0;
        for (int i=0;i<order.getOrdersDetail().size();i++){
            if (order.getOrdersDetail().get(i).getOrderstatus().getOrderstatusID()==1){
                Originalprice+=order.getOrdersDetail().get(i).getQuantity()*order.getOrdersDetail().get(i).getProduct().getPrice();
            }

        }
        order.setOriginalprice(Originalprice);
        order.setUpdatedAt(Constants.getCurrentDay());
        orderRepo.save(order);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", Constants.OK,order)
        );
    }
    @PostMapping(value = "/user/insertorderdetail")
        ResponseEntity<ResponseObject> insert(@RequestParam  String productname,
                                          @RequestParam String method){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account account = accountRepo.findByUsername(auth.getName()).get();
        String email = account.getUser().getEmail();
        Optional<UserDTO> userDTO = userService.findUserByEmail(email);
        Optional<CartItem> cartItem = cartItemRepo.GetCartItemByProductnameEmail(productname.trim(),email);
        Optional<Product> product= productRepo.findProductByName(productname.trim());
        Optional<Payment> payment= paymentRepo.GetByMethod(method);
        Optional<OrderDetail > orderDetail= orderdetailRepo.GetDetailByUserProduct(email,productname.trim(),method);
        if (userDTO.isPresent() && cartItem.isPresent()){
             if (product.get().getStatus()!=1){
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("FAILED","Sản phẩm không được mở bán","")
                );
            } else  if (payment.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("FAILED","Không tìm thấy PaymentMethod yêu cầu","")
                );
            } else if (cartItem.get().getQuantity()< 0){
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("FAILED","Số lượng mua không được âm","")
                );
            } else if (cartItem.get().getQuantity()>= product.get().getQuantity()){
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("FAILED","Hàng trong kho không đủ yêu cầu","")
                );
            }else if (orderDetail.isPresent()){
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("FAILED","Bạn đã có 1 đơn sản phẩm tương tự. Bạn vui lòng chuyển tới trang update. redirect:/user/updateorderdetail","")
                );
            } else
            {
                OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
                orderDetailDTO.setEmail(email.trim());
                orderDetailDTO.setStatus(orderStatusRepo.findById(1).get().getStatusname());
                orderDetailDTO.setQuantity(cartItem.get().getQuantity());
                orderDetailDTO.setPaymentmethod(payment.get().getPaymentmethod());
                orderDetailDTO.setProductname(productname);
                orderDetailService.Insert(orderDetailDTO);

                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK",Constants.INSERT_SUCCESS,"") // Sau chuyển hướng qua thanh toán nếu là VNPay
                );
            }

        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("OK", Constants.USER_NOT_FOUND,"")
            );
        }
    }

    @PutMapping(value = "/user/updateorderdetail")
    ResponseEntity<ResponseObject> Update(@RequestBody OrderDetailDTO orderDetailDTO){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account account = accountRepo.findByUsername(auth.getName()).get();
        String email = account.getUser().getEmail();
        Optional<UserDTO> userDTO = userService.findUserByEmail(email);
        Optional<Product> product= productRepo.findProductByName(orderDetailDTO.getProductname());
        Optional<OrderStatus> orderStatus = orderStatusRepo.findByName(orderDetailDTO.getStatus());
        Optional<Payment> payment= paymentRepo.GetByMethod(orderDetailDTO.getPaymentmethod());
        Optional<OrderDetail > orderDetail= orderdetailRepo.GetDetailByUserProduct(email,orderDetailDTO.getProductname().trim(),orderDetailDTO.getPaymentmethod());
        if (userDTO.isPresent()){
            if (orderDetail.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("FAILED","Không tìm thấy OrderDetail yêu cầu. redirect:/user/insertorderdetail","")
                );
            } else
            if (product.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("FAILED","Không tìm thấy Product yêu cầu","")
                );
            }else if (orderStatus.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("FAILED","Không tìm thấy OrderStatus yêu cầu","")
                );
            } else if (payment.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("FAILED","Không tìm thấy PaymentMethod yêu cầu","")
                );
            } else if (orderDetailDTO.getQuantity()< 0){
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("FAILED","Số lượng mua không được âm","")
                );
            } else if (orderDetailDTO.getQuantity()>= product.get().getQuantity()){
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("FAILED","Hàng trong kho không đủ yêu cầu","")
                );
            }else {
                orderDetailService.Insert(orderDetailDTO);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK",Constants.INSERT_SUCCESS,orderDetailDTO)
                );
            }

        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("OK", Constants.USER_NOT_FOUND,"")
            );
        }
    }

    @GetMapping(value = "/orderdetail/status/{Statusid}/odID/{odID}")
    ResponseEntity<ResponseObject> Status(@PathVariable int Statusid,@PathVariable int odID){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account account = accountRepo.findByUsername(auth.getName()).get();
        String email = account.getUser().getEmail();
        Optional<OrderDetail> orderDetail= orderdetailRepo.GetDetailByUserOdID(email,odID);
        if (orderDetail.isPresent()){
            OrderStatus orderStatus= orderStatusRepo.findById(Statusid).get();
            orderDetail.get().setOrderstatus(orderStatus);
            orderdetailRepo.save(orderDetail.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK",Constants.UPDATE_SUCCESS,orderDetail.get())
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("NOT FOUND",Constants.NOT_FOUND,"")
            );
        }
    }


}
