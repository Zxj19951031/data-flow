package org.example.job.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.job.model.Job;

@Mapper
public interface JobMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Job record);

    Job selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Job record);

}