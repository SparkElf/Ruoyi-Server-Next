package com.ruoyi.bpm.controller.definition;


import com.ruoyi.bpm.controller.definition.vo.form.BpmFormPageReqVO;
import com.ruoyi.bpm.controller.definition.vo.form.BpmFormRespVO;
import com.ruoyi.bpm.controller.definition.vo.form.BpmFormSaveReqVO;
import com.ruoyi.bpm.domain.definition.BpmFormDO;
import com.ruoyi.bpm.service.definition.BpmFormService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.object.BeanUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static com.ruoyi.common.core.domain.AjaxResult.success;
import static com.ruoyi.common.utils.CollectionUtils.convertList;


@Tag(name = "管理后台 - 动态表单")
@RestController
@RequestMapping("/bpm/form")
@Validated
public class BpmFormController {

    @Resource
    private BpmFormService formService;

    @PostMapping("/create")
    @Operation(summary = "创建动态表单")
    @PreAuthorize("@ss.hasPermi('bpm:form:create')")
    public AjaxResult createForm(@Valid @RequestBody BpmFormSaveReqVO createReqVO) {
        return success(formService.createForm(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新动态表单")
    @PreAuthorize("@ss.hasPermi('bpm:form:update')")
    public AjaxResult updateForm(@Valid @RequestBody BpmFormSaveReqVO updateReqVO) {
        formService.updateForm(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除动态表单")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermi('bpm:form:delete')")
    public AjaxResult deleteForm(@RequestParam("id") Long id) {
        formService.deleteForm(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得动态表单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermi('bpm:form:query')")
    public AjaxResult getForm(@RequestParam("id") Long id) {
        BpmFormDO form = formService.getForm(id);
        return success(BeanUtils.toBean(form, BpmFormRespVO.class));
    }

    @GetMapping({"/list-all-simple", "/simple-list"})
    @Operation(summary = "获得动态表单的精简列表", description = "用于表单下拉框")
    public AjaxResult getFormSimpleList() {
        List<BpmFormDO> list = formService.getFormList();
        return success(convertList(list, formDO -> // 只返回 id、name 字段
                new BpmFormRespVO().setId(formDO.getId()).setName(formDO.getName())));
    }

    @GetMapping("/page")
    @Operation(summary = "获得动态表单分页")
    @PreAuthorize("@ss.hasPermi('bpm:form:query')")
    public AjaxResult getFormPage(@Valid BpmFormPageReqVO pageVO) {
        TableDataInfo pageResult = formService.getFormPage(pageVO);
        return success(BeanUtils.toBean(pageResult, BpmFormRespVO.class));
    }

}
