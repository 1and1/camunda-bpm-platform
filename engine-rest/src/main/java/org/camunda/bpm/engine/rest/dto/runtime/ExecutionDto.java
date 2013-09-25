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
package org.camunda.bpm.engine.rest.dto.runtime;

import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.engine.runtime.Execution;

public class ExecutionDto {

  private String id;
  private String processInstanceId;
  private boolean ended;
  private String superExecutionId;
  private String parentId;
  private String activityId;
  private String activityName;
  private boolean isActive;
  private int suspensionState;
  
  
  public static ExecutionDto fromExecution(Execution execution) {
    ExecutionDto dto = new ExecutionDto();
    dto.id = execution.getId();
    dto.processInstanceId = execution.getProcessInstanceId();
    dto.ended = execution.isEnded();
    if(execution instanceof ExecutionEntity) {
			dto.superExecutionId = ((ExecutionEntity) execution)
					.getSuperExecutionId();
			dto.parentId = ((ExecutionEntity) execution).getParentId();
			dto.activityId = ((ExecutionEntity) execution)
					.getCurrentActivityId();
			dto.activityName = ((ExecutionEntity) execution)
					.getCurrentActivityName();
			dto.isActive = ((ExecutionEntity) execution).isActive();
			dto.suspensionState = ((ExecutionEntity) execution)
					.getSuspensionState();
    }
    return dto;
  }

  public String getId() {
    return id;
  }

  public String getProcessInstanceId() {
    return processInstanceId;
  }

  public boolean isEnded() {
    return ended;
  }
  
  public String getSuperExecution() {
	return superExecutionId;
 }

 public String getParentId() {
	return parentId;
 }

 public String getActivityId() {
	return activityId;
 }

 public String getActivityName() {
	return activityName;
 }

 public boolean isActive() {
	return isActive;
 }

 public int getSuspensionState() {
	return suspensionState;
 }
}
