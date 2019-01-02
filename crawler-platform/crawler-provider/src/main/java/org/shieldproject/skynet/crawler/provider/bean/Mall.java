package org.shieldproject.skynet.crawler.provider.bean;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Mall {
    @Id
    private String id;
    private String name;
    private String code;
    private String domain;
}
