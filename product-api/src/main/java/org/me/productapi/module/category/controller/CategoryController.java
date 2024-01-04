package org.me.productapi.module.category.controller;

import org.me.productapi.config.exception.SuccessResponse;
import org.me.productapi.module.category.dto.CategoryRequest;
import org.me.productapi.module.category.dto.CategoryResponse;
import org.me.productapi.module.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService service;

    @PostMapping
    public CategoryResponse save(@RequestBody CategoryRequest request){

        return service.save(request);
    }

    @GetMapping
    public List<CategoryResponse> findAll(){

        return service.findAll();
    }

    @GetMapping("{id}")
    public CategoryResponse findById(@PathVariable Integer id){

        return service.findByIdResponse(id);
    }

    @GetMapping("description/{description}")
    public List<CategoryResponse> findByDescription(@PathVariable String description){

        return service.findByDescription(description);
    }

    @DeleteMapping("{id}")
    public SuccessResponse delete(@PathVariable Integer id){

        return service.delete(id);
    }

    @PutMapping("{id}")
    public CategoryResponse update(@RequestBody CategoryRequest request, @PathVariable Integer id){

        return service.update(request, id);
    }
}
