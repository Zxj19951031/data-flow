package org.example.job.controller;

import org.example.job.commons.SystemResponse;
import org.example.job.service.JobInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
