package org.shieldproject.skynet.crawler.provider.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class Category implements Serializable {
    private String id;
    private String pid;
    private String name;
    private String code;
    private String fullCode;
}
