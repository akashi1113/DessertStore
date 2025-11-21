package com.csu.productservice.controller;

import com.csu.productservice.dto.CommodityResponse;
import com.csu.productservice.entity.Product;
import com.csu.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/product/")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("products/{id}")
    @ResponseBody
    public CommodityResponse<List<Product>> getProductListByCategory(@PathVariable("id") String categoryId){
        return productService.getProductListByCategory(categoryId);
    }

    @GetMapping("{id}")
    @ResponseBody
    public CommodityResponse<Product> getProduct(@PathVariable("id") String productId){
        return productService.getProduct(productId);
    }
}
