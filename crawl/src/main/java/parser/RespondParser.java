package parser;

import crawler.test.FileReader;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.seimicrawler.xpath.JXDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parser.lianjia.HouseInfo;

import java.util.*;


/**
 * Created by kp on 2020/2/22.
 */
/**
 * 获得首页的作用是获取下一层的连接地址
 * */
public class RespondParser {

    private final static Logger logger = LoggerFactory.getLogger(RespondParser.class);
    private JXDocument jxDocument;

    public RespondParser(String str){
        jxDocument = JXDocument.create(str);
    }

    public void loadRecordList(String xpath, HouseInfo houseInfo) {
        List<Object> jxNode = jxDocument.sel(xpath);
        for (Object o : jxNode) {
            if (o instanceof Element) {
                for (Node node : ((Element) o).childNodes()) {
                    houseInfo.addDealRecord(FrontendEleParser.parseDealRecord(node));
                }
            }
        }
    }

    public Map<String, String> getLianjiaBaseAttr(String xpath) {
        Map<String, String> map = new HashMap<String, String>();
        List<Object> jxNode = jxDocument.sel(xpath);
        for (Object o : jxNode) {
            if (o instanceof Element) {
                for (Node node : ((Element) o).childNodes()) {
                    map.putAll(FrontendEleParser.parseLi(node));
                }
            }
        }
        return map;
    }

    public String getLianjiaLocation(String xpath) {
        List<Object> list = jxDocument.sel(xpath);
        String title = ((String) list.get(0));
        int index = title.indexOf(" ");
        if (index == -1) {
            return title;
        }
        return title.substring(0, index - 1);
    }

    public void getLianjiaChengjiaoLinks(String xpath, List<String> links) {
        List<Object> list = jxDocument.sel(xpath);
        for (Object e : list) {
            if (e instanceof Element) {
                Element element = (Element)e;
                String url = element.attributes().get("href");
                links.add(url);
            }
        }
    }

    public void getLinksByLabel(String xpath, String label, List<String> links) {
        xpath = removeHtml(xpath);
        List<Object> list = jxDocument.sel(xpath);
        for (Object e : list) {
            if (e instanceof Element) {
                Element element = (Element)e;
                if (element.childNodeSize() != 0) {
                    try {
                        searchLinks(element, label, links);
                    }catch (Exception ex) {
                        logger.error("skip for", ex);
                    }
                }
            }
        }
    }

    private void searchLinks(Element element, String label, List<String> links) {
        Stack<Node> stack = new Stack<Node>();
        pop(element, stack);
        search(stack, label, links);
    }
    /**
     * 将所有的数据压入栈，目的从根部开始检索
     * */
    private void pop(Node ele, Stack<Node> stack) {
        stack.add(ele);
        if (ele.childNodeSize() != 0) {
            List<Node> list = ele.childNodes();
            for (Node node : list) {
                pop(node, stack);
            }
        }
    }

    private void search(Stack<Node> stack, String label, List<String> links) {
        while (!stack.empty()){
            Node node = stack.pop();
            parseLink(node, label, links);
            //每个标签对应的连接是唯一的，找到后就可以返回
            if (!links.isEmpty()) {
                return;
            }
        }
    }

    private void parseLink(Node ele, String label, List<String> links)  {
        String text = ele.toString();
        if (text.contains(label)) {
            links.addAll(MatchUtils.matchLink(text));
        }
    }

    /**
     * 退出迭代的两种方式，1：抛出异常，2.加入标签
     * */
    private void dfs(Element ele, String label, List<String> links) throws Exception {
        //has reach the bottom of the tree;
        if (ele.childNodeSize() == 0) {
            parseLink(ele, label, links);
        }
        List<Node> children = ele.childNodes();
        out:
        for (Node e : children) {
            if (e instanceof Element) {
                dfs((Element) e, label, links);
                parseLink(e, label, links);
            }
            if (e instanceof TextNode) {
                TextNode textNode = (TextNode)e;
                if (label.equals(textNode.text())){
                    break out;
                }
            }
        }
    }

    private String removeHtml(String path) {
        String str = path.replaceAll("html", "");
        return str;
    }

    public static void main(String[] args) {
        FileReader reader = new FileReader();
        String test = reader.readFile("/Users/kp/IdeaProjects/java/stock/crawl/src/main/resources/raw/easter.html");
        RespondParser respondParser = new RespondParser(test);
        List<String> links = new ArrayList<String>();
        respondParser.getLinksByLabel("/body/div[2]/div[8]/div/div[2]/div[1]/div[2]", "指数", links);
        System.out.println(links);
    }
}
