package org.example.job.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.job.enums.InstanceStatusEnum;
import org.example.job.exceptions.SystemError;
import org.example.job.exceptions.SystemException;
import org.example.job.mapper.DatasourceMapper;
import org.example.job.mapper.JobInstanceMapper;
import org.example.job.mapper.JobMapper;
import org.example.job.model.Datasource;
import org.example.job.model.Job;
import org.example.job.model.JobInstance;
import org.example.job.service.JobInstanceService;
import org.example.job.service.PluginService;
import org.example.plugins.common.commons.CoreConstant;
import org.example.plugins.common.commons.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author zhuxj
 * @since 2021/1/11
 */
@Slf4j
@Service
public class JobInstanceServiceImpl implements JobInstanceService {

    @Autowired
    private JobInstanceMapper jobInstanceMapper;

    @Autowired
    private JobMapper jobMapper;

    @Autowired
    private DatasourceMapper datasourceMapper;

    @Autowired
    private PluginService pluginService;

    @Override
    @Transactional
    public int save(Integer id) {

        Job job = jobMapper.selectByPrimaryKey(id);
        if (job == null) {
            log.error("目标任务不存在，任务编号={}", id);
            throw SystemException.newException(SystemError.PARAMETER_ERROR, "目标任务不存在");
        }

        //构造新的实例，填充任务快照内容
        JobInstance jobInstance = new JobInstance();
        jobInstance.setJobId(id);
        jobInstance.setFromDatasource(job.getFromDatasource());
        jobInstance.setToDatasource(job.getToDatasource());
        jobInstance.setPluginConfig(job.getPluginConfig());
        jobInstance.setStatus(InstanceStatusEnum.RUNNING.getValue());
        jobInstance.setStartTime(new Date());

        //补充数据源连接信息至配置形成最终递交给datax运行的配置文件
        JsonObject finalConfig = JsonObject.from(jobInstance.getPluginConfig());

        Datasource from = this.datasourceMapper.selectByPrimaryKey(job.getFromDatasource());
        Datasource to = this.datasourceMapper.selectByPrimaryKey(job.getToDatasource());
        finalConfig = this.pluginService.fixReaderDatasourceParameters(from, finalConfig);
        finalConfig = this.pluginService.fixWriterDatasourceParameters(to, finalConfig);

        //记录的新增要在发起交换之前，以保证当任务一旦发生错误实例记录已经生成
        int cnt = this.jobInstanceMapper.insert(jobInstance);
        log.info("新增{}条实例记录成功，初始状态为 RUNNING", cnt);
        finalConfig.set(CoreConstant.JOB_ID,jobInstance.getId());

        //异步发起交换任务
        this.pluginService.runDatax(finalConfig);


        return jobInstance.getId();
    }

    @Override
    public int update(JobInstance jobInstance) {
        return this.jobInstanceMapper.updateByPrimaryKeySelective(jobInstance);
    }

    @Override
    public int updateStatus(Integer id, InstanceStatusEnum status) {

        return this.jobInstanceMapper.updateStatus(id, status.getValue());
    }

    @Override
    public List<JobInstance> findByJobId(Integer jobId) {
        return this.jobInstanceMapper.selectByJobId(jobId);
    }
}
