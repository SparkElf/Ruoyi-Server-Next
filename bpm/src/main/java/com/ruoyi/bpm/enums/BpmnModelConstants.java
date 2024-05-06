package com.ruoyi.bpm.enums;

import org.flowable.engine.runtime.ProcessInstance;

/**
 * BPMN XML 常量信息
 *
 * @author 芋道源码
 */
public interface BpmnModelConstants {

    String BPMN_FILE_SUFFIX = ".bpmn";

    /**
     * BPMN 中的命名空间
     */
    String NAMESPACE = "http://flowable.org/bpmn";

    /**
     * BPMN UserTask 的扩展属性，用于标记候选人策略
     */
    String USER_TASK_CANDIDATE_STRATEGY = "candidateStrategy";
    /**
     * BPMN UserTask 的扩展属性，用于标记候选人参数
     */
    String USER_TASK_CANDIDATE_PARAM = "candidateParam";

    /**
     * 流程实例的变量 - 状态
     *
     * @see ProcessInstance#getProcessVariables()
     */
    String PROCESS_INSTANCE_VARIABLE_STATUS = "PROCESS_STATUS";
    /**
     * 流程实例的变量 - 发起用户选择的审批人 Map
     *
     * @see ProcessInstance#getProcessVariables()
     */
    String PROCESS_INSTANCE_VARIABLE_START_USER_SELECT_ASSIGNEES = "PROCESS_START_USER_SELECT_ASSIGNEES";

    /**
     * 任务的变量 - 状态
     *
     * @see org.flowable.task.api.Task#getTaskLocalVariables()
     */
   String TASK_VARIABLE_STATUS = "TASK_STATUS";
    /**
     * 任务的变量 - 理由
     *
     * 例如说：审批通过、不通过的理由
     *
     * @see org.flowable.task.api.Task#getTaskLocalVariables()
     */
    String TASK_VARIABLE_REASON = "TASK_REASON";
}
