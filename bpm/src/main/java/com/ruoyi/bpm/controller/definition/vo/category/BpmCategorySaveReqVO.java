package com.ruoyi.controller.definition.vo.category;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - BPM 流程分类新增/修改 Request VO")
@Data
public class BpmCategorySaveReqVO {

    @Schema(description = "分类编号",required = true, example = "3167")
    private Long id;

    @Schema(description = "分类名", required = true, example = "王五")
    @NotEmpty(message = "分类名不能为空")
    private String name;

    @Schema(description = "分类描述", example = "你猜")
    private String description;

    @Schema(description = "分类标志", required = true, example = "OA")
    @NotEmpty(message = "分类标志不能为空")
    private String code;

    @Schema(description = "分类状态", required = true, example = "1")
    @NotNull(message = "分类状态不能为空")
    private Integer status;

    @Schema(description = "分类排序", required = true)
    @NotNull(message = "分类排序不能为空")
    private Integer sort;

}