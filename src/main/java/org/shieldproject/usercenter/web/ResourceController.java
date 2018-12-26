package org.shieldproject.usercenter.web;

import org.shieldproject.usercenter.bean.Resource;
import org.shieldproject.usercenter.exception.SystemException;
import org.shieldproject.usercenter.service.ResourceService;
import org.shoper.commons.responseentity.BaseResponse;
import org.shoper.commons.responseentity.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by ShawnShoper on 2017/3/10.
 */
@RestController
public class ResourceController {
    @Autowired
    ResourceService resourceService;

    @GetMapping(value = "/resource", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse getResource(Integer page, Integer limit) {
        ResponseBuilder custom = ResponseBuilder.custom();
        try {
            List<Resource> resources = resourceService.getResource(page, limit);
            int total = resourceService.getResourceSize();
            custom.data(resources).currPage(page).pageSize(limit).totalCount(total);
        } catch (SystemException e) {
            custom.failed(e.getMessage(), e.getCode());
        }
        return custom.build();
    }


    @GetMapping(value = "/resourceTree", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse getResourceTree() {
        ResponseBuilder custom = ResponseBuilder.custom();
            List<Map<String,Object>> resources = resourceService.getResourceTree();
            custom.data(resources);
        return custom.build();
    }
}
