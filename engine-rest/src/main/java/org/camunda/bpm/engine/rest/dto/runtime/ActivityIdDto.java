package org.camunda.bpm.engine.rest.dto.runtime;

import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;

public class ActivityIdDto {

	  private String id;
	  private String name;
	  
	  public static ActivityIdDto fromActivity(ActivityImpl activity) {
		  ActivityIdDto result = new ActivityIdDto();    
	    result.id = activity.getId();
	    result.name = (String) activity.getProperty("name");
	    return result;
	  }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
