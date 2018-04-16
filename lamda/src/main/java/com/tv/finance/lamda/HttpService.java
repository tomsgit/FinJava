package com.tv.finance.lamda;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.client.fluent.Request;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpService {

	private static final Logger logger = LogManager.getLogger(HttpService.class);

	public static InputStream getStream(String url) {
		try {
			return Request.Get(url).execute().returnContent().asStream();
		} catch (IOException e) {
			logger.error("IO exception while getting ",url);
			logger.error("IO exception",e);
		}
		return null;
	}
	/*
	 * public void get(String url) {
	 * 
	 * CloseableHttpClient httpclient = null; CloseableHttpResponse response = null;
	 * HttpGet httpGet = new HttpGet(url);
	 * 
	 * try { httpclient = HttpClients.createDefault(); response =
	 * httpclient.execute(httpGet); logger.debug(response.getStatusLine());
	 * HttpEntity entity = response.getEntity(); // do something useful with the
	 * response body // and ensure it is fully consumed entity.
	 * EntityUtils.consume(entity); } catch (IOException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); } finally { try { response.close(); } catch
	 * (IOException e) { // TODO Auto-generated catch block e.printStackTrace(); } }
	 * 
	 * }
	 */

	/*public void post() {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost("http://targethost/login");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("username", "vip"));
		nvps.add(new BasicNameValuePair("password", "secret"));

		CloseableHttpResponse response2 = null;
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			response2 = httpclient.execute(httpPost);
			System.out.println(response2.getStatusLine());
			HttpEntity entity2 = response2.getEntity();
			// do something useful with the response body
			// and ensure it is fully consumed
			EntityUtils.consume(entity2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				response2.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}*/

}
