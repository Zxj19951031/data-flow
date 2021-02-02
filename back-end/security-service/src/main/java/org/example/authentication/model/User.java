package org.example.authentication.model;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * tb_user
 * @author 
 */
@Data
public class User implements Serializable {
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 记录有效性 0-有效，1-无效
     */
    private Integer status;

    private static final long serialVersionUID = 1L;
}