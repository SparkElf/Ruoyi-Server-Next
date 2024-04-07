package com.ruoyi.bpm.service.definition.dto;


import cn.hutool.core.util.StrUtil;
import com.ruoyi.bpm.controller.definition.vo.model.BpmModelCreateReqVO;
import com.ruoyi.bpm.controller.definition.vo.model.BpmModelUpdateReqVO;
import com.ruoyi.common.utils.JsonUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.flowable.engine.repository.Model;

import java.util.Objects;

/**
 * BPM 流程 MetaInfo Response DTO
 * 主要用于 { Model#setMetaInfo(String)} 的存储
 *
 * 最终，它的字段和 {@link cn.iocoder.yudao.module.bpm.dal.dataobject.definition.BpmProcessDefinitionInfoDO} 是一致的
 *
 * @author 芋道源码
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BpmModelMetaInfoRespDTO {

    /**
     * 流程图标
     */
    private String icon;
    /**
     * 流程描述
     */
    private String description;

    /**
     * 表单类型
     */
    private Integer formType;
    /**
     * 表单编号
     * 在表单类型为 {@link BpmModelFormTypeEnum#NORMAL} 时
     */
    private Long formId;
    /**
     * 自定义表单的提交路径，使用 Vue 的路由地址
     * 在表单类型为 {@link BpmModelFormTypeEnum#CUSTOM} 时
     */
    private String formCustomCreatePath;
    /**
     * 自定义表单的查看路径，使用 Vue 的路由地址
     * 在表单类型为 {@link BpmModelFormTypeEnum#CUSTOM} 时
     */
    private String formCustomViewPath;
    public BpmModelMetaInfoRespDTO(Model model) {
        BeanUtils.copyProperties(model.getMetaInfo(),this);
    }
    public BpmModelMetaInfoRespDTO copy(BpmModelUpdateReqVO reqVO){
        if (StrUtil.isNotEmpty(icon)) {
            setIcon(icon);
        }
        if (StrUtil.isNotEmpty(description)) {
            setDescription(description);
        }
        if (Objects.nonNull(formType)) {
            setFormType(formType);
            setFormId(formId);
            setFormCustomCreatePath(formCustomCreatePath);
            setFormCustomViewPath(formCustomViewPath);
        }
        return this;
    }

    public BpmModelMetaInfoRespDTO copy(BpmModelCreateReqVO reqVO){
        if (StrUtil.isNotEmpty(reqVO.getDescription())) {
            setDescription(description);
        }
        return this;
    }
    public String toString(){
        return JsonUtils.toJsonString(this);
    }
}
