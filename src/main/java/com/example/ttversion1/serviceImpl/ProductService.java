package com.example.ttversion1.serviceImpl;

import com.example.ttversion1.dto.*;
import com.example.ttversion1.entity.Product;
import com.example.ttversion1.entity.ProductType;
import com.example.ttversion1.repository.ProductRepo;
import com.example.ttversion1.repository.ProductTypeRepo;
import com.example.ttversion1.service.IProductService;
import com.example.ttversion1.shareds.Constants;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService implements IProductService {
    @Autowired
    private ProductRepo productRepo;
    private ModelMapper modelMapper= new ModelMapper();
    @Autowired
    private ProductTypeRepo productTypeRepo;

    @Override
    public List<Product> getAll() {
        return productRepo.findAll();
    }
    @Override
    public List<ProductDTO> GetProductByProductType(String name) {
        return productRepo.GetByProductType(name).stream().map(
                product -> {
                    ProductDTO productDTO= modelMapper.map(product,ProductDTO.class);
                    ProductTypeDTO productTypeDTO =modelMapper.map(product.getProducttype(),ProductTypeDTO.class);
                    List<ProductImageDTO> productImageDTOS = product.getProductimages().stream().map(
                            productImage -> {
                                ProductImageDTO productImageDTO = modelMapper.map(productImage,ProductImageDTO.class);
                                return productImageDTO;
                            }
                    ).collect(Collectors.toList());
                    List<ProductReviewDTO> productReviewDTOS= product.getProductreviews().stream().map(
                            productReview -> {
                                ProductReviewDTO productReviewDTO = modelMapper.map(productReview,ProductReviewDTO.class);
                                return productReviewDTO;
                            }
                    ).collect(Collectors.toList());
                    List<CartItemDTO> cartItemDTOS = product.getCartitems().stream().map(
                            cartItem -> {
                                CartItemDTO cartItemDTO= modelMapper.map(cartItem,CartItemDTO.class);
                                return cartItemDTO;
                            }
                    ).collect(Collectors.toList());
                    List<OrderDetailDTO> orderDetailDTOS = product.getOrderdetails().stream().map(
                            orderDetail -> {
                                OrderDetailDTO orderDetailDTO= modelMapper.map(orderDetail,OrderDetailDTO.class);
                                return orderDetailDTO;
                            }
                    ).collect(Collectors.toList());
                    productDTO.setProductImageDTOS(productImageDTOS);
                    productDTO.setProductReviewDTOS(productReviewDTOS);
                    productDTO.setCartItemDTOS(cartItemDTOS);
                    productDTO.setOrderDetailDTOS(orderDetailDTOS);
                    productDTO.setProductTypeDTO(productTypeDTO);
                    return productDTO;

                }
        ).collect(Collectors.toList());
    }

    @Override
    public Optional<ProductDTO> findProductbyProductname(String productname) {
        Optional<ProductDTO> RS = productRepo.findProductByName(productname).map(
                product -> {
                    ProductDTO productDTO= modelMapper.map(product,ProductDTO.class);
                    ProductTypeDTO productTypeDTO =modelMapper.map(product.getProducttype(),ProductTypeDTO.class);
                    List<ProductImageDTO> productImageDTOS = product.getProductimages().stream().map(
                            productImage -> {
                                ProductImageDTO productImageDTO = modelMapper.map(productImage,ProductImageDTO.class);
                                return productImageDTO;
                            }
                    ).collect(Collectors.toList());
                    List<ProductReviewDTO> productReviewDTOS= product.getProductreviews().stream().map(
                            productReview -> {
                                ProductReviewDTO productReviewDTO = modelMapper.map(productReview,ProductReviewDTO.class);
                                return productReviewDTO;
                            }
                    ).collect(Collectors.toList());
                    List<CartItemDTO> cartItemDTOS = product.getCartitems().stream().map(
                            cartItem -> {
                                CartItemDTO cartItemDTO= modelMapper.map(cartItem,CartItemDTO.class);
                                return cartItemDTO;
                            }
                    ).collect(Collectors.toList());
                    List<OrderDetailDTO> orderDetailDTOS = product.getOrderdetails().stream().map(
                            orderDetail -> {
                                OrderDetailDTO orderDetailDTO= modelMapper.map(orderDetail,OrderDetailDTO.class);
                                return orderDetailDTO;
                            }
                    ).collect(Collectors.toList());
                    productDTO.setProductImageDTOS(productImageDTOS);
                    productDTO.setProductReviewDTOS(productReviewDTOS);
                    productDTO.setCartItemDTOS(cartItemDTOS);
                    productDTO.setOrderDetailDTOS(orderDetailDTOS);
                    productDTO.setProductTypeDTO(productTypeDTO);
                    return productDTO;

                }
        );
        return RS;
    }


    @Override
    public List<ProductDTO> GetProductbyStatus(int status) {
        return productRepo.GetProductByStatus(status).stream().map(
                product -> {
                    ProductDTO productDTO= modelMapper.map(product,ProductDTO.class);
                    ProductTypeDTO productTypeDTO =modelMapper.map(product.getProducttype(),ProductTypeDTO.class);
                    List<ProductImageDTO> productImageDTOS = product.getProductimages().stream().map(
                            productImage -> {
                                ProductImageDTO productImageDTO = modelMapper.map(productImage,ProductImageDTO.class);
                                return productImageDTO;
                            }
                    ).collect(Collectors.toList());
                    List<ProductReviewDTO> productReviewDTOS= product.getProductreviews().stream().map(
                            productReview -> {
                                ProductReviewDTO productReviewDTO = modelMapper.map(productReview,ProductReviewDTO.class);
                                return productReviewDTO;
                            }
                    ).collect(Collectors.toList());
                    List<CartItemDTO> cartItemDTOS = product.getCartitems().stream().map(
                            cartItem -> {
                                CartItemDTO cartItemDTO= modelMapper.map(cartItem,CartItemDTO.class);
                                return cartItemDTO;
                            }
                    ).collect(Collectors.toList());
                    List<OrderDetailDTO> orderDetailDTOS = product.getOrderdetails().stream().map(
                            orderDetail -> {
                                OrderDetailDTO orderDetailDTO= modelMapper.map(orderDetail,OrderDetailDTO.class);
                                return orderDetailDTO;
                            }
                    ).collect(Collectors.toList());
                    productDTO.setProductImageDTOS(productImageDTOS);
                    productDTO.setProductReviewDTOS(productReviewDTOS);
                    productDTO.setCartItemDTOS(cartItemDTOS);
                    productDTO.setQuantity(product.getQuantity());
                    productDTO.setOrderDetailDTOS(orderDetailDTOS);
                    productDTO.setProductTypeDTO(productTypeDTO);
                    return productDTO;

                }
        ).collect(Collectors.toList());
    }

    @Override
    public void ChangeStatus(String productname, int status) {
            Optional<Product> product = productRepo.findProductByName(productname);
            product.get().setStatus(status);
            productRepo.save(product.get());
    }

    @Override
    public void Save(Product product) {
            productRepo.save(product);
    }

    @Override
    public void Insert(ProductDTO productDTO) {
       Product newProduct = new Product();
       newProduct.setAvatarproduct(productDTO.getAvatarproduct());
       newProduct.setDiscount(productDTO.getDiscount());
       newProduct.setPrice(productDTO.getPrice());
       newProduct.setCreatedAt(Constants.getCurrentDay());
       newProduct.setStatus(0);
       newProduct.setQuantity(productDTO.getQuantity());
       newProduct.setProductname(productDTO.getProductname());
       newProduct.setUpdatedAt(Constants.getCurrentDay());
       newProduct.setTitle(productDTO.getTitle());
        ProductType productType= productTypeRepo.findByName(productDTO.getProductTypeDTO().getProducttypename()).get();
       newProduct.setProducttype(productType);
        productRepo.save(newProduct);
    }

    @Override
    public void Update(ProductDTO productDTO) {
        Optional<Product> product = productRepo.findProductByName(productDTO.getProductname());
        product.get().setAvatarproduct(productDTO.getAvatarproduct());
        product.get().setDiscount(productDTO.getDiscount());
        product.get().setUpdatedAt(Constants.getCurrentDay());
        product.get().setTitle(productDTO.getTitle());
        productRepo.save( product.get());
    }

    @Override
    public void Delete(String productname) {
        Optional<Product> product = productRepo.findProductByName(productname);
        productRepo.delete(product.get());
    }
}
