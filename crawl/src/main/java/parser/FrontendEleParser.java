package parser;

import org.jsoup.nodes.Node;
import parser.lianjia.HouseInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kp on 2020/8/7.
 */
public class FrontendEleParser {

    public static Map<String, String> parseLi(Node node) {
        Map<String, String> map = new HashMap<String, String>(1);
        List<Node> nodes = node.childNodes();
        Node node1 = nodes.get(0);
        String text = node1.childNodes().get(0).toString();
        String value = nodes.get(1).toString();
        map.put(text, value);
        return map;
    }

    public static HouseInfo.DealRecord parseDealRecord(Node node) {
        HouseInfo.DealRecord dealRecord = new HouseInfo.DealRecord();
        List<Node> nodes = node.childNodes();
        String price = nodes.get(0).childNodes().get(0).toString();
        dealRecord.setUnitPrice(price);
        dealRecord.setDetailInfo(nodes.get(1).childNodes().get(0).toString());
        return dealRecord;
    }
}
