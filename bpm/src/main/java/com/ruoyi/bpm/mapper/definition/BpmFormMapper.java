package com.ruoyi.mapper.definition;



import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.PageUtils;
import com.ruoyi.common.utils.StrUtils;
import com.ruoyi.controller.definition.vo.form.BpmFormPageReqVO;
import com.ruoyi.domain.definition.BpmFormDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 动态表单 Mapper
 *
 * @author 风里雾里
 */
@Mapper
public interface BpmFormMapper extends BaseMapper<BpmFormDO> {

    default TableDataInfo selectPage(BpmFormPageReqVO reqVO) {
        PageUtils.startPage(reqVO.getPageNum(), reqVO.getPageSize());
        var list= selectList(new LambdaQueryWrapper<BpmFormDO>()
                .like(StrUtil.isNotEmpty(reqVO.getName()), BpmFormDO::getName, reqVO.getName())
                .orderByDesc(BpmFormDO::getId)
        );
        return new TableDataInfo(list,new PageInfo<>(list).getTotal());
    }

}
