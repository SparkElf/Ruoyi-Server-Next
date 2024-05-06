package com.ruoyi.common.core.page;

import lombok.Data;

@Data
public class PageParam {
    private Integer pageNum;
    private Integer pageSize;
    public Integer getFirstPage(){
        return (pageNum-1)*pageSize;
    }
}
