package com.ruoyi.bpm.framework.flowable.core.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.map.MapUtil;
import com.ruoyi.bpm.controller.definition.vo.process.BpmProcessDefinitionRespVO;
import com.ruoyi.bpm.controller.task.vo.instance.BpmProcessInstanceRespVO;
import com.ruoyi.bpm.domain.definition.BpmCategoryDO;
import com.ruoyi.bpm.domain.definition.BpmFormDO;
import com.ruoyi.bpm.domain.definition.BpmProcessDefinitionInfoDO;
import com.ruoyi.bpm.event.BpmProcessInstanceStatusEvent;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.CollectionUtils;
import com.ruoyi.common.utils.MapUtils;
import com.ruoyi.common.utils.object.BeanUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.UserTask;
import org.flowable.common.engine.impl.db.SuspensionState;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;

import java.util.List;
import java.util.Map;

public class ProcessUtils {
    public static List<BpmProcessDefinitionRespVO> buildProcessDefinitionList(List<ProcessDefinition> list,
                                                                              Map<String, Deployment> deploymentMap,
                                                                              Map<String, BpmProcessDefinitionInfoDO> processDefinitionInfoMap,
                                                                              Map<Long, BpmFormDO> formMap,
                                                                              Map<String, BpmCategoryDO> categoryMap) {
        return CollectionUtils.convertList(list, definition -> {
            Deployment deployment = MapUtil.get(deploymentMap, definition.getDeploymentId(), Deployment.class);
            BpmProcessDefinitionInfoDO processDefinitionInfo = MapUtil.get(processDefinitionInfoMap, definition.getId(), BpmProcessDefinitionInfoDO.class);
            BpmFormDO form = null;
            if (processDefinitionInfo != null) {
                form = MapUtil.get(formMap, processDefinitionInfo.getFormId(), BpmFormDO.class);
            }
            BpmCategoryDO category = MapUtil.get(categoryMap, definition.getCategory(), BpmCategoryDO.class);
            return buildProcessDefinition(definition, deployment, processDefinitionInfo, form, category, null, null);
        });
    }
    public static BpmProcessDefinitionRespVO buildProcessDefinition(ProcessDefinition definition,
                                                                    Deployment deployment,
                                                                    BpmProcessDefinitionInfoDO processDefinitionInfo,
                                                                    BpmFormDO form,
                                                                    BpmCategoryDO category,
                                                                    BpmnModel bpmnModel,
                                                                    List<UserTask> startUserSelectUserTaskList) {
        BpmProcessDefinitionRespVO respVO = BeanUtils.toBean(definition, BpmProcessDefinitionRespVO.class);
        respVO.setSuspensionState(definition.isSuspended() ? SuspensionState.SUSPENDED.getStateCode() : SuspensionState.ACTIVE.getStateCode());
        // Deployment
        if (deployment != null) {
            respVO.setDeploymentTime(deployment.getDeploymentTime());
        }
        // BpmProcessDefinitionInfoDO
        if (processDefinitionInfo != null) {
            BeanUtil.copyProperties(processDefinitionInfo, respVO,"id");
            // Form
            if (form != null) {
                respVO.setFormName(form.getName());
            }
        }
        // Category
        if (category != null) {
            respVO.setCategoryName(category.getName());
        }
        // BpmnModel
        if (bpmnModel != null) {
            respVO.setBpmnXml(BpmnModelUtils.getBpmnXml(bpmnModel));
            respVO.setStartUserSelectTasks(BeanUtils.toBean(startUserSelectUserTaskList, BpmProcessDefinitionRespVO.UserTask.class));
        }
        return respVO;
    }
    public static BpmProcessInstanceStatusEvent buildProcessInstanceStatusEvent(Object source, HistoricProcessInstance instance, Integer status) {
        return new BpmProcessInstanceStatusEvent(source).setId(instance.getId()).setStatus(status)
                .setProcessDefinitionKey(instance.getProcessDefinitionKey()).setBusinessKey(instance.getBusinessKey());
    }
    public static BpmProcessInstanceStatusEvent buildProcessInstanceStatusEvent(Object source, ProcessInstance instance, Integer status) {;
        return new BpmProcessInstanceStatusEvent(source).setId(instance.getId()).setStatus(status)
                .setProcessDefinitionKey(instance.getProcessDefinitionKey()).setBusinessKey(instance.getBusinessKey());
    }
    public static List<BpmProcessInstanceRespVO> buildBpmProcessInstanceList(List<HistoricProcessInstance> pageResult,
                                                                          Map<String, ProcessDefinition> processDefinitionMap,
                                                                          Map<String, BpmCategoryDO> categoryMap,
                                                                          Map<String, List<Task>> taskMap,
                                                                          Map<Long, SysUser> userMap,
                                                                          Map<Long, SysDept> deptMap) {
        List<BpmProcessInstanceRespVO> vpPageResult = BeanUtils.toBean(pageResult, BpmProcessInstanceRespVO.class);
        for (int i = 0; i < pageResult.size(); i++) {
            BpmProcessInstanceRespVO respVO = vpPageResult.get(i);
            respVO.setStatus(FlowableUtils.getProcessInstanceStatus(pageResult.get(i)));
            MapUtils.findAndThen(processDefinitionMap, respVO.getProcessDefinitionId(),
                    processDefinition -> respVO.setCategory(processDefinition.getCategory()));
            MapUtils.findAndThen(categoryMap, respVO.getCategory(), category -> respVO.setCategoryName(category.getName()));
            respVO.setTasks(BeanUtils.toBean(taskMap.get(respVO.getId()), BpmProcessInstanceRespVO.Task.class));
            // user
            if (userMap != null) {
                SysUser startUser = userMap.get(Long.parseLong(pageResult.get(i).getStartUserId()));
                respVO.setStartUser(BeanUtils.toBean(startUser, BpmProcessInstanceRespVO.User.class));
                MapUtils.findAndThen(deptMap, startUser.getDeptId(), dept -> respVO.getStartUser().setDeptName(dept.getDeptName()));
            }
        }
        return vpPageResult;
    }
    public static BpmProcessInstanceRespVO buildProcessInstance(HistoricProcessInstance processInstance,
                                                                ProcessDefinition processDefinition,
                                                                BpmProcessDefinitionInfoDO processDefinitionExt,
                                                                String bpmnXml,
                                                                SysUser startUser,
                                                                SysDept dept) {
        BpmProcessInstanceRespVO respVO = BeanUtils.toBean(processInstance, BpmProcessInstanceRespVO.class);
        respVO.setStatus(FlowableUtils.getProcessInstanceStatus(processInstance));
        respVO.setFormVariables(FlowableUtils.getProcessInstanceFormVariable(processInstance));
        // definition
        respVO.setProcessDefinition(BeanUtils.toBean(processDefinition, BpmProcessDefinitionRespVO.class));
        BeanUtil.copyProperties(processDefinitionExt, respVO.getProcessDefinition());
        respVO.getProcessDefinition().setBpmnXml(bpmnXml);
        // user
        if (startUser != null) {
            respVO.setStartUser(BeanUtils.toBean(startUser, BpmProcessInstanceRespVO.User.class));
            if (dept != null) {
                respVO.getStartUser().setDeptName(dept.getDeptName());
            }
        }
        return respVO;
    }
}
