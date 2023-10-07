package com.example.ttversion1.NewsAndEvents.controller;

import com.example.ttversion1.NewsAndEvents.model.Events;
import com.example.ttversion1.NewsAndEvents.model.Voucher;
import com.example.ttversion1.NewsAndEvents.repository.EventsRepo;
import com.example.ttversion1.NewsAndEvents.repository.VoucherRepo;
import com.example.ttversion1.ResponseObject;
import com.example.ttversion1.entity.Product;
import com.example.ttversion1.login.entity.Account;
import com.example.ttversion1.login.repository.AccountRepo;
import com.example.ttversion1.repository.ProductRepo;
import com.example.ttversion1.shareds.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class EventsController {
    @Autowired
    private EventsRepo eventsRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private VoucherRepo voucherRepo;

    @GetMapping(value = "/admin/events")
    List<Events> GetAll(){
        return eventsRepo.findAll();
    }
    @PostMapping(value = "/admin/news/insertevent")
    ResponseEntity<ResponseObject> insert(@RequestParam String eventtitle,
                                          @RequestParam String productname,
                                          @RequestParam MultipartFile avatarevent,
                                          @RequestParam int discount,
                                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate){
        Optional<Product> product = productRepo.findProductByName(productname.trim());
        Optional<Events> events = eventsRepo.GetEvents(eventtitle,productname);
        Date currentDate = new Date();
        if (startDate.before(currentDate)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("BAD REQUEST", "Ngày bắt đầu không được sớm hơn hiện tại", null)
            );
        } else  if (startDate.after(endDate)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("BAD REQUEST", "Ngày bắt đầu phải trước ngày kết thúc", null)
            );
        }else
        if (discount<0 || discount>100){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("NOT IMPLEMENTED","Discount từ 0-100 thôi","")
            );
        }else if (product.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("NOT IMPLEMENTED","Sản phầm không tồn tại",productname)
            );
        }else if (avatarevent.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("NOT IMPLEMENTED", Constants.IMG_NOT_FOUND,"")
            );
        } else if (events.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("NOT IMPLEMENTED", Constants.ALREADY_EXIST,events)
            );
        } else{
            Events newObj= new Events();
            newObj.setEventtitle(eventtitle);
            newObj.setHotflag(false); // Chưa cài nổi bật
            newObj.setStartDate(startDate);
            newObj.setDiscount(discount);
            newObj.setEndDate(endDate);
            newObj.setCreatedAt(Constants.getCurrentDay());
            newObj.setUpdateAt(Constants.getCurrentDay());
            newObj.setProduct(product.get());

            Path path = Paths.get("uploadavatarevent/");
            try{
                InputStream inputStream= avatarevent.getInputStream();
                Files.copy(inputStream,path.resolve(eventtitle.trim().toLowerCase()+avatarevent.getOriginalFilename().toLowerCase()),
                        StandardCopyOption.REPLACE_EXISTING);
                newObj.setAvatarevent(eventtitle.trim().toLowerCase()+avatarevent.getOriginalFilename().toLowerCase());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            eventsRepo.save(newObj);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", Constants.INSERT_SUCCESS,eventtitle)
            );
        }
    }
    @PutMapping(value = "/admin/news/updateevent")
    ResponseEntity<ResponseObject> insert(@RequestParam int eventId,
                                          @RequestParam MultipartFile avatarevent,
                                          @RequestParam int discount,
                                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate){

        Optional<Events> events=eventsRepo.findById(eventId);
        Date currentDate = new Date();
        if (startDate.before(currentDate)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("BAD REQUEST", "Ngày bắt đầu không được sớm hơn hiện tại", null)
            );
        } else  if (startDate.after(endDate)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("BAD REQUEST", "Ngày bắt đầu phải trước ngày kết thúc", null)
            );
        }else if (avatarevent.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("NOT IMPLEMENTED", Constants.IMG_NOT_FOUND,"")
            );
        } else if (discount<0 || discount>100){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("NOT IMPLEMENTED","Discount từ 0-100 thôi","")
            );
        }else if (events.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("NOT IMPLEMENTED", Constants.ALREADY_EXIST,events)
            );
        } else{

            events.get().setStartDate(startDate);
            events.get().setEndDate(endDate);
            events.get().setDiscount(discount);
            events.get().setUpdateAt(Constants.getCurrentDay());
            Path path = Paths.get("uploadavatarevent/");
            try{
                Path oldImagePath = path.resolve(events.get().getAvatarevent());
                if (Files.exists(oldImagePath)) {
                    Files.delete(oldImagePath); // Xóa tệp hình ảnh cũ
                }
                InputStream inputStream= avatarevent.getInputStream();
                Files.copy(inputStream,path.resolve(events.get().getEventtitle().trim().toLowerCase()+avatarevent.getOriginalFilename().toLowerCase()),
                        StandardCopyOption.REPLACE_EXISTING);
                events.get().setAvatarevent(events.get().getEventtitle().trim().toLowerCase()+avatarevent.getOriginalFilename().toLowerCase());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            eventsRepo.save(events.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", Constants.INSERT_SUCCESS,events)
            );
        }
    }
    @GetMapping("/admin/events/hotflagactive/{idevent}")
    ResponseEntity<ResponseObject> HotflagActive(@PathVariable int idevent){
        Optional<Events> events = eventsRepo.findById(idevent);
        if (events.isPresent()){
            events.get().setHotflag(true);
            events.get().setUpdateAt(Constants.getCurrentDay());
            eventsRepo.save(events.get());
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("OK", Constants.UPDATE_SUCCESS,events.get().getEventtitle())
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("NOT IMPLEMENTED", Constants.NOT_FOUND,"")
            );
        }
    }
    @GetMapping("/admin/events/hotflagdisable/{idevent}")
    ResponseEntity<ResponseObject> HotflagDisabled(@PathVariable int idevent){
        Optional<Events> events = eventsRepo.findById(idevent);
        if (events.isPresent()){
            events.get().setHotflag(false);
            events.get().setUpdateAt(Constants.getCurrentDay());
            eventsRepo.save(events.get());
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("OK", Constants.UPDATE_SUCCESS,events.get().getEventtitle())
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("NOT IMPLEMENTED", Constants.NOT_FOUND,"")
            );
        }
    }
    @GetMapping("/admin/events/delete/{idevent}")
    ResponseEntity<ResponseObject> DELETE(@PathVariable int idevent){
        Optional<Events> events = eventsRepo.findById(idevent);
        if (events.isPresent()){
            eventsRepo.delete(events.get());
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("OK", Constants.DELETE_SUCCESS,"")
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("NOT IMPLEMENTED", Constants.NOT_FOUND,"")
            );
        }
    }
    @GetMapping(value = "/events/detailevent/{eventID}")
    ResponseEntity<ResponseObject> Voucher(@PathVariable int eventID){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account account = accountRepo.findByUsername(auth.getName()).get();
        Optional<Events> events = eventsRepo.findById(eventID);
        if (events.isPresent()){
            Voucher voucher = new Voucher();
            int dem =0;
            do {
                String code =Constants.generateVoucherCode();
                Optional<Voucher> vouchercheck=voucherRepo.GetVoucherByCode(code,account.getUser().getEmail());
                if (vouchercheck.isPresent()){
                    dem++;
                }
            } while (dem==0);
            voucher.setCode(Constants.generateVoucherCode());
            voucher.setTitle(events.get().getEventtitle()+" "+ events.get().getProduct().getTitle());
            voucher.setDiscount(100); // Cái này chỉ là voucher đính kèm. Không phải giảm giá của sự kiện.Giống như được tặng kèm ý
            voucher.setUser(account.getUser());
            voucher.setCreatedAt(Constants.getCurrentDay());
            voucherRepo.save(voucher);
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("OK", Constants.DELETE_SUCCESS,events.get())
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("NOT IMPLEMENTED", Constants.NOT_FOUND,"")
            );
        }
    }
}
