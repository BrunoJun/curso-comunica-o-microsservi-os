package org.me.productapi.module.supplier.repository;

import org.me.productapi.module.category.model.Category;
import org.me.productapi.module.supplier.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {

    List<Supplier> findByNameIgnoreCaseContaining(String name);
}
