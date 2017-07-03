package com.incontact.test.configurations;

/**
 * @author sashrivastava (Sameer Shrivastava)
 *
 */

public class PathProvider {
	
	private final String JMETER_HOME_DIR = "C:\\apache-jmeter-3.2\\apache-jmeter-3.2";
	private final String JMETER_FTL_REPORT_PATH = "report\\report.jtl";
	private final String JMETER_CSV_REPORT_PATH = "report\\report.csv";
	private final String JMETER_JMX_FILE_PATH = "report\\jmeter_api_sample.jmx";
	private final String USER_CONFIG_XML = "config\\config.xml";
	
	
	private PathProvider(){}
	
    private static class SingletonHelper{
        private static final PathProvider INSTANCE = new PathProvider();
    }
    
    public static PathProvider getInstance(){
        return SingletonHelper.INSTANCE;
    }
	
	public String getJmeterHomeDir(){
		return JMETER_HOME_DIR;
	}
	
	public String getJmeterFtlReportPath(){
		return JMETER_FTL_REPORT_PATH;
	}
	
	public String getJmeterCsvReportPath(){
		return JMETER_CSV_REPORT_PATH;
	}

	public String getJmeterJmxFilePath() {
		return JMETER_JMX_FILE_PATH;
	}
	
	public String getUserConfigXmlFilePath(){
		return USER_CONFIG_XML;
	}
	

}
