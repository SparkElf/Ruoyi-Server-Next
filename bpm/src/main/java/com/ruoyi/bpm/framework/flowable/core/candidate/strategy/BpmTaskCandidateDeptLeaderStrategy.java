package com.ruoyi.bpm.framework.flowable.core.candidate.strategy;


import com.ruoyi.bpm.framework.flowable.core.candidate.BpmTaskCandidateStrategy;
import com.ruoyi.bpm.enums.task.BpmTaskCandidateStrategyEnum;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.utils.StrUtils;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysUserService;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

import static com.ruoyi.common.utils.CollectionUtils.convertSet;


/**
 * 部门的负责人 {@link BpmTaskCandidateStrategy} 实现类
 *
 * @author kyle
 */
@Component
public class BpmTaskCandidateDeptLeaderStrategy implements BpmTaskCandidateStrategy {

    @Resource
    private ISysDeptService deptApi;
    @Resource
    private ISysUserService userApi;
    @Override
    public BpmTaskCandidateStrategyEnum getStrategy() {
        return BpmTaskCandidateStrategyEnum.DEPT_LEADER;
    }

    @Override
    public void validateParam(String param) {
        Set<Long> deptIds = StrUtils.splitToLongSet(param);
        deptApi.validateDeptList(deptIds);
    }

    @Override
    public Set<Long> calculateUsers(DelegateExecution execution, String param) {
        Set<Long> deptIds = StrUtils.splitToLongSet(param);
        List<SysDept> depts = deptApi.getDeptList(deptIds);
        return convertSet(depts, sysDept -> {
            var user=userApi.selectUserByUserName(sysDept.getLeader());
            return user.getUserId();
        });
    }

}