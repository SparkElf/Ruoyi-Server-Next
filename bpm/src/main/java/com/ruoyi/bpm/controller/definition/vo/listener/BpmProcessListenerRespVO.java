package com.ruoyi.bpm.controller.definition.vo.listener;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - BPM 流程监听器 Response VO")
@Data
public class BpmProcessListenerRespVO {

    @Schema(description = "编号", required=true, example = "13089")
    private Long id;

    @Schema(description = "监听器名字", required=true, example = "赵六")
    private String name;

    @Schema(description = "监听器类型", required=true, example = "execution")
    private String type;

    @Schema(description = "监听器状态", required=true, example = "1")
    private Integer status;

    @Schema(description = "监听事件", required=true, example = "start")
    private String event;

    @Schema(description = "监听器值类型", required=true, example = "class")
    private String valueType;

    @Schema(description = "监听器值", required=true)
    private String value;

    @Schema(description = "创建时间", required=true)
    private LocalDateTime createTime;

}