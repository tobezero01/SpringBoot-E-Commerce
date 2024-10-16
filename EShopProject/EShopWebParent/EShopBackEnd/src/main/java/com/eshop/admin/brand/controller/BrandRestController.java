package com.eshop.admin.brand.controller;

import com.eshop.admin.brand.BrandService;
import com.eshop.admin.brand.CategoryDTO;
import com.eshop.admin.exception.BrandNotFoundException;
import com.eshop.admin.exception.BrandNotFoundRestException;
import com.eshop.common.entity.Brand;
import com.eshop.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
public class BrandRestController {
    @Autowired
    private BrandService brandService;

    @PostMapping("/brands/check_unique")
    public String checkUnique( Integer id ,String name) {
        return brandService.checkUnique(id, name);
    }

    @GetMapping("/brands/{id}/categories")
    public List<CategoryDTO> listCategoriesByBrand(@PathVariable(name = "id") Integer brandId) throws BrandNotFoundRestException {
        List<CategoryDTO> listCategories = new ArrayList<>();
        try {
            Brand brand = brandService.get(brandId);
            Set<Category> categories = brand.getCategories();

            for (Category category : categories) {
                CategoryDTO dto = new CategoryDTO(category.getId(), category.getName());
                listCategories.add(dto);
            }

            return listCategories;

        }catch (BrandNotFoundException exception) {
            throw new BrandNotFoundRestException();
        }
    }
}
