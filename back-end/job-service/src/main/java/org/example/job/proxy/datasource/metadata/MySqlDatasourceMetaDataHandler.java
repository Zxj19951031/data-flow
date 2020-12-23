package org.example.job.proxy.datasource.metadata;

import com.mysql.cj.jdbc.MysqlDataSource;
import lombok.extern.slf4j.Slf4j;
import org.example.job.enums.DatasourceMetadataEnum;
import org.example.job.exceptions.DatabaseError;
import org.example.job.exceptions.SystemException;
import org.example.job.model.Datasource;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhuxj
 * @since 2020/12/23
 */

@Slf4j
@Component
public class MySqlDatasourceMetaDataHandler implements DatasourceMetaDataHandler {

    private static final String TABLE_CAT = "TABLE_CAT";
    private static final String TABLE_NAME = "TABLE_NAME";
    private static final String COLUMN_NAME = "COLUMN_NAME";
    private static final String TYPE_NAME = "TYPE_NAME";
    private static final String COLUMN_SIZE = "COLUMN_SIZE";

    private static final int CONNECT_TIMEOUT_MS = 30000;

    @Override
    public List<String> query(Datasource datasource, DatasourceMetadataEnum metadataEnum, String... variable) {

        switch (metadataEnum) {
            case SCHEMA:
                log.info("query schemas in database");
                return this.querySchema(datasource.getHost(), datasource.getPort(), datasource.getUsername(), datasource.getPassword());
            case TABLE:
                log.info("query tables in schema {}", variable[0]);
                return this.queryTable(datasource.getHost(), datasource.getPort(), datasource.getUsername(), datasource.getPassword(), variable[0]);
            case COLUMN:
                log.info("query columns in table {}.{} ", variable[0], variable[1]);
                return this.queryColumn(datasource.getHost(), datasource.getPort(), datasource.getUsername(), datasource.getPassword(), variable[0], variable[1]);
            default:
                throw new IllegalArgumentException("unknown DatasourceMetadataEnum");
        }
    }

    private List<String> querySchema(String host, Integer port, String username, String password) {

        Connection connection = null;
        ResultSet rs = null;
        try {
            MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setServerName(host);
            dataSource.setPort(port);
            dataSource.setUser(username);
            dataSource.setPassword(password);
            dataSource.setConnectTimeout(CONNECT_TIMEOUT_MS);

            List<String> schemas = new ArrayList<>();
            connection = dataSource.getConnection();
            rs = connection.getMetaData().getCatalogs();
            while (rs.next()) {
                schemas.add(rs.getString(TABLE_CAT));
            }
            return schemas;
        } catch (SQLException e) {
            log.error("查询数据源Schema信息异常", e);
            throw SystemException.newException(DatabaseError.METADATA_QUERY_ERROR, e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                    log.info("关闭 Connection 连接");
                } catch (SQLException e) {
                    log.error("关闭 Connection 连接失败", e);
                }
            }

            if (rs != null) {
                try {
                    rs.close();
                    log.info("关闭 ResultSet 连接");
                } catch (SQLException e) {
                    log.error("关闭 ResultSet 连接失败", e);
                }
            }
        }

    }

    private List<String> queryTable(String host, Integer port, String username, String password, String schema) {
        Connection connection = null;
        ResultSet rs = null;
        try {
            MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setServerName(host);
            dataSource.setPort(port);
            dataSource.setUser(username);
            dataSource.setPassword(password);
            dataSource.setConnectTimeout(CONNECT_TIMEOUT_MS);

            List<String> schemas = new ArrayList<>();
            connection = dataSource.getConnection();
            rs = connection.getMetaData().getTables(schema, null, null, null);
            while (rs.next()) {
                schemas.add(rs.getString(TABLE_NAME));
            }
            return schemas;
        } catch (SQLException e) {
            log.error("查询数据源Schema信息异常", e);
            throw SystemException.newException(DatabaseError.METADATA_QUERY_ERROR, e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                    log.info("connection closed");
                } catch (SQLException e) {
                    log.error("关闭 Connection 连接失败", e);
                }
            }

            if (rs != null) {
                try {
                    rs.close();
                    log.info("result set closed");
                } catch (SQLException e) {
                    log.error("关闭 ResultSet 连接失败", e);
                }
            }
        }
    }

    private List<String> queryColumn(String host, Integer port, String username, String password, String schema, String table) {
        Connection connection = null;
        ResultSet rs = null;
        try {
            MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setServerName(host);
            dataSource.setPort(port);
            dataSource.setUser(username);
            dataSource.setPassword(password);
            dataSource.setConnectTimeout(CONNECT_TIMEOUT_MS);

            List<String> schemas = new ArrayList<>();
            connection = dataSource.getConnection();
            rs = connection.getMetaData().getColumns(schema, null, table, "%");
            while (rs.next()) {
                schemas.add(String.format("%s[%s(%s)]", rs.getString(COLUMN_NAME), rs.getString(TYPE_NAME), rs.getString(COLUMN_SIZE)));
            }
            return schemas;
        } catch (SQLException e) {
            log.error("查询数据源Schema信息异常", e);
            throw SystemException.newException(DatabaseError.METADATA_QUERY_ERROR, e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                    log.info("connection closed");
                } catch (SQLException e) {
                    log.error("关闭 Connection 连接失败", e);
                }
            }

            if (rs != null) {
                try {
                    rs.close();
                    log.info("result set closed");
                } catch (SQLException e) {
                    log.error("关闭 ResultSet 连接失败", e);
                }
            }
        }
    }
}
