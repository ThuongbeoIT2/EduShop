package com.example.ttversion1.controller;

import com.example.ttversion1.ResponseObject;
import com.example.ttversion1.login.configuration.VnpayConfig;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
@RequestMapping(value = "/api/vnpay")

public class VnpayPaymentController {

    @PostMapping("/make")
    public ResponseEntity<ResponseObject> createPayment( HttpServletRequest request, @RequestParam(name = "vnp_OrderInfo") String vnp_OrderInfo,
                                                        @RequestParam(name = "vnp_OrderType") String ordertype, @RequestParam(name = "vnp_Amount") Integer amount,
                                                        @RequestParam(name = "vnp_Locale") String language, @RequestParam(name = "vnp_BankCode", defaultValue = "") String bankcode,
                                                        @RequestParam(name = "vnp_TxnRef") Integer invoice_id) {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_TxnRef = Integer.toString(invoice_id);
        String vnp_IpAddr = VnpayConfig.getIpAddress(request);
        String vnp_TmnCode = VnpayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100));
        vnp_Params.put("vnp_CurrCode", "VND");
        if (bankcode != null && bankcode.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bankcode);
        }
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", ordertype);

        if (language != null && !language.isEmpty()) {
            vnp_Params.put("vnp_Locale", language);
        } else {
            vnp_Params.put("vnp_Locale", "vn");
        }
        vnp_Params.put("vnp_ReturnUrl", VnpayConfig.vnp_Returnurl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Date dt = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = formatter.format(dt);
        String vnp_CreateDate = dateString;
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        // Build data to hash and querystring
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                try {
                // Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, "UTF-8"));
                // Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        String queryUrl = query.toString();
        String vnp_SecureHash = VnpayConfig.hmacSHA512(VnpayConfig.vnp_HashSecret, hashData.toString());
        System.out.println("HashData=" + hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VnpayConfig.vnp_Url + "?" + queryUrl;
        vnp_Params.put("redirect_url", paymentUrl);



        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Thanh toán ",vnp_Params)
        );
    }


    @GetMapping(value = "/result")
    // [GET] localhost:8080/api/vnpay/result
    public ModelAndView completePayment(HttpSession httpSession, HttpServletRequest request,
                                                                           @RequestParam(name = "vnp_OrderInfo") String vnp_OrderInfo,
                                                                           @RequestParam(name = "vnp_Amount") Integer vnp_Amount,
                                                                           @RequestParam(name = "vnp_BankCode", defaultValue = "") String vnp_BankCode,
                                                                           @RequestParam(name = "vnp_BankTranNo") String vnp_BankTranNo,
                                                                           @RequestParam(name = "vnp_CardType") String vnp_CardType,
                                                                           @RequestParam(name = "vnp_PayDate") String vnp_PayDate,
                                                                           @RequestParam(name = "vnp_ResponseCode") String vnp_ResponseCode,
                                                                           @RequestParam(name = "vnp_TransactionNo") String vnp_TransactionNo,
                                                                           @RequestParam(name = "vnp_TxnRef") String vnp_TxnRef
    ) {
        String year = vnp_PayDate.substring(0, 4);
        String month = vnp_PayDate.substring(4, 6);
        String date = vnp_PayDate.substring(6, 8);
        String hour = vnp_PayDate.substring(8, 10);
        String minutes = vnp_PayDate.substring(10, 12);
        String second = vnp_PayDate.substring(12, 14);

        String timePay = date + "-" + month + "-" + year + " " + hour + ":" + minutes + ":" + second;

        String redirectUrl = "http://localhost:3000/checkout/successful?data=";
        String params = "success=" + (vnp_ResponseCode.equals("00") ? "true" : "false")
                + "&OrderInfo=" + vnp_OrderInfo
                + "&Amount=" + vnp_Amount.toString()
                + "&BankCode=" + vnp_BankCode
                + "&BankTranNo=" + vnp_BankTranNo
                + "&CardType=" + vnp_CardType
                + "&PayDate=" + timePay
                + "&PaymentMethod=VNPAY"
                + "&VnPay_ResponseCode=" + vnp_ResponseCode
                + "&PaymentID=" + vnp_TransactionNo
                + "&OrderID=" + vnp_TxnRef;

        params = Base64.getEncoder().encodeToString(params.getBytes());

        return new ModelAndView("redirect:" + redirectUrl + params);
    }
}