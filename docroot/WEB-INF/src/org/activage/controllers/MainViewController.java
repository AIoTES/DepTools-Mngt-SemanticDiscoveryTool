package org.activage.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.activage.backend.HTTPClient;
import org.activage.entities.Device;
import org.activage.entities.Platform;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@ManagedBean(name = "mainViewController")
@ViewScoped
public class MainViewController {
		
	private static final String PLATFORMS_URL = "mw2mw/platforms";
	private static final String DEVICES_URL = "mw2mw/devices";
	private static final String CLIENTS_URL = "mw2mw/clients";

	private String getBaseUrl(){
//		String baseUrl =  "http://localhost:8080/api/";
		String baseUrl = System.getenv("AIOTES_API");
		if (baseUrl == null || baseUrl.isEmpty()){
			baseUrl = "http://localhost:8080/api/";
		}
		System.out.println("==> " + baseUrl);
//		System.out.println("****> " + System.getenv("AIOTES_API"));
//		Map<String, String> map = System.getenv();
//		for (Entry<String, String> x : map.entrySet()){
//			System.out.println(x.getKey() + " --> " + x.getValue());
//		}
		return baseUrl;
	}
	
	public List<Platform> getPlatforms(String clientID) throws Exception{
		List<Platform> platforms = new ArrayList<Platform>();
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Client-ID", clientID);
		String platformsResult = HTTPClient.sendGet(getBaseUrl() + PLATFORMS_URL, headers);
		JsonArray results = new JsonParser().parse(platformsResult).getAsJsonArray();
		for (int i=0; i < results.size(); i++){
			JsonObject platform = results.get(i).getAsJsonObject();
			Platform p = new Platform();
			p.setPlatformId(platform.get("platformId").getAsString());
			p.setType(platform.get("type").getAsString());
			p.setBaseEndpoint(platform.get("baseEndpoint").getAsString());
			if (!platform.get("location").isJsonNull()){
				p.setLocation(platform.get("location").getAsString());
			}
			if (!platform.get("name").isJsonNull()){
				p.setName(platform.get("name").getAsString());
			}
			if (!platform.get("clientId").isJsonNull()){
				p.setClientId(platform.get("clientId").getAsString());
			}
			JsonObject platformStatistics = platform.get("platformStatistics").getAsJsonObject();
			if (platformStatistics != null && !platformStatistics.isJsonNull() && platformStatistics.has("deviceCount")){
				p.setDeviceCount(platformStatistics.get("deviceCount").getAsInt());
			}
			platforms.add(p);
		}
		return platforms;
	}
	
	public void createClient(String clientID) throws IOException{
		System.out.println("create client = " + clientID);
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Client-ID", clientID);
		JsonObject jsonObj = new JsonObject();
		jsonObj.addProperty("clientId", clientID);
		jsonObj.addProperty("responseFormat", "JSON_LD");
		jsonObj.addProperty("responseDelivery", "CLIENT_PULL");
		jsonObj.addProperty("receivingCapacity", 10);
		HTTPClient.sendPost(getBaseUrl() + CLIENTS_URL, jsonObj.toString(), headers);
	}

	public List<Device> getDevices(String clientID, List<Platform> platforms) throws Exception{
		List<Device> devices = new ArrayList<Device>();
		
		for (Platform p : platforms){
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Client-ID", clientID);
			headers.put("platformId", p.getPlatformId());
			String devicesResult = HTTPClient.sendGet(getBaseUrl() + DEVICES_URL, headers);
			JsonArray results = new JsonParser().parse(devicesResult).getAsJsonArray();
			for (int i=0; i < results.size(); i++){
				JsonObject device = results.get(i).getAsJsonObject();
				Device d = new Device();
				d.setDeviceId(device.get("deviceId").getAsString());
				d.setHostedBy(device.get("hostedBy").getAsString());
				if (!device.get("location").isJsonNull()){
					d.setLocation(device.get("location").getAsString());
				}
				if (!device.get("name").isJsonNull()){
					d.setName(device.get("name").getAsString());
				}
				if (!device.get("detects").isJsonNull()){
					d.setDetects(device.get("detects").getAsString());
				}
				for (JsonElement s : device.get("deviceTypes").getAsJsonArray()){
					d.addDeviceType(s.getAsString());
				}
				for (JsonElement s : device.get("observes").getAsJsonArray()){
					d.addObserves(s.getAsString());
				}
				for (JsonElement s : device.get("hosts").getAsJsonArray()){
					d.addHosts(s.getAsString());
				}
				System.out.println(d);
				if (!devices.contains(d)){
					devices.add(d);
				}
			}		
		}
		return devices;
	}
}
