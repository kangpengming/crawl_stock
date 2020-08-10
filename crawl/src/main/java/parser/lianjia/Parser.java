package parser.lianjia;

import crawler.Crawler;
import crawler.HttpClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parser.RespondParser;

import java.util.*;

/**
 * Created by kp on 2020/8/4.
 */
public class Parser {

    private static final Logger logger = LoggerFactory.getLogger(Parser.class);
    private Crawler crawler = new Crawler();
    private int totalPage = 100;
    private int numPerPage = 31;


    public void init() {
        HttpClientConfig config = new HttpClientConfig();
        config.setConnectionTimeout(30000);
        config.setRequestTimeout(30000);
        config.setSocketTimeout(30000);
        this.crawler.setConfig(config);
        this.crawler.init();
    }
    public Set<String> parseHomePage(int startIndex, int endIndex) {
        Set<String> set = new HashSet<String>();
        for (int i = startIndex; i < endIndex; i++) {
            String url = LianjiaXpath.HOME_TRADE_LINK + "/pg" + i;
            String context = this.crawler.craw(url);
            RespondParser respondParser = new RespondParser(context);
            for (int j = 1; j < numPerPage; j++) {
                List<String> list = new ArrayList<String>();
                respondParser.getLianjiaChengjiaoLinks("/body/div[5]/div[1]/ul/li["+ j +"]/div/div[1]/a", list);
                set.addAll(list);
            }
        }
        return set;
    }

    public Set<HouseInfo> parseHousesInfo(Set<String> links) {
        Set<HouseInfo> houseInfos = new HashSet<HouseInfo>();
        for (String s : links) {
            String content = null;
            int count = 0;
            while (content == null && count < 5) {
                content = this.crawler.craw(s);
                count ++;
            }
            if (content == null) {
                logger.error(s);
                continue;
            }
            RespondParser respondParser = new RespondParser(content);
            HouseInfo info = parseHouseInfo(respondParser);
            houseInfos.add(info);
        }
        return houseInfos;
    }

    private HouseInfo parseHouseInfo(RespondParser respondParser) {
        HouseInfo houseInfo = new HouseInfo();
        Map<String, String> map = respondParser.getLianjiaBaseAttr(LianjiaXpath.BASE_ATTR_XPATH);
        Map<String, String> tradeMap = respondParser.getLianjiaBaseAttr(LianjiaXpath.TRADE_ATTR_XPATH);
        respondParser.loadRecordList(LianjiaXpath.RECORD_LIST_XPATH, houseInfo);
        houseInfo.addBase(map);
        houseInfo.addTrade(tradeMap);
        houseInfo.setLocation(respondParser.getLianjiaLocation(LianjiaXpath.TITLE_LOCATION_XPATH));
        return houseInfo;
    }

    public static void main(String[] args) {
        System.out.println("start crawl");
        Parser parser = new Parser();
        parser.init();
        List<HouseInfo> houseInfos = new ArrayList<HouseInfo>();
        CSVGenerator csvGenerator = new CSVGenerator();
        int num = 100;
        //100页分成多少页
        int avg = parser.totalPage / num;
        for (int i = 0; i < num; i ++) {
            int startIndex = avg * i + 1;
            int endIndex = avg * (i + 1) + 1;
            Set<String> strings = parser.parseHomePage(startIndex, endIndex);
            try {
                Thread.sleep(i % 6 * 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            houseInfos.addAll(parser.parseHousesInfo(strings));
        }
        for (HouseInfo houseInfo : houseInfos) {
            csvGenerator.add(houseInfo);
        }
        csvGenerator.generateCSV("/Users/kp/Desktop/lianjia.csv");
    }
}
