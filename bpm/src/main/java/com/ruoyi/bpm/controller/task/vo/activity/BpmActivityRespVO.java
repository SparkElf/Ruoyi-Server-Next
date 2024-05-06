package com.ruoyi.bpm.controller.task.vo.activity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Schema(description = "管理后台 - 流程活动的 Response VO")
@Data
public class BpmActivityRespVO {

    @Schema(description = "流程活动的标识", required=true, example = "1024")
    private String key;
    @Schema(description = "流程活动的类型", required=true, example = "StartEvent")
    private String type;

    @Schema(description = "流程活动的开始时间", required=true)
    private Date startTime;
    @Schema(description = "流程活动的结束时间", required=true)
    private Date endTime;

    @Schema(description = "关联的流程任务的编号", example = "2048")
    private String taskId; // 关联的流程任务，只有 UserTask 等类型才有

}
