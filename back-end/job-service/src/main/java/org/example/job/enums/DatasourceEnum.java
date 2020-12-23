package org.example.job.enums;

import lombok.extern.slf4j.Slf4j;
import org.example.job.exceptions.SystemError;
import org.example.job.exceptions.SystemException;

/**
 * @author zhuxj
 * @since 2020/12/21
 */
@Slf4j
public enum DatasourceEnum {
    MYSQL(1);

    DatasourceEnum(int value) {
        this.value = value;
    }

    private final int value;

    public int getValue() {
        return value;
    }

    public static DatasourceEnum getByValue(int value) {
        for (DatasourceEnum i : DatasourceEnum.values()) {
            if (i.value == value) {
                return i;
            }
        }
        log.error("枚举类型异常,value={}", value);
        throw SystemException.newException(SystemError.ENUM_ERROR);
    }
}
