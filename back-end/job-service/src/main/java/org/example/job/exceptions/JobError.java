package org.example.job.exceptions;

/**
 * 系统内容异常
 *
 * @author zhuxj
 * @since 2020/10/13
 */
public enum JobError implements IErrorCode {
    REGISTER_ERROR(8001, "注册任务失败"),
    CANCEL_ERROR(8002, "注销任务失败");

    private final int code;
    private final String description;

    JobError(int code, String description) {
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