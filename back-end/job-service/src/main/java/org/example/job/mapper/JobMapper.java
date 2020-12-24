package org.example.job.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.job.dto.JobQueryDTO;
import org.example.job.model.Job;

import java.util.List;

@Mapper
public interface JobMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Job record);

    Job selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Job record);

    List<Job> select(JobQueryDTO params);
}