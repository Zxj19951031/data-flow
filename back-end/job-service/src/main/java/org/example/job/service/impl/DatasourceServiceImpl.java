package org.example.job.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.job.commons.DatasourceConstants;
import org.example.job.dto.DatasourceQueryDTO;
import org.example.job.enums.DatasourceEnum;
import org.example.job.exceptions.DatabaseError;
import org.example.job.exceptions.SystemError;
import org.example.job.exceptions.SystemException;
import org.example.job.mapper.DatasourceMapper;
import org.example.job.mapper.JobMapper;
import org.example.job.model.Datasource;
import org.example.job.model.Job;
import org.example.job.proxy.datasource.metadata.DatasourceMetaDataHandler;
import org.example.job.proxy.datasource.metadata.MySqlDatasourceMetaDataHandler;
import org.example.job.service.DatasourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhuxj
 * @since 2020/12/21
 */
@Service
@Slf4j
public class DatasourceServiceImpl implements DatasourceService {

    @Autowired
    private DatasourceMapper datasourceMapper;
    @Autowired
    private JobMapper jobMapper;

    @Override
    public int save(Datasource datasource) {
        Assert.notNull(datasource, "数据源对象不可空");

        DatasourceEnum type = DatasourceEnum.getByValue(datasource.getType());
        if (DatasourceEnum.MYSQL.equals(type)) {
            Assert.notNull(datasource.getHost(), "主机地址不可以为空");
            if (datasource.getPort() == null || datasource.getPort() <= 0) {
                log.info("数据源端口错误，将调整为默认值，from = {}，to = {}", datasource.getPort(), DatasourceConstants.MYSQL_DEFAULT_PORT);
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
        log.info("查询完成,id={},record={}", id, datasource);
        return datasource;
    }

    @Override
    public int deleteById(Integer id) {
        List<Job> jobs = jobMapper.selectByDatasource(id);
        if (jobs != null && jobs.size() > 0) {
            String jobNames = jobs.stream().map(Job::getName).collect(Collectors.joining(","));
            log.error("删除任务失败，存在和数据源关联的任务，数据源编号 = {}，任务名称 = [{}]", id, jobNames);
            throw SystemException.newException(DatabaseError.CONSTRAINT_ERROR,
                    String.format("存在和数据源关联的任务，请先删除任务!\n任务名称:%s", jobNames));
        }
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

    @Override
    public boolean connectivity(Datasource datasource) {
        assert datasource != null;
        DatasourceMetaDataHandler handler;
        switch (DatasourceEnum.getByValue(datasource.getType())) {
            case MYSQL:
                handler = new MySqlDatasourceMetaDataHandler();
                break;
            default:
                log.error("不合法的枚举类型，value={}", datasource.getType());
                throw SystemException.newException(SystemError.ENUM_ERROR);
        }
        return handler.connection(datasource);
    }
}
