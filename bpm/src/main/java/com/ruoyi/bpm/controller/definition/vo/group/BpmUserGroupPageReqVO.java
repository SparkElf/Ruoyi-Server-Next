package com.ruoyi.bpm.controller.definition.vo.group;


import com.ruoyi.common.core.domain.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@Schema(description = "管理后台 - 用户组分页 Request VO")
@Data
public class BpmUserGroupPageReqVO extends PageParam {

    @Schema(description = "编号", example = "1024")
    private Long id;

    @Schema(description = "组名", example = "芋道")
    private String name;

    @Schema(description = "状态", example = "1")
    private Integer status;


    @Schema(description = "创建时间")
    private Date[] createTime;

}