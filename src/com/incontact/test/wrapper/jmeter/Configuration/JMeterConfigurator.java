package com.incontact.test.wrapper.jmeter.Configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.reporters.Summariser;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.util.JMeterUtils;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import com.incontact.test.configurations.PathProvider;


/**
 * 
 * @author sashrivastava (Sameer Shrivastava)
 *
 */
public class JMeterConfigurator {


	private StandardJMeterEngine jmeterEngine;
	PathProvider pathProvider = PathProvider.getInstance();
	JMeterTreeManager jmeterTreeManager = JMeterTreeManager.getInstance();

	private JMeterConfigurator(){}

	private static class SingletonHelper{
		private static final JMeterConfigurator INSTANCE = new JMeterConfigurator();
	}

	public static JMeterConfigurator getInstance(){
		return SingletonHelper.INSTANCE;
	}

	@BeforeSuite
	public void initializeJMeterEngine(){

		System.out.println("initializing Jmeter Engine..");
		//Set jmeter home for the jmeter utils to load
		File jmeterHome = new File(pathProvider.getJmeterHomeDir());
		String slash = System.getProperty("file.separator");

		if (jmeterHome.exists()) {
			File jmeterProperties = new File(jmeterHome.getPath() + slash + "bin" + slash + "jmeter.properties");
			if (jmeterProperties.exists()) {

				//Create JMeter Engine
				jmeterEngine = new StandardJMeterEngine();

				//JMeter initialization (properties, log levels, locale, etc)
				JMeterUtils.setJMeterHome(jmeterHome.getPath());
				JMeterUtils.loadJMeterProperties(jmeterProperties.getPath());
				JMeterUtils.initLogging();// you can comment this line out to see extra log messages of i.e. DEBUG level
				JMeterUtils.initLocale();


			}
		}
	}

	/**
	 * TODO : Somehow this method is not required, which is a mystery in itself,
	 *  to look into the flow and get the answer
	@AfterTest
	public void configureEngine(){

		System.out.println("configureAfterTest");
		jmeterEngine.configure(jmeterTreeManager.getRootTree());
	}
	 */


	/**
	 * This Method is not used when integrated with testNG
	//@AfterSuite
	public void runTest(){

		System.out.println("running test suites");
		try {
			jmeterEngine.runTest();
			// System.out.println("Test completed. See " + jmeterHome + slash + "report.jtl file for results");
			// System.out.println("JMeter .jmx script is available at " + jmeterHome + slash + "jmeter_api_sample.jmx");
		} catch (JMeterEngineException e) {
			e.printStackTrace();
		}
	}
	 */

	@AfterSuite(alwaysRun = true)
	public void createJmeterJmxFile(){

		System.out.println("creating jmx file for debug support");

		// save generated test plan to JMeter's .jmx file format
		try {
			SaveService.saveTree(jmeterTreeManager.getRootTree(), new FileOutputStream(pathProvider.getJmeterJmxFilePath()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//@BeforeSuite (dependsOnMethods={"initializeJMeterEngine"})
	//TODO : look into this method, not working properly and then include in the processing
	public void createJmeterReports(){

		System.out.println("creating jmeter reports");

		//add Summarizer output to get test progress in stdout like:
		// summary =      2 in   1.3s =    1.5/s Avg:   631 Min:   290 Max:   973 Err:     0 (0.00%)
		Summariser summer = null;
		String summariserName = JMeterUtils.getPropDefault("summariser.name", "summary");
		if (summariserName.length() > 0) {
			summer = new Summariser(summariserName);
		}

		// Store execution results into a .jtl file, we can save file as csv also
		String reportFile = pathProvider.getJmeterFtlReportPath() ;
		String csvFile = pathProvider.getJmeterCsvReportPath();
		ResultCollector logger = new ResultCollector(summer);
		logger.setFilename(reportFile);
		ResultCollector csvlogger = new ResultCollector(summer);
		csvlogger.setFilename(csvFile);
		jmeterTreeManager.getRootTree().add(jmeterTreeManager.getRootTree().getArray()[0], logger);
		jmeterTreeManager.getRootTree().add(jmeterTreeManager.getRootTree().getArray()[0], csvlogger);

	}

	public StandardJMeterEngine getJMeterEngine(){
		return jmeterEngine;
	}
}
