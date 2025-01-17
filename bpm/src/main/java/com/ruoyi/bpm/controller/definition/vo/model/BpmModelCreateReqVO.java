package com.ruoyi.bpm.controller.definition.vo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.flowable.engine.repository.Model;

import javax.validation.constraints.NotEmpty;

import static com.ruoyi.bpm.framework.flowable.core.util.BpmnModelUtils.buildMetaInfoStr;

@Schema(description = "管理后台 - 流程模型的创建 Request VO")
@Data
public class BpmModelCreateReqVO {

    @Schema(description = "流程标识",required=true, example = "process_yudao")
    @NotEmpty(message = "流程标识不能为空")
    private String key;

    @Schema(description = "流程名称",required=true, example = "芋道")
    @NotEmpty(message = "流程名称不能为空")
    private String name;

    @Schema(description = "流程描述", example = "我是描述")
    private String description;
    public void copyTo(Model model) {
        model.setName(getName());
        model.setKey(getKey());
        model.setMetaInfo(buildMetaInfoStr(null,
                null, getDescription(),
                null, null, null, null));
    }
}
