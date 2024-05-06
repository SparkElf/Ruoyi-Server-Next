package com.ruoyi.common.core.page;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {
    /** 总记录数 */
    private long total;

    /** 列表数据 */
    private List<T> rows;
}
