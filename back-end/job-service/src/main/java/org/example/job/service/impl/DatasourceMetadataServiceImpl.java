package org.example.job.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.job.enums.DatasourceEnum;
import org.example.job.enums.DatasourceMetadataEnum;
import org.example.job.exceptions.SystemError;
import org.example.job.exceptions.SystemException;
import org.example.job.model.Datasource;
import org.example.job.proxy.datasource.metadata.DatasourceMetaDataHandler;
import org.example.job.proxy.datasource.metadata.MySqlDatasourceMetaDataHandler;
import org.example.job.service.DatasourceMetadataService;
import org.example.job.service.DatasourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhuxj
 * @since 2020/12/23
 */
@Service
@Slf4j
public class DatasourceMetadataServiceImpl implements DatasourceMetadataService {

    @Autowired
    private DatasourceService datasourceService;


    @Override
    public List<String> querySchemas(Integer id) {

        Datasource datasource = this.datasourceService.findById(id);
        DatasourceMetaDataHandler handler;
        switch (DatasourceEnum.getByValue(datasource.getType())) {
            case MYSQL:
                handler = new MySqlDatasourceMetaDataHandler();
                break;
            default:
                log.error("不合法的枚举类型，value={}", datasource.getType());
                throw SystemException.newException(SystemError.ENUM_ERROR);
        }
        return handler.query(datasource, DatasourceMetadataEnum.SCHEMA);
    }

    @Override
    public List<String> queryTables(Integer id, String schema) {

        Datasource datasource = this.datasourceService.findById(id);
        DatasourceMetaDataHandler handler;
        switch (DatasourceEnum.getByValue(datasource.getType())) {
            case MYSQL:
                handler = new MySqlDatasourceMetaDataHandler();
                break;
            default:
                log.error("不合法的枚举类型，value={}", datasource.getType());
                throw SystemException.newException(SystemError.ENUM_ERROR);
        }
        return handler.query(datasource, DatasourceMetadataEnum.TABLE, schema);
    }

    @Override
    public List<String> queryColumns(Integer id, String schema, String table) {
        Datasource datasource = this.datasourceService.findById(id);
        DatasourceMetaDataHandler handler;
        switch (DatasourceEnum.getByValue(datasource.getType())) {
            case MYSQL:
                handler = new MySqlDatasourceMetaDataHandler();
                break;
            default:
                log.error("不合法的枚举类型，value={}", datasource.getType());
                throw SystemException.newException(SystemError.ENUM_ERROR);
        }
        return handler.query(datasource, DatasourceMetadataEnum.COLUMN, schema, table);
    }
}
