package org.shieldproject.skynet.crawler.provider.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.shoper.commons.http.HttpClient;
import org.shoper.commons.http.HttpClientBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class CrawlerTask {
    int page = 0;
    String url = "https://list.jd.com/list.html?cat=9987,653,655&page=#{page}&sort=sort_rank_asc&trans=1&JL=6_0_0#J_main";
    @Scheduled(fixedDelay = 1000)
    public void init() {

        //抓取当前分类的页码数
        int page = catchPage(url);
        catchItems(page);
    }

    private void catchItems(int page) {
        for (int i = 1; i <= page; i++) {
            String url = this.url.replace("#{page}", i + "");
            try {
                HttpClient build = HttpClientBuilder.custom().url(url).charset("utf-8").timeout(1).timeoutUnit(TimeUnit.MINUTES).build();
                String html = build.doGet();
                Document parse = Jsoup.parse(html);
                Elements elementsByClass = parse.getElementsByClass("gl-item");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Integer catchPage(String url) {
        try {
            HttpClient build = HttpClientBuilder.custom().url(url).charset("utf-8").timeoutUnit(TimeUnit.SECONDS).timeout(10).build();
            String data = build.doGet();
            Document parse = Jsoup.parse(data);
            Element page = parse.getElementsByClass("p-num").get(0);
            Element child = page.child(page.childNodeSize() - 1);
            String pageNum = child.text();
            return Integer.valueOf(pageNum);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
