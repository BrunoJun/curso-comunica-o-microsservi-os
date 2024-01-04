package org.me.productapi.module.product.service;

import org.me.productapi.config.exception.SuccessResponse;
import org.me.productapi.config.exception.ValidationException;
import org.me.productapi.module.category.dto.CategoryRequest;
import org.me.productapi.module.category.dto.CategoryResponse;
import org.me.productapi.module.category.model.Category;
import org.me.productapi.module.category.repository.CategoryRepository;
import org.me.productapi.module.category.service.CategoryService;
import org.me.productapi.module.product.dto.ProductRequest;
import org.me.productapi.module.product.dto.ProductResponse;
import org.me.productapi.module.product.model.Product;
import org.me.productapi.module.product.repository.ProductRepository;
import org.me.productapi.module.supplier.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private CategoryService categoryService;

    public ProductResponse findByIdResponse(Integer id){

        validateInformedId(id);

        return ProductResponse.of(findById(id));
    };

    public List<ProductResponse> findAll(){

        return repository.findAll()
                .stream()
                .map(product -> ProductResponse.of(product))
                .collect(Collectors.toList());
    };

    public List<ProductResponse> findByName(String name){

        if (isEmpty(name)){

            throw new ValidationException("Name not informed.");
        }

        return repository.findByNameIgnoreCaseContaining(name)
                .stream()
                .map(product -> ProductResponse.of(product))
                .collect(Collectors.toList());
    };

    public List<ProductResponse> findBySupplierId(Integer supplierId){

        validateInformedId(supplierId);

        return repository.findBySupplierId(supplierId)
                .stream()
                .map(product -> ProductResponse.of(product))
                .collect(Collectors.toList());
    };

    public List<ProductResponse> findByCategoryId(Integer categoryId){

        validateInformedId(categoryId);

        return repository.findByCategoryId(categoryId)
                .stream()
                .map(product -> ProductResponse.of(product))
                .collect(Collectors.toList());
    };

    public Product findById(Integer id){

        validateInformedId(id);

        return repository
                .findById(id)
                .orElseThrow(() -> new ValidationException("There's no product for the given ID."));
    }

    public ProductResponse save(ProductRequest request){

        validateProductDataInformed(request);
        validateSupplierAndCategoryIdInformed(request);

        var supplier =  supplierService.findById(request.getSupplierId());
        var category = categoryService.findById(request.getCategoryId());

        var product = repository.save(Product.of(request, category, supplier));
        return ProductResponse.of(product);
    }

    public ProductResponse update(ProductRequest request, Integer id){

        validateProductDataInformed(request);
        validateSupplierAndCategoryIdInformed(request);

        var supplier =  supplierService.findById(request.getSupplierId());
        var category = categoryService.findById(request.getCategoryId());

        var product = Product.of(request, category, supplier);
        product.setId(id);
        repository.save(product);

        return ProductResponse.of(product);
    }

    public void validateProductDataInformed(ProductRequest request){

        if (isEmpty(request.getName())){

            throw new ValidationException("The product's name was not informed.");
        }

        if (isEmpty(request.getQuantityAvailable())){

            throw new ValidationException("The product's quantity was not informed.");
        }

        if (request.getQuantityAvailable() <= 0){

            throw new ValidationException("The quantyity should not be less or equal to zero.");
        }
    }

    public void validateSupplierAndCategoryIdInformed(ProductRequest request){


        if (isEmpty(request.getCategoryId())){

            throw new ValidationException("The category's id was not informed.");
        }

        if (isEmpty(request.getSupplierId())){

            throw new ValidationException("The supplier's id was not informed.");
        }
    }

    public SuccessResponse delete(Integer id){

        validateInformedId(id);

        repository.deleteById(id);

        return SuccessResponse.create("The product was deleted.");
    }

    public void validateInformedId(Integer id){

        if (isEmpty(id)){

            throw new ValidationException("Id not informed.");
        }
    }

    public Boolean existsByCategoryId(Integer categoryId){

        return repository.existsByCategoryId(categoryId);
    }

    public Boolean existsBySupplierId(Integer supplierId){

        return repository.existsBySupplierId(supplierId);
    }
}
