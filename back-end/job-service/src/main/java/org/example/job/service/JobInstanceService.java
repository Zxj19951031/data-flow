package org.example.job.service;

import org.example.job.enums.InstanceStatusEnum;
import org.example.job.model.JobInstance;

/**
 * @author zhuxj
 * @since 2021/1/11
 */
public interface JobInstanceService {
    /**
     * 新增实例
     *
     * @param jobId 任务编号
     * @return 实例编号
     */
    int save(Integer jobId);

    /**
     * 更新实例的开始结束时间和读取写入记录数
     *
     * @param jobInstance 实例
     * @return 更新记录数
     */
    int update(JobInstance jobInstance);

    /**
     * 更新实例状态
     *
     * @param id     实例编号
     * @param status 状态
     * @return 更新记录数
     */
    int updateStatus(Integer id, InstanceStatusEnum status);
}
