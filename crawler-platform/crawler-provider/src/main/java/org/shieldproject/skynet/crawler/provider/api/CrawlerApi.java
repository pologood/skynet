package org.shieldproject.skynet.crawler.provider.api;

import org.shieldproject.skynet.crawler.provider.bean.Category;
import org.shieldproject.skynet.crawler.provider.repository.CategoryRepository;
import org.shoper.commons.responseentity.BaseResponse;
import org.shoper.commons.responseentity.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class CrawlerApi {
    @Autowired
    CategoryRepository categoryRepository;

    @PostMapping("/category")
    public BaseResponse insertCategory(@RequestBody Category category) {
        category.setId(UUID.randomUUID().toString());
        categoryRepository.saveCategory(category);
        return ResponseBuilder.custom().data(category).build();
    }

    @GetMapping("/category/{id}")
    public BaseResponse findCategory(@PathVariable String id) {
        return ResponseBuilder.custom().data(categoryRepository.findCategoryById(id)).build();
    }
}
