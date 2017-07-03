package com.incontact.test.wrapper.jmeter.Samplers;

import java.util.HashMap;

import org.apache.jmeter.protocol.http.control.gui.HttpTestSampleGui;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.protocol.http.util.HTTPConstants;
import org.apache.jmeter.testelement.TestElement;
import com.incontact.test.configurations.TestDomainContainer;

/**
 * 
 * @author sashrivastava (Sameer Shrivastava)
 *
 */
public class HttpSampler implements JMeterSampler {

	private HTTPSamplerProxy sampler = new HTTPSamplerProxy();
	private String samplerName = "default";
	
	public HttpSampler(String samplerName){
		this.samplerName = samplerName;
	}
	
	public HTTPSamplerProxy getSampler(){

		return sampler; 
	}
	

	public void configureSampler(TestDomainContainer container) {

		sampler.setProperty(TestElement.TEST_CLASS, HTTPSamplerProxy.class.getName());
		sampler.setProperty(TestElement.GUI_CLASS, HttpTestSampleGui.class.getName());
		sampler.setDomain(container.getUrl());
		sampler.setPort(Integer.parseInt(container.getPort()));
		sampler.setProtocol(container.getProtocol()); 
		sampler.setPath("/");
		sampler.setPostBodyRaw(true);
		sampler.setName(samplerName);
		if(!container.getApiKey().equals("default")){
			sampler.addArgument("apikey", container.getApiKey());
		}
	}
	
	public void addBodyToRequest(String body){
		sampler.addNonEncodedArgument("", body, "");
	}
	
	public void setPath(String path){
		sampler.setPath(path);
	}
	
	public void setHttpMethod(String method){
		
		//add more cases as and when required
		switch(method){
		case HTTPConstants.POST:
			sampler.setMethod(HTTPConstants.POST);
			break;
		case HTTPConstants.GET:
			sampler.setMethod(HTTPConstants.GET);
			break;
		default:
			System.out.println("Matching case not found : "+method);
			break;
		}
	}
	
	public void setFollowRedirects(boolean value){
		sampler.setFollowRedirects(value);
	}
	
	public void setUseKeepAlive(boolean value){
		sampler.setUseKeepAlive(value);
	}
	
	public void addArgument(String key, String value){
		sampler.addArgument(key, value);
	}
	
	public void addArgument(HashMap<String, String> argumentMap){
		
		for(String key : argumentMap.keySet()){
			sampler.addArgument(key, argumentMap.get(key));
		}
	}
}
