package org.camunda.bpm.engine.rest.dto.runtime;

import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;

public class ActivityIdDto {

	  protected String id;
	  protected String name;

	  public String getId() {
	    return id;
	  }

	  public String getName() {
	    return name; 
	  }
	  
	  public static ActivityIdDto fromActivity(ActivityImpl activity) {
		  ActivityIdDto result = new ActivityIdDto();    
	    result.id = activity.getId();
	    result.name = (String) activity.getProperty("name");
	    return result;
	  }
}
