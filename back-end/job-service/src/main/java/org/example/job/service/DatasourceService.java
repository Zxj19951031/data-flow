package org.example.job.service;

import org.example.job.dto.DatasourceQueryDTO;
import org.example.job.model.Datasource;

import java.util.List;

/**
 * @author zhuxj
 * @since 2020/12/21
 */
public interface DatasourceService {
    int save(Datasource datasource);

    /**
     * 通过ID查询
     *
     * @param id ID
     * @return Datasource
     */
    Datasource findById(Integer id);


    /**
     * 通过ID删除
     *
     * @param id ID
     * @return deleted count
     */
    int deleteById(Integer id);

    /**
     * 更新记录
     *
     * @param datasource Datasource
     * @return updated count
     */
    int update(Datasource datasource);


    /**
     * 查询列表
     *
     * @param params 查询参数
     * @return 符合条件的列表
     * @see DatasourceQueryDTO
     */
    List<Datasource> listByParams(DatasourceQueryDTO params);
}
