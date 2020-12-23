package org.example.job.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * tb_datasource
 *
 * @author zhuxj
 * @since 2020/12/21
 */
@Data
public class Datasource implements Serializable {
    private Integer id;

    /**
     * 数据源名称
     */
    private String name;

    /**
     * 数据源类型
     */
    private Integer type;

    /**
     * 主机地址
     */
    private String host;

    /**
     * 服务端口
     */
    private Integer port;

    /**
     * 登录用户名
     */
    private String username;

    /**
     * 登录密码
     */
    private String password;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    private Integer status;

    private static final long serialVersionUID = 1L;
}