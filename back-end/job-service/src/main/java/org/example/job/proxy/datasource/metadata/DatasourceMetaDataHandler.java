package org.example.job.proxy.datasource.metadata;

import org.example.job.enums.DatasourceMetadataEnum;
import org.example.job.model.Datasource;

import java.util.List;

/**
 * @author zhuxj
 * @since 2020/12/23
 */
public interface DatasourceMetaDataHandler {

    public List<String> query(Datasource datasource, DatasourceMetadataEnum metadataEnum, String... variables);

    public boolean connection(Datasource datasource);

}
