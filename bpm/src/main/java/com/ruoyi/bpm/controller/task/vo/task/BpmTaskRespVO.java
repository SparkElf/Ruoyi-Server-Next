package com.ruoyi.bpm.controller.task.vo.task;


import com.ruoyi.bpm.controller.task.vo.instance.BpmProcessInstanceRespVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Schema(description = "管理后台 - 流程任务 Response VO")
@Data
@Accessors(chain = true)
public class BpmTaskRespVO {

    @Schema(description = "任务编号", required=true, example = "1024")
    private String id;

    @Schema(description = "任务名字", required=true, example = "芋道")
    private String name;

    @Schema(description = "创建时间", required=true)
    private LocalDateTime createTime;

    @Schema(description = "结束时间", required=true)
    private LocalDateTime endTime;

    @Schema(description = "持续时间", example = "1000")
    private Long durationInMillis;

    @Schema(description = "任务状态", required=true, example = "2")
    private Integer status; // 参见 BpmTaskStatusEnum 枚举

    @Schema(description = "审批理由", required=true, example = "2")
    private String reason;

    /**
     * 负责人的用户信息
     */
    private BpmProcessInstanceRespVO.User ownerUser;
    /**
     * 审核的用户信息
     */
    private BpmProcessInstanceRespVO.User assigneeUser;

    @Schema(description = "任务定义的标识", required=true, example = "Activity_one")
    private String taskDefinitionKey;

    @Schema(description = "所属流程实例编号", required=true, example = "8888")
    private String processInstanceId;
    /**
     * 所属流程实例
     */
    private ProcessInstance processInstance;

    @Schema(description = "父任务编号", required=true, example = "1024")
    private String parentTaskId;
    @Schema(description = "子任务列表（由加签生成）", required=true, example = "childrenTask")
    private List<BpmTaskRespVO> children;

    @Schema(description = "表单编号", example = "1024")
    private Long formId;
    @Schema(description = "表单名字", example = "请假表单")
    private String formName;
    @Schema(description = "表单的配置-JSON 字符串")
    private String formConf;
    @Schema(description = "表单项的数组")
    private List<String> formFields;
    @Schema(description = "提交的表单值", required=true)
    private Map<String, Object> formVariables;

    @Data
    @Schema(description = "流程实例")
    public static class ProcessInstance {

        @Schema(description = "流程实例编号", required=true, example = "1024")
        private String id;

        @Schema(description = "流程实例名称", required=true, example = "芋道")
        private String name;

        @Schema(description = "提交时间", required=true)
        private LocalDateTime createTime;

        @Schema(description = "流程定义的编号", required=true, example = "2048")
        private String processDefinitionId;

        /**
         * 发起人的用户信息
         */
        private BpmProcessInstanceRespVO.User startUser;

    }

}
