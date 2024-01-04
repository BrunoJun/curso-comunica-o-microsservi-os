package org.me.productapi.module.supplier.service;

import org.me.productapi.config.exception.SuccessResponse;
import org.me.productapi.config.exception.ValidationException;
import org.me.productapi.module.category.dto.CategoryResponse;
import org.me.productapi.module.product.service.ProductService;
import org.me.productapi.module.supplier.dto.SupplierRequest;
import org.me.productapi.module.supplier.dto.SupplierResponse;
import org.me.productapi.module.supplier.model.Supplier;
import org.me.productapi.module.supplier.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ProductService productService;

    public SupplierResponse findByIdResponse(Integer id){

        return SupplierResponse.of(findById(id));
    };

    public List<SupplierResponse> findAll(){

        return supplierRepository.findAll()
                .stream()
                .map(supplier -> SupplierResponse.of(supplier))
                .collect(Collectors.toList());
    };

    public List<SupplierResponse> findByName(String name){

        if (isEmpty(name)){

            throw new ValidationException("Name not informed.");
        }

        return supplierRepository.findByNameIgnoreCaseContaining(name)
                .stream()
                .map(supplier -> SupplierResponse.of(supplier))
                .collect(Collectors.toList());
    };

    public Supplier findById(Integer id){

        validateInformedId(id);

        return supplierRepository
                .findById(id)
                .orElseThrow(() -> new ValidationException("There's no supplier for the given ID."));
    }

    public SupplierResponse save(SupplierRequest request){

        validateSupplierNameInformed(request);
        var supplier = supplierRepository.save(Supplier.of(request));
        return SupplierResponse.of(supplier);
    }

    public SupplierResponse update(SupplierRequest request, Integer id){

        validateSupplierNameInformed(request);
        var supplier = Supplier.of(request);
        supplier.setId(id);
        supplierRepository.save(supplier);
        return SupplierResponse.of(supplier);
    }

    public void validateSupplierNameInformed(SupplierRequest request) {

        if (isEmpty(request.getName())) {

            throw new ValidationException("The category name was not informed");
        }
    }

    public SuccessResponse delete(Integer id){

        validateInformedId(id);

        if (productService.existsBySupplierId(id)){

            throw new ValidationException("You cannot delete this supplier.");
        }

        supplierRepository.deleteById(id);

        return SuccessResponse.create("The supplier was deleted.");
    }

    public void validateInformedId(Integer id){

        if (isEmpty(id)){

            throw new ValidationException("Id not informed.");
        }
    }
}
