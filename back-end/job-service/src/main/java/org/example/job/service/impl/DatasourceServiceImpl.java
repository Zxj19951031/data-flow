package org.example.job.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.job.DatasourceConstants;
import org.example.job.dto.DatasourceQueryDTO;
import org.example.job.enums.DatasourceEnum;
import org.example.job.mapper.DatasourceMapper;
import org.example.job.model.Datasource;
import org.example.job.service.DatasourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author zhuxj
 * @since 2020/12/21
 */
@Service
@Slf4j
public class DatasourceServiceImpl implements DatasourceService {

    @Autowired
    private DatasourceMapper datasourceMapper;

    @Override
    public int save(Datasource datasource) {
        Assert.notNull(datasource, "数据源对象不可空");
        Assert.notNull(DatasourceEnum.getByValue(datasource.getType()), "错误的枚举类型");

        DatasourceEnum type = DatasourceEnum.getByValue(datasource.getType());
        if (DatasourceEnum.MYSQL.equals(type)) {
            Assert.notNull(datasource.getHost(), "主机地址不可以为空");
            if (datasource.getPort() == null || datasource.getPort() <= 0) {
                log.info("数据源端口错误，将调整为默认值，from={}，to={}", datasource.getPort(), DatasourceConstants.MYSQL_DEFAULT_PORT);
                datasource.setPort(DatasourceConstants.MYSQL_DEFAULT_PORT);
            }
        }

        int cnt = this.datasourceMapper.insert(datasource);
        log.info("新增完成，record={}，cnt={}", datasource, cnt);
        return datasource.getId();
    }

    @Override
    public Datasource findById(Integer id) {
        Datasource datasource = this.datasourceMapper.selectByPrimaryKey(id);
        log.info("查询完成，id={}，record={}", id, datasource);
        return datasource;
    }

    @Override
    public int deleteById(Integer id) {
        int cnt = this.datasourceMapper.deleteByPrimaryKey(id);
        log.info("删除完成，id={}，cnt={}", id, cnt);
        return cnt;
    }

    @Override
    public int update(Datasource datasource) {
        int cnt = this.datasourceMapper.updateByPrimaryKeySelective(datasource);
        log.info("更新完成，record={}，cnt={}", datasource, cnt);
        return cnt;
    }

    @Override
    public List<Datasource> listByParams(DatasourceQueryDTO params) {

        return this.datasourceMapper.select(params);
    }
}
