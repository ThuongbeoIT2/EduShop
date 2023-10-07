package com.example.ttversion1.login.controller;

import HaNoi.QA.libPersonal.EmailMix;
import com.example.ttversion1.ResponseObject;
import com.example.ttversion1.login.dto.AccountDTO;
import com.example.ttversion1.login.dto.UserDTO;
import com.example.ttversion1.login.entity.Account;
import com.example.ttversion1.login.entity.Help;
import com.example.ttversion1.login.entity.VerificationToken;
import com.example.ttversion1.login.repository.AccountRepo;
import com.example.ttversion1.login.repository.HelpRepository;
import com.example.ttversion1.login.repository.VerificationTokenRepository;
import com.example.ttversion1.login.service.IAccountService;
import com.example.ttversion1.login.service.IUserService;
import com.example.ttversion1.login.service.IVerificationTokenService;
import com.example.ttversion1.shareds.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class AccountController {
    @Autowired
    private IAccountService accountService;
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private HelpRepository helpRepository;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
   @Autowired
   private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private IUserService userService;
    @Autowired
    private IVerificationTokenService verificationTokenService;
//    @GetMapping(value = "/account/getadmin")
//    List<AccountDTO> GetAdmin(){
//        return accountService.GetAllAdmin();
//    }
    @GetMapping(value = "/admin/account/getuser")
    List<AccountDTO> GetUser(){
        return accountService.GetAllUser();
    }
    @GetMapping(value = "/admin/account/search")
    ResponseEntity<ResponseObject> GetAccountByUsername(@RequestParam String name){
        Optional<AccountDTO> RS= accountService.GetByUsername(name.trim());
        RS.get().setPassword(null);
        if (RS.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", Constants.OK,RS.get())
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("FAILED",Constants.NOT_FOUND,"")
            );
        }
    }
    @GetMapping(value = "/admin/getuser/account")
    ResponseEntity<ResponseObject> findAccountByEmail(@RequestParam String email){
        Optional<AccountDTO> accountDTO= accountService.findAccountByEmail(email.trim());
        if (accountDTO.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK",Constants.ALREADY_EXIST,accountDTO.get().getUsername())
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("FAILED",Constants.NOT_FOUND,"")
            );
        }
    }
    @PostMapping(value = "/admin/account/insert")
    ResponseEntity<ResponseObject> insertNewAccount(@RequestParam String username,
                                                    @RequestParam(value = "avatar",required = true) MultipartFile avatar,
                                                    @RequestParam String password,
                                                    @RequestParam String matchingpassword,
                                                    @RequestParam String email,
                                                    Model model){
       if(password.trim().equals(matchingpassword.trim())){
           Optional<AccountDTO> accountDTO= accountService.findAccountByEmail(email.trim());
           Optional<UserDTO> userDTO= userService.findUserByEmail(email.trim());
           if (userDTO.isEmpty()){
               return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                       new ResponseObject("FAILED",Constants.USER_NOT_FOUND,"")
               );
           }else if (accountDTO.isPresent()){
               return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                       new ResponseObject("FAILED",Constants.ACCOUNT_EXIST,"")
               );
           } else if (avatar.isEmpty()){
               return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                       new ResponseObject("FAILED",Constants.IMG_NOT_FOUND,"")
               );
           }else{
               AccountDTO newObj= new AccountDTO();
               newObj.setUserDTO(userDTO.get());
               newObj.setUsername(username.trim());
               newObj.setPassword(password.trim());
               newObj.setMatchingPassword(matchingpassword.trim());
               Path path = Paths.get("uploadavatar/");
               try{
                   InputStream inputStream= avatar.getInputStream();
                   Files.copy(inputStream,path.resolve(username.trim().toLowerCase()+avatar.getOriginalFilename()),
                           StandardCopyOption.REPLACE_EXISTING);
                   newObj.setAvatar(username.trim().toLowerCase()+avatar.getOriginalFilename().toLowerCase());
                   model.addAttribute("NEWACCOUNT",newObj);
               } catch (IOException e) {
                   throw new RuntimeException(e);
               }
               accountService.Insert(newObj);
               Optional<AccountDTO> RS= accountService.findAccountByEmail(email);
               String token= verificationTokenRepository.findByAccount(RS.get().getUsername()).getToken();
               String recipientAddress = RS.get().getUserDTO().getEmail();
               String subject = "Xác nhận tài khoản";
               String confirmationUrl
                       =   "http://localhost:9000/DTIT2/registrationConfirm.html?token=" + token;
               String message = "Tài khoản được khởi tạo từ ShopEdu. Tên tài khoản email : " +  recipientAddress + " .Nhấp vào liên kết sau để xác nhận đăng ký tài khoản:\n" + confirmationUrl;

               EmailMix e = new EmailMix("thuong0205966@huce.edu.vn", "ztdzxxoqvmbvsfuk",0);
               e.sendContentToVer2(recipientAddress,subject,message);
               return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK",Constants.INSERT_SUCCESS,"")
                );
           }
       }else {
           return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                   new ResponseObject("FAILED","Password and matchingPassword is not valid","")
           );
       }


    }
    @GetMapping("/registrationConfirm.html")
   ResponseEntity<ResponseObject> confirmRegistration(
             @RequestParam("token") String token) {
        System.out.println("Start account verification");
        VerificationToken verificationToken = verificationTokenService.getVerificationToken(token.trim());
        Account account = verificationToken.getAccount();
        Calendar cal = Calendar.getInstance();
        if (verificationToken == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("NOT FOUND", Constants.NOT_FOUND, "")
            );
        } else if (verificationToken.getExpiryDate().getTime() <= cal.getTime().getTime()) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED", "Link dẫn đã hết hạn. Vui lòng liên hệ trợ giúp tại trang chủ", "")
            );

        } else {
            account.setEnabled(true);
            accountService.Save(account);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK","Kích hoạt thành công tài khoản của bạn", "")
            );
        }
    }
    @PostMapping(value = "/account/resettoken")
    ResponseEntity<ResponseObject> resetToken(@RequestParam(name = "email", required = true) String email) {
        Optional<AccountDTO> RSDTO= accountService.findAccountByEmail(email.trim().toLowerCase());
        System.out.println(RSDTO.get());
        if(RSDTO.isPresent()){
            Account account = accountRepo.findAccountByEmail(email.trim()).get();
            VerificationToken oldtoken = verificationTokenRepository.findByAccount(account.getUsername());
            if (oldtoken== null){
                String token = UUID.randomUUID().toString();
                VerificationToken verificationToken = new VerificationToken(token, account);
                verificationTokenRepository.save(verificationToken);
                String recipientAddress = account.getUser().getEmail();
                String subject = "Xác nhận tài khoản";
                String confirmationUrl
                        =   "http://localhost:9000/DTIT2/registrationConfirm.html?token=" + token;
                String message = "Tài khoản được khởi tạo từ ShopEdu. Tên tài khoản email : " +  recipientAddress + " .Nhấp vào liên kết sau để xác nhận đăng ký tài khoản:\n" + confirmationUrl;

                EmailMix e = new EmailMix("thuong0205966@huce.edu.vn", "ztdzxxoqvmbvsfuk",0);
                e.sendContentToVer2(recipientAddress,subject,message);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK",Constants.SEND_EMAIL_SUCCESS,"")
                );
            }else {
                verificationTokenRepository.delete(oldtoken);
                String token = UUID.randomUUID().toString();
                VerificationToken verificationToken = new VerificationToken(token, account);
                verificationTokenRepository.save(verificationToken);
                String recipientAddress = account.getUser().getEmail();
                String subject = "Xác nhận tài khoản";
                String confirmationUrl
                        =   "http://localhost:9000/DTIT2/registrationConfirm.html?token=" + token;
                String message = "Tài khoản được khởi tạo từ ShopEdu. Cấp lại token xác nhận kích hoạt.Tên tài khoản email : " +  recipientAddress + " .Nhấp vào liên kết sau để xác nhận đăng ký tài khoản:\n" + confirmationUrl;

                EmailMix e = new EmailMix("thuong0205966@huce.edu.vn", "ztdzxxoqvmbvsfuk",0);
                e.sendContentToVer2(recipientAddress,subject,message);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK",Constants.RESEND_EMAIL_SUCCESS,"")
                );
            }

        }else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED",Constants.ACCOUNT_NOT_EXIST,"")
            );
        }

    }
    @PostMapping("/confirmresetpassword")
    ResponseEntity<ResponseObject> confirmResetPassword(@RequestParam(name = "oldpassword", required = true) String oldpassword,
                               @RequestParam(name = "password", required = true) String password,
                               @RequestParam(name = "matchingpassword",required = true) String matchingpassword
                              ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<AccountDTO> accountDTO = accountService.GetByUsername(auth.getName().trim());
        if (accountDTO.isPresent()){
            if(password.trim().equals(matchingpassword.trim())){
                Optional<Account> account = accountRepo.findByUsername(accountDTO.get().getUsername());

                if(passwordEncoder.matches(oldpassword,account.get().getPassword())){
                    accountDTO.get().setPassword(password);
                    accountDTO.get().setMatchingPassword(matchingpassword);
                    accountService.Update(accountDTO.get());
                    return ResponseEntity.status(HttpStatus.OK).body(
                            new ResponseObject("OK",Constants.UPDATE_SUCCESS,"")
                    );
                }else {
                    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                            new ResponseObject("FAILED","Mật khẩu cũ không trùng khớp","")
                    );
                }

            }else {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("FAILED","Password and matchingPassword in valid","")
                );
            }
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("FAILED",Constants.EXIST_EMAIL,"")
            );
        }

    }
    @PostMapping("/forgotpassword")
    ResponseEntity<ResponseObject> confirmForgotPassword(@RequestParam String email) {
        Optional<AccountDTO> accountDTO = accountService.findAccountByEmail(email.trim());
        Optional<Help> help = helpRepository.findByEmail(email);
        if (accountDTO.isPresent()){
            if (help.isEmpty()){
                String otp=Constants.generateTempPwd(6);
                String recipientAddress = email.trim();
                String subject = "Xác nhận cấp lại mật khẩu";

                String message = "Thông báo được tạo từ ShopEdu. Tên tài khoản email : " +  recipientAddress + " .Mã OTP xác thực lấy lại mật khẩu của bạn là:\n" + otp;

                EmailMix e = new EmailMix("thuong0205966@huce.edu.vn", "ztdzxxoqvmbvsfuk",0);
                e.sendContentToVer2(recipientAddress,subject,message);
                Account account = accountRepo.findAccountByEmail(email.trim()).get();
                Help newhelp= new Help(otp,account,email.trim());
                helpRepository.save(newhelp);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK",Constants.SEND_EMAIL_SUCCESS,"")
                );
            }else {
                helpRepository.delete(help.get());
                String otp=Constants.generateTempPwd(6);
                String recipientAddress = email.trim();
                String subject = "Xác nhận cấp lại mật khẩu";

                String message = "Thông báo được tạo từ ShopEdu. Tên tài khoản email : " +  recipientAddress + " .Mã OTP xác thực lấy lại mật khẩu của bạn là:\n" + otp;

                EmailMix e = new EmailMix("thuong0205966@huce.edu.vn", "ztdzxxoqvmbvsfuk",0);
                e.sendContentToVer2(recipientAddress,subject,message);
                Account account = accountRepo.findAccountByEmail(email.trim()).get();
                Help newhelp= new Help(otp,account,email.trim());
                helpRepository.save(newhelp);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK",Constants.SEND_EMAIL_SUCCESS,"")
                );
            }


        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("FAILED",Constants.EXIST_EMAIL,"")
            );
        }
    }
    @PostMapping("/account/confirmotp")
    ResponseEntity<ResponseObject> confirmotp(@RequestParam String email,
                                              @RequestParam String OTP,
                                              @RequestParam String newpass){
        Optional<Help> help = helpRepository.findByEmail(email.trim());
        Calendar cal = Calendar.getInstance();
        if (help.isPresent()){
            if (help.get().getExpiryDate().getTime() <= cal.getTime().getTime()){

                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("FAILED","Mã OTP của bạn đã quá hạn","")
                );
            }else {
                if (help.get().getOtp().equals(OTP.trim())){
                    help.get().getAccount().setPassword(passwordEncoder.encode(newpass));
                    accountRepo.save(help.get().getAccount());
                    helpRepository.delete(help.get());
                    return ResponseEntity.status(HttpStatus.OK).body(
                            new ResponseObject("OK","Đặt lại mật khẩu thành công","")
                    );
                }else {
                    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                            new ResponseObject("FAILED","Nhập mã OTP không đúng. Vui lòng kiểm tra lại","")
                    );
                }
            }
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("FAILED",Constants.NOT_FOUND,"")
            );
        }
    }
    @GetMapping(value = "/admin/account/delete")
    ResponseEntity<ResponseObject> delete(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<Account> account = accountRepo.findByUsername(auth.getName().trim());
        if(account.isPresent()){
            VerificationToken verificationToken= verificationTokenRepository.findByAccount(account.get().getUsername());
            verificationTokenRepository.delete(verificationToken);
            accountService.Delete(auth.getName().trim());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK",Constants.DELETE_SUCCESS,"")
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("FAILED",Constants.NOT_FOUND,"")
            );
        }
    }

}
