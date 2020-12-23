package org.example.job.vo;

import lombok.Data;
import org.example.job.model.Datasource;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author zhuxj
 * @since 2020/12/21
 */
@Data
public class DatasourceSaveVO {

    @NotBlank(message = "数据源名称必填")
    @Size(min = 1, max = 32, message = "超出数据源名称长度32个字符限制")
    private String name;

    @NotNull(message = "数据源类型必选")
    private Integer type;

    private String host;

    private Integer port;

    private String username;

    private String password;

    public Datasource toDatasource() {
        Datasource datasource = new Datasource();
        BeanUtils.copyProperties(this, datasource);
        return datasource;
    }

}
