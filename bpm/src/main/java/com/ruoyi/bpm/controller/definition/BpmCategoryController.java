package com.ruoyi.bpm.controller.definition;


import com.ruoyi.bpm.controller.definition.vo.category.BpmCategoryPageReqVO;
import com.ruoyi.bpm.controller.definition.vo.category.BpmCategoryRespVO;
import com.ruoyi.bpm.controller.definition.vo.category.BpmCategorySaveReqVO;
import com.ruoyi.bpm.domain.definition.BpmCategoryDO;
import com.ruoyi.bpm.service.definition.BpmCategoryService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.CommonStatusEnum;
import com.ruoyi.common.utils.object.BeanUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;

import static com.ruoyi.common.core.domain.AjaxResult.success;
import static com.ruoyi.common.utils.CollectionUtils.convertList;


@Tag(name = "管理后台 - BPM 流程分类")
@RestController
@RequestMapping("/bpm/category")
@Validated
public class BpmCategoryController {

    @Resource
    private BpmCategoryService categoryService;

    @PostMapping("/create")
    @Operation(summary = "创建流程分类")
    @PreAuthorize("@ss.hasPermi('bpm:category:create')")
    public AjaxResult createCategory(@Valid @RequestBody BpmCategorySaveReqVO createReqVO) {
        return success(categoryService.createCategory(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新流程分类")
    @PreAuthorize("@ss.hasPermi('bpm:category:update')")
    public AjaxResult updateCategory(@Valid @RequestBody BpmCategorySaveReqVO updateReqVO) {
        categoryService.updateCategory(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除流程分类")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermi('bpm:category:delete')")
    public AjaxResult deleteCategory(@RequestParam("id") Long id) {
        categoryService.deleteCategory(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得流程分类")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermi('bpm:category:query')")
    public AjaxResult getCategory(@RequestParam("id") Long id) {
        BpmCategoryDO category = categoryService.getCategory(id);
        return success(BeanUtils.toBean(category, BpmCategoryRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得流程分类分页")
    @PreAuthorize("@ss.hasPermi('bpm:category:query')")
    public AjaxResult getCategoryPage(@Valid BpmCategoryPageReqVO pageReqVO) {
        TableDataInfo pageResult = categoryService.getCategoryPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, BpmCategoryRespVO.class));
    }

    @GetMapping("/simple-list")
    @Operation(summary = "获取流程分类的精简信息列表", description = "只包含被开启的分类，主要用于前端的下拉选项")
    public AjaxResult getCategorySimpleList() {
        List<BpmCategoryDO> list = categoryService.getCategoryListByStatus(CommonStatusEnum.ENABLE.getStatus());
        list.sort(Comparator.comparingInt(BpmCategoryDO::getSort));
        return success(convertList(list, category -> new BpmCategoryRespVO().setId(category.getId())
                .setName(category.getName()).setCode(category.getCode())));
    }

}