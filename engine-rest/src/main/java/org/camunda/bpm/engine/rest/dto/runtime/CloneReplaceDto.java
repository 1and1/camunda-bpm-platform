package org.camunda.bpm.engine.rest.dto.runtime;


public class CloneReplaceDto {
    String targetActivityId;

	 public static CloneReplaceDto fromActivityId(String activityId) {
		 CloneReplaceDto dto = new CloneReplaceDto();    
	    dto.targetActivityId = activityId;	 
	    return dto;
	  }
    
	public String getTargetActivityId() {
		return targetActivityId;
	}

	public void setTargetActivityId(String targetActivityId) {
		this.targetActivityId = targetActivityId;
	}
    
    
}
