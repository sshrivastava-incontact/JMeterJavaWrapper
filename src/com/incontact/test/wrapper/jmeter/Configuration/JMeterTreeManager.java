package com.incontact.test.wrapper.jmeter.Configuration;


import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.control.gui.LoopControlPanel;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.threads.gui.ThreadGroupGui;
import org.apache.jorphan.collections.HashTree;

/**
 * 
 * @author sashrivastava (Sameer Shrivastava)
 *
 */
public class JMeterTreeManager {
	
	private static HashTree rootTree;
	
    private JMeterTreeManager(){
    }
	
    private static class SingletonHelper{
        private static final JMeterTreeManager INSTANCE = new JMeterTreeManager();
    }
    
    public static JMeterTreeManager getInstance(){
        return SingletonHelper.INSTANCE;
    }


	public HashTree addThreadGroupToRootTree(TestPlan testPlan, ThreadGroup threadGroup){
		
		return getRootTree().add(testPlan, threadGroup);
	}
	

	public ThreadGroup createThreadGroup(String threadGrpName, int threads, int loops){
		
		// Loop Controller
		LoopController loopController = new LoopController();
		loopController.setLoops(loops);
		loopController.setFirst(true);
		loopController.setProperty(TestElement.TEST_CLASS, LoopController.class.getName());
		loopController.setProperty(TestElement.GUI_CLASS, LoopControlPanel.class.getName());
		loopController.initialize();

		// Thread Group
		ThreadGroup threadGroup = new ThreadGroup();
		threadGroup.setName(threadGrpName);
		threadGroup.setNumThreads(threads);
		threadGroup.setRampUp(1);
		threadGroup.setSamplerController(loopController);
		threadGroup.setProperty(TestElement.TEST_CLASS, ThreadGroup.class.getName());
		threadGroup.setProperty(TestElement.GUI_CLASS, ThreadGroupGui.class.getName());
		
		return threadGroup;
	}
	
	
	public HashTree getRootTree(){
		
		if(rootTree==null){
		     rootTree = new HashTree();
		}
		return rootTree;
	}
	
	public void addTestPlanToRootTree(TestPlan testPlan){
		
		getRootTree().add(testPlan);
	}
	
	public void createTestPlanTree(){
		
	}
	
	public void getTestPlanTree(){
		
	}
	
	public void addSamplerToThreadGroup(){
		
	}
	
	public void addListnerToThreadGroup(){
		
	}
	
	public void addListnerToTestPlan(){
		
	}
	
	
}
