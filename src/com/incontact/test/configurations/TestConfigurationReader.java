package com.incontact.test.configurations;

/**
 * @author sashrivastava (Sameer Shrivastava)
 *
 */


import org.testng.annotations.BeforeSuite;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class TestConfigurationReader {
	

	PathProvider pathProvider = PathProvider.getInstance();
	private String testJasonString = "{\"operation\": \"echo\",\"payload\": {\"somekey1\": \"somevalue1\",\"somekey2\": \"somevalue2\"}}";
	private HashMap<String, TestDomainContainer> testDomainContainerrMap = new HashMap<String, TestDomainContainer>();

	private TestConfigurationReader(){
		
	}
	
    private static class SingletonHelper{
        private static final TestConfigurationReader INSTANCE = new TestConfigurationReader();
    }
    
    public static TestConfigurationReader getInstance(){
        return SingletonHelper.INSTANCE;
    }
    
	public String getTestJasonString() {
		return testJasonString;
	}

	public TestDomainContainer getTestDomainContainer(String key){
		
		return testDomainContainerrMap.get(key);
	}

	@BeforeSuite
	public void loadConfigurations(){
		
		/* TODO : 
		 *  if userInputXML then load from xml
		 *  else load from excel
		 *  this  initialization is not very clean, need to create a class from where
		 *  all the initialization takes place in sequence.
		 * */
		 
		TestConfigurationReader.getInstance().loadConfigurationFromXml();
	}

	/*
	 * should contain a loader method that will parse an input file and load all the values and fields required.
	 * 2 options should be provided to the user a) 1 time parsing of all the variables b) fetch a parameter as and when required.
	 */

	public void loadConfigurationFromXml(){

		try {
			
			/*
			 * Read the xml content as string and then remove all the carriage return and new lines
			 * to avoid false nodes while parsing the xml content which is the case when \n and \r are treated as nodes 
			 * as per mixed format of xml. we don't change the file content itself as it is not recommended to change
			 * the content of user provided configuration file. 
			 * TODO : it looks like the replace all is not working, need another way to format xml
			 */
			//File inputFile = new File(pathProvider.getUserConfigXmlFilePath());
			String configXmlContent = new String(Files.readAllBytes(Paths.get(pathProvider.getUserConfigXmlFilePath())));
			configXmlContent.replaceAll("(?:\\n|\\r)", "");
		    InputSource inputSource = new InputSource();
		    inputSource.setCharacterStream(new StringReader(configXmlContent));
		    
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			//Document doc = builder.parse(inputFile);
			Document doc = builder.parse(inputSource);
			doc.getDocumentElement().normalize();
			System.out.println("Parsing Configuration xml");

			/*
			 * want to parse the elements in the "Configuration Tag, others are ignored"
			 */
			//NodeList configNodeList = doc.getDocumentElement().getElementsByTagName("Configurations");
			NodeList configNodeList = doc.getDocumentElement().getChildNodes();

			if(configNodeList==null){
				System.out.println("Err: configNodesList is null");
				return;
			}
			
			// can not use iterator as node list do not implement Iterator Interface
			for(int index=0; index<configNodeList.getLength();index++){

				Node configNode = configNodeList.item(index);
				if (configNode.getNodeType() == Node.ELEMENT_NODE) {
					Element configNodeElement = (Element) configNode;
					
					//parse nodes with tags "TestDoamin"
					//NodeList testDomainList = configNodeElement.getElementsByTagName("TestDomain");
					NodeList testDomainList = configNodeElement.getChildNodes();
					extractTestDoimans(testDomainList);
					
					//extract other nodes and values here
				}else{
					System.out.println("Warning : This is a Node Type : "+configNode.getNodeType() + "skipping" );
				}       		
			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void extractTestDoimans(NodeList testDomainList){

		if(testDomainList==null||testDomainList.getLength()<=0){
			System.out.println("Err: node is null or empty");
			return;
		}
		
		for(int itemIndex=0; itemIndex<testDomainList.getLength();itemIndex++){
			
			Node itemMainNode = testDomainList.item(itemIndex);
			
			if (itemMainNode.getNodeType() == Node.ELEMENT_NODE) {
				
				Element itemMainNodeElement = (Element) itemMainNode;
				//NodeList itemList = testDomainNodeElement.getElementsByTagName("item");
				TestDomainContainer domainContainer = new TestDomainContainer();
				domainContainer.setName(itemMainNodeElement.getAttributes().getNamedItem("name").getNodeValue());
				NodeList itemChildList = itemMainNodeElement.getChildNodes();
				if(itemChildList==null||itemChildList.getLength()<=0){
					
					System.out.println("Err: node is null or empty");
					continue;
				}
				
				for(int itemChildIndex=0; itemChildIndex<itemChildList.getLength();itemChildIndex++){
					
					Node itemChild = itemChildList.item(itemChildIndex);
					if (itemChild.getNodeType() == Node.ELEMENT_NODE) {
						
						Element itemChildElement = (Element) itemChild;
							
							switch(itemChildElement.getNodeName()){
								
							case "url":
								domainContainer.setUrl(itemChildElement.getTextContent());
								break;
							case "protocol":
								domainContainer.setProtocol(itemChildElement.getTextContent());
								break;
							case "port":
								domainContainer.setPort(itemChildElement.getTextContent());
								break;
							case "serviceType":
								domainContainer.setServiceType(itemChildElement.getTextContent());
								break;
							case "apiKey":
								domainContainer.setApiKey(itemChildElement.getTextContent());
								break;
							case "username":
								domainContainer.setUserName(itemChildElement.getTextContent());
								break;
							case "password":
								domainContainer.setPassword(itemChildElement.getTextContent());
								break;
							default :
								System.out.println("Warning: case not found for "+itemChildElement.getNodeName());
								break;						
							}
													
					}else{
						System.out.println("Warning : This is a Node Type : "+itemChild.getNodeType() + "skipping" );

					}
				}	
				testDomainContainerrMap.put(domainContainer.getName(),domainContainer);
			}else{
				System.out.println("Warning : This is a Node Type : "+itemMainNode.getNodeType() + "skipping" );
			}     
		}

	}

	public void loadConfigurationFromExcel(){

	}

}

