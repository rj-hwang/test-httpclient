package test.httpclient;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dragon on 2015/4/14.
 */
public class QuickStartTest {
	private static Logger logger = LoggerFactory.getLogger(QuickStartTest.class);
	private static String URL = "http://nan.so/";
	private static HttpHost PROXY = new HttpHost("127.0.0.1", 8888);

	@Test
	// @ref https://hc.apache.org/httpcomponents-client-4.4.x/quickstart.html
	public void httpClientQuickStart() throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();

		// get
		HttpGet httpGet = new HttpGet(URL);
		CloseableHttpResponse response = httpclient.execute(PROXY, httpGet);
		try {
			logger.debug("StatusLine={}", response.getStatusLine());
			logger.debug("html={}", EntityUtils.toString(response.getEntity()));
		} finally {
			response.close();
		}

		// post
		HttpPost httpPost = new HttpPost(URL);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("username", "dragon"));
		nvps.add(new BasicNameValuePair("password", "888888"));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		response = httpclient.execute(PROXY, httpPost);
		try {
			logger.debug("StatusLine={}", response.getStatusLine());
			logger.debug("html={}", EntityUtils.toString(response.getEntity()));
		} finally {
			response.close();
		}
	}
}
