package com.ruoyi.bpm.controller.definition.vo.model;


import com.ruoyi.bpm.service.definition.dto.BpmModelMetaInfoRespDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.Model;

import java.time.LocalDateTime;
import java.util.Date;

@Schema(description = "管理后台 - 流程模型 Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class BpmModelRespVO {

    @Schema(description = "编号", required=true, example = "1024")
    private String id;

    @Schema(description = "流程标识", required=true, example = "process_yudao")
    private String key;

    @Schema(description = "流程名称", required=true, example = "芋道")
    private String name;

    @Schema(description = "流程图标", example = "https://www.iocoder.cn/yudao.jpg")
    private String icon;

    @Schema(description = "流程描述", example = "我是描述")
    private String description;

    @Schema(description = "流程分类编码", example = "1")
    private String category;
    @Schema(description = "流程分类名字", example = "请假")
    private String categoryName;

    @Schema(description = "表单类型-参见 bpm_model_form_type 数据字典", example = "1")
    private Integer formType;

    @Schema(description = "表单编号", example = "1024")
    private Long formId; // 在表单类型为 {@link BpmModelFormTypeEnum#CUSTOM} 时，必须非空
    @Schema(description = "表单名字", example = "请假表单")
    private String formName;

    @Schema(description = "自定义表单的提交路径", example = "/bpm/oa/leave/create")
    private String formCustomCreatePath; // 使用 Vue 的路由地址-在表单类型为 {@link BpmModelFormTypeEnum#CUSTOM} 时，必须非空
    @Schema(description = "自定义表单的查看路径", example = "/bpm/oa/leave/view")
    private String formCustomViewPath; // ，使用 Vue 的路由地址-在表单类型为 {@link BpmModelFormTypeEnum#CUSTOM} 时，必须非空

    @Schema(description = "创建时间", required=true)
    private Date createTime;

    @Schema(description = "BPMN XML", required=true)
    private String bpmnXml;

    /**
     * 最新部署的流程定义
     */
//    private BpmProcessDefinitionRespVO processDefinition;
    public BpmModelRespVO(Model model,
                   byte[] bpmnBytes) {
        this(model, new BpmModelMetaInfoRespDTO(model));
        if (bpmnBytes!=null&&bpmnBytes.length!=0) {
            setBpmnXml(new String(bpmnBytes));
        }

    }
    public BpmModelRespVO(Model model,
    BpmModelMetaInfoRespDTO metaInfo) {
        setId(model.getId());
        setName(model.getName());
        setKey(model.getKey());setCategory(model.getCategory());
        setCreateTime(model.getCreateTime());
        // Form
        if (metaInfo != null) {
            setFormType(metaInfo.getFormType()).setFormId(metaInfo.getFormId())
                    .setFormCustomCreatePath(metaInfo.getFormCustomCreatePath())
                    .setFormCustomViewPath(metaInfo.getFormCustomViewPath());
            setIcon(metaInfo.getIcon()).setDescription(metaInfo.getDescription());
        }
//        if (form != null) {
//            modelRespVO.setFormId(form.getId()).setFormName(form.getName());
//        }
//        // Category
//        if (category != null) {
//            modelRespVO.setCategoryName(category.getName());
//        }
//        // ProcessDefinition
//        if (processDefinition != null) {
//            modelRespVO.setProcessDefinition(BeanUtils.toBean(processDefinition, BpmProcessDefinitionRespVO.class));
//            modelRespVO.getProcessDefinition().setSuspensionState(processDefinition.isSuspended() ?
//                    SuspensionState.SUSPENDED.getStateCode() : SuspensionState.ACTIVE.getStateCode());
//            if (deployment != null) {
//                modelRespVO.getProcessDefinition().setDeploymentTime(DateUtils.of(deployment.getDeploymentTime()));
//            }
//        }
    }
}
