package org.camunda.bpm.engine.test.bpmn.async;

import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.history.HistoricDetail;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.test.PluggableProcessEngineTestCase;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.test.Deployment;
import org.junit.Assert;

public class AsyncStartEventTest extends PluggableProcessEngineTestCase {

  @Deployment
  public void testAsyncStartEvent() {
    runtimeService.startProcessInstanceByKey("asyncStartEvent");
    
    Task task = taskService.createTaskQuery().singleResult();
    Assert.assertNull("The user task should not have been reached yet", task);
    
    Assert.assertEquals(1, runtimeService.createExecutionQuery().activityId("startEvent").count());
    
    waitForJobExecutorToProcessAllJobs(6000L);
    task = taskService.createTaskQuery().singleResult();
    
    Assert.assertEquals(0, runtimeService.createExecutionQuery().activityId("startEvent").count());
    
    Assert.assertNotNull("The user task should have been reached", task);
  }
  
  @Deployment
  public void testAsyncStartEventListeners() {
    ProcessInstance instance = runtimeService.startProcessInstanceByKey("asyncStartEvent");
    
    Assert.assertNull(runtimeService.getVariable(instance.getId(), "listener"));
    
    waitForJobExecutorToProcessAllJobs(6000L);
    
    Assert.assertNotNull(runtimeService.getVariable(instance.getId(), "listener"));
  }
  
  @Deployment(resources = "org/camunda/bpm/engine/test/bpmn/async/AsyncStartEventTest.testAsyncStartEvent.bpmn20.xml")
  public void testAsyncStartEventHistory() {
    if(processEngineConfiguration.getHistoryLevel() > ProcessEngineConfigurationImpl.HISTORYLEVEL_NONE) {
      runtimeService.startProcessInstanceByKey("asyncStartEvent");

      HistoricProcessInstance historicInstance = historyService.createHistoricProcessInstanceQuery().singleResult();
      Assert.assertNotNull(historicInstance);
      Assert.assertNotNull(historicInstance.getStartTime());

      HistoricActivityInstance historicStartEvent = historyService.createHistoricActivityInstanceQuery().singleResult();
      Assert.assertNull(historicStartEvent);
    }
  }

  @Deployment(resources = "org/camunda/bpm/engine/test/bpmn/async/AsyncStartEventTest.testAsyncStartEvent.bpmn20.xml")
  public void testAsyncStartEventVariableHistory() {
    Map<String, Object> variables = new HashMap<String, Object>();
    variables.put("foo", "bar");
    String processInstanceId = runtimeService.startProcessInstanceByKey("asyncStartEvent", variables).getId();

    assertEquals(1, runtimeService.createProcessInstanceQuery().count());

    executeAvailableJobs();

    Task task = taskService.createTaskQuery().singleResult();
    assertNotNull(task);

    taskService.complete(task.getId());

    // assert process instance is ended
    assertEquals(0, runtimeService.createProcessInstanceQuery().count());

    if(processEngineConfiguration.getHistoryLevel() > ProcessEngineConfigurationImpl.HISTORYLEVEL_ACTIVITY) {
      HistoricVariableInstance variable = historyService.createHistoricVariableInstanceQuery().singleResult();
      assertNotNull(variable);
      assertEquals("foo", variable.getVariableName());
      assertEquals("bar", variable.getValue());
      assertEquals(processInstanceId, variable.getActivtyInstanceId());

      if(processEngineConfiguration.getHistoryLevel() > ProcessEngineConfigurationImpl.HISTORYLEVEL_AUDIT) {

        String startEventId = historyService
            .createHistoricActivityInstanceQuery()
            .activityId("startEvent")
            .singleResult()
            .getId();

        HistoricDetail historicDetail = historyService
            .createHistoricDetailQuery()
            .singleResult();

        assertEquals(startEventId, historicDetail.getActivityInstanceId());

      }

    }
  }

  @Deployment
  public void testMultipleAsyncStartEvents() {
    Map<String, Object> variables = new HashMap<String, Object>();
    variables.put("foo", "bar");
    runtimeService.correlateMessage("newInvoiceMessage", new HashMap<String, Object>(), variables);

    assertEquals(1, runtimeService.createProcessInstanceQuery().count());

    executeAvailableJobs();

    Task task = taskService.createTaskQuery().singleResult();
    assertNotNull(task);
    assertEquals("taskAfterMessageStartEvent", task.getTaskDefinitionKey());

    taskService.complete(task.getId());

    // assert process instance is ended
    assertEquals(0, runtimeService.createProcessInstanceQuery().count());

  }

  @Deployment(resources = {"org/camunda/bpm/engine/test/bpmn/async/AsyncStartEventTest.testMultipleAsyncStartEvents.bpmn20.xml"})
  public void testMultipleAsyncStartEventsVariableHistory() {
    Map<String, Object> variables = new HashMap<String, Object>();
    variables.put("foo", "bar");
    runtimeService.correlateMessage("newInvoiceMessage", new HashMap<String, Object>(), variables);

    assertEquals(1, runtimeService.createProcessInstanceQuery().count());

    executeAvailableJobs();

    Task task = taskService.createTaskQuery().singleResult();
    assertNotNull(task);
    taskService.complete(task.getId());

    // assert process instance is ended
    assertEquals(0, runtimeService.createProcessInstanceQuery().count());

    if(processEngineConfiguration.getHistoryLevel() > ProcessEngineConfigurationImpl.HISTORYLEVEL_ACTIVITY) {

      String processInstanceId = historyService
          .createHistoricProcessInstanceQuery()
          .singleResult()
          .getId();

      HistoricVariableInstance variable = historyService.createHistoricVariableInstanceQuery().singleResult();
      assertNotNull(variable);
      assertEquals("foo", variable.getVariableName());
      assertEquals("bar", variable.getValue());
      assertEquals(processInstanceId, variable.getActivtyInstanceId());

      if(processEngineConfiguration.getHistoryLevel() > ProcessEngineConfigurationImpl.HISTORYLEVEL_AUDIT) {

        String theStartActivityInstanceId = historyService
            .createHistoricActivityInstanceQuery()
            .activityId("messageStartEvent")
            .singleResult()
            .getId();

        HistoricDetail historicDetail = historyService
            .createHistoricDetailQuery()
            .singleResult();

        assertEquals(theStartActivityInstanceId, historicDetail.getActivityInstanceId());

      }
    }

  }
}
