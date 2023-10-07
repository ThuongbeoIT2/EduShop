package com.example.ttversion1.service;


import com.example.ttversion1.dto.ProductDTO;
import com.example.ttversion1.entity.Product;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IProductService {
    List<Product> getAll();
    List<ProductDTO> GetProductByProductType(@Param("name") String name);
    Optional<ProductDTO> findProductbyProductname(String productname);
    List<ProductDTO> GetProductbyStatus(@Param("status") int status);
    void ChangeStatus(String productname, int status);
    void Save(Product product);
    void Insert(ProductDTO productDTO);
    void Update(ProductDTO productDTO);
    void Delete(@Param("productname") String productname);
}
