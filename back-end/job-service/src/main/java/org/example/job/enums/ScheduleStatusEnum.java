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
    SCHEDULED(1);

    private final int value;

    ScheduleStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
