package org.example.job.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.job.dto.DatasourceQueryDTO;
import org.example.job.model.Datasource;

import java.util.List;

@Mapper
public interface DatasourceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Datasource record);

    Datasource selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Datasource record);

    List<Datasource> select(DatasourceQueryDTO params);
}