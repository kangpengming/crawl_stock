import crawler.Crawler;
import crawler.HttpClientConfig;
import parser.RespondParser;
import Request.HomeRequest;
import Request.StockDataRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kp on 2020/2/21.
 */
public class Main {

    public static void main(String args[]) {
        HttpClientConfig config = createConfig();
        Crawler crawler = new Crawler();
        crawler.setConfig(config);
        crawler.init();
        String content = crawler.crawHome(new HomeRequest());
        RespondParser parser = new RespondParser(content);
        List<String> list = new ArrayList<String>();
        parser.getLinksByLabel("/body/div[2]/div[8]/div/div[2]/div[1]/div[2]", "指数", list);

        StockDataRequest stockDataRequest = new StockDataRequest();
        //stockDataRequest.setId();
        //crawler.crawlData()
        System.out.println(list);

    }

    private static HttpClientConfig createConfig(){
        HttpClientConfig config = new HttpClientConfig();
        config.setRequestTimeout(3000);
        config.setConnectionTimeout(3000);
        config.setSocketTimeout(3000);
        return config;
    }
}
