package com.ruoyi.bpm.service.task;


import com.ruoyi.bpm.controller.task.vo.activity.BpmActivityRespVO;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;


/**
 * BPM 活动实例 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Slf4j
@Validated
public class BpmActivityServiceImpl implements BpmActivityService {

    @Resource
    private HistoryService historyService;

    @Override
    public List<BpmActivityRespVO> getActivityListByProcessInstanceId(String processInstanceId) {
        List<HistoricActivityInstance> activityList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId).list();
        return activityList.stream().map(activity->{
            BpmActivityRespVO bpmActivityRespVO = new BpmActivityRespVO();
            bpmActivityRespVO.setKey(activity.getActivityId());
            bpmActivityRespVO.setType(activity.getActivityType());
            bpmActivityRespVO.setTaskId(activity.getTaskId());
            bpmActivityRespVO.setStartTime(activity.getStartTime());
            bpmActivityRespVO.setEndTime(activity.getEndTime());
            return bpmActivityRespVO;
        }).toList();
    }

    @Override
    public List<HistoricActivityInstance> getHistoricActivityListByExecutionId(String executionId) {
        return historyService.createHistoricActivityInstanceQuery().executionId(executionId).list();
    }

}
