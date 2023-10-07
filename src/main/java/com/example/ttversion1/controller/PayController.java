package com.example.ttversion1.controller;

import com.example.ttversion1.NewsAndEvents.model.Voucher;
import com.example.ttversion1.NewsAndEvents.repository.VoucherRepo;
import com.example.ttversion1.ResponseObject;
import com.example.ttversion1.entity.OrderDetail;
import com.example.ttversion1.entity.OrderStatus;
import com.example.ttversion1.entity.Pay;
import com.example.ttversion1.login.entity.Account;
import com.example.ttversion1.login.repository.AccountRepo;
import com.example.ttversion1.repository.OrderStatusRepo;
import com.example.ttversion1.repository.OrderdetailRepo;
import com.example.ttversion1.repository.PayRepo;
import com.example.ttversion1.shareds.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class PayController {
    @Autowired
    private PayRepo payRepo;
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private VoucherRepo voucherRepo;
    @Autowired
    private OrderdetailRepo orderdetailRepo;
    @Autowired
    private OrderStatusRepo orderStatusRepo;


    @GetMapping(value = "/pay/getallbyUser")
    ResponseEntity<ResponseObject> PayLog(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account account = accountRepo.findByUsername(auth.getName()).get();
        String email = account.getUser().getEmail();
        List<Pay> pays = payRepo.PayLogByUser(email);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Lịch sử thanh toán",pays)
        );
    }
    @PostMapping(value = "/pay/orderdetail")
    ResponseEntity<ResponseObject> PayOrderDetail(@RequestParam int orderdetailID,
                                                  @RequestParam(required = false) String VoucherCode){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account account = accountRepo.findByUsername(auth.getName()).get();
        String email = account.getUser().getEmail();
        Optional<OrderDetail> orderDetail = orderdetailRepo.findById(orderdetailID);
        Optional<Voucher> voucher = voucherRepo.GetVoucherByCode(VoucherCode,email);
        if (orderDetail.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED","Chưa tìm thấy đơn hàng","")
            );
        }else {
            if (voucher.isPresent()){
                Pay pay = new Pay();
                List<OrderDetail> list = new ArrayList<>();
                list.add(orderDetail.get());
                pay.setOrderDetails(list);
                pay.setAccount(account);
                pay.setVoucher(voucher.get());
                pay.setCreatedAt(Constants.getCurrentDay());
                double Originalprice=orderDetail.get().getPricetotal()*(100-voucher.get().getDiscount())/100;
                pay.setOriginalprice(Originalprice);
                payRepo.save(pay);
                OrderStatus orderStatus =orderStatusRepo.findById(2).get();
                orderDetail.get().setOrderstatus(orderStatus);
                orderdetailRepo.save(orderDetail.get());
                orderDetail.get().setPay(pay);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK","Thanh Toán Thành Công",pay)
                );
            }
            else {
                    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                            new ResponseObject("FAILED"," Mã giảm giá của bạn không hợp lệ","")
                    );
                }

        }
    }
    @PostMapping(value = "/payall/orderdetail")
    ResponseEntity<ResponseObject> PayAllOrderDetail(@RequestParam(required = false) String VoucherCode){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account account = accountRepo.findByUsername(auth.getName()).get();
        String email = account.getUser().getEmail();
        List<OrderDetail> orderDetails= orderdetailRepo.GetPayDetailByUser(email);
        Optional<Voucher> voucher = voucherRepo.GetVoucherByCode(VoucherCode,email);
        if (orderDetails.size()==0){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED","Các đơn hàng của bạn đã thanh toán toàn bộ hoặc không tồn tại","")
            );
        }else {
            if (voucher.isPresent()){
                Pay pay = new Pay();
                pay.setOrderDetails(orderDetails);
                pay.setAccount(account);
                pay.setVoucher(voucher.get());
                pay.setCreatedAt(Constants.getCurrentDay());
                double Originalprice=0;
                for (int i=0;i<orderDetails.size();i++){
                    Originalprice+= orderDetails.get(i).getPricetotal();
                    OrderStatus orderStatus =orderStatusRepo.findById(2).get();
                    orderDetails.get(i).setOrderstatus(orderStatus);
                    orderdetailRepo.save(orderDetails.get(i));
                }
                pay.setOriginalprice(Originalprice*(100-voucher.get().getDiscount())/100);
                payRepo.save(pay);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK","Thanh Toán Thành Công",pay)
                );
            }else {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("FAILED"," Mã giảm giá của bạn không hợp lệ","")
                );
            }
        }
    }
}
