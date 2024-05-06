package com.ruoyi.mapper.category;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.mapper.LambdaQueryWrapperX;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.PageUtils;
import com.ruoyi.controller.definition.vo.category.BpmCategoryPageReqVO;
import com.ruoyi.domain.definition.BpmCategoryDO;

import icu.mhb.mybatisplus.plugln.base.mapper.JoinBaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * BPM 流程分类 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface BpmCategoryMapper extends JoinBaseMapper<BpmCategoryDO> {

    default TableDataInfo selectPage(BpmCategoryPageReqVO reqVO) {
        PageUtils.startPage(reqVO.getPageNum(), reqVO.getPageSize());
        var list= selectList(new LambdaQueryWrapperX<BpmCategoryDO>()
                .likeIfPresent(BpmCategoryDO::getName, reqVO.getName())
                .likeIfPresent(BpmCategoryDO::getCode, reqVO.getCode())
                .eqIfPresent(BpmCategoryDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(BpmCategoryDO::getCreateTime, reqVO.getCreateTime())
                .orderByAsc(BpmCategoryDO::getSort)
        );
        return new TableDataInfo(list,new PageInfo<>(list).getTotal());
    }

    default BpmCategoryDO selectByName(String name) {
        return selectOne(new LambdaQueryWrapper<BpmCategoryDO>().eq(BpmCategoryDO::getName, name));
    }

    default BpmCategoryDO selectByCode(String code) {
        return selectOne(new QueryWrapper<BpmCategoryDO>().lambda().eq( BpmCategoryDO::getCode, code));
    }

    default List<BpmCategoryDO> selectListByCode(Collection<String> codes) {
        return selectList(new LambdaQueryWrapper<BpmCategoryDO>().in(BpmCategoryDO::getCode, codes));
    }

    default List<BpmCategoryDO> selectListByStatus(Integer status) {
        return selectList(new LambdaQueryWrapper<BpmCategoryDO>().in(BpmCategoryDO::getStatus, status));
    }

}