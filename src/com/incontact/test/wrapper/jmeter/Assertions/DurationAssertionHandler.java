package com.incontact.test.wrapper.jmeter.Assertions;


/**
 * 
 * @author sashrivastava (Sameer Shrivastava)
 *
 */

import org.apache.jmeter.assertions.DurationAssertion;


public class DurationAssertionHandler implements JMeterAssertionHandler {


	// Create Duration Assertion
	/**.................................................
	 * null pointer issue that happens here happens when locale is not
	 * initialized before calling this method in JMeterUtils, is tracked by
	 * https://bz.apache.org/bugzilla/show_bug.cgi?id=61050, 
	 * 
	 */
	private DurationAssertion durationAssertion = new DurationAssertion();
	
	public DurationAssertionHandler(){
		
	}
	
	public DurationAssertion getAssertion(){
		return durationAssertion;
	}
	
	
	// configurations to be expanded as per requirement
	public void configureAssertion(String name, String comment, int duration,
			DurationAssertionScope scope, String variableName){
		
		durationAssertion.setName(name);
		durationAssertion.setComment(comment);
		durationAssertion.setAllowedDuration(duration);
		
		switch(scope){
		
		case PARENT:
			durationAssertion.setScopeParent();
			break;
		case CHILDREN:
			durationAssertion.setScopeChildren();
			break;
		case ALL:
			durationAssertion.setScopeAll();
			break;
		case VARIABLE:
			durationAssertion.setScopeVariable(variableName);
			break;
		default:
			System.out.println("Warning : Matching case not found : "+scope);
			break;
		}
		
		durationAssertion.setEnabled(true);	
	}
	
	
	public enum DurationAssertionScope {
		
		PARENT,
		CHILDREN,
		ALL,
		VARIABLE
	}
	   
}
