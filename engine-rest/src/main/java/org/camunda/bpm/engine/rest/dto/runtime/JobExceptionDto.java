package org.camunda.bpm.engine.rest.dto.runtime;

public class JobExceptionDto {

	private String stackTrace;

	public static JobExceptionDto fromTrace(String trace){
	   JobExceptionDto dto = new JobExceptionDto();
	   dto.stackTrace = trace;		
	   return dto;
	}
	
	public String getStackTrace() {
		return stackTrace;
	}
}
