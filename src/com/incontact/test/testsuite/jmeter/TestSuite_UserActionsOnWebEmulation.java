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
 * 
 * @author sashrivastava (Sameer Shrivastava)
 *
 */


public class TestSuite_UserActionsOnWebEmulation {

	/**
	 * In this demo we will be 1st browsing to stackoveflow.com and then logging in
	 * actions were 1st recorded using Blaze Meter and then using JMeter UI
	 * translated into the code. Kinldy update the user name and passowrd in config/config.xml
	 * under item <item name="stackoverflow">
	 */

	JMeterConfigurator jmeterConfigurator = JMeterConfigurator.getInstance();
	JMeterTreeManager jmeterTreeManager = JMeterTreeManager.getInstance();
	PathProvider pathProvider = PathProvider.getInstance();
	TestConfigurationReader testConfigs = TestConfigurationReader.getInstance();
	SamplerFactory samplerFactory = new SamplerFactory();
	TestPlan testPlan;

	public TestSuite_UserActionsOnWebEmulation(){

		// creating a JMeter test plan and adding it in the tree structure 
		testPlan = new TestPlan("StackOverFlow");
		testPlan.setProperty(TestElement.TEST_CLASS, TestPlan.class.getName());
		testPlan.setProperty(TestElement.GUI_CLASS, TestPlanGui.class.getName());

		// add test Plan to the root tree
		jmeterTreeManager.addTestPlanToRootTree(testPlan);

	}

	/**
	 * test case written using record and play.
	 * recording was done using blaze meter (chrome plugin) and then was converted into JMeter script
	 */

	@Test(description="Login @stackoverflow.com")
	public void TestCase1_SO() {


		// create a JMeter 
		ThreadGroup threadGroup = jmeterTreeManager.createThreadGroup("TestCase1_SO", 1, 1);
		HashTree threadGroupHashTree = jmeterTreeManager.addThreadGroupToRootTree(testPlan, threadGroup);
		TestDomainContainer domainContainer = testConfigs.getTestDomainContainer("stackoverflow");

		/**
		 * TODO:  Enhance the capability and features of HttpSampler to 
		 * reduce the test case number of lines of code more.
		 */
		// Step1
		HttpSampler httpSampler1 = (HttpSampler) samplerFactory.getSampler("http", "TC1_SO_STEP1");
		httpSampler1.configureSampler(domainContainer);
		httpSampler1.setHttpMethod(HTTPConstants.GET);
		httpSampler1.setFollowRedirects(true);
		httpSampler1.setUseKeepAlive(true);

		HTTPSampleResult result1 = (HTTPSampleResult) httpSampler1.getSampler().sample();  
		threadGroupHashTree.add(httpSampler1);

		// Assertions for Step1
		assertEquals(true, result1.isSuccessful());
		assertEquals( "200", result1.getResponseCode());
		assertEquals("text/html; charset=utf-8", result1.getContentType());
		assertEquals("utf-8", result1.getDataEncodingNoDefault());
		assertEquals(true, result1.getResponseData().length>0);

		// Step2
		HttpSampler httpSampler2 = (HttpSampler) samplerFactory.getSampler("http", "TC1_SO_STEP2");
		httpSampler2.configureSampler(domainContainer);
		httpSampler2.setHttpMethod(HTTPConstants.GET);
		httpSampler2.setPath("users/login");
		httpSampler2.setFollowRedirects(true);
		httpSampler2.setUseKeepAlive(true);
		httpSampler2.addArgument("ssrc", "head");
		httpSampler2.addArgument("returnurl", "http%3a%2f%2fstackoverflow.com%2f");

		HTTPSampleResult result2 = (HTTPSampleResult) httpSampler2.getSampler().sample();  
		threadGroupHashTree.add(httpSampler2);

		// Assertions for Step2
		assertEquals(true, result2.isSuccessful());
		assertEquals( "200", result2.getResponseCode());
		assertEquals("text/html; charset=utf-8", result2.getContentType());
		assertEquals("utf-8", result2.getDataEncodingNoDefault());
		assertEquals(true, result2.getResponseData().length>0);

		// Step3
		HttpSampler httpSampler3 = (HttpSampler) samplerFactory.getSampler("http", "TC1_SO_STEP3");
		httpSampler3.configureSampler(domainContainer);
		httpSampler3.setHttpMethod(HTTPConstants.POST);
		httpSampler3.setPath("users/login-or-signup/validation/track");
		httpSampler3.setFollowRedirects(true);
		httpSampler3.setUseKeepAlive(true);
		httpSampler3.addArgument("isSignup", "false");
		httpSampler3.addArgument("isPassword", "false");
		httpSampler3.addArgument("openidusername", "");
		httpSampler3.addArgument("ssrc", "head");
		httpSampler3.addArgument("openididentifier", "");
		httpSampler3.addArgument("submitbutton", "Log in");
		httpSampler3.addArgument("oauthserver", "");
		httpSampler3.addArgument("isLogin", "true");
		httpSampler3.addArgument("oauthversion", "");
		httpSampler3.addArgument("isAddLogin", "false");
		httpSampler3.addArgument("password", "YOUR_PASSWORD_HERE"); //provide your password here or configure via config xml/excel file
		httpSampler3.addArgument("hasCaptcha", "false");
		httpSampler3.addArgument("fkey", "50c90264e5ceb3b789a10dbf6c5cfe8b");
		httpSampler3.addArgument("email", "YOUR_USER_NAME_HERE"); //provide username here or configure via config xml/excel file
		
		HTTPSampleResult result3 = (HTTPSampleResult) httpSampler3.getSampler().sample();  
		threadGroupHashTree.add(httpSampler3);

		// Assertions for Step3
		assertEquals(true, result3.isSuccessful());
		assertEquals( "204", result3.getResponseCode());
		assertEquals("text/plain", result3.getContentType());
		//assertEquals("utf-8", result3.getDataEncodingNoDefault());
		assertEquals(false, result3.getResponseData().length>0);
		
		// Step4
		HttpSampler httpSampler4 = (HttpSampler) samplerFactory.getSampler("http", "TC1_SO_STEP4");
		httpSampler4.configureSampler(domainContainer);
		httpSampler4.setHttpMethod(HTTPConstants.POST);
		httpSampler4.setPath("users/login-or-signup/validation/track");
		httpSampler4.setFollowRedirects(true);
		httpSampler4.setUseKeepAlive(true);
		httpSampler4.addArgument("password", "YOUR_PASSWORD_HERE"); //provide your password here or configure via config xml/excel file
		httpSampler4.addArgument("fkey", "50c90264e5ceb3b789a10dbf6c5cfe8b");
		httpSampler4.addArgument("oauth_server", "");
		httpSampler4.addArgument("ssrc", "head");
		httpSampler4.addArgument("returnurl", "http%3a%2f%2fstackoverflow.com%2f");
		httpSampler4.addArgument("openid_identifier", "");
		httpSampler4.addArgument("email", "YOUR_USER_NAME_HERE"); //provide usename here or configure via config xml/excel file
		httpSampler4.addArgument("oauth_version", "");
		httpSampler4.addArgument("openid_username", "");
		

		HTTPSampleResult result4 = (HTTPSampleResult) httpSampler4.getSampler().sample();  
		threadGroupHashTree.add(httpSampler4);

		// Assertions for Step3
		assertEquals(true, result4.isSuccessful());
		assertEquals( "204", result4.getResponseCode());
		assertEquals("text/plain", result4.getContentType());

	}

}
