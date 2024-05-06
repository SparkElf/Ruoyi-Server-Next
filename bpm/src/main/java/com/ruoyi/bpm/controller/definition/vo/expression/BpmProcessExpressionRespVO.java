package com.ruoyi.bpm.controller.definition.vo.expression;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - BPM 流程表达式 Response VO")
@Data
public class BpmProcessExpressionRespVO {

    @Schema(description = "编号", example = "3870")

    private Long id;

    @Schema(description = "表达式名字", example = "李四")

    private String name;

    @Schema(description = "表达式状态", example = "1")
    private Integer status;

    @Schema(description = "表达式")
    private String expression;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}