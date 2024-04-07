package com.ruoyi.bpm.convert;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

import com.ruoyi.bpm.controller.definition.vo.model.BpmModelCreateReqVO;
import com.ruoyi.bpm.controller.definition.vo.model.BpmModelUpdateReqVO;
import com.ruoyi.bpm.service.definition.dto.BpmModelMetaInfoRespDTO;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ProcessDefinition;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 流程模型 Convert
 *
 * @author yunlongn
 */
@Mapper
public interface BpmModelConvert {

    BpmModelConvert INSTANCE = Mappers.getMapper(BpmModelConvert.class);


    default void copy(Model model, BpmModelCreateReqVO bean) {
        model.setName(bean.getName());
        model.setKey(bean.getKey());
        model.setMetaInfo(new BpmModelMetaInfoRespDTO().copy(bean).toString());
    }

    default void copy(Model model, BpmModelUpdateReqVO bean) {
        model.setName(bean.getName());
        model.setCategory(bean.getCategory());
        model.setMetaInfo(new BpmModelMetaInfoRespDTO(model).copy(bean).toString());
    }

}
