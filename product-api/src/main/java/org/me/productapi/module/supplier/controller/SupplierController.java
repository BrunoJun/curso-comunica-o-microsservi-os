package org.me.productapi.module.supplier.controller;

import org.me.productapi.config.exception.SuccessResponse;
import org.me.productapi.module.category.dto.CategoryResponse;
import org.me.productapi.module.product.dto.ProductRequest;
import org.me.productapi.module.product.dto.ProductResponse;
import org.me.productapi.module.supplier.dto.SupplierRequest;
import org.me.productapi.module.supplier.dto.SupplierResponse;
import org.me.productapi.module.supplier.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supplier")
public class SupplierController {

    @Autowired
    private SupplierService service;

    @PostMapping
    public SupplierResponse save(@RequestBody SupplierRequest request){

        return service.save(request);
    }

    @GetMapping
    public List<SupplierResponse> findAll(){

        return service.findAll();
    }

    @GetMapping("{id}")
    public SupplierResponse findById(@PathVariable Integer id){

        return service.findByIdResponse(id);
    }

    @GetMapping("name/{name}")
    public List<SupplierResponse> findByName(@PathVariable String name){

        return service.findByName(name);
    }

    @DeleteMapping("{id}")
    public SuccessResponse delete(@PathVariable Integer id){

        return service.delete(id);
    }

    @PutMapping("{id}")
    public SupplierResponse update(@RequestBody SupplierRequest request, @PathVariable Integer id){

        return service.update(request, id);
    }
}
