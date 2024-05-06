package com.ruoyi.bpm.framework.flowable.core.candidate.strategy;


import com.ruoyi.bpm.enums.task.BpmTaskCandidateStrategyEnum;
import com.ruoyi.bpm.framework.flowable.core.candidate.BpmTaskCandidateStrategy;
import com.ruoyi.common.utils.StrUtils;
import com.ruoyi.system.service.ISysRoleService;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

/**
 * 角色 {@link BpmTaskCandidateStrategy} 实现类
 *
 * @author kyle
 */
@Component
public class BpmTaskCandidateRoleStrategy implements BpmTaskCandidateStrategy {

    @Resource
    private ISysRoleService roleApi;


    @Override
    public BpmTaskCandidateStrategyEnum getStrategy() {
        return BpmTaskCandidateStrategyEnum.ROLE;
    }

    @Override
    public void validateParam(String param) {
        Set<Long> roleIds = StrUtils.splitToLongSet(param);
        roleApi.validateRoleList(roleIds);
    }

    @Override
    public Set<Long> calculateUsers(DelegateExecution execution, String param) {
        Set<Long> roleIds = StrUtils.splitToLongSet(param);
        return roleApi.getUserRoleIdListByRoleIds(roleIds);
    }

}