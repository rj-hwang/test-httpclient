package test.httpclient;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by dragon on 2015/4/14.
 */
public class CookieTest {
	private static Logger logger = LoggerFactory.getLogger(CookieTest.class);
	private static String URL = "http://nan.so/";		// 要访问的网址
	private static String DOMAIN = "nan.so";			// cookie的domain
	private static HttpHost PROXY = new HttpHost("127.0.0.1", 8888);// 代理

	@Test
	public void byHttpContext() throws Exception {
		// 通过 HttpContext 为每个 request 设置不同的 cookie
		BasicCookieStore cookieStore = new BasicCookieStore();
		BasicClientCookie cookie = new BasicClientCookie("by", "dragon");
		cookie.setDomain(DOMAIN);	// need
		cookieStore.addCookie(cookie);
		HttpContext localContext = new BasicHttpContext();
		localContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);

		HttpGet request = new HttpGet(URL);
		HttpClient httpClient = HttpClients.createDefault();
		HttpResponse response = executeRequest(httpClient, request, localContext);

		logger.debug("StatusCode={}", response.getStatusLine());
		logger.debug("html={}", EntityUtils.toString(response.getEntity()));
	}

	@Test
	public void byHttpClient() throws Exception {
		BasicCookieStore cookieStore = new BasicCookieStore();
		BasicClientCookie cookie = new BasicClientCookie("by", "dragon");
		cookie.setDomain(DOMAIN);    // need
		cookieStore.addCookie(cookie);
		HttpClient httpClient = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();

		HttpGet request = new HttpGet(URL);
		HttpResponse response =  executeRequest(httpClient, request, null);

		logger.debug("StatusCode={}", response.getStatusLine());
		logger.debug("html={}", EntityUtils.toString(response.getEntity()));
	}

	@Test
	public void byHeader() throws Exception {
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(URL);
		request.setHeader("Cookie", "by=dragon");
		HttpResponse response =  executeRequest(httpClient, request, null);
		logger.debug("StatusCode={}", response.getStatusLine());
		logger.debug("html={}", EntityUtils.toString(response.getEntity()));
	}

	private HttpResponse executeRequest(HttpClient httpClient, HttpGet request, HttpContext localContext) throws IOException {
		if(PROXY != null) {
			if(localContext != null) {
				return httpClient.execute(PROXY, request, localContext);
			}else{
				return httpClient.execute(PROXY, request);
			}
		}else{
			if(localContext != null) {
				return httpClient.execute(request, localContext);
			}else{
				return httpClient.execute(request);
			}
		}
	}
}
