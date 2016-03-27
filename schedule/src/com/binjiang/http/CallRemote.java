package com.binjiang.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.ssl.SSLContexts;

/**
 * @author javebean 
 */	
public class CallRemote {

	private static final  String BASEURL= "http://sql.bjxy.cn/";
	
	private static CloseableHttpClient httpclient = null;
	
	/**
	 * 设置请求参数
	 * @param param
	 * @return
	 */
	private static List<NameValuePair> setParamMap(Map<String,String> param){
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> map : param.entrySet()) {
			nvps.add(new BasicNameValuePair(map.getKey(), map.getValue()));
		}
		return nvps;
	}
	
	
	/**
	 * 设置超时  
	 * @return
	 */
	private static RequestConfig setRequestConfig(){
		/* 设置超时 */
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectionRequestTimeout(10000).setConnectTimeout(10000)
				.setSocketTimeout(10000).build();
		return requestConfig;
	}
	
	
	/**
	 * get请求
	 * @param interfaceName
	 * @param param
	 * @param httpclient
	 * @return
	 */
	public static String httpGetFunc(String interfaceName, Map<String,String> param){
		try {
			HttpGet httpGet = null;
			if(null==param){
				httpGet = new HttpGet(interfaceName);
			}else{
				String paramString = URLEncodedUtils.format(setParamMap(param), "utf-8");
				httpGet = new HttpGet(interfaceName + "?" + paramString);
			}
			//httpGet.setConfig(setRequestConfig());
			 CloseableHttpResponse execute = httpclient.execute(httpGet);
			 
			 return getOutResultString(execute);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
			return null;
	}
	
	
	/**
	 * post请求
	 * @param interfaceName
	 * @param param
	 * @param httpclient
	 * @return
	 */
	public static String httpPostFunc(String interfaceName, Map<String,String> param){
		HttpPost httpPost = new HttpPost(interfaceName);
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(setParamMap(param), "UTF-8"));
			CloseableHttpResponse chr =  httpclient.execute(httpPost);
			return getOutResultString(chr);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 初始化httpClient
	 * @return
	 */
	public CallRemote(){
		SSLContext sslContext = null;
		try {
			sslContext = SSLContexts.custom().build();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		SSLConnectionSocketFactory sf = new SSLConnectionSocketFactory(
				sslContext, new String[] { "TLSv1", "TLSv1.1", "TLSv1.2" },
				null, new HostnameVerifier() {
					@Override
					public boolean verify(String hostname, SSLSession session) {
						return true;
					}

				});
		httpclient = HttpClients.custom().setSSLSocketFactory(sf).build();
	}
	
	
	/**
	 * 得到输出结果
	 * @param response
	 * @return
	 */
	public static String getOutResultString(CloseableHttpResponse response){
		StringBuilder out = new StringBuilder();
		try(InputStream body2 = response.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(body2,"UTF-8"));){
			String line;
			while ((line = reader.readLine()) != null) {
				out.append(line);
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		return out.toString();
	}
	/**
	 * 得到登陆参数
	 * @return
	 */
	
	public static Map<String,String> getLogin_param(){
		Map<String,String> param = new HashMap<String, String>();
		String __VIEWSTATE = "/wEPDwUKMTM5MjUxOTk4Nw9kFgJmD2QWHgICDxAPFgIeB1Zpc2libGVoZGQWAWZkAgMPEA8WAh8AaGRkZGQCBA8QDxYCHwBoZGRkZAIFDxAPFgIeBFRleHQFCeaVmeWKoeWKnmRkZGQCBg8QDxYCHwBoZGRkZAIHDxAPFgIfAQUG5a2m6ZmiZGRkZAIIDxAPFgIfAQUG5a2m5YqeZGRkZAIJDxAPFgIfAQUJ54+t5Li75Lu7ZGRkZAIKDxAPFgIfAQUG5pWZ5biIZGRkZAILDxAPFgIfAQUG5a2m55SfZGRkZAIMDxAPFgIfAGhkZGRkAg0PEA8WAh8AaGRkZGQCDg8QDxYCHwBoZGRkZAIPDxAPFgIfAGhkZGRkAhAPEA8WAh8AaGRkZGQYAQUeX19Db250cm9sc1JlcXVpcmVQb3N0QmFja0tleV9fFgsFDFJhZGlvQnV0dG9uNAUMUmFkaW9CdXR0b240BQxSYWRpb0J1dHRvbjIFDFJhZGlvQnV0dG9uMgUMUmFkaW9CdXR0b241BQxSYWRpb0J1dHRvbjUFDFJhZGlvQnV0dG9uNgUMUmFkaW9CdXR0b242BQxSYWRpb0J1dHRvbjEFDFJhZGlvQnV0dG9uMQUMUmFkaW9CdXR0b24zgSA0j9U6LMstXJZ0msYa1SCi3L4=";
		param.put("__VIEWSTATE", __VIEWSTATE);
		/*param.put("TextBox1", "20122344003");
		param.put("TextBox2", "888888");
		param.put("js", "RadioButton3");*/
		param.put("Button1", "登陆");
		param.put("Button2", "重填");
		return param;
	}
	

	/**
	 * 获取redirect后的动态url
	 * @param httpclient
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static String getRedirectUrl()
			throws IOException, ClientProtocolException {
		HttpGet httpget = new HttpGet(BASEURL);
        HttpContext context = new BasicHttpContext(); 
        HttpResponse response = httpclient.execute(httpget, context); 
        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
            throw new IOException(response.getStatusLine().toString());
		HttpUriRequest currentReq = (HttpUriRequest) context.getAttribute( 
                HttpCoreContext.HTTP_REQUEST);
        HttpHost currentHost = (HttpHost)  context.getAttribute( 
        		HttpCoreContext.HTTP_TARGET_HOST);
        String currentUrl = (currentReq.getURI().isAbsolute()) ? currentReq.getURI().toString() : (currentHost.toURI() + currentReq.getURI());
		return currentUrl;
	}
	
}
