package org.shieldproject.skynet.crawler.provider.bean;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Item {
    @Id
    private String id;
    private String mallCategoryId;
    private String title;
    private String url;
    private String pics;
    private BigDecimal price;
    private Date updateTime;
}
