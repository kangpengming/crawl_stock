package crawler;

import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by kp on 2020/8/12.
 */
public class HttpClientPool {

    private List<CloseableHttpAsyncClient> clients = new ArrayList<CloseableHttpAsyncClient>();
    private HttpClientConfig config;
    private CloseableHttpAsyncClient closeableHttpAsyncClient;

    public void init() {
        HttpClient httpClient = new HttpClient();
        httpClient.setConfig(this.config);
        this.closeableHttpAsyncClient = httpClient.init();
        this.clients.add(this.closeableHttpAsyncClient);
        //clients.addAll(httpClient.buildProxyClient());
    }

    public void setConfig(HttpClientConfig config) {
        this.config = config;
    }

    public CloseableHttpAsyncClient getCloseableHttpAsyncClient() {
        return clients.get(new Random().nextInt() % 1);
    }
}
