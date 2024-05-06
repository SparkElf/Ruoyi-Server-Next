package com.ruoyi.bpm.controller.task.vo.instance;

import com.ruoyi.bpm.enums.task.BpmProcessInstanceStatusEnum;
import com.ruoyi.common.core.domain.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@Schema(description = "管理后台 - 流程实例分页 Request VO")
@Data
public class BpmProcessInstancePageReqVO extends PageParam {

    @Schema(description = "流程名称", example = "芋道")
    private String name;

    @Schema(description = "流程定义的编号", example = "2048")
    private String processDefinitionId;

    @Schema(description = "流程实例的状态", example = "1")
    private Integer status;

    @Schema(description = "流程分类", example = "1")
    private String category;

    @Schema(description = "创建时间")
    private Date[] createTime;

    @Schema(description = "发起用户编号", example = "1024")
    private Long startUserId; // 注意，只有在【流程实例】菜单，才使用该参数

}
