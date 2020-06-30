package Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * Created by kp on 2020/2/25.
 */
public class MatchUtils {

    public static List<String> matchLink(String content) {
        Pattern linkPattern = compile("<a[^>]*href=(\\\"([^\\\"]*)\\\"|\\'([^\\']*)\\'|([^\\\\s>]*))[^>]*>(.*?)</a>");
        Matcher matcher = linkPattern.matcher(content);
        List<String> links = new ArrayList<String>();
        if (matcher.find()) {
            for (int i = 0, index = matcher.groupCount(); i < index; i++) {
                String matchLink = matcher.group(i);
                if (matchLink == null) {
                   continue;
                }
                String link = matchEle("href=\"(.*?)\"", matchLink);
                if (link == null) {
                    continue;
                }
                links.add(link);
            }
        }
        return links;
    }

    private static String matchEle(String regex, String source){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
        if (matcher.find()) return matcher.group(1);
        return null;
    }

    public static void main(String args[]) {
        String content = "<a href=\"https://trade.1234567.com.cn/zsb/default\" >指数宝</a>";
        List<String> linkResult = matchLink(content);
        System.out.println(linkResult);
    }
}
