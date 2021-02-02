package org.example.authentication.model;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * tb_resource
 * @author 
 */
@Data
public class Resource implements Serializable {
    private Integer id;

    /**
     * 菜单或功能名称
     */
    private String name;

    /**
     * 菜单或功能标签，用于后台进行权限判断
     */
    private String label;

    /**
     * 上级菜单编号
     */
    private Integer pid;

    /**
     * 菜单等级，从0计数一级菜单，功能均为一级
     */
    private Integer level;

    /**
     * 排序字段
     */
    private Integer sort;

    /**
     * 菜单或功能全路径
     */
    private String code;

    /**
     * 前端元数据，维护icon，前端路由等
     */
    private String metadata;

    /**
     * 是否为功能，0-否，1-是
     */
    private Byte isFunc;

    /**
     * 记录创建时间
     */
    private Date createTime;

    /**
     * 记录更新时间
     */
    private Date updateTime;

    /**
     * 记录有效性，0-有效，1-无效
     */
    private Integer status;

    private static final long serialVersionUID = 1L;
}