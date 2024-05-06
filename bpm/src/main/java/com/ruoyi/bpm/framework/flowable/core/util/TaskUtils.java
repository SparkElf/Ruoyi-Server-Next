package com.ruoyi.bpm.framework.flowable.core.util;

import cn.hutool.core.map.MapUtil;
import com.ruoyi.bpm.controller.task.vo.instance.BpmProcessInstanceRespVO;
import com.ruoyi.bpm.controller.task.vo.task.BpmTaskRespVO;
import com.ruoyi.bpm.domain.definition.BpmFormDO;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.CollectionUtils;
import com.ruoyi.common.utils.object.BeanUtils;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.ruoyi.common.utils.CollectionUtils.convertList;
import static com.ruoyi.common.utils.MapUtils.findAndThen;

public class TaskUtils {
    public static List<BpmTaskRespVO> buildTaskListByParentTaskId(List<Task> taskList,
                                                    Map<Long, SysUser> userMap,
                                                    Map<Long, SysDept> deptMap) {
        return convertList(taskList, task -> BeanUtils.toBean(task, BpmTaskRespVO.class, taskVO -> {
            SysUser assignUser = userMap.get(Long.parseLong(task.getAssignee()));
            if (assignUser != null) {
                taskVO.setAssigneeUser(BeanUtils.toBean(assignUser, BpmProcessInstanceRespVO.User.class));
                SysDept dept = deptMap.get(assignUser.getDeptId());
                if (dept != null) {
                    taskVO.getAssigneeUser().setDeptName(dept.getDeptName());
                }
            }
            SysUser ownerUser = userMap.get(Long.parseLong(task.getOwner()));
            if (ownerUser != null) {
                taskVO.setOwnerUser(BeanUtils.toBean(ownerUser, BpmProcessInstanceRespVO.User.class));
                findAndThen(deptMap, ownerUser.getDeptId(), dept -> taskVO.getOwnerUser().setDeptName(dept.getDeptName()));
            }
        }));
    }
    public static List<BpmTaskRespVO> buildTaskListByProcessInstanceId(List<HistoricTaskInstance> taskList,
                                                                       HistoricProcessInstance processInstance,
                                                                       Map<Long, BpmFormDO> formMap,
                                                                       Map<Long, SysUser> userMap,
                                                                       Map<Long, SysDept> deptMap) {
        List<BpmTaskRespVO> taskVOList = convertList(taskList, task -> {
            BpmTaskRespVO taskVO = BeanUtils.toBean(task, BpmTaskRespVO.class);
            taskVO.setStatus(FlowableUtils.getTaskStatus(task)).setReason(FlowableUtils.getTaskReason(task));
            // 流程实例
            SysUser startUser = userMap.get(Long.parseLong(processInstance.getStartUserId()));
            taskVO.setProcessInstance(BeanUtils.toBean(processInstance, BpmTaskRespVO.ProcessInstance.class));
            taskVO.getProcessInstance().setStartUser(BeanUtils.toBean(startUser, BpmProcessInstanceRespVO.User.class));
            // 表单信息
            BpmFormDO form = MapUtil.get(formMap, Long.parseLong(task.getFormKey()), BpmFormDO.class);
            if (form != null) {
                taskVO.setFormId(form.getId()).setFormName(form.getName()).setFormConf(form.getConf())
                        .setFormFields(form.getFields()).setFormVariables(FlowableUtils.getTaskFormVariable(task));
            }
            // 用户信息
            SysUser assignUser = userMap.get(Long.parseLong(task.getAssignee()));
            if (assignUser != null) {
                taskVO.setAssigneeUser(BeanUtils.toBean(assignUser, BpmProcessInstanceRespVO.User.class));
                findAndThen(deptMap, assignUser.getDeptId(), dept -> taskVO.getAssigneeUser().setDeptName(dept.getDeptName()));
            }
            SysUser ownerUser = userMap.get(Long.parseLong(task.getOwner()));
            if (ownerUser != null) {
                taskVO.setOwnerUser(BeanUtils.toBean(ownerUser, BpmProcessInstanceRespVO.User.class));
                findAndThen(deptMap, ownerUser.getDeptId(), dept -> taskVO.getOwnerUser().setDeptName(dept.getDeptName()));
            }
            return taskVO;
        });
        return taskVOList;
    }
    public static List<BpmTaskRespVO> buildTaskList(List<HistoricTaskInstance> pageResult,
                                Map<String, HistoricProcessInstance> processInstanceMap,
                                Map<Long, SysUser> userMap,
                                Map<Long, SysDept> deptMap) {
        List<BpmTaskRespVO> taskVOList = convertList(pageResult, task -> {
            BpmTaskRespVO taskVO = BeanUtils.toBean(task, BpmTaskRespVO.class);
            taskVO.setStatus(FlowableUtils.getTaskStatus(task)).setReason(FlowableUtils.getTaskReason(task));
            // 用户信息
            SysUser assignUser = userMap.get(Long.parseLong(task.getAssignee()));
            if (assignUser != null) {
                taskVO.setAssigneeUser(BeanUtils.toBean(assignUser, BpmProcessInstanceRespVO.User.class));
                findAndThen(deptMap, assignUser.getDeptId(), dept -> taskVO.getAssigneeUser().setDeptName(dept.getDeptName()));
            }
            // 流程实例
            HistoricProcessInstance processInstance = processInstanceMap.get(taskVO.getProcessInstanceId());
            if (processInstance != null) {
                SysUser startUser = userMap.get(Long.parseLong(processInstance.getStartUserId()));
                taskVO.setProcessInstance(BeanUtils.toBean(processInstance, BpmTaskRespVO.ProcessInstance.class));
                taskVO.getProcessInstance().setStartUser(BeanUtils.toBean(startUser, BpmProcessInstanceRespVO.User.class));
            }
            return taskVO;
        });
        return taskVOList;
    }
    public static void copyTo(TaskEntityImpl parentTask, TaskEntityImpl childTask) {
        childTask.setName(parentTask.getName());
        childTask.setDescription(parentTask.getDescription());
        childTask.setCategory(parentTask.getCategory());
        childTask.setParentTaskId(parentTask.getId());
        childTask.setProcessDefinitionId(parentTask.getProcessDefinitionId());
        childTask.setProcessInstanceId(parentTask.getProcessInstanceId());
//        childTask.setExecutionId(parentTask.getExecutionId()); // TODO 芋艿：新加的，不太确定；尴尬，不加时，子任务不通过会失败（报错）；加了，子任务审批通过会失败（报错）
        childTask.setTaskDefinitionKey(parentTask.getTaskDefinitionKey());
        childTask.setTaskDefinitionId(parentTask.getTaskDefinitionId());
        childTask.setPriority(parentTask.getPriority());
        childTask.setCreateTime(new Date());
        childTask.setTenantId(parentTask.getTenantId());
    }
    public static List<BpmTaskRespVO> buildTodoTaskPage(List<Task> pageResult,
                                                        Map<String, ProcessInstance> processInstanceMap,
                                                        Map<Long, SysUser> userMap) {
        return BeanUtils.toBean(pageResult, BpmTaskRespVO.class, taskVO -> {
            ProcessInstance processInstance = processInstanceMap.get(taskVO.getProcessInstanceId());
            if (processInstance == null) {
                return;
            }
            taskVO.setProcessInstance(BeanUtils.toBean(processInstance, BpmTaskRespVO.ProcessInstance.class));
            SysUser startUser = userMap.get(Long.parseLong(processInstance.getStartUserId()));
            taskVO.getProcessInstance().setStartUser(BeanUtils.toBean(startUser, BpmProcessInstanceRespVO.User.class));
        });
    }
}
