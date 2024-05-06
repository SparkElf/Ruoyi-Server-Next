package com.ruoyi.bpm.controller.task.vo.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 流程任务的转办 Request VO")
@Data
public class BpmTaskTransferReqVO {

    @Schema(description = "任务编号", required=true, example = "1024")
    @NotEmpty(message = "任务编号不能为空")
    private String id;

    @Schema(description = "新审批人的用户编号", required=true, example = "2048")
    @NotNull(message = "新审批人的用户编号不能为空")
    private Long assigneeUserId;

    @Schema(description = "转办原因", required=true, example = "做不了决定，需要你先帮忙瞅瞅")
    @NotEmpty(message = "转办原因不能为空")
    private String reason;

}
