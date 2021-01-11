package org.example.job.service;

import org.example.job.dto.DatasourceQueryDTO;
import org.example.job.model.Datasource;

import java.util.List;

/**
 * @author zhuxj
 * @since 2020/12/21
 */
public interface DatasourceService {
    /**
     * 新增数据源
     *
     * @param datasource Datasource
     * @return 保存记录数
     */
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
     * 约束：被任务引用的数据源不能删除
     *
     * @param id ID
     * @return deleted count
     */
    int deleteById(Integer id);

    /**
     * 更新记录
     * 注意：数据源的类型不允许更新，因为当存在任务引用该数据源作为读写源时，修改类型会导致调度实例生成时配置异常
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

    /**
     * 获取联通性
     *
     * @param datasource 数据源内容
     */
    boolean connectivity(Datasource datasource);
}
