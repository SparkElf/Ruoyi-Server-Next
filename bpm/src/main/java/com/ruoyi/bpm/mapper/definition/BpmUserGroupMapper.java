package com.ruoyi.mapper.definition;

import cn.hutool.core.util.StrUtil;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.PageUtils;
import com.ruoyi.controller.definition.vo.group.BpmUserGroupPageReqVO;
import com.ruoyi.controller.definition.vo.listener.BpmProcessListenerPageReqVO;
import com.ruoyi.domain.definition.BpmProcessListenerDO;
import com.ruoyi.domain.definition.BpmUserGroupDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户组 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface BpmUserGroupMapper extends BaseMapper<BpmUserGroupDO> {

    default TableDataInfo selectPage(BpmUserGroupPageReqVO reqVO) {
        PageUtils.startPage(reqVO.getPageNum(), reqVO.getPageSize());
        var list= selectList(new LambdaQueryWrapper<BpmUserGroupDO>()
                .like(StrUtil.isNotEmpty(reqVO.getName()),BpmUserGroupDO::getName, reqVO.getName())
                .eq(reqVO.getStatus()!=null,BpmUserGroupDO::getStatus, reqVO.getStatus())
                .between(BpmUserGroupDO::getCreateTime, reqVO.getCreateTime()[0],reqVO.getCreateTime()[1])
                .orderByDesc(BpmUserGroupDO::getId)
        );
        return new TableDataInfo(list,new PageInfo<>(list).getTotal());
    }

    default List<BpmUserGroupDO> selectListByStatus(Integer status) {
        return selectList(new LambdaQueryWrapper<BpmUserGroupDO>().eq( BpmUserGroupDO::getStatus, status));
    }

}
