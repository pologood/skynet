package org.shieldproject.skynet.crawler.provider.service;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.shieldproject.skynet.crawler.provider.bean.Item;
import org.shoper.commons.http.HttpClient;
import org.shoper.commons.http.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Component
public class CrawlerTask {
    int page = 0;
    String url = "https://list.jd.com/list.html?cat=9987,653,655";
    String pageUrl = url + "&page=#{page}&sort=sort_rank_asc&trans=1&JL=6_0_0#J_main";
    String itemUrl = "https://item.jd.com/#{id}.html";
    @Autowired
    RedisTemplate<String, String> redisTemplate;
//    @Autowired
//    MongoTemplate mongoTemplate;

    @PostConstruct
    public void init() {

        //抓取当前分类的页码数
        int page = catchPage(url);
        //抓取所有页的ids。
        Thread thread1 = new Thread(() -> catchItems(page));
        thread1.start();
        //获取商品的sku
        Thread thread2 = new Thread(() -> fetchItemsSku());
        thread2.start();
        //获取商品详细数据
        Thread thread3 = new Thread(() -> fetchItemDetail());
        thread3.start();
    }

    ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private void fetchItemDetail() {
        int count = 0;
        for (; ; count++) {
            String id = redisTemplate.opsForList().rightPop("JD_ITEM_SKU", 5, TimeUnit.SECONDS);
            if (id == null) continue;
            if (id.equals("over")) break;
            executorService.submit(() -> {
                try {
                    String url = itemUrl.replace("#{id}", id);
                    HttpClient build = HttpClientBuilder.custom().retry(3).url(url).timeoutUnit(TimeUnit.MINUTES).timeout(1).charset("utf-8").build();
                    String data = build.doGet();
                    Document parse = Jsoup.parse(data);
                    Elements elementsByClass = parse.getElementsByClass("sku-name");
                    Element element = elementsByClass.get(0);
                    String title = element.text();
                    Item item = new Item();
                    item.setId(("JD_" + id));
                    item.setTitle(title);
                    item.setMallCategoryId("4b4419da-522a-4e18-8726-05af49a8d932");
                    item.setUpdateTime(new Date());
                    item.setUrl(url);
//                    mongoTemplate.save(item);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        System.out.println("实际商品数" + count);
    }

    private void fetchItemsSku() {
        int count = 0;
        for (; ; count++) {
            String id = redisTemplate.opsForList().rightPop("JD_ITEM_ID", 5, TimeUnit.SECONDS);
            if (id == null) continue;
            if (id.equals("over")) break;
            fetchItemsSku(id);

        }
        redisTemplate.opsForList().leftPush("JS_ITEM_SKU", "over");
        System.out.println("列表页商品数量--" + count);
    }


    public void fetchItemsSku(String id) {
        String url = itemUrl.replace("#{id}", id);
        try {
            HttpClient build = HttpClientBuilder.custom().retry(3).url(url).timeoutUnit(TimeUnit.MINUTES).timeout(1).charset("utf-8").build();
            String data = build.doGet();
            Document parse = Jsoup.parse(data);
            Elements script = parse.getElementsByTag("script");
            Element element = script.get(0);
            if (element != null) {
                String replace = element.html();//.replace("var pageConfig = ", "");
                replace += "\npageConfig.product.colorSize;";
                ScriptEngineManager manager = new ScriptEngineManager();
                ScriptEngine engine = manager.getEngineByExtension("js");
                //获取商品对应的sku集合
                ScriptObjectMirror eval = (ScriptObjectMirror) engine.eval(replace);
                Collection<Object> values = eval.values();
                for (Object value : values) {
                    ScriptObjectMirror v = (ScriptObjectMirror) value;
                    String skuId = null;
                    Object skuId1 = v.get("skuId");
                    if (skuId1 instanceof Double) {
                        skuId = new BigDecimal((Double) skuId1).toPlainString();
                    } else if (skuId1 instanceof Integer) {
                        skuId = new BigDecimal((Integer) skuId1).toPlainString();
                    } else if (skuId1 instanceof Long) {
                        skuId = new BigDecimal((Long) skuId1).toPlainString();
                    }
                    redisTemplate.opsForList().leftPush("JD_ITEM_SKU", skuId);
                }
            } else {
                System.out.println("页面有变动");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void catchItems(int page) {
        IntStream.rangeClosed(1, page).forEach(i -> {
            String url = this.pageUrl.replace("#{page}", i + "");
            try {
                HttpClient build = HttpClientBuilder.custom().retry(3).url(url).charset("utf-8").timeout(1).timeoutUnit(TimeUnit.MINUTES).build();
                String html = build.doGet();
                Document parse = Jsoup.parse(html);
                Elements items = parse.getElementsByClass("gl-item");
                System.out.println(url);
                for (Element item : items) {
                    Element child = item.child(0);
                    String skuid = child.attr("data-sku");
                    redisTemplate.opsForList().leftPush("JD_ITEM_ID", skuid);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        redisTemplate.opsForList().leftPush("JD_ITEM_ID", "over");
    }

    private Integer catchPage(String url) {
        try {
            HttpClient build = HttpClientBuilder.custom().retry(3).url(url).charset("utf-8").timeoutUnit(TimeUnit.SECONDS).timeout(10).build();
            String data = build.doGet();
            Document parse = Jsoup.parse(data);
            Element page = parse.getElementById("J_bottomPage");
            Elements children = page.children().first().children();
            String pageNum = children.get(children.size() - 2).text();
            return Integer.valueOf(pageNum);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
