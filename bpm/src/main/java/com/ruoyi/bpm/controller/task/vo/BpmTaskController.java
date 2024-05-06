package com.ruoyi.bpm.controller.task.vo;

import cn.hutool.core.collection.CollUtil;

import com.ruoyi.bpm.controller.task.vo.task.*;
import com.ruoyi.bpm.domain.definition.BpmFormDO;
import com.ruoyi.bpm.framework.flowable.core.util.TaskUtils;
import com.ruoyi.bpm.service.definition.BpmFormService;
import com.ruoyi.bpm.service.task.BpmProcessInstanceService;
import com.ruoyi.bpm.service.task.BpmTaskService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static com.ruoyi.bpm.framework.flowable.core.util.TaskUtils.*;
import static com.ruoyi.common.utils.CollectionUtils.*;


@Tag(name = "管理后台 - 流程任务实例")
@RestController
@RequestMapping("/bpm/task")
@Validated
public class BpmTaskController extends BaseController {

    @Resource
    private BpmTaskService taskService;
    @Resource
    private BpmProcessInstanceService processInstanceService;
    @Resource
    private BpmFormService formService;

    @Resource
    private ISysUserService adminUserApi;
    @Resource
    private ISysDeptService deptApi;

    @GetMapping("todo-page")
    @Operation(summary = "获取 Todo 待办任务分页")
    @PreAuthorize("@ss.hasPermi('bpm:task:query')")
    public TableDataInfo getTaskTodoPage(@Valid BpmTaskPageReqVO pageVO) {
        TableDataInfo pageResult = taskService.getTaskTodoPage(getUserId(), pageVO);
        var list=(List<Task>)pageResult.getRows();
        if (list.isEmpty()) {
            return pageResult;
        }

        // 拼接数据
        Map<String, ProcessInstance> processInstanceMap = processInstanceService.getProcessInstanceMap(
                convertSet(list, Task::getProcessInstanceId));
        Map<Long, SysUser> userMap = adminUserApi.getUserMap(
                convertSet(processInstanceMap.values(), instance -> Long.valueOf(instance.getStartUserId())));
        return new TableDataInfo(buildTodoTaskPage(list, processInstanceMap, userMap),pageResult.getTotal());
    }

    @GetMapping("done-page")
    @Operation(summary = "获取 Done 已办任务分页")
    @PreAuthorize("@ss.hasPermi('bpm:task:query')")
    public TableDataInfo getTaskDonePage(@Valid BpmTaskPageReqVO pageVO) {
        TableDataInfo pageResult = taskService.getTaskDonePage(getUserId(), pageVO);
        if (CollUtil.isEmpty(pageResult.getRows())) {
            return pageResult;
        }
        List<HistoricTaskInstance> list=(List<HistoricTaskInstance>)pageResult.getRows();
        // 拼接数据
        Map<String, HistoricProcessInstance> processInstanceMap = processInstanceService.getHistoricProcessInstanceMap(
                convertSet(list, HistoricTaskInstance::getProcessInstanceId));
        Map<Long, SysUser> userMap = adminUserApi.getUserMap(
                convertSet(processInstanceMap.values(), instance -> Long.valueOf(instance.getStartUserId())));
        return new TableDataInfo(TaskUtils.buildTaskList(list, processInstanceMap, userMap, null),pageResult.getTotal());
    }

    @GetMapping("manager-page")
    @Operation(summary = "获取全部任务的分页", description = "用于【流程任务】菜单")
    @PreAuthorize("@ss.hasPermi('bpm:task:mananger-query')")
    public TableDataInfo getTaskManagerPage(@Valid BpmTaskPageReqVO pageVO) {
        var pageResult = taskService.getTaskPage(getUserId(), pageVO);
        var list=(List<HistoricTaskInstance>)pageResult.getRows();
        if (CollUtil.isEmpty(list)) {
            return new TableDataInfo();
        }

        // 拼接数据
        Map<String, HistoricProcessInstance> processInstanceMap = processInstanceService.getHistoricProcessInstanceMap(
                convertSet(list, HistoricTaskInstance::getProcessInstanceId));
        // 获得 User 和 Dept Map
        Set<Long> userIds = convertSet(processInstanceMap.values(), instance -> Long.valueOf(instance.getStartUserId()));
        userIds.addAll(convertSet(list, task -> Long.parseLong(task.getAssignee())));
        Map<Long, SysUser> userMap = adminUserApi.getUserMap(userIds);
        Map<Long, SysDept> deptMap = deptApi.getDeptMap(
                convertSet(userMap.values(), SysUser::getDeptId));
        var taskList=buildTaskList(list, processInstanceMap, userMap, deptMap);
        return new TableDataInfo(taskList,pageResult.getTotal());
    }

    @GetMapping("/list-by-process-instance-id")
    @Operation(summary = "获得指定流程实例的任务列表", description = "包括完成的、未完成的")
    @Parameter(name = "processInstanceId", description = "流程实例的编号", required = true)
    @PreAuthorize("@ss.hasPermi('bpm:task:query')")
    public AjaxResult getTaskListByProcessInstanceId(
            @RequestParam("processInstanceId") String processInstanceId) {
        List<HistoricTaskInstance> taskList = taskService.getTaskListByProcessInstanceId(processInstanceId);
        if (CollUtil.isEmpty(taskList)) {
            return success(Collections.emptyList());
        }

        // 拼接数据
        HistoricProcessInstance processInstance = processInstanceService.getHistoricProcessInstance(processInstanceId);
        // 获得 User 和 Dept Map
        Set<Long> userIds = convertSetByFlatMap(taskList, task ->
                Stream.of(Long.parseLong(task.getAssignee()), Long.parseLong(task.getOwner())));
        userIds.add(Long.parseLong(processInstance.getStartUserId()));
        Map<Long, SysUser> userMap = adminUserApi.getUserMap(userIds);
        Map<Long, SysDept> deptMap = deptApi.getDeptMap(
                convertSet(userMap.values(), SysUser::getDeptId));
        // 获得 Form Map
        Map<Long, BpmFormDO> formMap = formService.getFormMap(
                convertSet(taskList, task -> Long.parseLong(task.getFormKey())));
        var resList=buildTaskListByProcessInstanceId(taskList, processInstance,
                formMap, userMap, deptMap);
        return success(resList);
    }

    @PutMapping("/approve")
    @Operation(summary = "通过任务")
    @PreAuthorize("@ss.hasPermi('bpm:task:update')")
    public AjaxResult approveTask(@Valid @RequestBody BpmTaskApproveReqVO reqVO) {
        taskService.approveTask(getUserId(), reqVO);
        return success(true);
    }

    @PutMapping("/reject")
    @Operation(summary = "不通过任务")
    @PreAuthorize("@ss.hasPermi('bpm:task:update')")
    public AjaxResult rejectTask(@Valid @RequestBody BpmTaskRejectReqVO reqVO) {
        taskService.rejectTask(getUserId(), reqVO);
        return success(true);
    }

    @GetMapping("/list-by-return")
    @Operation(summary = "获取所有可回退的节点", description = "用于【流程详情】的【回退】按钮")
    @Parameter(name = "taskId", description = "当前任务ID", required = true)
    @PreAuthorize("@ss.hasPermi('bpm:task:update')")
    public AjaxResult getTaskListByReturn(@RequestParam("id") String id) {
        List<UserTask> userTaskList = taskService.getUserTaskListByReturn(id);
        return success(convertList(userTaskList, userTask -> // 只返回 id 和 name
                new BpmTaskRespVO().setName(userTask.getName()).setTaskDefinitionKey(userTask.getId())));
    }

    @PutMapping("/return")
    @Operation(summary = "回退任务", description = "用于【流程详情】的【回退】按钮")
    @PreAuthorize("@ss.hasPermi('bpm:task:update')")
    public AjaxResult returnTask(@Valid @RequestBody BpmTaskReturnReqVO reqVO) {
        taskService.returnTask(getUserId(), reqVO);
        return success(true);
    }

    @PutMapping("/delegate")
    @Operation(summary = "委派任务", description = "用于【流程详情】的【委派】按钮")
    @PreAuthorize("@ss.hasPermi('bpm:task:update')")
    public AjaxResult delegateTask(@Valid @RequestBody BpmTaskDelegateReqVO reqVO) {
        taskService.delegateTask(getUserId(), reqVO);
        return success(true);
    }

    @PutMapping("/transfer")
    @Operation(summary = "转派任务", description = "用于【流程详情】的【转派】按钮")
    @PreAuthorize("@ss.hasPermi('bpm:task:update')")
    public AjaxResult transferTask(@Valid @RequestBody BpmTaskTransferReqVO reqVO) {
        taskService.transferTask(getUserId(), reqVO);
        return success(true);
    }

    @PutMapping("/create-sign")
    @Operation(summary = "加签", description = "before 前加签，after 后加签")
    @PreAuthorize("@ss.hasPermi('bpm:task:update')")
    public AjaxResult createSignTask(@Valid @RequestBody BpmTaskSignCreateReqVO reqVO) {
        taskService.createSignTask(getUserId(), reqVO);
        return success(true);
    }

    @DeleteMapping("/delete-sign")
    @Operation(summary = "减签")
    @PreAuthorize("@ss.hasPermi('bpm:task:update')")
    public AjaxResult deleteSignTask(@Valid @RequestBody BpmTaskSignDeleteReqVO reqVO) {
        taskService.deleteSignTask(getUserId(), reqVO);
        return success(true);
    }

    @GetMapping("/list-by-parent-task-id")
    @Operation(summary = "获得指定父级任务的子任务列表") // 目前用于，减签的时候，获得子任务列表
    @Parameter(name = "parentTaskId", description = "父级任务编号", required = true)
    @PreAuthorize("@ss.hasPermi('bpm:task:query')")
    public AjaxResult getTaskListByParentTaskId(@RequestParam("parentTaskId") String parentTaskId) {
        List<Task> taskList = taskService.getTaskListByParentTaskId(parentTaskId);
        if (CollUtil.isEmpty(taskList)) {
            return success(Collections.emptyList());
        }
        // 拼接数据
        Map<Long, SysUser> userMap = adminUserApi.getUserMap(convertSetByFlatMap(taskList,
                user -> Stream.of(Long.parseLong(user.getAssignee()), Long.parseLong(user.getOwner()))));
        Map<Long, SysDept> deptMap = deptApi.getDeptMap(
                convertSet(userMap.values(), SysUser::getDeptId));
        return success(TaskUtils.buildTaskListByParentTaskId(taskList, userMap, deptMap));
    }

}
