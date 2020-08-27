package crawler;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kp on 2020/2/21.
 */
public class HttpClient {

    private HttpClientConfig config;

    public HttpClientConfig getConfig() {
        return config;
    }

    public void setConfig(HttpClientConfig config) {
        this.config = config;
    }

    public CloseableHttpAsyncClient init() {
        IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
                .setIoThreadCount(Runtime.getRuntime().availableProcessors())
                .build();
        ConnectingIOReactor ioReactor;
        try {
            ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
        } catch (IOReactorException e) {
            throw new RuntimeException(e);
        }
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(this.config.getConnectionTimeout())
                .setSocketTimeout(this.config.getSocketTimeout())
                .setConnectionRequestTimeout(this.config.getRequestTimeout())
                .build();
        PoolingNHttpClientConnectionManager connManager = new PoolingNHttpClientConnectionManager(ioReactor);
        connManager.setMaxTotal(100);
        connManager.setDefaultMaxPerRoute(100);
        CloseableHttpAsyncClient closeableHttpAsyncClient = HttpAsyncClients.custom()
                .setConnectionManager(connManager)
                .setDefaultRequestConfig(requestConfig)
                .build();
        closeableHttpAsyncClient.start();
        return closeableHttpAsyncClient;
    }

    public List<CloseableHttpAsyncClient> buildProxyClient() {
        List<CloseableHttpAsyncClient> closeableHttpAsyncClients = new ArrayList<CloseableHttpAsyncClient>();
        for (int i = 0; i < ProxyInfo.IPS.length; i++) {
            HttpHost proxy = new HttpHost(ProxyInfo.IPS[i], Integer.parseInt(ProxyInfo.PORTS[i]));
            CredentialsProvider provider = new BasicCredentialsProvider();
            provider.setCredentials(new AuthScope(proxy), new UsernamePasswordCredentials(ProxyInfo.users[i], ProxyInfo.pwds[i]));
            CloseableHttpAsyncClient httpClient = HttpAsyncClients.custom().setDefaultCredentialsProvider(provider).setProxy(proxy).build();
            httpClient.start();
            closeableHttpAsyncClients.add(httpClient);
        }
        return closeableHttpAsyncClients;

    }
}
