package com.example.ttversion1.NewsAndEvents.controller;

import com.example.ttversion1.NewsAndEvents.model.News;
import com.example.ttversion1.NewsAndEvents.repository.NewsRepo;
import com.example.ttversion1.ResponseObject;
import com.example.ttversion1.login.entity.Account;
import com.example.ttversion1.login.repository.AccountRepo;
import com.example.ttversion1.shareds.Constants;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;
import java.util.Optional;


@RestController
public class NewsController {
    @Autowired
    private NewsRepo newsRepo;
    @Autowired
    private AccountRepo accountRepo;
    @GetMapping(value = "/news")
    List<News> GetAll(){
        return newsRepo.findAll();
    }
    @PostMapping(value = "/admin/news/insertnews")
    ResponseEntity<ResponseObject> insert(@RequestParam String title,
                                          @RequestParam String content,
                                          @RequestParam MultipartFile avatarpost){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account account = accountRepo.findByUsername(auth.getName()).get();
        Optional<News> news = newsRepo.getByTitle(title);
        if (news.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("NOT IMPLEMENTED", Constants.ALREADY_EXIST,title)
            );
        }else if (avatarpost.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("NOT IMPLEMENTED", Constants.IMG_NOT_FOUND,"")
            );
        } else {
            News newObj= new News();
            newObj.setTitle(title);
            newObj.setContent(content);
            newObj.setHotflag(false); // Chưa cài nổi bật
            newObj.setCreatedAt(Constants.getCurrentDay());
            newObj.setUpdateAt(Constants.getCurrentDay());
            newObj.setUser(account.getUser());
            Path path = Paths.get("uploadavatarnews/");
            try{
                InputStream inputStream= avatarpost.getInputStream();
                Files.copy(inputStream,path.resolve(title.trim().toLowerCase()+avatarpost.getOriginalFilename().toLowerCase()),
                        StandardCopyOption.REPLACE_EXISTING);
                newObj.setAvatarpost(title.trim().toLowerCase()+avatarpost.getOriginalFilename().toLowerCase());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            newsRepo.save(newObj);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", Constants.INSERT_SUCCESS,title)
            );
        }
    }
    @PutMapping(value = "/admin/news/updatenews")
    ResponseEntity<ResponseObject> Update(@RequestParam String title,
                                          @RequestParam String content,
                                          @RequestParam MultipartFile avatarpost){

        Optional<News> news = newsRepo.getByTitle(title);
        if (news.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("NOT IMPLEMENTED", Constants.NOT_FOUND,news.get())
            );
        }else if (avatarpost.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("NOT IMPLEMENTED", Constants.IMG_NOT_FOUND,"")
            );
        } else {
            news.get().setContent(content);
            news.get().setUpdateAt(Constants.getCurrentDay());
            Path path = Paths.get("uploadavatarnews/");
            try{
                Path oldImagePath = path.resolve(news.get().getAvatarpost());
                if (Files.exists(oldImagePath)) {
                    Files.delete(oldImagePath); // Xóa tệp hình ảnh cũ
                }
                InputStream inputStream= avatarpost.getInputStream();
                Files.copy(inputStream,path.resolve(title.trim().toLowerCase()+avatarpost.getOriginalFilename().toLowerCase()),
                        StandardCopyOption.REPLACE_EXISTING);
                news.get().setAvatarpost(title.trim().toLowerCase()+avatarpost.getOriginalFilename().toLowerCase());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            newsRepo.save(news.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", Constants.UPDATE_SUCCESS,title)
            );
        }
    }
    @GetMapping("/admin/news/hotflagactive/{idnew}")
    ResponseEntity<ResponseObject> HotflagActive(@PathVariable int idnew){
        Optional<News> news = newsRepo.findById(idnew);
        if (news.isPresent()){
            news.get().setHotflag(true);
            news.get().setUpdateAt(Constants.getCurrentDay());
            newsRepo.save(news.get());
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("OK", Constants.UPDATE_SUCCESS,news.get().getTitle())
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("NOT IMPLEMENTED", Constants.NOT_FOUND,"")
            );
        }
    }
    @GetMapping("/admin/news/hotflagdisable/{idnew}")
    ResponseEntity<ResponseObject> HotflagDisabled(@PathVariable int idnew){
        Optional<News> news = newsRepo.findById(idnew);
        if (news.isPresent()){
            news.get().setHotflag(false);
            news.get().setUpdateAt(Constants.getCurrentDay());
            newsRepo.save(news.get());
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("OK", Constants.UPDATE_SUCCESS,news.get().getTitle())
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("NOT IMPLEMENTED", Constants.NOT_FOUND,"")
            );
        }
    }
    @GetMapping("/admin/news/delete/{idnew}")
    ResponseEntity<ResponseObject> DELETE(@PathVariable int idnew){
        Optional<News> news = newsRepo.findById(idnew);
        if (news.isPresent()){
           newsRepo.delete(news.get());
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("OK", Constants.DELETE_SUCCESS,"")
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("NOT IMPLEMENTED", Constants.NOT_FOUND,"")
            );
        }
    }
}
