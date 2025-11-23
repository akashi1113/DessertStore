package com.csu.productservice.controller;
import com.csu.productservice.dto.CommodityResponse;
import com.csu.productservice.entity.Category;
import com.csu.productservice.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/api/categories")
public class CategoryController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public CommodityResponse<List<Category>> getCategoryList(){
        return categoryService.getCategoryList();
    }

    @GetMapping("/{id}")
    public CommodityResponse<Category> getCategory(@PathVariable("id") String categoryId){
        return categoryService.getById(categoryId);
    }
}