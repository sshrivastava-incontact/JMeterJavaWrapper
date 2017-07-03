package com.incontact.test.wrapper.jmeter.Assertions;

import org.apache.jmeter.assertions.SizeAssertion;

/**
 * 
 * @author sashrivastava (Sameer Shrivastava)
 *
 */

public class SizeAssertionHandler implements JMeterAssertionHandler {


	private SizeAssertion sizeAssertion = new SizeAssertion();


	public SizeAssertionHandler(){

	}

	public SizeAssertion getAssertion(){
		return sizeAssertion;
	}

	public void configureAssertion(String name, String comment, int size,
			SizeAssertionTestField testField, SizeAssertionTypeOfComparision typeOfcomparision ){

		sizeAssertion.setName(name);
		sizeAssertion.setComment(comment);
		sizeAssertion.setAllowedSize(size);

		switch(testField){

		case FULL_RESPONSE:
			sizeAssertion.setTestFieldNetworkSize();
			break;
		case RESPONSE_HEADER:
			sizeAssertion.setTestFieldResponseHeaders();
			break;
		case RESPONSE_BODY:
			sizeAssertion.setTestFieldResponseBody();
			break;
		case RESPONSE_CODE:
			sizeAssertion.setTestFieldResponseCode();
			break;
		case RESPONSE_MESSAGE:
			sizeAssertion.setTestFieldResponseMessage();
			break;
		default:
			System.out.println("Warning : Matching case not found : "+testField);
			break;
		}
		
		switch(typeOfcomparision){
		
		case EQUAL:
			sizeAssertion.setCompOper(1);
			break;
		case NOT_EQUAL:
			sizeAssertion.setCompOper(2);
			break;
		case GREATER_THAN:
			sizeAssertion.setCompOper(3);
			break;
		case LESS_THAN:
			sizeAssertion.setCompOper(4);
			break;
		case GREATER_THAN_OR_EQUAL:
			sizeAssertion.setCompOper(5);
			break;
		case LESS_THAN_OR_EQUAL:
			sizeAssertion.setCompOper(6);
			break;
		default:
			System.out.println("Warning : Matching case not found : "+typeOfcomparision);
		}

		sizeAssertion.setEnabled(true);
	}



	public enum SizeAssertionApplyTo {

		MAIN_AND_SUB_SAMPLE,
		MAIN_SAMPLE,
		SUB_SAMPLE,
		JMETER_VARIABLE
	}

	public enum SizeAssertionTestField {

		FULL_RESPONSE,
		RESPONSE_HEADER,
		RESPONSE_BODY,
		RESPONSE_CODE,
		RESPONSE_MESSAGE
	}

	public enum SizeAssertionTypeOfComparision {

		EQUAL,
		NOT_EQUAL,
		GREATER_THAN,
		LESS_THAN,
		GREATER_THAN_OR_EQUAL,
		LESS_THAN_OR_EQUAL	
	}




}
