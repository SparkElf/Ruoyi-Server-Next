package com.ruoyi.mapper.definition;

import cn.hutool.core.util.StrUtil;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.PageUtils;
import com.ruoyi.controller.definition.vo.expression.BpmProcessExpressionPageReqVO;
import com.ruoyi.controller.definition.vo.listener.BpmProcessListenerPageReqVO;
import com.ruoyi.domain.definition.BpmProcessExpressionDO;
import com.ruoyi.domain.definition.BpmProcessListenerDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * BPM 流程监听器 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface BpmProcessListenerMapper extends BaseMapper<BpmProcessListenerDO> {

    default TableDataInfo selectPage(BpmProcessListenerPageReqVO reqVO) {
        PageUtils.startPage(reqVO.getPageNum(), reqVO.getPageSize());
        var list= selectList(new LambdaQueryWrapper<BpmProcessListenerDO>()
                .like(StrUtil.isNotEmpty(reqVO.getName()),BpmProcessListenerDO::getName, reqVO.getName())
                .eq(StrUtil.isNotEmpty(reqVO.getType()),BpmProcessListenerDO::getType, reqVO.getType())
                .eq(StrUtil.isNotEmpty(reqVO.getType()),BpmProcessListenerDO::getEvent, reqVO.getEvent())
                .eq(reqVO.getStatus()!=null,BpmProcessListenerDO::getStatus, reqVO.getStatus())
                .orderByDesc(BpmProcessListenerDO::getId)
        );
        return new TableDataInfo(list,new PageInfo<>(list).getTotal());
    }

}