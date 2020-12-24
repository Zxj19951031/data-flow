package org.example.job.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhuxj
 * @since 2020/12/24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobQueryDTO {

    private String name;

    private Integer scheduleStatus;
}
