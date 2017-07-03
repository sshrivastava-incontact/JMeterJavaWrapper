package com.incontact.test.wrapper.jmeter.Samplers;

/**
 * 
 * @author sashrivastava (Sameer Shrivastava)
 *
 */

public class SamplerFactory {

	public JMeterSampler getSampler(String samplerType, String samplerName){

		//add more cases as and when required
		switch(samplerType){
		case "http":
			return new HttpSampler(samplerName);
		case "ftp":
			return new FtpSampler();
		default:
			System.out.println("case not found");
			return null;
		}
	}


}
