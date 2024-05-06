package com.ruoyi.controller.definition.vo.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - 动态表单 Response VO")
@Data
public class BpmFormRespVO {

    @Schema(description = "表单编号",required=true, example = "1024")
    private Long id;

    @Schema(description = "表单名称",required=true, example = "芋道")
    @NotNull(message = "表单名称不能为空")
    private String name;

    @Schema(description = "表单的配置-JSON 字符串",required=true)
    @NotNull(message = "表单的配置不能为空")
    private String conf;

    @Schema(description = "表单项的数组-JSON 字符串的数组",required=true)
    @NotNull(message = "表单项的数组不能为空")
    private List<String> fields;

    @Schema(description = "表单状态",required=true, example = "1")
    @NotNull(message = "表单状态不能为空")
    private Integer status; // 参见 CommonStatusEnum 枚举

    @Schema(description = "备注", example = "我是备注")
    private String remark;

    @Schema(description = "创建时间",required=true)
    private LocalDateTime createTime;

}
