package cn.yiming1234.wechat_auto_reply.utils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * HTTP访问类，对Apache HttpClient进行简单封装，适配器模式
 */
public class MyHttpClient {
	private Logger logger = Logger.getLogger("MyHttpClient");

	private static CloseableHttpClient httpClient = HttpClients.createDefault();

	private static MyHttpClient instance = null;

	private static CookieStore cookieStore;

	static {
		cookieStore = new BasicCookieStore();

		// 将CookieStore设置到httpClient中
		httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
	}

	public static String getCookie(String name) {
		List<Cookie> cookies = cookieStore.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equalsIgnoreCase(name)) {
				return cookie.getValue();
			}
		}
		return null;

	}

	/**
	 * 获取cookies
	 */
	public static MyHttpClient getInstance() {
		if (instance == null) {
			synchronized (MyHttpClient.class) {
				if (instance == null) {
					instance = new MyHttpClient();
				}
			}
		}
		return instance;
	}

	private MyHttpClient() {

	}

	/**
	 * 处理GET请求
	 */
	public HttpEntity doGet(String url, List<BasicNameValuePair> params, boolean redirect,
							Map<String, String> headerMap) {
		HttpEntity entity = null;
		HttpGet httpGet = new HttpGet();

		try {
			if (params != null) {
				String paramStr = EntityUtils.toString(new UrlEncodedFormEntity(params, Consts.UTF_8));
				httpGet = new HttpGet(url + "?" + paramStr);
			} else {
				httpGet = new HttpGet(url);
			}
			if (!redirect) {
				httpGet.setConfig(RequestConfig.custom().setRedirectsEnabled(false).build()); // 禁止重定向
			}
			httpGet.setHeader("User-Agent", Config.USER_AGENT);
			if (headerMap != null) {
				Set<Entry<String, String>> entries = headerMap.entrySet();
				for (Entry<String, String> entry : entries) {
					httpGet.setHeader(entry.getKey(), entry.getValue());
				}
			}
			CloseableHttpResponse response = httpClient.execute(httpGet);
			entity = response.getEntity();
		} catch (ClientProtocolException e) {
			logger.info(e.getMessage());
		} catch (IOException e) {
			logger.info(e.getMessage());
		}

		return entity;
	}

	/**
	 * 处理POST请求
	 */
	public HttpEntity doPost(String url, String paramsStr) {
		HttpEntity entity = null;
		HttpPost httpPost = new HttpPost();
		try {
			StringEntity params = new StringEntity(paramsStr, Consts.UTF_8);
			httpPost = new HttpPost(url);
			httpPost.setEntity(params);
			httpPost.setHeader("Content-type", "application/json; charset=utf-8");
			httpPost.setHeader("User-Agent", Config.USER_AGENT);
			CloseableHttpResponse response = httpClient.execute(httpPost);
			entity = response.getEntity();
		} catch (ClientProtocolException e) {
			logger.info(e.getMessage());
		} catch (IOException e) {
			logger.info(e.getMessage());
		}

		return entity;
	}

	/**
	 * 处理POST请求
	 */
	public HttpEntity doPost(String url, String json, Map<String, String> headers) throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		StringEntity entity = new StringEntity(json, "UTF-8");
		httpPost.setEntity(entity);

		// Set headers
		for (Map.Entry<String, String> entry : headers.entrySet()) {
			httpPost.setHeader(entry.getKey(), entry.getValue());
		}

		CloseableHttpResponse response = httpClient.execute(httpPost);
		return response.getEntity();
	}

	/**
	 * 上传文件到服务器
	 */
	public HttpEntity doPostFile(String url, HttpEntity reqEntity) {
		HttpEntity entity = null;
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("User-Agent", Config.USER_AGENT);
		httpPost.setEntity(reqEntity);
		try {
			CloseableHttpResponse response = httpClient.execute(httpPost);
			entity = response.getEntity();

		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return entity;
	}

	/*
	 * 获取httpClient
	 */
	public static CloseableHttpClient getHttpClient() {
		return httpClient;
	}

}