package org.me.productapi.module.supplier.dto;

import lombok.Data;
import org.me.productapi.module.supplier.model.Supplier;
import org.springframework.beans.BeanUtils;

@Data
public class SupplierResponse {

    private Integer id;
    private String name;

    public static SupplierResponse of(Supplier supplier){

        var response = new SupplierResponse();
        BeanUtils.copyProperties(supplier, response);
        return response;
    }
}

