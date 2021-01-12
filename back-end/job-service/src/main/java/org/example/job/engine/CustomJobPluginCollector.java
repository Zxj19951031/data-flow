package org.example.job.engine;

import org.example.job.enums.InstanceStatusEnum;
import org.example.job.model.JobInstance;
import org.example.job.service.JobInstanceService;
import org.example.plugins.common.collectors.JobPluginCollector;

import java.time.ZoneId;
import java.util.Date;

/**
 * @author zhuxj
 * @since 2021/1/12
 */
public class CustomJobPluginCollector extends JobPluginCollector {

    //任务编号，这里的任务指的是Datax的任务，即业务层的实例，不能和tb_job混淆
    private int jobId;

    private JobInstanceService jobInstanceService;

    public void setJobInstanceService(JobInstanceService jobInstanceService) {
        this.jobInstanceService = jobInstanceService;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    /**
     * 重写阶段性汇报逻辑，再阶段性汇报结束后，更新任务读写数
     */
    @Override
    public void cycleReport() {
        super.cycleReport();
        JobInstance jobInstance = new JobInstance();
        jobInstance.setId(jobId);
        Date date = Date.from(this.startLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
        jobInstance.setStartTime(date);
        jobInstance.setReadCnt(this.readCnt.longValue());
        jobInstance.setWriteCnt(this.writeCnt.longValue());
        this.jobInstanceService.update(jobInstance);
    }

    /**
     * 任务结束后的汇报，再将开始结束时间和任务读写数同步至库
     */
    @Override
    public void finalReport(boolean hasError, boolean warning) {
        super.finalReport(hasError, warning);
        JobInstance jobInstance = new JobInstance();
        jobInstance.setId(jobId);
        Date date = Date.from(this.endLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
        jobInstance.setEndTime(date);
        jobInstance.setReadCnt(this.readCnt.longValue());
        jobInstance.setWriteCnt(this.writeCnt.longValue());

        if (hasError) {
            jobInstance.setStatus(InstanceStatusEnum.ERROR.getValue());
        } else if (warning) {
            jobInstance.setStatus(InstanceStatusEnum.WARNING.getValue());
        } else {
            jobInstance.setStatus(InstanceStatusEnum.SUCCESS.getValue());
        }
        this.jobInstanceService.update(jobInstance);
    }


}
