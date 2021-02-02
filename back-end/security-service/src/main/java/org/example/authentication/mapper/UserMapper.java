package org.example.authentication.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.authentication.model.User;

@Mapper
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    User selectByUsername(String username);
}