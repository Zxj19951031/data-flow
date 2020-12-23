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

    @GetMapping(value = "/datasource/{id}/schemas")
    public SystemResponse<List<String>> querySchemas(@PathVariable Integer id) {
        List<String> schemas = this.datasourceMetadataService.querySchemas(id);
        return SystemResponse.success(schemas);
    }

    @GetMapping(value = "/datasource/{id}/tables")
    public SystemResponse<List<String>> queryTables(@PathVariable Integer id,
                                                    @RequestParam String schema) {
        List<String> tables = this.datasourceMetadataService.queryTables(id, schema);
        return SystemResponse.success(tables);
    }

    @GetMapping(value = "/datasource/{id}/columns")
    public SystemResponse<List<String>> queryColumns(@PathVariable Integer id,
                                                     @RequestParam String schema,
                                                     @RequestParam String table) {
        List<String> columns = this.datasourceMetadataService.queryColumns(id, schema, table);
        return SystemResponse.success(columns);
    }
}
