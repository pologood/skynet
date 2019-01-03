package org.shieldproject.skynet.crawler.provider.bean;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
//库存  https://c0.3.cn/stocks?callback=jQuery218653&type=getstocks&skuIds=6077916%2C6077948%2C3995645%2C6077950%2C6084345%2C6077946%2C6084379%2C3133859%2C3133857%2C3133855%2C3133853%2C3133851%2C6084361%2C5110517%2C3133847%2C6084357%2C3133845%2C6084359%2C3133843%2C3133841%2C6084387%2C6084355&area=22_1930_50949_52153&_=1546521597015
@Data
public class Item {
    private String id;
    private String mallCategoryId;
    //标题
    private String title;
    //链接
    private String url;
    //图片
    private String pics;
    //当前价格
    private BigDecimal price;
    //更新时间
    private Date updateTime;
    //停止售卖,或者下架
    private boolean over;
}
