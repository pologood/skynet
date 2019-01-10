package org.shieldproject.skynet.crawler.provider;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class Application {
    //    public static void main(String[] args) {
//        SpringApplication.run(Application.class, args);
//    }
    public static void main(String[] args) {
        List<String> region = Arrays.asList("成都市", "自贡市");
        String address = "成都市武侯区武侯大道";
        String tmp = null;
        for (String re : region)
            if ((tmp = address.replace(re, "")).length() != address.length() || (tmp = address.replace(re.substring(0, re.length() - 1), "")).length() != address.length())
                break;
        System.out.println(tmp);
    }
}
