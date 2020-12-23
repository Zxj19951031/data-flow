package org.example.job.service;

import java.util.List;

/**
 * @author zhuxj
 * @since 2020/12/23
 */
public interface DatasourceMetadataService {
    /**
     * 查询数据源的schemas
     *
     * @param id 数据源编号
     * @return list of schemas
     */
    List<String> querySchemas(Integer id);

    /**
     * 查询数据源的schema下的所有表
     *
     * @param id     数据源标号
     * @param schema 数据源模式
     * @return list of tables
     */
    List<String> queryTables(Integer id, String schema);

    /**
     * 查询数据源的schema.table的所有字段
     *
     * @param id     数据源编号
     * @param schema 数据源模式
     * @param table  数据源表
     * @return list of columns ,format: COLUMN_NAME[TYPE_NAME(COLUMN_SIZE)]
     */
    List<String> queryColumns(Integer id, String schema, String table);
}
