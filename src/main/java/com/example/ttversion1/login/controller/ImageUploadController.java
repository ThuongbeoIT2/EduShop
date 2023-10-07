package com.example.ttversion1.login.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class ImageUploadController {
    @GetMapping(value = "/getavatar/{photo}")
    @ResponseBody
    ResponseEntity<ByteArrayResource> getImage(@PathVariable("photo") String photo) {
        if (!photo.equals("") || photo != null){
           try {
               Path filename = Paths.get("uploadavatar",photo);
               byte[] buffer = Files.readAllBytes(filename);
               ByteArrayResource byteArrayResource= new ByteArrayResource(buffer);
               return ResponseEntity.ok().contentLength(buffer.length)
                       .contentType(MediaType.parseMediaType("image/png"))
                       .body(byteArrayResource);
           } catch (Exception e){
               System.out.println("Error");
           }
        }
        return ResponseEntity.badRequest().build();
    }
    @GetMapping(value = "/producttype/{photo}")
    @ResponseBody
    ResponseEntity<ByteArrayResource> getProductTypeImage(@PathVariable("photo") String photo) {
        if (!photo.equals("") || photo != null){
            try {
                Path filename = Paths.get("uploadproducttypeimg",photo);
                byte[] buffer = Files.readAllBytes(filename);
                ByteArrayResource byteArrayResource= new ByteArrayResource(buffer);
                return ResponseEntity.ok().contentLength(buffer.length)
                        .contentType(MediaType.parseMediaType("image/png"))
                        .body(byteArrayResource);
            } catch (Exception e){
                System.out.println("Error");
            }
        }
        return ResponseEntity.badRequest().build();
    }
    @GetMapping(value = "/product/{photo}")
    @ResponseBody
    ResponseEntity<ByteArrayResource> getProductImage(@PathVariable("photo") String photo) {
        if (!photo.equals("") || photo != null){
            try {
                Path filename = Paths.get("uploadphotoproducts",photo);
                byte[] buffer = Files.readAllBytes(filename);
                ByteArrayResource byteArrayResource= new ByteArrayResource(buffer);
                return ResponseEntity.ok().contentLength(buffer.length)
                        .contentType(MediaType.parseMediaType("image/png"))
                        .body(byteArrayResource);
            } catch (Exception e){
                System.out.println("Error");
            }
        }
        return ResponseEntity.badRequest().build();
    }
    @GetMapping(value = "/avatarproduct/{photo}")
    @ResponseBody
    ResponseEntity<ByteArrayResource> getAvatarProductImage(@PathVariable("photo") String photo) {
        if (!photo.equals("") || photo != null){
            try {
                Path filename = Paths.get("uploadavatarproduct",photo);
                byte[] buffer = Files.readAllBytes(filename);
                ByteArrayResource byteArrayResource= new ByteArrayResource(buffer);
                return ResponseEntity.ok().contentLength(buffer.length)
                        .contentType(MediaType.parseMediaType("image/png"))
                        .body(byteArrayResource);
            } catch (Exception e){
                System.out.println("Loi hinh anh");
            }
        }
        return ResponseEntity.badRequest().build();
    }
    @GetMapping(value = "/avatarnews/{photo}")
    @ResponseBody
    ResponseEntity<ByteArrayResource> getAvatarNewsImage(@PathVariable("photo") String photo) {
        if (!photo.equals("") || photo != null){
            try {
                Path filename = Paths.get("uploadavatarnews",photo);
                byte[] buffer = Files.readAllBytes(filename);
                ByteArrayResource byteArrayResource= new ByteArrayResource(buffer);
                return ResponseEntity.ok().contentLength(buffer.length)
                        .contentType(MediaType.parseMediaType("image/png"))
                        .body(byteArrayResource);
            } catch (Exception e){
                System.out.println("Loi hinh anh");
            }
        }
        return ResponseEntity.badRequest().build();
    }
    @GetMapping(value = "/avatarevents/{photo}")
    @ResponseBody
    ResponseEntity<ByteArrayResource> getAvatarEventImage(@PathVariable("photo") String photo) {
        if (!photo.equals("") || photo != null){
            try {
                Path filename = Paths.get("uploadavatarevent",photo);
                byte[] buffer = Files.readAllBytes(filename);
                ByteArrayResource byteArrayResource= new ByteArrayResource(buffer);
                return ResponseEntity.ok().contentLength(buffer.length)
                        .contentType(MediaType.parseMediaType("image/png"))
                        .body(byteArrayResource);
            } catch (Exception e){
                System.out.println("Loi hinh anh");
            }
        }
        return ResponseEntity.badRequest().build();
    }
    @GetMapping(value = "/avatarnewsimg/{photo}")
    @ResponseBody
    ResponseEntity<ByteArrayResource> NewsImage(@PathVariable("photo") String photo) {
        if (!photo.equals("") || photo != null){
            try {
                Path filename = Paths.get("uploadphotonews",photo);
                byte[] buffer = Files.readAllBytes(filename);
                ByteArrayResource byteArrayResource= new ByteArrayResource(buffer);
                return ResponseEntity.ok().contentLength(buffer.length)
                        .contentType(MediaType.parseMediaType("image/png"))
                        .body(byteArrayResource);
            } catch (Exception e){
                System.out.println("Loi hinh anh");
            }
        }
        return ResponseEntity.badRequest().build();
    }
    @GetMapping(value = "/avatareventimg/{photo}")
    @ResponseBody
    ResponseEntity<ByteArrayResource> EventsImage(@PathVariable("photo") String photo) {
        if (!photo.equals("") || photo != null){
            try {
                Path filename = Paths.get("uploadphotoevents",photo);
                byte[] buffer = Files.readAllBytes(filename);
                ByteArrayResource byteArrayResource= new ByteArrayResource(buffer);
                return ResponseEntity.ok().contentLength(buffer.length)
                        .contentType(MediaType.parseMediaType("image/png"))
                        .body(byteArrayResource);
            } catch (Exception e){
                System.out.println("Loi hinh anh");
            }
        }
        return ResponseEntity.badRequest().build();
    }
}
