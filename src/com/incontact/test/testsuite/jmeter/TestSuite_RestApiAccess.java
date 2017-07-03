package com.incontact.test.testsuite.jmeter;

import static org.testng.Assert.assertEquals;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.jmeter.assertions.AssertionResult;
import org.apache.jmeter.control.gui.TestPlanGui;
import org.apache.jmeter.protocol.http.sampler.HTTPSampleResult;
import org.apache.jmeter.protocol.http.util.HTTPConstants;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jorphan.collections.HashTree;
import org.testng.annotations.Test;
import com.incontact.test.configurations.PathProvider;
import com.incontact.test.configurations.TestConfigurationReader;
import com.incontact.test.configurations.TestDomainContainer;
import com.incontact.test.wrapper.jmeter.Assertions.AssertionFactory;
import com.incontact.test.wrapper.jmeter.Assertions.DurationAssertionHandler;
import com.incontact.test.wrapper.jmeter.Assertions.DurationAssertionHandler.DurationAssertionScope;
import com.incontact.test.wrapper.jmeter.Assertions.ResponseAssertionHandler;
import com.incontact.test.wrapper.jmeter.Assertions.ResponseAssertionHandler.ResponseAssertionMatchRule;
import com.incontact.test.wrapper.jmeter.Assertions.ResponseAssertionHandler.ResponseAssertionTestFields;
import com.incontact.test.wrapper.jmeter.Assertions.SizeAssertionHandler;
import com.incontact.test.wrapper.jmeter.Assertions.SizeAssertionHandler.SizeAssertionTestField;
import com.incontact.test.wrapper.jmeter.Assertions.SizeAssertionHandler.SizeAssertionTypeOfComparision;
import com.incontact.test.wrapper.jmeter.Configuration.JMeterConfigurator;
import com.incontact.test.wrapper.jmeter.Configuration.JMeterTreeManager;
import com.incontact.test.wrapper.jmeter.Samplers.HttpSampler;
import com.incontact.test.wrapper.jmeter.Samplers.SamplerFactory;

/**
 * 
 * @author sashrivastava (Sameer Shrivastava)
 *
 */

public class TestSuite_RestApiAccess {

	/**
	 * for this demo we will be using 
	 * API from http://www.cricapi.com/how-to-use.aspx to fetch the 
	 * live cricket score, currently the api key being used is associated with
	 * author's account, kindly update /config/config.xml <item name="cricketApi">
	 * node with your key. config items and structure is for demo purpose and has to be 
	 * updated and evolved as per the framework needs and design.
	 */
	JMeterConfigurator jmeterConfigurator = JMeterConfigurator.getInstance();
	JMeterTreeManager jmeterTreeManager = JMeterTreeManager.getInstance();
	PathProvider pathProvider = PathProvider.getInstance();
	TestConfigurationReader testConfigs = TestConfigurationReader.getInstance();
	SamplerFactory samplerFactory = new SamplerFactory();
	AssertionFactory assertionFactory = new AssertionFactory();
	TestPlan testPlan;

	public TestSuite_RestApiAccess(){

		// creating a JMeter test plan and adding it in the tree structure 
		testPlan = new TestPlan("RestApiAccess");
		testPlan.setProperty(TestElement.TEST_CLASS, TestPlan.class.getName());
		testPlan.setProperty(TestElement.GUI_CLASS, TestPlanGui.class.getName());

		// add test Plan to the root tree
		jmeterTreeManager.addTestPlanToRootTree(testPlan);
	}

	@Test(description="send a request to server supporting rest api using HTTP Sampler")
	public void RestApi_TestCase1() {


		// Create a JMeter thread group
		ThreadGroup threadGroup = jmeterTreeManager.createThreadGroup("TestCase1_Rest", 1, 1);
		HashTree threadGroupHashTree = jmeterTreeManager.addThreadGroupToRootTree(testPlan, threadGroup);
		TestDomainContainer domainContainer = testConfigs.getTestDomainContainer("cricketApi");

		// Create HTTP Sampler 
		HttpSampler httpSampler = (HttpSampler) samplerFactory.getSampler("http", "FixureRequest");
		httpSampler.setHttpMethod(HTTPConstants.POST);
		httpSampler.configureSampler(domainContainer);
		httpSampler.setPath("api/matches/"); // this can also come from the configuration file
		httpSampler.addBodyToRequest(testConfigs.getTestJasonString());

		// Get result from sampler
		HTTPSampleResult result = (HTTPSampleResult) httpSampler.getSampler().sample();  
		
		/**
		 * Following is the demo of how we can use JMeter Assertions
		 */
		
		// Create JMeter Response Assertion (also test JMeter Assertion feature)
		List<String> testPatterns = new ArrayList<String>();
		testPatterns.add("\"matchStarted\":false");
		ResponseAssertionHandler resAsserHandler = (ResponseAssertionHandler) assertionFactory.getAssertion("response");
		resAsserHandler.configureAssertion("ResponseAssertionForRest", "",
				ResponseAssertionTestFields.RESPONSE_DATA, ResponseAssertionMatchRule.SUBSTRING
				, testPatterns);
		
		// Create JMeter Duration Assertion (also test JMeter Assertion feature)
		DurationAssertionHandler durationAsserHandler = (DurationAssertionHandler) assertionFactory.getAssertion("duration");
		durationAsserHandler.configureAssertion("DurationAssertionForRest", "",
				1, DurationAssertionScope.ALL, "");
		
		// Create JMeter Size Assertion (also test JMeter Assertion feature)
		SizeAssertionHandler sizeAssertionHandler = (SizeAssertionHandler) assertionFactory.getAssertion("size");
		sizeAssertionHandler.configureAssertion("SizeAssertionForRest", "", 5000,
				SizeAssertionTestField.FULL_RESPONSE, SizeAssertionTypeOfComparision.LESS_THAN_OR_EQUAL);

		
		// Get assertion result from received response, Evaluating JMeter Assertions
		AssertionResult resAsserResult = resAsserHandler.getAssertion().getResult(result);
		AssertionResult durationAsserRes = durationAsserHandler.getAssertion().getResult(result);
		AssertionResult sizeAssertionRes = sizeAssertionHandler.getAssertion().getResult(result);
		
		// Assert Received Response and Assertions using TestNG features
		assertEquals( "200", result.getResponseCode());
		assertEquals(true,resAsserResult.isFailure());
		assertEquals(true, durationAsserRes.isFailure());
		assertEquals(false, sizeAssertionRes.isFailure());
		
		threadGroupHashTree.add(httpSampler);
		threadGroupHashTree.add(resAsserResult);
		threadGroupHashTree.add(durationAsserRes);
		threadGroupHashTree.add(sizeAssertionRes);

		/**
		 * Following logs are for Demo Purpose
		 */
		String responseByteToString = new String(result.getResponseData(), StandardCharsets.UTF_8);
		System.out.println("ResponsFromSampler : \n"+responseByteToString);
		System.out.println("\nResponseAssertionResult : \n"+
							"isError : " + resAsserResult.isError()+"\n"+
							"isFailure : "+resAsserResult.isFailure()+"\n"+
							"failure Message : "+resAsserResult.getFailureMessage());
		System.out.println("\nDurationAssertionResult : \n"+
							"isError : " + durationAsserRes.isError()+"\n"+
							"isFailure : "+durationAsserRes.isFailure()+"\n"+
							"failure Message : "+durationAsserRes.getFailureMessage());
		System.out.println("\nSizeAssertionResult : \n"+
							"isError : " + sizeAssertionRes.isError()+"\n"+
							"isFailure : "+sizeAssertionRes.isFailure()+"\n"+
							"failure Message : "+sizeAssertionRes.getFailureMessage());

	}

}
