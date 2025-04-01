package hooks;

import java.net.URI;

import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;

public class CustomEventListener implements ConcurrentEventListener {
	
	public static String stepName;
	public static String testCaseName;
	public static Status stepStatus;
	public static Status testCaseStatus;
	public static String source;

    @Override
    public void setEventPublisher(EventPublisher publisher) {
    	publisher.registerHandlerFor(TestSourceRead.class, this::handleTestSourceRead);
        publisher.registerHandlerFor(TestStepFinished.class, this::handleTestStepFinished);
    }
    
    private void handleTestSourceRead(TestSourceRead event) {
    	source = event.getSource();
        // Do something with testCaseName and testCaseStatus
    }
 
    private void handleTestStepFinished(TestStepFinished event) {
    	if (event.getTestStep()instanceof PickleStepTestStep ) {
			PickleStepTestStep testStep = (PickleStepTestStep) event.getTestStep();
			stepName = testStep.getStep().getText();
		}
    	else if (event.getTestStep()instanceof HookTestStep) {
    		HookTestStep  testStep = (HookTestStep) event.getTestStep();
			stepName = testStep.getCodeLocation();
    	}
		stepStatus = event.getResult().getStatus();
    }
}
