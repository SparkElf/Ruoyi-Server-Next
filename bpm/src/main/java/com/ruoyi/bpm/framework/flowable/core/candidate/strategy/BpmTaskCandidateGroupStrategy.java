package com.ruoyi.bpm.framework.flowable.core.candidate.strategy;


import com.ruoyi.bpm.domain.definition.BpmUserGroupDO;
import com.ruoyi.bpm.enums.task.BpmTaskCandidateStrategyEnum;
import com.ruoyi.bpm.framework.flowable.core.candidate.BpmTaskCandidateStrategy;
import com.ruoyi.bpm.service.definition.BpmUserGroupService;
import com.ruoyi.common.utils.StrUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.ruoyi.common.utils.CollectionUtils.convertSetByFlatMap;


/**
 * 用户组 {@link BpmTaskCandidateStrategy} 实现类
 *
 * @author kyle
 */
@Component
public class BpmTaskCandidateGroupStrategy implements BpmTaskCandidateStrategy {

    @Resource
    private BpmUserGroupService userGroupService;

    @Override
    public BpmTaskCandidateStrategyEnum getStrategy() {
        return BpmTaskCandidateStrategyEnum.USER_GROUP;
    }

    @Override
    public void validateParam(String param) {
        Set<Long> groupIds = StrUtils.splitToLongSet(param);
        userGroupService.getUserGroupList(groupIds);
    }

    @Override
    public Set<Long> calculateUsers(DelegateExecution execution, String param) {
        Set<Long> groupIds = StrUtils.splitToLongSet(param);
        List<BpmUserGroupDO> groups = userGroupService.getUserGroupList(groupIds);
        return convertSetByFlatMap(groups, BpmUserGroupDO::getUserIds, Collection::stream);
    }

}