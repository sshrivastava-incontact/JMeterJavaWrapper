package com.incontact.test.testsuite.jmeter;


/**
 * @author sashrivastava (Sameer Shrivastava)
 *
 */

import static org.testng.Assert.assertEquals;
import org.apache.jmeter.control.gui.TestPlanGui;
import org.apache.jmeter.protocol.http.control.gui.HttpTestSampleGui;
import org.apache.jmeter.protocol.http.sampler.HTTPSampleResult;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.protocol.http.util.HTTPConstants;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jorphan.collections.HashTree;
import org.testng.annotations.Test;
import com.incontact.test.configurations.PathProvider;
import com.incontact.test.configurations.TestConfigurationReader;
import com.incontact.test.configurations.TestDomainContainer;
import com.incontact.test.wrapper.jmeter.Configuration.JMeterConfigurator;
import com.incontact.test.wrapper.jmeter.Configuration.JMeterTreeManager;
import com.incontact.test.wrapper.jmeter.Samplers.SamplerFactory;

public class TestSuite_AwsLambdaApiGateway_withoutWrapper {


	/**
	 * Test Cases to demo the TC writing without using wrappers over JMeter.
	 * this is for comparison purpose, for comparison with test cases using wrapper.
	 * compare these test cases with the test cases in TestSuite_AwsLambdaApiGateway.java
	 */
	
	JMeterConfigurator jmeterConfigurator = JMeterConfigurator.getInstance();
	JMeterTreeManager jmeterTreeManager = JMeterTreeManager.getInstance();
	PathProvider pathProvider = PathProvider.getInstance();
	TestConfigurationReader testConfigs =TestConfigurationReader.getInstance();
	SamplerFactory samplerFactory = new SamplerFactory();
	TestPlan testPlan;

	public TestSuite_AwsLambdaApiGateway_withoutWrapper(){

		// creating a JMeter test plan and adding it in the tree structure 
		testPlan = new TestPlan("AwsLambdaApiGateway");
		testPlan.setProperty(TestElement.TEST_CLASS, TestPlan.class.getName());
		testPlan.setProperty(TestElement.GUI_CLASS, TestPlanGui.class.getName());

		// add test Plan to the root tree
		jmeterTreeManager.addTestPlanToRootTree(testPlan);

	}


	@Test(description="send http POST method to a AWS Lambda via API gateway")
	public void TestCase1() {


		// create a JMeter thread
		ThreadGroup threadGroup = jmeterTreeManager.createThreadGroup("TestCase1", 1, 1);
		HashTree threadGroupHashTree = jmeterTreeManager.addThreadGroupToRootTree(testPlan, threadGroup);
		TestDomainContainer domainContainer = testConfigs.getTestDomainContainer("awsLambda");

		// First HTTP Sampler 
		HTTPSamplerProxy httpSampler = new HTTPSamplerProxy(); 
		httpSampler.setDomain(domainContainer.getUrl());
		httpSampler.setPort(Integer.parseInt(domainContainer.getPort()));
		httpSampler.setProtocol(domainContainer.getProtocol()); 
		httpSampler.setPath("/");
		httpSampler.setMethod(HTTPConstants.POST);
		httpSampler.addNonEncodedArgument("", testConfigs.getTestJasonString(), "");
		httpSampler.setPostBodyRaw(true);

		httpSampler.setName("HTTPSampler");
		httpSampler.setProperty(TestElement.TEST_CLASS, HTTPSamplerProxy.class.getName());
		httpSampler.setProperty(TestElement.GUI_CLASS, HttpTestSampleGui.class.getName());

		HTTPSampleResult result = (HTTPSampleResult) httpSampler.sample();  

		threadGroupHashTree.add(httpSampler);
		//jmeterConfigurator.createJmeterReports();

		assertEquals( "200", result.getResponseCode());


	}
	
	@Test(description="ErrorScenario : send http POST method to a AWS Lambda via API gateway, wrong assert")
	public void TestCase2() {


		// create a JMeter 
		ThreadGroup threadGroup = jmeterTreeManager.createThreadGroup("TestCase2", 1, 1);
		HashTree threadGroupHashTree = jmeterTreeManager.addThreadGroupToRootTree(testPlan, threadGroup);
		TestDomainContainer domainContainer = testConfigs.getTestDomainContainer("awsLambda");


		// First HTTP Sampler 
		HTTPSamplerProxy httpSampler = new HTTPSamplerProxy();
		httpSampler.setDomain(domainContainer.getUrl());
		httpSampler.setPort(Integer.parseInt(domainContainer.getPort()));
		httpSampler.setProtocol(domainContainer.getProtocol()); 
		httpSampler.setPath("/");
		httpSampler.setMethod(HTTPConstants.POST);
		httpSampler.addNonEncodedArgument("", testConfigs.getTestJasonString(), ""); 
		httpSampler.setPostBodyRaw(true);

		httpSampler.setName("HTTPSampler");
		httpSampler.setProperty(TestElement.TEST_CLASS, HTTPSamplerProxy.class.getName());
		httpSampler.setProperty(TestElement.GUI_CLASS, HttpTestSampleGui.class.getName());

		HTTPSampleResult result = (HTTPSampleResult) httpSampler.sample();  

		threadGroupHashTree.add(httpSampler);
		//jmeterConfigurator.createJmeterReports();

		assertEquals( "100", result.getResponseCode());


	}

}
