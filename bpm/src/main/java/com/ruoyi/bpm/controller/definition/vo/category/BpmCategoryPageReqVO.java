package com.ruoyi.bpm.controller.definition.vo.category;


import com.ruoyi.common.core.domain.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;


@Schema(description = "管理后台 - BPM 流程分类分页 Request VO")
@Data
public class BpmCategoryPageReqVO extends PageParam {

    @Schema(description = "分类名", example = "王五")
    private String name;

    @Schema(description = "分类标志", example = "OA")
    private String code;

    @Schema(description = "分类状态", example = "1")
    private Integer status;

    @Schema(description = "创建时间")
    private Date[] createTime;

}