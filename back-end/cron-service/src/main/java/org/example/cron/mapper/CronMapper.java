package org.example.cron.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.cron.model.Cron;

import java.util.List;

@Mapper
public interface CronMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cron record);

    int updateByPrimaryKeySelective(Cron record);

    Cron selectOne(Integer id);

    List<Cron> selectList(String name);
}