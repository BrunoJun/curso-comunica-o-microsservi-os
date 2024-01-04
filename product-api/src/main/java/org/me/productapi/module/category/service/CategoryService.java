package org.me.productapi.module.category.service;

import org.me.productapi.config.exception.SuccessResponse;
import org.me.productapi.config.exception.ValidationException;
import org.me.productapi.module.category.dto.CategoryRequest;
import org.me.productapi.module.category.dto.CategoryResponse;
import org.me.productapi.module.category.model.Category;
import org.me.productapi.module.category.repository.CategoryRepository;
import org.me.productapi.module.product.service.ProductService;
import org.me.productapi.module.supplier.model.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductService productService;

    public CategoryResponse findByIdResponse(Integer id){

        validateInformedId(id);

        return CategoryResponse.of(findById(id));
    };

    public List<CategoryResponse> findAll(){

        return categoryRepository.findAll()
                .stream()
                .map(category -> CategoryResponse.of(category))
                .collect(Collectors.toList());
    };

    public List<CategoryResponse> findByDescription(String description){

        if (isEmpty(description)){

            throw new ValidationException("Description not informed.");
        }

        return categoryRepository.findByDescriptionIgnoreCaseContaining(description)
                .stream()
                .map(category -> CategoryResponse.of(category))
                .collect(Collectors.toList());
    };

    public Category findById(Integer id){

        validateInformedId(id);

        return categoryRepository
                .findById(id)
                .orElseThrow(() -> new ValidationException("There's no category for the given ID."));
    }

    public CategoryResponse save(CategoryRequest request){

        validateCategoryNameInformed(request);
        var category = categoryRepository.save(Category.of(request));
        return CategoryResponse.of(category);
    }

    public CategoryResponse update(CategoryRequest request, Integer id){

        validateCategoryNameInformed(request);
        var category = Category.of(request);
        category.setId(id);
        categoryRepository.save(category);
        return CategoryResponse.of(category);
    }

    public void validateCategoryNameInformed(CategoryRequest request){

        if (isEmpty(request.getDescription())){

            throw new ValidationException("The category description was not informed");
        }
    }

    public SuccessResponse delete(Integer id){

        validateInformedId(id);

        if (productService.existsByCategoryId(id)){

            throw new ValidationException("You cannot delete this category.");
        }

        categoryRepository.deleteById(id);

        return SuccessResponse.create("The category was deleted.");
    }

    public void validateInformedId(Integer id){

        if (isEmpty(id)){

            throw new ValidationException("Id not informed.");
        }
    }
}
