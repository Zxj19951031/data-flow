package org.example.cron.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * tb_cron
 *
 * @author zhuxj
 * @since 2020/12/16
 */
@Data
public class Cron implements Serializable {
    private Integer id;

    /**
     * 调度名称
     */
    private String name;

    /**
     * 调度表达式
     */
    private String expression;

    /**
     * 前端元数据
     */
    private String metadata;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    private Integer status;

    private static final long serialVersionUID = 1L;
}