package org.shieldproject.skynet.crawler.provider.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.shoper.commons.http.HttpClient;
import org.shoper.commons.http.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Component
public class CrawlerTask {
    int page = 0;
    String url = "https://list.jd.com/list.html?cat=9987,653,655";
    String pageUrl = url + "&page=#{page}&sort=sort_rank_asc&trans=1&JL=6_0_0#J_main";
    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @PostConstruct
    public void init() {

        //抓取当前分类的页码数
        int page = catchPage(url);
        //抓取所有页的ids。
        Thread thread1 = new Thread(() -> catchItems(page));
        thread1.start();

        Thread thread2 = new Thread(() -> fetchItemDetail());
        thread2.start();

    }

    private void fetchItemDetail() {
        int count = 0;
        for (; ; count++) {
            String id = redisTemplate.opsForList().rightPop("JD_ITEM_ID", 5, TimeUnit.SECONDS);
            if (id == null) continue;
            if (id.equals("over")) break;
            System.out.println(id);
        }
        System.out.println("--" + count);
    }

    private void catchItems(int page) {
        IntStream.range(1,page).forEach(i->{
            String url = this.pageUrl.replace("#{page}", i + "");
            try {
                HttpClient build = HttpClientBuilder.custom().url(url).charset("utf-8").timeout(1).timeoutUnit(TimeUnit.MINUTES).build();
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
            HttpClient build = HttpClientBuilder.custom().url(url).charset("utf-8").timeoutUnit(TimeUnit.SECONDS).timeout(10).build();
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
