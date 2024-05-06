package com.ruoyi.bpm.framework.flowable.core.candidate.expression;


import com.ruoyi.bpm.service.task.BpmProcessInstanceService;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

/**
 * 分配给发起人审批的 Expression 流程表达式
 *
 * @author 芋道源码
 */
@Component
public class BpmTaskAssignStartUserExpression {

    @Resource
    private BpmProcessInstanceService processInstanceService;

    public Set<Long> calculateUsers(org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl execution) {
        ProcessInstance processInstance = processInstanceService.getProcessInstance(execution.getProcessInstanceId());
        Long startUserId = Long.parseLong(processInstance.getStartUserId());
        return Set.of(startUserId);
    }

}
