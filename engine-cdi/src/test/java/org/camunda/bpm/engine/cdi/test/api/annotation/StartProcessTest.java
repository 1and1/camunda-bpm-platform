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
package org.camunda.bpm.engine.cdi.test.api.annotation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.camunda.bpm.engine.cdi.BusinessProcess;
import org.camunda.bpm.engine.cdi.impl.annotation.StartProcessInterceptor;
import org.camunda.bpm.engine.cdi.test.CdiProcessEngineTestCase;
import org.camunda.bpm.engine.cdi.test.impl.beans.DeclarativeProcessController;
import org.camunda.bpm.engine.test.Deployment;
import org.junit.Test;

/**
 * Testcase for assuring that the {@link StartProcessInterceptor} behaves as
 * expected.
 * 
 * @author Daniel Meyer
 */
public class StartProcessTest extends CdiProcessEngineTestCase {

  @Test
  @Deployment(resources = "org/camunda/bpm/engine/cdi/test/api/annotation/StartProcessTest.bpmn20.xml")
  public void testStartProcessByKey() {

    assertNull(runtimeService.createProcessInstanceQuery().singleResult());

    getBeanInstance(DeclarativeProcessController.class).startProcessByKey();
    BusinessProcess businessProcess = getBeanInstance(BusinessProcess.class);

    assertNotNull(runtimeService.createProcessInstanceQuery().singleResult());

    assertEquals("Activiti", businessProcess.getVariable("name"));
    
    businessProcess.startTask(taskService.createTaskQuery().singleResult().getId());
    businessProcess.completeTask();
  }


}
