package com.ruoyi.bpm.controller.task.vo;

import cn.hutool.core.collection.CollUtil;

import com.ruoyi.bpm.controller.task.vo.instance.BpmProcessInstanceCancelReqVO;
import com.ruoyi.bpm.controller.task.vo.instance.BpmProcessInstanceCreateReqVO;
import com.ruoyi.bpm.controller.task.vo.instance.BpmProcessInstancePageReqVO;
import com.ruoyi.bpm.domain.definition.BpmCategoryDO;
import com.ruoyi.bpm.domain.definition.BpmProcessDefinitionInfoDO;
import com.ruoyi.bpm.framework.flowable.core.util.BpmnModelUtils;
import com.ruoyi.bpm.service.definition.BpmCategoryService;
import com.ruoyi.bpm.service.definition.BpmProcessDefinitionService;
import com.ruoyi.bpm.service.task.BpmProcessInstanceService;
import com.ruoyi.bpm.service.task.BpmTaskService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.task.api.Task;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static com.ruoyi.bpm.framework.flowable.core.util.ProcessUtils.buildBpmProcessInstanceList;
import static com.ruoyi.bpm.framework.flowable.core.util.ProcessUtils.buildProcessInstance;
import static com.ruoyi.common.utils.CollectionUtils.convertList;
import static com.ruoyi.common.utils.CollectionUtils.convertSet;

@Tag(name = "管理后台 - 流程实例") // 流程实例，通过流程定义创建的一次“申请”
@RestController
@RequestMapping("/bpm/process-instance")
@Validated
public class BpmProcessInstanceController extends BaseController {

    @Resource
    private BpmProcessInstanceService processInstanceService;
    @Resource
    private BpmTaskService taskService;
    @Resource
    private BpmProcessDefinitionService processDefinitionService;
    @Resource
    private BpmCategoryService categoryService;

    @Resource
    private ISysUserService adminUserApi;
    @Resource
    private ISysDeptService deptApi;

    @GetMapping("/my-page")
    @Operation(summary = "获得我的实例分页列表", description = "在【我的流程】菜单中，进行调用")
    @PreAuthorize("@ss.hasPermi('bpm:process-instance:query')")
    public TableDataInfo getProcessInstanceMyPage(
            @Valid BpmProcessInstancePageReqVO pageReqVO) {
        TableDataInfo pageResult = processInstanceService.getProcessInstancePage(SecurityUtils.getUserId(), pageReqVO);
        List<HistoricProcessInstance> list=(List<HistoricProcessInstance>)pageResult.getRows();
        if (pageResult.isEmpty()) {
            return pageResult;
        }

        // 拼接返回
        Map<String, List<Task>> taskMap = taskService.getTaskMapByProcessInstanceIds(
                convertList(list, HistoricProcessInstance::getId));
        Map<String, ProcessDefinition> processDefinitionMap = processDefinitionService.getProcessDefinitionMap(
                convertSet(list, HistoricProcessInstance::getProcessDefinitionId));
        Map<String, BpmCategoryDO> categoryMap = categoryService.getCategoryMap(
                convertSet(processDefinitionMap.values(), ProcessDefinition::getCategory));
        var piList=buildBpmProcessInstanceList(list,
                processDefinitionMap, categoryMap, taskMap, null, null);
        return new TableDataInfo(piList, pageResult.getTotal());
    }

    @GetMapping("/manager-page")
    @Operation(summary = "获得管理流程实例的分页列表", description = "在【流程实例】菜单中，进行调用")
    @PreAuthorize("@ss.hasPermi('bpm:process-instance:manager-query')")
    public TableDataInfo getProcessInstanceManagerPage(
            @Valid BpmProcessInstancePageReqVO pageReqVO) {
        TableDataInfo pageResult = processInstanceService.getProcessInstancePage(
                null, pageReqVO);
        List<HistoricProcessInstance> list=(List<HistoricProcessInstance>)pageResult.getRows();
        if (pageResult.isEmpty()) {
            return pageResult;
        }

        // 拼接返回
        Map<String, List<Task>> taskMap = taskService.getTaskMapByProcessInstanceIds(
                convertList(list, HistoricProcessInstance::getId));
        Map<String, ProcessDefinition> processDefinitionMap = processDefinitionService.getProcessDefinitionMap(
                convertSet(list, HistoricProcessInstance::getProcessDefinitionId));
        Map<String, BpmCategoryDO> categoryMap = categoryService.getCategoryMap(
                convertSet(processDefinitionMap.values(), ProcessDefinition::getCategory));
        // 发起人信息
        Map<Long, SysUser> userMap = adminUserApi.getUserMap(
                convertSet(list, processInstance -> Long.parseLong(processInstance.getStartUserId())));
        Map<Long, SysDept> deptMap = deptApi.getDeptMap(
                convertSet(userMap.values(), SysUser::getDeptId));

        var piList=buildBpmProcessInstanceList(list,
                processDefinitionMap, categoryMap, taskMap, userMap, deptMap);
        return new TableDataInfo(piList, pageResult.getTotal());
    }

    @PostMapping("/create")
    @Operation(summary = "新建流程实例")
    @PreAuthorize("@ss.hasPermi('bpm:process-instance:query')")
    public AjaxResult createProcessInstance(@Valid @RequestBody BpmProcessInstanceCreateReqVO createReqVO) {
        return success(processInstanceService.createProcessInstance(getUserId(), createReqVO));
    }

    @GetMapping("/get")
    @Operation(summary = "获得指定流程实例", description = "在【流程详细】界面中，进行调用")
    @Parameter(name = "id", description = "流程实例的编号", required = true)
    @PreAuthorize("@ss.hasPermi('bpm:process-instance:query')")
    public AjaxResult getProcessInstance(@RequestParam("id") String id) {
        HistoricProcessInstance processInstance = processInstanceService.getHistoricProcessInstance(id);
        if (processInstance == null) {
            return success(null);
        }

        // 拼接返回
        ProcessDefinition processDefinition = processDefinitionService.getProcessDefinition(
                processInstance.getProcessDefinitionId());
        BpmProcessDefinitionInfoDO processDefinitionInfo = processDefinitionService.getProcessDefinitionInfo(
                processInstance.getProcessDefinitionId());
        String bpmnXml = BpmnModelUtils.getBpmnXml(
                processDefinitionService.getProcessDefinitionBpmnModel(processInstance.getProcessDefinitionId()));
        SysUser startUser = adminUserApi.selectUserById(Long.parseLong(processInstance.getStartUserId()));
        SysDept dept = null;
        if (startUser != null) {
            dept = deptApi.selectDeptById(startUser.getDeptId());
        }
        return success(buildProcessInstance(processInstance,
                processDefinition, processDefinitionInfo, bpmnXml, startUser, dept));
    }

    @DeleteMapping("/cancel-by-start-user")
    @Operation(summary = "用户取消流程实例", description = "取消发起的流程")
    @PreAuthorize("@ss.hasPermi('bpm:process-instance:cancel')")
    public AjaxResult cancelProcessInstanceByStartUser(
            @Valid @RequestBody BpmProcessInstanceCancelReqVO cancelReqVO) {
        processInstanceService.cancelProcessInstanceByStartUser(getUserId(), cancelReqVO);
        return success(true);
    }

    @DeleteMapping("/cancel-by-admin")
    @Operation(summary = "管理员取消流程实例", description = "管理员撤回流程")
    @PreAuthorize("@ss.hasPermi('bpm:process-instance:cancel-by-admin')")
    public AjaxResult cancelProcessInstanceByManager(
            @Valid @RequestBody BpmProcessInstanceCancelReqVO cancelReqVO) {
        processInstanceService.cancelProcessInstanceByAdmin(getUserId(), cancelReqVO);
        return success(true);
    }

}
