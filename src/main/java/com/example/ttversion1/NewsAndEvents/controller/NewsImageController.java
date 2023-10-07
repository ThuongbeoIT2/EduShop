package com.example.ttversion1.NewsAndEvents.controller;

import com.example.ttversion1.NewsAndEvents.model.News;
import com.example.ttversion1.NewsAndEvents.model.NewsImage;
import com.example.ttversion1.NewsAndEvents.repository.NewsImageRepo;
import com.example.ttversion1.NewsAndEvents.repository.NewsRepo;
import com.example.ttversion1.ResponseObject;
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
public class NewsImageController {
    @Autowired
    private NewsImageRepo newsImageRepo;
    @Autowired
    private NewsRepo newsRepo;
    @GetMapping("/NewsImg")
    List<NewsImage> GetAll(){
        return newsImageRepo.findAll();
    }
    @GetMapping(value = "/news/{newsID}")
    ResponseEntity<ResponseObject> GetByEventID(@PathVariable int newsID){
        List<NewsImage> newsImages = newsImageRepo.GetImgByNewID(newsID);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Lấy thành công các newsImage  :",newsImages)
        );
    }

    @GetMapping(value = "/admin/newsimg/status-1/{newsimgID}")
    ResponseEntity<ResponseObject> ChangeStatus1(@PathVariable int newsimgID){
        Optional<NewsImage> newsImage = newsImageRepo.findById(newsimgID);
        if (newsImage.isPresent()){
            newsImage.get().setStatus(true);
            newsImage.get().setUpdateAt(Constants.getCurrentDay());
            newsImageRepo.save(newsImage.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK","Cập nhật nổi bật thành công","")
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("NOT FOUND", "Không tồn tại EventImg yêu cầu ", "")
            );
        }
    }
    @GetMapping(value = "/admin/newsimg/status-0/{newsimgID}")
    ResponseEntity<ResponseObject> ChangeStatus0(@PathVariable int newsimgID){
        Optional<NewsImage> newsImage = newsImageRepo.findById(newsimgID);
        if (newsImage.isPresent()){
            newsImage.get().setStatus(true);
            newsImage.get().setUpdateAt(Constants.getCurrentDay());
            newsImageRepo.save(newsImage.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK","Cập nhật tắt nổi bật","")
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("NOT FOUND", "Không tồn tại EventImg yêu cầu ", "")
            );
        }
    }
    @GetMapping(value = "/admin/newsimg/delete/{newsimgID}")
    ResponseEntity<ResponseObject> Delete(@PathVariable int newsimgID){
        Optional<NewsImage> newsImage = newsImageRepo.findById(newsimgID);
        if (newsImage.isPresent()){

            newsImageRepo.delete(newsImage.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK",Constants.DELETE_SUCCESS,"")
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("NOT FOUND", "Không tồn tại EventImg yêu cầu ", "")
            );
        }
    }
    @PostMapping(value = "/admin/newsimg/insert")
    ResponseEntity<ResponseObject> insertProductImg(@RequestParam int newsID,
                                                    @RequestParam String title,
                                                    @RequestParam MultipartFile postimg){
        Optional<News> news = newsRepo.findById(newsID);
        if (postimg.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED",Constants.IMG_NOT_FOUND,"")
            );
        } else if (news.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED",Constants.NOT_FOUND,"")
            );
        } else {
            NewsImage newsImage= new NewsImage();
            newsImage.setNews(news.get());
            news.get().setCountImg(news.get().getCountImg()+1);
            newsRepo.save(news.get());
            newsImage.setStatus(false);
            newsImage.setCreatedAt(Constants.getCurrentDay());
            newsImage.setTitle(title);
            newsImage.setUpdateAt(Constants.getCurrentDay());
            Path path = Paths.get("uploadphotonews/");
            try{

                InputStream inputStream= postimg.getInputStream();
                Files.copy(inputStream,path.resolve(news.get().getTitle().trim().toLowerCase()+news.get().getCountImg()+postimg.getOriginalFilename()),
                        StandardCopyOption.REPLACE_EXISTING);
                newsImage.setPostImg(news.get().getTitle().trim().toLowerCase()+news.get().getCountImg()+postimg.getOriginalFilename().toLowerCase());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            newsImageRepo.save(newsImage);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", Constants.INSERT_SUCCESS,"")
            );
        }
    }
}
