package com.ruoyi.bpm.controller.definition.vo.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - BPM 流程分类 Response VO")
@Data
@Accessors(chain = true)
public class BpmCategoryRespVO {

    @Schema(description = "分类编号", required = true, example = "3167")
    private Long id;

    @Schema(description = "分类名", required = true, example = "王五")
    private String name;

    @Schema(description = "分类标志", required = true, example = "OA")
    private String code;

    @Schema(description = "分类描述", required = true, example = "你猜")
    private String description;

    @Schema(description = "分类状态", required = true, example = "1")
    private Integer status;

    @Schema(description = "分类排序", required = true)
    private Integer sort;

    @Schema(description = "创建时间", required = true)
    private LocalDateTime createTime;

}