package org.example.job.controller;

import org.example.job.commons.SystemResponse;
import org.example.job.service.DatasourceMetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 数据源元数据信息管理
 *
 * @author zhuxj
 * @since 2020/12/23
 */
@RestController
public class DatasourceMetadataController {

    @Autowired
    private DatasourceMetadataService datasourceMetadataService;

    /**
     * 查询数据源模式列表
     *
     * @param id 数据源编号
     * @return 数据源模式名称列表
     */
    @GetMapping(value = "/datasource/{id}/schemas")
    public SystemResponse<List<String>> querySchemas(@PathVariable Integer id) {
        List<String> schemas = this.datasourceMetadataService.querySchemas(id);
        return SystemResponse.success(schemas);
    }

    /**
     * 查询数据源表信息
     *
     * @param id     数据源编号
     * @param schema 数据源模式名称
     * @return 数据源表名称列表
     */
    @GetMapping(value = "/datasource/{id}/tables")
    public SystemResponse<List<String>> queryTables(@PathVariable Integer id,
                                                    @RequestParam String schema) {
        List<String> tables = this.datasourceMetadataService.queryTables(id, schema);
        return SystemResponse.success(tables);
    }

    /**
     * 查询数据源字段列表
     *
     * @param id     数据源编号
     * @param schema 数据源模式名称
     * @param table  数据源表名称
     * @return 数据源字段名称列表
     */
    @GetMapping(value = "/datasource/{id}/columns")
    public SystemResponse<List<String>> queryColumns(@PathVariable Integer id,
                                                     @RequestParam String schema,
                                                     @RequestParam String table) {
        List<String> columns = this.datasourceMetadataService.queryColumns(id, schema, table);
        return SystemResponse.success(columns);
    }
}
