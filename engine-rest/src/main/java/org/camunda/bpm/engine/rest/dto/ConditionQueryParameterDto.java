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
package org.camunda.bpm.engine.rest.dto;

import java.util.HashMap;
import java.util.Map;
import org.camunda.bpm.engine.impl.QueryOperator;

/**
 * @author roman.smirnov
 */
public class ConditionQueryParameterDto {

  public static final String EQUALS_OPERATOR_NAME = "eq";
  public static final String NOT_EQUALS_OPERATOR_NAME = "neq";
  public static final String GREATER_THAN_OPERATOR_NAME = "gt";
  public static final String GREATER_THAN_OR_EQUALS_OPERATOR_NAME = "gteq";
  public static final String LESS_THAN_OPERATOR_NAME = "lt";
  public static final String LESS_THAN_OR_EQUALS_OPERATOR_NAME = "lteq";
  public static final String LIKE_OPERATOR_NAME = "like";

  private static final Map<String, QueryOperator> OPERATOR_NAME_MAP = new HashMap<String, QueryOperator>() {{
    put(EQUALS_OPERATOR_NAME, QueryOperator.EQUALS);
    put(NOT_EQUALS_OPERATOR_NAME, QueryOperator.NOT_EQUALS);
    put(GREATER_THAN_OPERATOR_NAME, QueryOperator.GREATER_THAN);
    put(GREATER_THAN_OR_EQUALS_OPERATOR_NAME, QueryOperator.GREATER_THAN_OR_EQUAL);
    put(LESS_THAN_OPERATOR_NAME, QueryOperator.LESS_THAN);
    put(LESS_THAN_OR_EQUALS_OPERATOR_NAME, QueryOperator.LESS_THAN_OR_EQUAL);
    put(LIKE_OPERATOR_NAME, QueryOperator.LIKE);
  }};

  private String operator;
  private Object value;

  public String getOperator() {
    return operator;
  }
  public void setOperator(String operator) {
    this.operator = operator;
  }
  public Object getValue() {
    return value;
  }
  public void setValue(Object value) {
    this.value = value;
  }

  public static QueryOperator getQueryOperator(String name) {
    return OPERATOR_NAME_MAP.get(name);
  }
}
