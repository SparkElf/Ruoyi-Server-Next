package com.ruoyi.bpm.controller.task.vo.instance;


import com.ruoyi.common.core.domain.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


@Schema(description = "管理后台 - 流程实例抄送的分页 Request VO")
@Data
public class BpmProcessInstanceCopyPageReqVO extends PageParam {

    @Schema(description = "流程名称", example = "芋道")
    private String processInstanceName;

    @Schema(description = "创建时间")
    private LocalDateTime[] createTime;

}
