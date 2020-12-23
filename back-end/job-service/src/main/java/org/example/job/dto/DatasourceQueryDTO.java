package org.example.job.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhuxj
 * @since 2020/12/22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatasourceQueryDTO {
    private String name;
    private Integer type;

}
