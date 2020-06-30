package Crawler;

import Request.HomeRequest;
import Request.StockDataRequest;
import com.sun.javafx.fxml.builder.URLBuilder;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by kp on 2020/2/21.
 */
public class Crawler {

    private static final Logger logger = LoggerFactory.getLogger(Crawler.class);
    private CloseableHttpAsyncClient client;
    private HttpClient httpClient;
    private HttpClientConfig config;

    public Crawler() {
    }

    public void init() {
        this.httpClient = new HttpClient();
        this.httpClient.setConfig(config);
        this.client = this.httpClient.init();
        this.client.start();
    }

    public void setConfig(HttpClientConfig config) {
        this.config = config;
    }

    public String crawHome(HomeRequest homeRequest){
        HttpGet post = createPost(homeRequest, EasterPath.HOMEPATH.getPath());
        Future<HttpResponse> future = this.client.execute(post, null);
        try {
            HttpResponse response = future.get();
            HttpEntity entity = response.getEntity();
            return parseEntity(entity);
        }catch (Exception e) {
            logger.error("request home is failed:", e);
        }
        return null;
    }

    public String crawlData(StockDataRequest request) {
        try {
            HttpGet get = createDataGet(request, EasterPath.STOCKDATAPATH.getPath());
            Future<HttpResponse> future = this.client.execute(get, null);
            HttpResponse response = future.get();
            HttpEntity entity = response.getEntity();
            return parseEntity(entity);
        }catch (Exception e) {
            logger.error("request data is failed:", e);
        }
        return null;
    }

    private HttpGet createDataGet(StockDataRequest request, String url) throws Exception {
        URIBuilder builder = new URIBuilder(url);
        builder.addParameters(createHeaders(request));
        return new HttpGet(builder.build());
    }

    private List<NameValuePair> createHeaders(StockDataRequest request) {
        List<NameValuePair> headers = new ArrayList<NameValuePair>();
        BasicNameValuePair tokenPair = new BasicNameValuePair("token", request.getToken());
        headers.add(tokenPair);
        BasicNameValuePair idPair = new BasicNameValuePair("id", String.valueOf(request.getId()));
        headers.add(idPair);
        BasicNameValuePair typePair = new BasicNameValuePair("type", request.getType());
        headers.add(typePair);
        BasicNameValuePair iscrPair = new BasicNameValuePair("iscr", String.valueOf(request.isIscr()));
        headers.add(iscrPair);
        BasicNameValuePair rtntypePairs = new BasicNameValuePair("rtntype", String.valueOf(request.getRtntype()));
        headers.add(rtntypePairs);
        return headers;
    }

    private HttpGet createPost(HomeRequest request, String url) {
        HttpGet httpPost = new HttpGet(url);
        return httpPost;
    }

    private String parseEntity(HttpEntity entity) throws Exception {
        if (entity == null) {
            throw new NullPointerException("entity is null");
        }
        String result = EntityUtils.toString(entity, "UTF-8");
        if (result == null) {
            throw new NullPointerException("entity result is null");
        }
        return result;
    }

    public static void main(String args[]) {
        Crawler crawler = new Crawler();
        HttpClientConfig config = new HttpClientConfig();
        config.setConnectionTimeout(3000);
        config.setRequestTimeout(3000);
        config.setSocketTimeout(3000);
        crawler.setConfig(config);
        crawler.init();
        String str = crawler.crawHome(new HomeRequest());
        System.out.println(str);
    }
}
