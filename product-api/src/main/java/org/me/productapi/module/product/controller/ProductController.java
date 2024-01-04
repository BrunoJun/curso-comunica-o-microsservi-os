package org.me.productapi.module.product.controller;

import org.me.productapi.config.exception.SuccessResponse;
import org.me.productapi.module.category.dto.CategoryRequest;
import org.me.productapi.module.category.dto.CategoryResponse;
import org.me.productapi.module.category.service.CategoryService;
import org.me.productapi.module.product.dto.ProductRequest;
import org.me.productapi.module.product.dto.ProductResponse;
import org.me.productapi.module.product.model.Product;
import org.me.productapi.module.product.service.ProductService;
import org.me.productapi.module.supplier.dto.SupplierResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService service;

    @PostMapping
    public ProductResponse save(@RequestBody ProductRequest request){

        return service.save(request);
    }

    @GetMapping
    public List<ProductResponse> findAll(){

        return service.findAll();
    }

    @GetMapping("{id}")
    public ProductResponse findById(@PathVariable Integer id){

        return service.findByIdResponse(id);
    }

    @GetMapping("supplierId/{supplierId}")
    public List<ProductResponse> findBySupplierId(@PathVariable Integer supplierId){

        return service.findBySupplierId(supplierId);
    }

    @GetMapping("categoryId/{categoryId}")
    public List<ProductResponse> findByCategoryId(@PathVariable Integer categoryId){

        return service.findByCategoryId(categoryId);
    }

    @GetMapping("name/{name}")
    public List<ProductResponse> findByName(@PathVariable String name){

        return service.findByName(name);
    }

    @DeleteMapping("{id}")
    public SuccessResponse delete(@PathVariable Integer id){

        return service.delete(id);
    }

    @PutMapping("{id}")
    public ProductResponse update(@RequestBody ProductRequest request, @PathVariable Integer id){

        return service.update(request, id);
    }
}
