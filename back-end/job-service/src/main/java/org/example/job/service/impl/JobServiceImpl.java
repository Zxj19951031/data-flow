package org.example.job.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.job.dto.JobQueryDTO;
import org.example.job.mapper.JobMapper;
import org.example.job.model.Job;
import org.example.job.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhuxj
 * @since 2020/12/24
 */
@Service
@Slf4j
public class JobServiceImpl implements JobService {
    @Autowired
    private JobMapper jobMapper;

    @Override
    public int save(Job job) {
        int cnt = this.jobMapper.insert(job);
        log.info("新增传输任务记录\n{}", job);
        return job.getId();
    }

    @Override
    public Job findById(Integer id) {
        Job job = this.jobMapper.selectByPrimaryKey(id);
        log.info("查询传输任务记录\nid={},record={}", id, job);
        return job;
    }

    @Override
    public int update(Job job) {
        int cnt = this.jobMapper.updateByPrimaryKeySelective(job);
        log.info("更新传输任务\ncnt={},record={}", cnt, job);
        return cnt;
    }

    @Override
    public int deleteById(Integer id) {
        int cnt = this.jobMapper.deleteByPrimaryKey(id);
        log.info("删除传输任务\nid={},cnt={}", id, cnt);
        return cnt;
    }

    @Override
    public List<Job> listByParams(JobQueryDTO params) {
        List<Job> jobs = this.jobMapper.select(params);
        log.info("查询任务\nparams={},size={}", params, jobs.size());
        return jobs;
    }
}
