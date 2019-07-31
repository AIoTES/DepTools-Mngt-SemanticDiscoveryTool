package org.activage.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Device {
	
	private List<String> deviceTypes = new ArrayList<String>();
	private String deviceId;
	private String hostedBy;
	private String location;
	private String name;
	private List<String> hosts = new ArrayList<String>();
	private List<String> observes = new ArrayList<String>();
	private String detects;
	private double similarityValue;
	
	public Device() {
		super();
	}
	
	public void addDeviceType(String deviceType){
		deviceTypes.add(deviceType);
	}
	
	public void addHosts(String host){
		hosts.add(host);
	}
	
	public void addObserves(String host){
		observes.add(host);
	}

	public List<String> getDeviceTypes() {
		return deviceTypes;
	}

	public void setDeviceTypes(List<String> deviceTypes) {
		this.deviceTypes = deviceTypes;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getHostedBy() {
		return hostedBy;
	}

	public void setHostedBy(String hostedBy) {
		this.hostedBy = hostedBy;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getHosts() {
		return hosts;
	}

	public void setHosts(List<String> hosts) {
		this.hosts = hosts;
	}

	public List<String> getObserves() {
		return observes;
	}

	public void setObserves(List<String> observes) {
		this.observes = observes;
	}

	public String getDetects() {
		return detects;
	}

	public void setDetects(String detects) {
		this.detects = detects;
	}
	
	public double getSimilarityValue() {
		return similarityValue;
	}

	public void setSimilarityValue(double similarityValue) {
		this.similarityValue = similarityValue;
	}

	public double findSimilarityScore(Device other){
		double score = 0;
		score += sameObjects(deviceTypes, other.deviceTypes) * 0.5;
		score += sameObjects(hosts, other.hosts) * 0.3;
		score += sameObjects(observes, other.observes) * 0.1;
		score += sameStrings(hostedBy, other.hostedBy) * 0.1;
		return score;
	}
	
	private double sameStrings(String s1, String s2){
		if (nullOrEmpty(s1) && nullOrEmpty(s2)){
			return 1;
		}
		if ((nullOrEmpty(s1) && !nullOrEmpty(s2)) || (nullOrEmpty(s2) && !nullOrEmpty(s1))){
			return 0;
		}
		if (s1.toLowerCase().equals(s2.toLowerCase())){
			return 1;
		}
		return 0;
	}
	
	private boolean nullOrEmpty(String s){
		return s == null || s.isEmpty();
	}
	
	private double sameObjects(Collection<? extends Object> collection1, Collection<? extends Object> collection2){
		if (collection1.isEmpty() && collection2.isEmpty()){
			return 1;
		}
		if ((collection1.isEmpty() && !collection2.isEmpty()) || (collection2.isEmpty() && !collection1.isEmpty())){
			return 0;
		}
		int same = 0;
		for (Object obj1 : collection1){
			for (Object obj2 : collection2){
				if (obj1.equals(obj2))
					same++;
			}
		}
		return same*1.0/collection1.size();
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((deviceId == null) ? 0 : deviceId.hashCode());
		result = prime * result
				+ ((hostedBy == null) ? 0 : hostedBy.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Device other = (Device) obj;
		if (deviceId == null) {
			if (other.deviceId != null)
				return false;
		} else if (!deviceId.equals(other.deviceId))
			return false;
		if (hostedBy == null) {
			if (other.hostedBy != null)
				return false;
		} else if (!hostedBy.equals(other.hostedBy))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Device [deviceTypes=" + deviceTypes + ", deviceId=" + deviceId
				+ ", hostedBy=" + hostedBy + ", location=" + location
				+ ", name=" + name + ", hosts=" + hosts + ", observes="
				+ observes + ", detects=" + detects + "]";
	}
	
}
