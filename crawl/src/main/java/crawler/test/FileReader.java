package crawler.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by kp on 2020/2/22.
 */
public class FileReader {
    private final static Logger logger = LoggerFactory.getLogger(FileReader.class);

    public String readFile(String path) {
        try {
            String finalText = "";
            String text = "";
            java.io.FileReader fileReader = new java.io.FileReader(path);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while((text = bufferedReader.readLine())!=null){
                finalText = finalText + "\n"+text;
            }
            return finalText;
        }catch (IOException e) {
            logger.error("read file is failed", e);
        }
        return null;
    }

    public static void main(String args[]) {
        FileReader reader = new FileReader();
        String test = reader.readFile("/Users/kp/IdeaProjects/java/stock/crawl/src/main/resources/raw/easter.html");
        logger.error(test);
    }
}
