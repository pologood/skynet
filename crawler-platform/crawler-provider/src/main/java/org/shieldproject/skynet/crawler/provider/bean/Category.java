package org.shieldproject.skynet.crawler.provider.bean;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Data
public class Category implements Serializable {
    @Id
    private String id;
    private String pid;
    private String name;
    private String code;
    private String fullCode;
}
