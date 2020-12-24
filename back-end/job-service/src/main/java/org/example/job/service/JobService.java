package org.example.job.service;

import org.example.job.dto.JobQueryDTO;
import org.example.job.model.Job;

import java.util.List;

/**
 * @author zhuxj
 * @since 2020/12/24
 */
public interface JobService {

    /**
     * 新增任务
     *
     * @param job job
     * @return job id
     */
    int save(Job job);


    /**
     * 通过ID查询任务
     *
     * @param id job id
     * @return Job
     */
    Job findById(Integer id);

    /**
     * 更新任务
     *
     * @param job 任务详情
     * @return 更新记录数
     */
    int update(Job job);

    /**
     * 删除任务
     *
     * @param id 任务编号
     * @return 删除记录数
     */
    int deleteById(Integer id);

    /**
     * 查询列表
     *
     * @param params 查询参数
     * @return list of jobs
     * @see JobQueryDTO
     */
    List<Job> listByParams(JobQueryDTO params);
}
