package com.ruoyi.bpm.service.definition;

import cn.hutool.core.collection.CollUtil;

import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.object.BeanUtils;
import com.ruoyi.bpm.controller.definition.vo.form.BpmFormPageReqVO;
import com.ruoyi.bpm.controller.definition.vo.form.BpmFormSaveReqVO;
import com.ruoyi.bpm.domain.definition.BpmFormDO;
import com.ruoyi.bpm.enums.ErrorCodeConstants;
import com.ruoyi.bpm.mapper.definition.BpmFormMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.*;

import static com.ruoyi.common.exception.ServiceExceptionUtil.exception;


/**
 * 动态表单 Service 实现类
 *
 * @author 风里雾里
 */
@Service
@Validated
public class BpmFormServiceImpl implements BpmFormService {

    @Resource
    private BpmFormMapper formMapper;

    @Override
    public Long createForm(BpmFormSaveReqVO createReqVO) {
        this.validateFields(createReqVO.getFields());
        // 插入
        BpmFormDO form = BeanUtils.toBean(createReqVO, BpmFormDO.class);
        formMapper.insert(form);
        // 返回
        return form.getId();
    }

    @Override
    public void updateForm(BpmFormSaveReqVO updateReqVO) {
        validateFields(updateReqVO.getFields());
        // 校验存在
        validateFormExists(updateReqVO.getId());
        // 更新
        BpmFormDO updateObj = BeanUtils.toBean(updateReqVO, BpmFormDO.class);
        formMapper.updateById(updateObj);
    }

    @Override
    public void deleteForm(Long id) {
        // 校验存在
        this.validateFormExists(id);
        // 删除
        formMapper.deleteById(id);
    }

    private void validateFormExists(Long id) {
        if (formMapper.selectById(id) == null) {
            throw exception(ErrorCodeConstants.FORM_NOT_EXISTS);
        }
    }

    @Override
    public BpmFormDO getForm(Long id) {
        return formMapper.selectById(id);
    }

    @Override
    public List<BpmFormDO> getFormList() {
        return formMapper.selectList(null);
    }

    @Override
    public List<BpmFormDO> getFormList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return formMapper.selectBatchIds(ids);
    }

    @Override
    public TableDataInfo getFormPage(BpmFormPageReqVO pageReqVO) {
        return formMapper.selectPage(pageReqVO);
    }

    /**
     * 校验 Field，避免 field 重复
     *
     * @param fields field 数组
     */
    private void validateFields(List<String> fields) {
        if (true) { // TODO 芋艿：兼容 Vue3 工作流：因为采用了新的表单设计器，所以暂时不校验
            return;
        }
//        Map<String, String> fieldMap = new HashMap<>(); // key 是 vModel，value 是 label
//        for (String field : fields) {
//            BpmFormFieldRespDTO fieldDTO = JsonUtils.parseObject(field, BpmFormFieldRespDTO.class);
//            Assert.notNull(fieldDTO);
//            String oldLabel = fieldMap.put(fieldDTO.getVModel(), fieldDTO.getLabel());
//            // 如果不存在，则直接返回
//            if (oldLabel == null) {
//                continue;
//            }
//            // 如果存在，则报错
//            throw exception(ErrorCodeConstants.FORM_FIELD_REPEAT, oldLabel, fieldDTO.getLabel(), fieldDTO.getVModel());
//        }
    }

}
