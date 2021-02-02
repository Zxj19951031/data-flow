package org.example.authentication.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.authentication.model.Resource;

import java.util.List;

@Mapper
public interface ResourceMapper {

    /**
     * 查询某个用户可见的资源
     * 不包括功能
     *
     * @param username 用户名
     * @return 菜单列表
     */
    List<Resource> selectByUsername(String username);
}