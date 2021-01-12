package org.example.job.commons;

/**
 * @author zhuxj
 * @since 2021/1/11
 */
public class PluginKeys {
    /**
     * jdbc 连接地址
     */
    public static final String JDBC_URL = "jdbcUrl";
    /**
     * 用户名
     */
    public static final String USERNAME = "username";
    /**
     * 密码
     */
    public static final String PASSWORD = "password";


    public static final String JOB_CONTENT_READER_PARAMETER = "job.content.reader.parameter";
    public static final String JOB_CONTENT_WRITER_PARAMETER = "job.content.writer.parameter";

    public static final String JOB_CONTENT_READER_NAME = "job.content.reader.name";
    public static final String JOB_CONTENT_WRITER_NAME = "job.content.writer.name";
    /**
     * 插件名称
     */
    public static final String MYSQL_READER = "mysqlreader";
    public static final String MYSQL_WRITER = "mysqlwriter";
}
