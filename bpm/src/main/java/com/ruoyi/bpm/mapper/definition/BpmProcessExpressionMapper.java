package com.ruoyi.mapper.definition;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.PageUtils;
import com.ruoyi.controller.definition.vo.expression.BpmProcessExpressionPageReqVO;
import com.ruoyi.domain.definition.BpmFormDO;
import com.ruoyi.domain.definition.BpmProcessExpressionDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * BPM 流程表达式 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface BpmProcessExpressionMapper extends BaseMapper<BpmProcessExpressionDO> {

    default TableDataInfo selectPage(BpmProcessExpressionPageReqVO reqVO) {
        PageUtils.startPage(reqVO.getPageNum(), reqVO.getPageSize());
        var list= selectList(new LambdaQueryWrapper<BpmProcessExpressionDO>()
                .like(StrUtil.isNotEmpty(reqVO.getName()),BpmProcessExpressionDO::getName, reqVO.getName())
                .eq(reqVO.getStatus()!=null,BpmProcessExpressionDO::getStatus, reqVO.getStatus())
                .orderByDesc(BpmProcessExpressionDO::getId)
        );
        return new TableDataInfo(list,new PageInfo<>(list).getTotal());
    }

}