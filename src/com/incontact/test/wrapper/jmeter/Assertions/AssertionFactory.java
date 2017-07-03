package com.incontact.test.wrapper.jmeter.Assertions;


/**
 * 
 * @author sashrivastava (Sameer Shrivastava)
 *
 */

public class AssertionFactory {
	
	/**
	 * To be expanded for other Assertions
	 * @param samplerType
	 * @return
	 */
	
	public JMeterAssertionHandler getAssertion(String samplerType){

		//add more cases as and when required
		switch(samplerType){
		case "size":
			return new SizeAssertionHandler();
		case "duration":
			return new DurationAssertionHandler();
		case "response":
			return new ResponseAssertionHandler();
		default:
			System.out.println("case not found");
			return null;
		}
	}

}
