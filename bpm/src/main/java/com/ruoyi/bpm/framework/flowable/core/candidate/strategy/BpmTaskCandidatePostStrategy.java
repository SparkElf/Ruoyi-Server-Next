package com.ruoyi.bpm.framework.flowable.core.candidate.strategy;


import com.ruoyi.bpm.enums.task.BpmTaskCandidateStrategyEnum;
import com.ruoyi.bpm.framework.flowable.core.candidate.BpmTaskCandidateStrategy;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.StrUtils;
import com.ruoyi.system.service.ISysPostService;
import com.ruoyi.system.service.ISysUserService;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

import static com.ruoyi.common.utils.CollectionUtils.convertSet;


/**
 * 岗位 {@link BpmTaskCandidateStrategy} 实现类
 *
 * @author kyle
 */
@Component
public class BpmTaskCandidatePostStrategy implements BpmTaskCandidateStrategy {

    @Resource
    private ISysPostService postApi;
    @Resource
    private ISysUserService userApi;

    @Override
    public BpmTaskCandidateStrategyEnum getStrategy() {
        return BpmTaskCandidateStrategyEnum.POST;
    }

    @Override
    public void validateParam(String param) {
        Set<Long> postIds = StrUtils.splitToLongSet(param);
        postApi.validatePostList(postIds);
    }

    @Override
    public Set<Long> calculateUsers(DelegateExecution execution, String param) {
        Set<Long> postIds = StrUtils.splitToLongSet(param);
        List<SysUser> users = userApi.getUserListByPostIds(postIds);
        return convertSet(users, SysUser::getUserId);
    }

}