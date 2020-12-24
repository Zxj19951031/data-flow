package org.example.job.enums;

/**
 * @author zhuxj
 * @since 2020/12/24
 */
public enum ScheduleStatusEnum {
    /**
     * 未运行
     */
    STOPPED(0),
    /**
     * 调度中
     */
    SCHEDULED(1),
    /**
     * 成功结束
     */
    FINISH_SUCCESS(2),
    /**
     * 异常结束
     */
    FINISH_ERROR(3),
    /**
     * 告警结束
     */
    FINISH_WARN(4);

    private final int value;

    ScheduleStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
