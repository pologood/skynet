package org.shieldproject.skynet.crawler.provider.service;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.apache.logging.log4j.util.SystemPropertiesPropertySource;
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
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author kezhijie
 * @date 2019/1/11 9:27
 */
@Component
public class SuningTask {
    // 苏宁数码分类
    private static final String SUNING_DIGITAL_URL = "https://list.suning.com/#20089";
    private static final String SUNING_PRODUCT_URL = "https://product.suning.com/#{suffix}/#{prefix}.html";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @PostConstruct
    public void init() {
        // 抓商品ID
        new Thread(() -> catchItems()).start();
        // 抓商品SKU
        new Thread(() -> fetchItemSku()).start();
        // 抓商品明细
        new Thread(() -> fetchItemDetail()).start();
    }

    private void fetchItemSku() {
        int count = 0;
        for (;;count++) {
            String id = redisTemplate.opsForList().rightPop("SN_ITEM_ID", 5, TimeUnit.SECONDS);
            if (id == null) continue;
            if (id.equals("over")) break;
            fetchItemSku(id);
        }

        redisTemplate.opsForList().leftPush("SN_ITEM_SKU", "over");
        System.out.println("苏宁列表页商品数量--" + count);
    }

    private void fetchItemSku(String id) {
        String[] var1 = id.split("\\|\\|\\|\\|\\|");

        String url = SUNING_PRODUCT_URL.replace("#{suffix}", var1[1]).replace("#{prefix}", var1[0]);

        try {
            HttpClient build = HttpClientBuilder.custom().retry(3).url(url).timeoutUnit(TimeUnit.SECONDS).timeout(5).charset("utf-8").build();
            String data = build.doGet();
            Document parse = Jsoup.parse(data);
            Element scriptElement = parse.getElementsByTag("script").first();
            if (Objects.nonNull(scriptElement)) {
                String replace = scriptElement.html();
//                replace = replace.replace("document.location.hostname", "0");
                replace = replace.substring(70).replaceAll("\\+document\\.location\\.hostname","").replaceAll("window\\.screen\\.width","1200");
                replace += "\nsn.clusterMap;";
                ScriptEngineManager manager = new ScriptEngineManager();
                ScriptEngine engine = manager.getEngineByExtension("js");

                // 获取当前商品所有SKU
                ScriptObjectMirror eval = (ScriptObjectMirror) engine.eval(replace);
                Collection<Object> values = eval.values();

                for (Object p : values) {
                    ScriptObjectMirror v = (ScriptObjectMirror) p;
                    String skuId = null;
                    ScriptObjectMirror v1 = (ScriptObjectMirror) v.get("itemCuPartNumber");
                    Set<Map.Entry<String, Object>> v3 = v1.entrySet();

                    for (Map.Entry<String, Object> map : v3) {
                        Map<String, Object> v4 = (Map<String, Object>) map.getValue();

                        for (Map.Entry<String, Object> m : v4.entrySet()) {
                            if (m.getKey().equals("partNumber")) {
                                skuId = new BigDecimal(m.getValue().toString()).toPlainString();
                                redisTemplate.opsForList().leftPush("SN_ITEM_SKU", skuId+"|||||"+var1[1]);
                            }
                        }

                    }
                }
            } else
                System.out.println("页面有变动");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    List<String> itemIdList = new ArrayList<>();

    // 抓取商品ID
    public void catchItems() {
        try {
            String categoryCode = SUNING_DIGITAL_URL.substring(SUNING_DIGITAL_URL.indexOf("#")+1);

            HttpClient build = HttpClientBuilder.custom().retry(3).url(SUNING_DIGITAL_URL).charset("utf-8").timeout(1).timeoutUnit(TimeUnit.MINUTES).build();
            String html = build.doGet();
            Document parse = Jsoup.parse(html);
            Element element = parse.getElementById(categoryCode);
            Elements items = element.getElementsByClass("price");
            System.out.println(SUNING_DIGITAL_URL);
            for (Element item : items) {
                // e.g. 600384083|||||0070156382
                String skuid = item.attr("datasku");
                itemIdList.add(skuid);
                redisTemplate.opsForList().leftPush("SN_ITEM_ID", skuid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        redisTemplate.opsForList().leftPush("SN_ITEM_ID", "over");
    }

    public List<String> getItemIdList() {
        return itemIdList;
    }

    ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private void fetchItemDetail() {
        int count = 0;
        for (; ; count++) {
            String id = redisTemplate.opsForList().rightPop("SN_ITEM_SKU", 5, TimeUnit.SECONDS);
            if (id == null) continue;
            if (id.equals("over")) break;
            executorService.submit(() -> {
                try {
                    String[] var1 = id.split("\\|\\|\\|\\|\\|");
                    String url = SUNING_PRODUCT_URL.replace("#{suffix}", var1[1]).replace("#{prefix}", var1[0]);
                    HttpClient build = HttpClientBuilder.custom().retry(3).url(url).timeoutUnit(TimeUnit.MINUTES).timeout(1).charset("utf-8").build();
                    String data = build.doGet();
                    Document parse = Jsoup.parse(data);
                    Element element = parse.getElementById("itemDisplayName");
                    String title = element.text();
                    Item item = new Item();
                    item.setId("SN_" + id);
                    item.setTitle(title);
                    item.setMallCategoryId("4b4419da-522a-4e18-8726-05af49a8d932");
                    item.setUpdateTime(new Date());
                    item.setUrl(url);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        }
        System.out.println("实际商品数：" + count);
    }

    public static void main(String[] args) {
        SuningTask suningTask = new SuningTask();
        suningTask.catchItems();
        suningTask.getItemIdList().forEach(p -> suningTask.fetchItemSku(p));
    }
}
