package org.shieldproject.skynet.crawler.provider.api;

import org.shieldproject.skynet.crawler.provider.bean.Category;
import org.shieldproject.skynet.crawler.provider.bean.Mall;
import org.shieldproject.skynet.crawler.provider.bean.MallCategory;
import org.shoper.commons.core.MD5Util;
import org.shoper.commons.responseentity.BaseResponse;
import org.shoper.commons.responseentity.ResponseBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class CrawlerApi {
//    @Autowired
//    CategoryRepository categoryRepository;

    @PostMapping("/category")
    public BaseResponse insertCategory(@RequestBody Category category) {
        category.setId(MD5Util.getMD5Code(category.getCode()));
//        categoryRepository.saveCategory(category);
        return ResponseBuilder.custom().data(category).build();
    }

    @PostMapping("/mall")
    public BaseResponse insertMall(@RequestBody Mall mall) {
        mall.setId(MD5Util.getMD5Code(mall.getCode()));
//        categoryRepository.saveMall(mall);
        return ResponseBuilder.custom().data(mall).build();
    }

    @PostMapping("/mallCategory")
    public BaseResponse insertMallCategory(@RequestBody MallCategory mallCategory) {
        mallCategory.setId(UUID.randomUUID().toString());
        mallCategory.setCreateTime(System.currentTimeMillis());
//        categoryRepository.saveMallCategory(mallCategory);
        return ResponseBuilder.custom().data(mallCategory).build();
    }

    @GetMapping("/category/{id}")
    public BaseResponse findCategory(@PathVariable String id) {
//        Object data = categoryRepository.findCategoryById(id);
        return ResponseBuilder.custom().data(null).build();
    }
}
