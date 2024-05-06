package com.ruoyi.common.core.domain;


import lombok.Data;


import java.io.Serializable;

import java.util.Date;

@Data
public abstract class BaseDO implements Serializable {

    /**
     * 创建时间
     */

    private Date createTime;
    /**
     * 最后更新时间
     */

    private Date updateTime;
    /**
     * 创建者，目前使用 SysUser 的 id 编号
     *
     * 使用 String 类型的原因是，未来可能会存在非数值的情况，留好拓展性。
     */
    private String create_by;
    /**
     * 更新者，目前使用 SysUser 的 id 编号
     *
     * 使用 String 类型的原因是，未来可能会存在非数值的情况，留好拓展性。
     */

    private String update_by;


}

