package org.example.job.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.example.job.commons.SystemResponse;
import org.example.job.model.JobInstance;
import org.example.job.service.JobInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhuxj
 * @since 2021/1/11
 */
@RestController
public class JobInstanceController {

    @Autowired
    private JobInstanceService jobInstanceService;

    /**
     * 创建任务调度实例，即运行任务
     *
     * @param id 任务编号
     * @return 是否成功创建实例
     */
    @PostMapping(value = "job/{id}/instance")
    public SystemResponse<Integer> run(@PathVariable Integer id) {

        int instanceId = this.jobInstanceService.save(id);
        return SystemResponse.success(instanceId);
    }


    /**
     * 查询某任务的所有调度实例
     *
     * @param id       任务编号
     * @param pageNum  分页参数，页码
     * @param pageSize 分页参数，单页大小
     * @return 实例列表
     */
    @GetMapping(value = "job/{id}/instances")
    public SystemResponse<PageInfo<JobInstance>> getInstances(@PathVariable Integer id,
                                                              @RequestParam Integer pageNum,
                                                              @RequestParam Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<JobInstance> jobs = this.jobInstanceService.findByJobId(id);
        return SystemResponse.success(new PageInfo<>(jobs));
    }
}
