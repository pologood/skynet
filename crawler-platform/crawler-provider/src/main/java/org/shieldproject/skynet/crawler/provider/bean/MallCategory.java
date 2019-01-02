package org.shieldproject.skynet.crawler.provider.bean;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class MallCategory {
    @Id
    private String id;
    private String mallId;
    private String categoryId;
    private Long loop;
    private String url;
    private Long createTime;
    private Long updateTime;
}
