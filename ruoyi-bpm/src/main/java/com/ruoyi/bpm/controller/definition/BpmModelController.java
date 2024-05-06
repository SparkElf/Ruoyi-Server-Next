package com.ruoyi.bpm.controller.definition;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.bpm.controller.definition.vo.model.*;
import com.ruoyi.bpm.service.definition.BpmModelService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.flowable.engine.repository.Model;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;



@Tag(name = "管理后台 - 流程模型")
@RestController
@RequestMapping("/bpm/model")
@Validated
public class BpmModelController extends BaseController {

    @Resource
    private BpmModelService modelService;
//    @Resource
//    private BpmFormService formService;
//    @Resource
//    private BpmCategoryService categoryService;
//    @Resource
//    private BpmProcessDefinitionService processDefinitionService;

    @GetMapping("/page")
    @Operation(summary = "获得模型分页")
    public TableDataInfo getModelPage(BpmModelPageReqVO pageVO) {
        //BpmModelRespVO
        IPage<Model> pageResult = modelService.getModelPage(pageVO);
        if (pageResult.getTotal()==0) {
            return new TableDataInfo();
        }

        // 拼接数据
        // 获得 Form 表单
//        Set<Long> formIds = convertSet(pageResult.getList(), model -> {
//            BpmModelMetaInfoRespDTO metaInfo = JsonUtils.parseObject(model.getMetaInfo(), BpmModelMetaInfoRespDTO.class);
//            return metaInfo != null ? metaInfo.getFormId() : null;
//        });
//        Map<Long, BpmFormDO> formMap = formService.getFormMap(formIds);
//        // 获得 Category Map
//        Map<String, BpmCategoryDO> categoryMap = categoryService.getCategoryMap(
//                convertSet(pageResult.getList(), Model::getCategory));
//        // 获得 Deployment Map
//        Set<String> deploymentIds = new HashSet<>();
//        pageResult.getList().forEach(model -> CollectionUtils.addIfNotNull(deploymentIds, model.getDeploymentId()));
//        Map<String, Deployment> deploymentMap = processDefinitionService.getDeploymentMap(deploymentIds);
//        // 获得 ProcessDefinition Map
//        List<ProcessDefinition> processDefinitions = processDefinitionService.getProcessDefinitionListByDeploymentIds(deploymentIds);
//        Map<String, ProcessDefinition> processDefinitionMap = convertMap(processDefinitions, ProcessDefinition::getDeploymentId);
        return new TableDataInfo(pageResult);
    }

    @GetMapping("/get")
    @Operation(summary = "获得模型")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermi('bpm:model:query')")
    public AjaxResult getModel(@RequestParam("id") String id) {
        Model model = modelService.getModel(id);
        if (model == null) {
            return null;
        }
        byte[] bpmnBytes = modelService.getModelBpmnXML(id);
        return success(new BpmModelRespVO(model, bpmnBytes));
    }

    @PostMapping("/create")
    @Operation(summary = "新建模型")
    @PreAuthorize("@ss.hasPermi('bpm:model:create')")
    public AjaxResult createModel(@Valid @RequestBody BpmModelCreateReqVO createRetVO) {
        return success(modelService.createModel(createRetVO, null));
    }

    @PutMapping("/update")
    @Operation(summary = "修改模型")
    @PreAuthorize("@ss.hasPermi('bpm:model:update')")
    public AjaxResult updateModel(@Valid @RequestBody BpmModelUpdateReqVO modelVO) {
        modelService.updateModel(modelVO);
        return success(true);
    }


//    @PostMapping("/deploy")
//    @Operation(summary = "部署模型")
//    @Parameter(name = "id", description = "编号", required = true, example = "1024")
//    @PreAuthorize("@ss.hasPermi('bpm:model:deploy')")
//    public AjaxResult deployModel(@RequestParam("id") String id) {
//        modelService.deployModel(id);
//        return success(true);
//    }
//
//    @PutMapping("/update-state")
//    @Operation(summary = "修改模型的状态", description = "实际更新的部署的流程定义的状态")
//    @PreAuthorize("@ss.hasPermi('bpm:model:update')")
//    public AjaxResult updateModelState(@Valid @RequestBody BpmModelUpdateStateReqVO reqVO) {
//        modelService.updateModelState(reqVO.getId(), reqVO.getState());
//        return success(true);
//    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除模型")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermi('bpm:model:delete')")
    public AjaxResult deleteModel(@RequestParam("id") String id) {
        modelService.deleteModel(id);
        return success(true);
    }

}
