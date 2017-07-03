package com.incontact.test.testsuite.jmeter;

import static org.testng.Assert.assertEquals;
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
import com.incontact.test.wrapper.jmeter.Samplers.HttpSampler;
import com.incontact.test.wrapper.jmeter.Configuration.JMeterConfigurator;
import com.incontact.test.wrapper.jmeter.Configuration.JMeterTreeManager;
import com.incontact.test.wrapper.jmeter.Samplers.SamplerFactory;

/**
 * @author sashrivastava (Sameer Shrivastava)
 *
 */


public class TestSuite_AwsLambdaApiGateway {


	/**
	 * Test Cases to demo the TC writing using wrappers over JMeter.
	 * this is for comparison purpose, for comparison with test cases without using wrapper.
	 * compare these test cases with the test cases in TestSuite_AwsLambdaApiGateway_withoutWrapper.java
	 */
	
	JMeterConfigurator jmeterConfigurator = JMeterConfigurator.getInstance();
	JMeterTreeManager jmeterTreeManager = JMeterTreeManager.getInstance();
	PathProvider pathProvider = PathProvider.getInstance();
	TestConfigurationReader testConfigs =TestConfigurationReader.getInstance();
	SamplerFactory samplerFactory = new SamplerFactory();
	TestPlan testPlan;

	public TestSuite_AwsLambdaApiGateway(){

		// creating a JMeter test plan and adding it in the tree structure 
		testPlan = new TestPlan("AwsLambdaApiGateway");
		testPlan.setProperty(TestElement.TEST_CLASS, TestPlan.class.getName());
		testPlan.setProperty(TestElement.GUI_CLASS, TestPlanGui.class.getName());

		// add test Plan to the root tree
		jmeterTreeManager.addTestPlanToRootTree(testPlan);

	}

	
	@Test(description="wrapper test send http POST method to a AWS Lambda via API gateway")
	public void TestCaseX() {


		// create a JMeter thread
		ThreadGroup threadGroup = jmeterTreeManager.createThreadGroup("TestCaseX", 1, 1);
		HashTree threadGroupHashTree = jmeterTreeManager.addThreadGroupToRootTree(testPlan, threadGroup);
		TestDomainContainer domainContainer = testConfigs.getTestDomainContainer("awsLambda");

		// Create HTTP Sampler 
		HttpSampler httpSampler = (HttpSampler) samplerFactory.getSampler("http", "MySampler");
		httpSampler.setHttpMethod(HTTPConstants.POST);
		httpSampler.configureSampler(domainContainer);
		httpSampler.addBodyToRequest(testConfigs.getTestJasonString());
		
		// Get the Result
		HTTPSampleResult result = (HTTPSampleResult) httpSampler.getSampler().sample();  

		// Add sampler in JMeter thread group
		threadGroupHashTree.add( httpSampler.getSampler());

		// Do all the assertions you want
		assertEquals( "200", result.getResponseCode());
	}

	@Test(description="ErrorScenario : send http POST method to a AWS Lambda via API gateway, wrong assert")
	public void TestCase2() {


		// create a JMeter 
		ThreadGroup threadGroup = jmeterTreeManager.createThreadGroup("TestCase2", 1, 1);
		HashTree threadGroupHashTree = jmeterTreeManager.addThreadGroupToRootTree(testPlan, threadGroup);
		TestDomainContainer domainContainer = testConfigs.getTestDomainContainer("awsLambda");


		// Create HTTP Sampler 
		HttpSampler httpSampler = (HttpSampler) samplerFactory.getSampler("http", "MySampler");
		httpSampler.setHttpMethod(HTTPConstants.POST);
		httpSampler.configureSampler(domainContainer);
		httpSampler.addBodyToRequest(testConfigs.getTestJasonString());
		
		// Get the Result
		HTTPSampleResult result = (HTTPSampleResult) httpSampler.getSampler().sample();  

		// Add sampler in JMeter thread group
		threadGroupHashTree.add( httpSampler.getSampler());

		// Do all the assertions you want
		assertEquals( "100", result.getResponseCode());


	}

}
