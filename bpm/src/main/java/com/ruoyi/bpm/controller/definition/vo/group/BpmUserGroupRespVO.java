package com.ruoyi.bpm.controller.definition.vo.group;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Schema(description = "管理后台 - 用户组 Response VO")
@Data
public class BpmUserGroupRespVO {

    @Schema(description = "编号",required=true, example = "1024")
    private Long id;

    @Schema(description = "组名",required=true, example = "芋道")
    private String name;

    @Schema(description = "描述",required=true, example = "芋道源码")
    private String description;

    @Schema(description = "成员编号数组",required=true, example = "1,2,3")
    private Set<Long> userIds;

    @Schema(description = "状态",required=true, example = "1")
    private Integer status;

    @Schema(description = "创建时间",required=true)
    private LocalDateTime createTime;

}
