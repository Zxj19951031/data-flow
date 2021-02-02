package org.example.authentication.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * @author zhuxj
 * @since 2021/1/28
 */
@Data
public class ResourceVO {

    /**
     * 编号
     */
    private Integer id;
    /**
     * 名称
     */
    private String name;
    /**
     * 前端元数据
     */
    private Object metadata;
    /**
     * 上级节点编号
     */
    @JsonIgnore
    private Integer pid;
    /**
     * 子菜单
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ResourceVO> children;
}
