package com.incontact.test.configurations;

/**
 * @author sashrivastava (Sameer Shrivastava)
 *
 */

/**
 * Sample Configuration Container
 */

public class TestDomainContainer extends ConfigContainer{

	private String name="default";
	private String url="default";
	private String protocol="http";
	private String port="80"; //generally port should be in int, but JMeter API takes port number as string
	private String serviceType="default";
	private String apiKey="default";
	private String userName="default";
	private String password="default";
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
