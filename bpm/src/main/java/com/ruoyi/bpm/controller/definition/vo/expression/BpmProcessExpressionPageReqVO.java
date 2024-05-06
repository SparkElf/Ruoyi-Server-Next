package com.ruoyi.bpm.controller.definition.vo.expression;


import com.ruoyi.common.core.domain.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


@Schema(description = "管理后台 - BPM 流程表达式分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BpmProcessExpressionPageReqVO extends PageParam {

    @Schema(description = "表达式名字", example = "李四")
    private String name;

    @Schema(description = "表达式状态", example = "1")

    private Integer status;

    @Schema(description = "创建时间")

    private LocalDateTime[] createTime;

}