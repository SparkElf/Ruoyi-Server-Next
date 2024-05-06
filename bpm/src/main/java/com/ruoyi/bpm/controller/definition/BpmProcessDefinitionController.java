package com.ruoyi.bpm.controller.definition;

import cn.hutool.core.collection.CollUtil;

import com.ruoyi.bpm.controller.definition.vo.process.BpmProcessDefinitionPageReqVO;
import com.ruoyi.bpm.controller.definition.vo.process.BpmProcessDefinitionRespVO;
import com.ruoyi.bpm.domain.definition.BpmCategoryDO;
import com.ruoyi.bpm.domain.definition.BpmFormDO;
import com.ruoyi.bpm.domain.definition.BpmProcessDefinitionInfoDO;
import com.ruoyi.bpm.framework.flowable.core.candidate.strategy.BpmTaskCandidateStartUserSelectStrategy;
import com.ruoyi.bpm.service.definition.BpmCategoryService;
import com.ruoyi.bpm.service.definition.BpmFormService;
import com.ruoyi.bpm.service.definition.BpmProcessDefinitionService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.ruoyi.bpm.framework.flowable.core.util.ProcessUtils.buildProcessDefinition;
import static com.ruoyi.bpm.framework.flowable.core.util.ProcessUtils.buildProcessDefinitionList;
import static com.ruoyi.common.utils.CollectionUtils.convertSet;


@Tag(name = "管理后台 - 流程定义")
@RestController
@RequestMapping("/bpm/process-definition")
@Validated
public class BpmProcessDefinitionController extends BaseController {

    @Resource
    private BpmProcessDefinitionService processDefinitionService;
    @Resource
    private BpmFormService formService;
    @Resource
    private BpmCategoryService categoryService;

    @GetMapping("/page")
    @Operation(summary = "获得流程定义分页")
    @PreAuthorize("@ss.hasPermi('bpm:process-definition:query')")
    public TableDataInfo getProcessDefinitionPage(
            BpmProcessDefinitionPageReqVO pageReqVO) {
        //BpmProcessDefinitionRespVO
        var pageResult = processDefinitionService.getProcessDefinitionPage(pageReqVO);
        if (pageResult.isEmpty()) {
            return pageResult;
        }
        var list=(List<ProcessDefinition>) pageResult.getRows();
        // 获得 Category Map
        Map<String, BpmCategoryDO> categoryMap = categoryService.getCategoryMap(
                convertSet(list, ProcessDefinition::getCategory));
        // 获得 Deployment Map
        Map<String, Deployment> deploymentMap = processDefinitionService.getDeploymentMap(
                convertSet(list, ProcessDefinition::getDeploymentId));
        // 获得 BpmProcessDefinitionInfoDO Map
        Map<String, BpmProcessDefinitionInfoDO> processDefinitionMap = processDefinitionService.getProcessDefinitionInfoMap(
                convertSet(list, ProcessDefinition::getId));
        // 获得 Form Map
        Map<Long, BpmFormDO> formMap = formService.getFormMap(
               convertSet(processDefinitionMap.values(), BpmProcessDefinitionInfoDO::getFormId));

        return new TableDataInfo(buildProcessDefinitionList(
                list, deploymentMap, processDefinitionMap, formMap, categoryMap),pageResult.getTotal());
    }

    @GetMapping ("/list")
    @Operation(summary = "获得流程定义列表")
    @Parameter(name = "suspensionState", description = "挂起状态", required = true, example = "1") // 参见 Flowable SuspensionState 枚举
    @PreAuthorize("@ss.hasPermi('bpm:process-definition:query')")
    public AjaxResult getProcessDefinitionList(
            @RequestParam("suspensionState") Integer suspensionState) {
        List<ProcessDefinition> list = processDefinitionService.getProcessDefinitionListBySuspensionState(suspensionState);
        if (CollUtil.isEmpty(list)) {
            return success(Collections.emptyList());
        }

        // 获得 BpmProcessDefinitionInfoDO Map
        Map<String, BpmProcessDefinitionInfoDO> processDefinitionMap = processDefinitionService.getProcessDefinitionInfoMap(
                convertSet(list, ProcessDefinition::getId));
        return success(buildProcessDefinitionList(
                list, null, processDefinitionMap, null, null));
    }

    @GetMapping ("/get")
    @Operation(summary = "获得流程定义")
    @Parameter(name = "id", description = "流程编号", required = true, example = "1024")
    @Parameter(name = "key", description = "流程定义标识", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermi('bpm:process-definition:query')")
    public AjaxResult getProcessDefinition(
            @RequestParam(value = "id", required = false) String id,
            @RequestParam(value = "key", required = false) String key) {
        ProcessDefinition processDefinition = id != null ? processDefinitionService.getProcessDefinition(id)
                : processDefinitionService.getActiveProcessDefinition(key);
        if (processDefinition == null) {
            return success(null);
        }
        BpmnModel bpmnModel = processDefinitionService.getProcessDefinitionBpmnModel(processDefinition.getId());
        List<UserTask> userTaskList = BpmTaskCandidateStartUserSelectStrategy.getStartUserSelectUserTaskList(bpmnModel);
        return success(buildProcessDefinition(
                processDefinition, null, null, null, null, bpmnModel, userTaskList));
    }

}
