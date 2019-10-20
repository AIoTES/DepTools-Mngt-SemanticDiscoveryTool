package org.activage.backend;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class HTTPClient {
	
	private static final String USER_AGENT = "Mozilla/5.0";
	
	public static String sendGet(String url, Map<String, String> map) throws Exception {	
		System.out.println("--> " + url);
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);
		for (Entry<String, String> x : map.entrySet()){
			con.setRequestProperty(x.getKey(), x.getValue());
		}

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());
		return response.toString();
	}
	
	public static String sendPost(String url, String json, Map<String, String> headers) throws IOException{
		System.out.println("---> " + json);
		return sendContent(url, json, "POST", headers);
	}
	
	public static String sendPut(String url, String json) throws IOException{
		System.out.println("PUT");
		System.out.println(json);
        return sendContent(url, json, "PUT", new HashMap<String, String>());
	}
	
	public static String sendDelete(String url) throws IOException{
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setDoOutput(true);
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestMethod("DELETE");
		
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'DELETE' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());
		return response.toString();
	}
	
	private static String sendContent(String url, String json, String requestMethod, Map<String, String> headers) throws IOException{
		URL obj = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
		conn.setDoOutput(true);
		conn.setDoInput(true);
        conn.setConnectTimeout(5000);
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setRequestProperty("User-Agent", USER_AGENT);     
        conn.setRequestMethod(requestMethod);
        
		for (Entry<String, String> x : headers.entrySet()){
			conn.setRequestProperty(x.getKey(), x.getValue());
		}
        
        OutputStream os = conn.getOutputStream();
        os.write(json.getBytes("UTF-8"));
        os.close();

        // read the response
        InputStream in = new BufferedInputStream(conn.getInputStream());
        String result = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
        System.out.println(result);

        in.close();
        conn.disconnect();

        return result;
	}

}
