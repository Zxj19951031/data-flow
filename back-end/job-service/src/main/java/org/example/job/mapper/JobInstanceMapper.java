package org.example.job.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.job.model.JobInstance;

@Mapper
public interface JobInstanceMapper {

    int insert(JobInstance record);

    JobInstance selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(JobInstance record);

    int updateStatus(@Param("id") Integer id, @Param("status") Integer status);
}