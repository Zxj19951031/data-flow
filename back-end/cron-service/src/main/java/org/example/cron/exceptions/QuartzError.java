package org.example.cron.exceptions;

/**
 * @author zhuxj
 * @since 2020/12/17
 */
public enum QuartzError implements IErrorCode {
    CRON_EXPRESSION_ERROR(5001, "调度表达式异常"),
    ;

    private final int code;
    private final String description;

    QuartzError(int code, String description) {
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