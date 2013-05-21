/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.engine.rest.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.rest.ProcessInstanceRestService;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.DeleteEngineEntityDto;
import org.camunda.bpm.engine.rest.dto.runtime.ProcessInstanceDto;
import org.camunda.bpm.engine.rest.dto.runtime.ProcessInstanceQueryDto;
import org.camunda.bpm.engine.rest.dto.runtime.SignalProcessInstanceDto;
import org.camunda.bpm.engine.rest.dto.runtime.VariableListDto;
import org.camunda.bpm.engine.rest.dto.runtime.VariableValueDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstanceQuery;

public class ProcessInstanceRestServiceImpl extends AbstractRestProcessEngineAware implements
    ProcessInstanceRestService {

  public ProcessInstanceRestServiceImpl() {
    super();
  }
  
  public ProcessInstanceRestServiceImpl(String engineName) {
    super(engineName);
  }
  

  @Override
  public ProcessInstanceDto getProcessInstance(String processInstanceId) {
    RuntimeService runtimeService = getProcessEngine().getRuntimeService();
    ProcessInstance instance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
    
    if (instance == null) {
      throw new InvalidRequestException(Status.NOT_FOUND, "Process instance with id " + processInstanceId + " does not exist");
    }
    
    ProcessInstanceDto result = ProcessInstanceDto.fromProcessInstance(instance);
    return result;
  }
  
  @Override
  public void deleteProcessInstance(String processInstanceId,
      DeleteEngineEntityDto dto) {
    RuntimeService runtimeService = getProcessEngine().getRuntimeService();
    try {
      runtimeService.deleteProcessInstance(processInstanceId, dto.getDeleteReason());
    } catch (ProcessEngineException e) {
      throw new InvalidRequestException(Status.NOT_FOUND, e, "Process instance with id " + processInstanceId + " does not exist");
    }
    
  }

  @Override
  public List<ProcessInstanceDto> getProcessInstances(
      UriInfo uriInfo, Integer firstResult, Integer maxResults) {
    ProcessInstanceQueryDto queryDto = new ProcessInstanceQueryDto(uriInfo.getQueryParameters());
    return queryProcessInstances(queryDto, firstResult, maxResults);
  }

  @Override
  public List<ProcessInstanceDto> queryProcessInstances(
      ProcessInstanceQueryDto queryDto, Integer firstResult, Integer maxResults) {
    RuntimeService runtimeService = getProcessEngine().getRuntimeService();
    ProcessInstanceQuery query = queryDto.toQuery(runtimeService);
    
    List<ProcessInstance> matchingInstances;
    if (firstResult != null || maxResults != null) {
      matchingInstances = executePaginatedQuery(query, firstResult, maxResults);
    } else {
      matchingInstances = query.list();
    }
    
    List<ProcessInstanceDto> instanceResults = new ArrayList<ProcessInstanceDto>();
    for (ProcessInstance instance : matchingInstances) {
      ProcessInstanceDto resultInstance = ProcessInstanceDto.fromProcessInstance(instance);
      instanceResults.add(resultInstance);
    }
    return instanceResults;
  }
  
  private List<ProcessInstance> executePaginatedQuery(ProcessInstanceQuery query, Integer firstResult, Integer maxResults) {
    if (firstResult == null) {
      firstResult = 0;
    }
    if (maxResults == null) {
      maxResults = Integer.MAX_VALUE;
    }
    return query.listPage(firstResult, maxResults); 
  }

  @Override
  public CountResultDto getProcessInstancesCount(UriInfo uriInfo) {
    ProcessInstanceQueryDto queryDto = new ProcessInstanceQueryDto(uriInfo.getQueryParameters());
    return queryProcessInstancesCount(queryDto);
  }

  @Override
  public CountResultDto queryProcessInstancesCount(ProcessInstanceQueryDto queryDto) {
    RuntimeService runtimeService = getProcessEngine().getRuntimeService();
    ProcessInstanceQuery query = queryDto.toQuery(runtimeService);
    
    long count = query.count();
    CountResultDto result = new CountResultDto();
    result.setCount(count);
    
    return result;
  }

  @Override
  public VariableListDto getVariables(String processInstanceId) {
    List<VariableValueDto> values = new ArrayList<VariableValueDto>();

    for (Map.Entry<String, Object> entry : getProcessEngine().getRuntimeService().getVariables(processInstanceId).entrySet()) {
      values.add(new VariableValueDto(entry.getKey(), entry.getValue(), entry.getValue().getClass().getSimpleName()));
    }

    return new VariableListDto(values);
  }

  @Override
  public void suspendProcessInstance(String processInstanceId) {
	
	try {
	  RuntimeService runtimeService = getProcessEngine().getRuntimeService();
	  runtimeService.suspendProcessInstanceById(processInstanceId);
	} catch (ProcessEngineException e){
	    throw new InvalidRequestException(Status.NOT_FOUND, e, "Process instance with id " + processInstanceId + " does not exist");
	} 
  }

  @Override
  public void activateProcessInstance(String processInstanceId) {
	try {
	  RuntimeService runtimeService = getProcessEngine().getRuntimeService();
	  runtimeService.activateProcessInstanceById(processInstanceId);
	} catch (ProcessEngineException e){
	  throw new InvalidRequestException(Status.NOT_FOUND, e, "Process instance with id " + processInstanceId + " does not exist");
	}
  }

  @Override
  public void signalProcessInstance(String processInstanceId, SignalProcessInstanceDto parameter) {
	
	try {
		RuntimeService runtimeService = getProcessEngine().getRuntimeService();
		String activityId = parameter.getActivityId();
		
		if (activityId != null && activityId.length() > 0) {
		
		  Execution execution = runtimeService.createExecutionQuery()
				  .processInstanceId(processInstanceId)
				  .activityId(activityId)
				  .singleResult();
		  
		  if (parameter.getVariables() != null) {
		    runtimeService.signal(execution.getId(), parameter.getVariables());
		  } else {
			  runtimeService.signal(execution.getId());  
		  }
		} else {
			throw new InvalidRequestException(Status.BAD_REQUEST, "Parameter activityId is missing");
		}
	} catch (ProcessEngineException e){
		throw new InvalidRequestException(Status.NOT_FOUND, e, "Process instance with id " + processInstanceId + " does not exist");
	}
  }
}
