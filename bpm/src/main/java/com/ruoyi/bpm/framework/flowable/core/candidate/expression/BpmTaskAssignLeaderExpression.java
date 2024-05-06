package com.ruoyi.bpm.framework.flowable.core.candidate.expression;


import com.ruoyi.bpm.service.task.BpmProcessInstanceService;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysUserService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Set;


import static java.util.Collections.emptySet;

/**
 * 分配给发起人的 Leader 审批的 Expression 流程表达式
 * 目前 Leader 的定义是，发起人所在部门的 Leader
 *
 * @author 芋道源码
 */
@Component
public class BpmTaskAssignLeaderExpression {

    @Resource
    private ISysUserService userApi;
    @Resource
    private ISysDeptService deptApi;

    @Resource
    private BpmProcessInstanceService processInstanceService;

    public Set<Long> calculateUsers(DelegateExecution execution, int level) {
        Assert.isTrue(level > 0, "level 必须大于 0");
        // 获得发起人
        ProcessInstance processInstance = processInstanceService.getProcessInstance(execution.getProcessInstanceId());
        Long startUserId = Long.parseLong(processInstance.getStartUserId());
        // 获得对应 leve 的部门
        SysDept dept = null;
        for (int i = 0; i < level; i++) {
            // 获得 level 对应的部门
            if (dept == null) {
                dept = getStartUserDept(startUserId);
                if (dept == null) { // 找不到发起人的部门，所以无法使用该规则
                    return emptySet();
                }
            } else {
                SysDept parentDept = deptApi.selectDeptById(dept.getParentId());
                if (parentDept == null) { // 找不到父级部门，所以只好结束寻找。原因是：例如说，级别比较高的人，所在部门层级比较少
                    break;
                }
                dept = parentDept;
            }
        }
        var leader=userApi.selectUserByUserName(dept.getLeader());
        return leader != null ? Set.of(leader.getUserId()) : emptySet();
    }

    private SysDept getStartUserDept(Long startUserId) {
        SysUser startUser = userApi.selectUserById(startUserId);
        if (startUser.getDeptId() == null) { // 找不到部门，所以无法使用该规则
            return null;
        }
        return deptApi.selectDeptById(startUser.getDeptId());
    }

}
