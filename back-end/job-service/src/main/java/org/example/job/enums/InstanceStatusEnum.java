package org.example.job.enums;

/**
 * @author zhuxj
 * @since 2021/1/11
 */
public enum InstanceStatusEnum {
    /**
     * 等待中
     */
    WAITING(-1),
    /**
     * 运行中
     */
    RUNNING(0),
    /**
     * 成功
     */
    SUCCESS(1),
    /**
     * 告警发生
     */
    WARNING(2),
    /**
     * 异常结束
     */
    ERROR(3);

    private final int value;

    InstanceStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
