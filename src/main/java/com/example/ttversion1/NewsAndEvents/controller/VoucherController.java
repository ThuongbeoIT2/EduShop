package com.example.ttversion1.NewsAndEvents.controller;


import com.example.ttversion1.NewsAndEvents.model.Voucher;
import com.example.ttversion1.NewsAndEvents.repository.VoucherRepo;
import com.example.ttversion1.ResponseObject;
import com.example.ttversion1.login.entity.Account;
import com.example.ttversion1.login.repository.AccountRepo;
import com.example.ttversion1.shareds.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;


@RestController
public class VoucherController {
    @Autowired
    private VoucherRepo voucherRepo;
    @Autowired
    private AccountRepo accountRepo;
    @GetMapping("/admin/eventsvoucher/allaccount")
    ResponseEntity<ResponseObject> VoucherEvent() {
        int discount = 20;
        List<Account> accounts = accountRepo.getAllByRoleUser();

        for (Account account : accounts) {
            int dem = 0;
            do {
                String code = Constants.generateVoucherCode();
                Optional<Voucher> vouchercheck = voucherRepo.GetVoucherByCode(code,account.getUser().getEmail());
                if (!vouchercheck.isPresent()) {
                    dem++;
                }
            } while (dem == 0);

            Voucher voucher = new Voucher();
            voucher.setCode(Constants.generateVoucherCode());
            voucher.setTitle("Nhận ngày Thường có hứng thú cho");
            voucher.setDiscount(discount);
            voucher.setUser(account.getUser());
            voucher.setCreatedAt(Constants.getCurrentDay());
            voucherRepo.save(voucher);
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Hoàn thành tác vụ", "")
        );
    }

}
