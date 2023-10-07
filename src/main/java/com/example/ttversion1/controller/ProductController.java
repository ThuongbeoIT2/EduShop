package com.example.ttversion1.controller;

import com.example.ttversion1.ResponseObject;
import com.example.ttversion1.dto.ProductDTO;
import com.example.ttversion1.dto.ProductTypeDTO;
import com.example.ttversion1.entity.Product;
import com.example.ttversion1.entity.ProductType;
import com.example.ttversion1.repository.ProductRepo;
import com.example.ttversion1.repository.ProductTypeRepo;
import com.example.ttversion1.service.IProductService;
import com.example.ttversion1.shareds.Constants;
import org.modelmapper.ModelMapper;
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
public class ProductController {
    @Autowired
    private IProductService productService;
    @Autowired
    private ProductTypeRepo productTypeRepo;
    private ModelMapper modelMapper= new ModelMapper();
    @Autowired
    private ProductRepo productRepo;
    @GetMapping(value = "/admin/product")
    List<Product> GetAll(){
        return productService.getAll();
    }
    @GetMapping("/product/detailproduct/{productname}")
    ResponseEntity<ResponseObject> GetproductByProductname(@PathVariable String productname){
        Optional<ProductDTO> product = productService.findProductbyProductname(productname);
        if (product.isPresent()){
            Product productdetail = productRepo.findProductByName(productname).get();
            productdetail.setView(productdetail.getView()+1);
            productRepo.save(productdetail);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", Constants.OK,product.get())
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("NOT FOUND",Constants.NOT_FOUND,"")
            );
        }
    }
    @GetMapping(value = "/product/search/{name}")
    ResponseEntity<ResponseObject> GetProductByProductType(@PathVariable String name) {
        Optional<ProductType> productType = productTypeRepo.findByName(name);
        if (productType.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", Constants.OK, productService.GetProductByProductType(name))
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("NOT FOUND", "Không tồn tại ProductType vừa nhập", "")
            );
        }
    }
    @GetMapping(value = "/admin/product/status/{status}")
    ResponseEntity<ResponseObject> GetProductByStatus(@PathVariable int status){
        if (status==1 || status==2 || status==3 || status==0){
            //Sau tạo bang statusproduct
            List<ProductDTO> productDTOS = productService.GetProductbyStatus(status);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", Constants.OK, productDTOS)
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("NOT FOUND", "Không tồn tại Trạng thái sản phẩm yêu cầu vừa nhập", "")
            );
        }

    }
    @PostMapping(value = "/admin/product/insertproduct")
    ResponseEntity<ResponseObject> Insert(@RequestParam String productname,
                                          @RequestParam String title,
                                          @RequestParam int discount,
                                          @RequestParam double price,
                                          @RequestParam MultipartFile avartarproduct,
                                          @RequestParam String name
                                          ){
        Optional<ProductType> productType = productTypeRepo.findByName(name);
        Optional<ProductDTO> RS = productService.findProductbyProductname(productname);
        if (productType.isPresent()){
            if (RS.isPresent()){
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("FAILED",Constants.ALREADY_EXIST,"")
                );
            }else if (discount<0 || discount>100)
            {
                    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                            new ResponseObject("FAILED","Giam gia tu 0-100 % thoi.Nhap lai di","")
                    );
            }else if (price<=0)
            {
                        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                                new ResponseObject("FAILED"," Giá sản phẩm phải lớn hơn 0 mà .Nhap lai di","")
                        );
            }else if (avartarproduct.isEmpty())
            {
                            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                                    new ResponseObject("FAILED","Thiếu AvatarProduct rồi .","")
                            );
            }else {
                ProductDTO productDTO = new ProductDTO();
                productDTO.setPrice(price);
                ProductTypeDTO productTypeDTO = modelMapper.map(productType.get(),ProductTypeDTO.class);
                productDTO.setProductTypeDTO(productTypeDTO);
                productDTO.setDiscount(discount);
                productDTO.setTitle(title);
                productDTO.setProductname(productname);
                Path path = Paths.get("uploadavatarproduct/");
                try{
                    InputStream inputStream= avartarproduct.getInputStream();
                    Files.copy(inputStream,path.resolve(productname.trim().toLowerCase()+avartarproduct.getOriginalFilename().toLowerCase()),
                            StandardCopyOption.REPLACE_EXISTING);
                    productDTO.setAvatarproduct(productname.trim().toLowerCase()+avartarproduct.getOriginalFilename().toLowerCase());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                productService.Insert(productDTO);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK",Constants.INSERT_SUCCESS,"")
                );
            }

        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("NOT FOUND", "Không tồn tại ProductType yêu cầu vừa nhập", "")
            );
        }
    }
    @PutMapping(value = "/admin/product/update")
    ResponseEntity<ResponseObject> Update(@RequestParam String title,
                                          @RequestParam int discount,
                                          @RequestParam(value = "avartarproduct") MultipartFile avartarproduct,
                                          @RequestParam String productname
                                          ){
        Optional<ProductDTO> productDTO = productService.findProductbyProductname(productname);
        if (productDTO.isPresent()){
           if (discount<0 && discount>100){
               return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                       new ResponseObject("FAILED","Giam gia tu 0-100 % thoi.Nhap lai di","")
               );
           } else if (avartarproduct.isEmpty()){
               return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                       new ResponseObject("FAILED","Thiếu AvatarProduct rồi .","")
               );
           }else {
               productDTO.get().setTitle(title);
               productDTO.get().setDiscount(discount);
               Path path = Paths.get("uploadavatarproduct/");
               try{
                   Path oldImagePath = path.resolve(productDTO.get().getAvatarproduct());
                   if (Files.exists(oldImagePath)) {
                       Files.delete(oldImagePath); // Xóa tệp hình ảnh cũ
                   }
                   InputStream inputStream= avartarproduct.getInputStream();
                   Files.copy(inputStream,path.resolve(productname.trim().toLowerCase()+avartarproduct.getOriginalFilename()),
                           StandardCopyOption.REPLACE_EXISTING);
                   productDTO.get().setAvatarproduct(productname.trim().toLowerCase()+avartarproduct.getOriginalFilename().toLowerCase());
               } catch (IOException e) {
                   return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                           new ResponseObject("ERROR", "Lỗi khi xử lý tệp hình ảnh.", e.getMessage())
                   );
               }
               productService.Update(productDTO.get());
               return ResponseEntity.status(HttpStatus.OK).body(
                       new ResponseObject("OK",Constants.UPDATE_SUCCESS,productDTO)
               );
           }
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("NOT FOUND", "Không tồn tại Product yêu cầu ", "")
            );
        }
    }
    @GetMapping(value = "/admin/product/delete/{productname}")
    ResponseEntity<ResponseObject> Delete(@PathVariable String productname){
        Optional<ProductDTO> productDTO = productService.findProductbyProductname(productname);
        if (productDTO.isPresent()){
            productService.Delete(productname);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK",Constants.DELETE_SUCCESS,"")
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("NOT FOUND", "Không tồn tại Product yêu cầu ", "")
            );
        }
    }
    @GetMapping(value = "/admin/product/status-1/{productname}")
    ResponseEntity<ResponseObject> ChangeStatus1(@PathVariable String productname){
            Optional<Product> product = productRepo.findProductByName(productname);
            if (product.isPresent()){
                product.get().setStatus(1);
                productRepo.save(product.get());
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK","Cập nhật mở bán thành công","")
                );
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("NOT FOUND", "Không tồn tại Product yêu cầu ", "")
                );
            }
    }
    @GetMapping(value = "/admin/product/status-2/{productname}")
    ResponseEntity<ResponseObject> ChangeStatus2(@PathVariable String productname){
        Optional<Product> product = productRepo.findProductByName(productname);
        if (product.isPresent()){
            product.get().setStatus(2);
            productRepo.save(product.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK","Cập nhật ngừng bán thành công","")
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("NOT FOUND", "Không tồn tại Product yêu cầu ", "")
            );
        }
    }
    @GetMapping(value = "/admin/product/status-3/{productname}")
    ResponseEntity<ResponseObject> ChangeStatus3(@PathVariable String productname){
        Optional<Product> product = productRepo.findProductByName(productname);
        if (product.isPresent()){
            product.get().setStatus(3);
            productRepo.save(product.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK","Cập nhật trạng thái hết hàng thành công","")
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("NOT FOUND", "Không tồn tại Product yêu cầu ", "")
            );
        }
    }

}
