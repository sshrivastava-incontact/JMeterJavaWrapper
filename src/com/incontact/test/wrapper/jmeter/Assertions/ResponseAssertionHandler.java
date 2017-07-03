package com.incontact.test.wrapper.jmeter.Assertions;

import java.util.List;
import org.apache.jmeter.assertions.ResponseAssertion;

/**
 * 
 * @author sashrivastava (Sameer Shrivastava)
 *
 */
public class ResponseAssertionHandler implements JMeterAssertionHandler {

	ResponseAssertion responseAssertion = new ResponseAssertion();

	public ResponseAssertionHandler(){

	}

	public ResponseAssertion getAssertion(){
		return responseAssertion;
	}

	public void configureAssertion(String name, String comments,
			ResponseAssertionTestFields testField, ResponseAssertionMatchRule matchRule,
			List<String> testPatterns){

		// Create Response Assertion
		responseAssertion.setName("ResponseAssertionForRest");

		switch(testField){

		case SAMPLE_URL:
			responseAssertion.setTestFieldURL();
			break;
		case RESPONSE_DATA:
			responseAssertion.setTestFieldResponseData();
			break;
		case RESPONSE_DATA_AS_DOCUMENT:
			responseAssertion.setTestFieldResponseDataAsDocument();
			break;
		case RESPONSE_CODE:
			responseAssertion.setTestFieldResponseCode();
			break;
		case RESPONSE_MESSAGE:
			responseAssertion.setTestFieldResponseMessage();
			break;
		case RESPONSE_HEADERS:
			responseAssertion.setTestFieldResponseHeaders();
			break;
		case REQUEST_HEADERS:
			responseAssertion.setTestFieldRequestHeaders();
			break;
		default:
			System.out.println("warning : matching case not found : "+testField);
		}


		/**note : Contains, Matches: Perl5-style regular expressions
		 *       Equals, Substring: plain text, case-sensitive
		 */

		switch(matchRule){

		case MATCH:
			responseAssertion.setToMatchType();
			break;
		case CONTAINS:
			responseAssertion.setToContainsType();
			break;
		case NOT:
			responseAssertion.setToNotType();
			break;
		case EQUALS:
			responseAssertion.setToEqualsType();
			break;
		case SUBSTRING:
			responseAssertion.setToSubstringType();
			break;
		case OR:
			responseAssertion.setToOrType();
			break;
		default:
			System.out.println("warning : matching case not found : "+matchRule);
		}
		
		for(String pattern : testPatterns){
			responseAssertion.addTestString(pattern);
		}

		responseAssertion.setEnabled(true);
	}

	public enum ResponseAssertionTestFields {

		SAMPLE_URL,
		RESPONSE_DATA,
		RESPONSE_DATA_AS_DOCUMENT,
		RESPONSE_CODE,
		RESPONSE_MESSAGE,
		RESPONSE_HEADERS,
		REQUEST_HEADERS
	}

	public enum ResponseAssertionMatchRule {

		MATCH,
		CONTAINS,
		NOT,
		EQUALS,
		SUBSTRING,
		OR
	}
}
