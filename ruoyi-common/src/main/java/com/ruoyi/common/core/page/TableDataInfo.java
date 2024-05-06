package com.ruoyi.common.core.page;

import com.ruoyi.common.constant.HttpStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 表格分页数据对象
 * 
 * @author ruoyi
 */
public class TableDataInfo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 总记录数 */
    private long total;

    /** 列表数据 */
    private List<?> rows;

    /** 消息状态码 */
    private int code;

    /** 消息内容 */
    private String msg;

    /**
     * 表格数据对象
     */
    public TableDataInfo()
    {
        this.total=0;
        this.rows=new ArrayList<>();
        setCode(HttpStatus.SUCCESS);
        setMsg("查询成功");
    }

    /**
     * 分页
     * 
     * @param list 列表数据
     * @param total 总记录数
     */
    public TableDataInfo(List<?> list, long total)
    {
        this.rows = list;
        this.total = total;
        setCode(HttpStatus.SUCCESS);
        setMsg("查询成功");
    }

    public long getTotal()
    {
        return total;
    }

    public void setTotal(long total)
    {
        this.total = total;
    }

    public List<?> getRows()
    {
        return rows;
    }

    public void setRows(List<?> rows)
    {
        this.rows = rows;
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }
    public boolean isEmpty(){
        return rows == null || rows.isEmpty();
    }
    public static TableDataInfo empty(){
        TableDataInfo tableDataInfo = new TableDataInfo();
        tableDataInfo.setRows(new ArrayList<Object>());
        tableDataInfo.setTotal(0);
        tableDataInfo.setCode(HttpStatus.SUCCESS);
        tableDataInfo.setMsg("查询成功");
        return tableDataInfo;
    }
}
