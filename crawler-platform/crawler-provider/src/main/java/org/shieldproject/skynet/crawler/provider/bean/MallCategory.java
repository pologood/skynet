package org.shieldproject.skynet.crawler.provider.bean;

import lombok.Data;

@Data
public class MallCategory {
    private String id;
    private String mallId;
    private String categoryId;
    private Long loop;
    private String url;
    private Long createTime;
    private Long updateTime;
}
