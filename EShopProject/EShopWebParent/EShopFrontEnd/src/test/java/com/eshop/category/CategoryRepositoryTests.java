package com.eshop.category;

import com.eshop.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryRepositoryTests {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testListEnabledCategory() {
        List<Category> categories = categoryRepository.findAllEnabled();

        categories.forEach(category -> {
            System.out.println(category.getName() + "( " + category.isEnabled() + " )");
        });
    }

    @Test
    public void testFindCategoryByAlias() {
        String alias = "something";
        Category category = categoryRepository.findByAliasEnabled(alias);

        assertThat(category).isNotNull();
    }
}
