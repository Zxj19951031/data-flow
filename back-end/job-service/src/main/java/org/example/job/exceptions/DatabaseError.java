package org.example.job.exceptions;

/**
 * 系统内容异常
 *
 * @author zhuxj
 * @since 2020/10/13
 */
public enum DatabaseError implements IErrorCode {
    METADATA_QUERY_ERROR(9001, "元数据查询异常"),
    CONNECTION_ERROR(9002, "数据源连接异常"),
    CONSTRAINT_ERROR(9003, "约束异常"),
    ;

    private final int code;
    private final String description;

    DatabaseError(int code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.description;
    }

    public String toString() {
        return String.format("Code:[%s], Describe:[%s]", this.code, this.description);
    }
}