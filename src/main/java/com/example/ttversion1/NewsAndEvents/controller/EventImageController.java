package com.example.ttversion1.NewsAndEvents.controller;

import com.example.ttversion1.NewsAndEvents.model.EventImage;
import com.example.ttversion1.NewsAndEvents.model.Events;
import com.example.ttversion1.NewsAndEvents.repository.EventImageRepo;
import com.example.ttversion1.NewsAndEvents.repository.EventsRepo;
import com.example.ttversion1.ResponseObject;
import com.example.ttversion1.entity.Product;
import com.example.ttversion1.entity.ProductImage;
import com.example.ttversion1.repository.ProductRepo;
import com.example.ttversion1.shareds.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@RestController
public class EventImageController {
    @Autowired
    private EventImageRepo imageRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private EventsRepo eventsRepo;
    @GetMapping("/eventImg")
    List<EventImage> GetAll(){
        return imageRepo.findAll();
    }
    @GetMapping(value = "/event/{eventID}")
    ResponseEntity<ResponseObject> GetByEventID(@PathVariable int eventID){
        List<EventImage> eventImages = imageRepo.GetImageByEventID(eventID);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Lấy thành công các EventImage  :",eventImages)
        );
    }

    @GetMapping(value = "/product/event/eventimage")
    ResponseEntity<ResponseObject> GetProductImgByProductName(@PathVariable String productname){
        Optional<Product> product = productRepo.findProductByName(productname);

        if (product.isPresent()){
            List<ProductImage> productImages= product.get().getProductimages();
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK",Constants.OK,productImages)
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("FAILED",Constants.NOT_FOUND,"")
            );
        }
    }
    @GetMapping(value = "/admin/eventimg/status-1/{eventimgID}")
    ResponseEntity<ResponseObject> ChangeStatus1(@PathVariable int eventimgID){
        Optional<EventImage> eventImage = imageRepo.findById(eventimgID);
        if (eventImage.isPresent()){
            eventImage.get().setStatus(true);
            eventImage.get().setUpdateAt(Constants.getCurrentDay());
            imageRepo.save(eventImage.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK","Cập nhật nổi bật thành công","")
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("NOT FOUND", "Không tồn tại EventImg yêu cầu ", "")
            );
        }
    }
    @GetMapping(value = "/admin/eventimg/status-0/{eventimgID}")
    ResponseEntity<ResponseObject> ChangeStatus0(@PathVariable int eventimgID){
        Optional<EventImage> eventImage = imageRepo.findById(eventimgID);
        if (eventImage.isPresent()){
            eventImage.get().setStatus(true);
            eventImage.get().setUpdateAt(Constants.getCurrentDay());
            imageRepo.save(eventImage.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK","Cập nhật tắt nổi bật","")
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("NOT FOUND", "Không tồn tại EventImg yêu cầu ", "")
            );
        }
    }
    @GetMapping(value = "/admin/eventimg/delete/{eventimgID}")
    ResponseEntity<ResponseObject> Delete(@PathVariable int eventimgID){
        Optional<EventImage> eventImage = imageRepo.findById(eventimgID);
        if (eventImage.isPresent()){

            imageRepo.delete(eventImage.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK",Constants.DELETE_SUCCESS,"")
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("NOT FOUND", "Không tồn tại EventImg yêu cầu ", "")
            );
        }
    }
    @PostMapping(value = "/admin/eventimg/insert")
    ResponseEntity<ResponseObject> insertProductImg(@RequestParam int eventID,
                                                    @RequestParam String title,
                                                    @RequestParam MultipartFile postimg){
        Optional<Events> events = eventsRepo.findById(eventID);
        if (postimg.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED",Constants.IMG_NOT_FOUND,"")
            );
        } else if (events.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED",Constants.NOT_FOUND,"")
            );
        } else {
            EventImage eventImage= new EventImage();
            eventImage.setEvents(events.get());
            events.get().setCountImg(events.get().getCountImg()+1);
            eventsRepo.save(events.get());
            eventImage.setStatus(false);
            eventImage.setCreatedAt(Constants.getCurrentDay());
            eventImage.setUpdateAt(Constants.getCurrentDay());
            eventImage.setTitle(title);
            Path path = Paths.get("uploadphotoevents/");
            try{

                InputStream inputStream= postimg.getInputStream();
                Files.copy(inputStream,path.resolve(events.get().getEventtitle().trim().toLowerCase()+events.get().getCountImg()+postimg.getOriginalFilename()),
                        StandardCopyOption.REPLACE_EXISTING);
                eventImage.setPostImg(events.get().getEventtitle().trim().toLowerCase()+events.get().getCountImg()+postimg.getOriginalFilename().toLowerCase());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            imageRepo.save(eventImage);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", Constants.INSERT_SUCCESS,"")
            );
        }
    }
}
