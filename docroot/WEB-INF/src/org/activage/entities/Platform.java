package org.activage.entities;

public class Platform {
	
	private String platformId;
	private String type;
	private String baseEndpoint;
	private String location;
	private String name;
	private String clientId;
	private int deviceCount = 0;
	
	
	public Platform() {
		super();
	}


	public String getPlatformId() {
		return platformId;
	}


	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getBaseEndpoint() {
		return baseEndpoint;
	}


	public void setBaseEndpoint(String baseEndpoint) {
		this.baseEndpoint = baseEndpoint;
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


	public String getClientId() {
		return clientId;
	}


	public void setClientId(String clientId) {
		this.clientId = clientId;
	}


	public int getDeviceCount() {
		return deviceCount;
	}


	public void setDeviceCount(int deviceCount) {
		this.deviceCount = deviceCount;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((platformId == null) ? 0 : platformId.hashCode());
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
		Platform other = (Platform) obj;
		if (platformId == null) {
			if (other.platformId != null)
				return false;
		} else if (!platformId.equals(other.platformId))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "Platform [platformId=" + platformId + ", type=" + type
				+ ", baseEndpoint=" + baseEndpoint + ", location=" + location
				+ ", name=" + name + ", clientId=" + clientId
				+ ", deviceCount=" + deviceCount + "]";
	}
}
