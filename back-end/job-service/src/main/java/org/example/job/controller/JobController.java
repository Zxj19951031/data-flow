package org.example.job.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.example.job.commons.SystemResponse;
import org.example.job.dto.JobQueryDTO;
import org.example.job.model.Datasource;
import org.example.job.model.Job;
import org.example.job.service.DatasourceService;
import org.example.job.service.JobService;
import org.example.job.vo.JobQueryVO;
import org.example.job.vo.JobSaveVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author zhuxj
 * @since 2020/12/24
 */
@RestController
public class JobController {

    @Autowired
    private JobService jobService;

    @Autowired
    private DatasourceService datasourceService;

    /**
     * 新增任务
     *
     * @param vo JobSaveVO
     * @return 任务编号
     */
    @PostMapping(value = "job")
    public SystemResponse<Integer> save(@Valid @RequestBody JobSaveVO vo) {
        Job job = vo.toJob();
        int id = this.jobService.save(job);
        return SystemResponse.success(id);
    }

    /**
     * 查询任务
     *
     * @param id 任务编号
     * @return Job
     */
    @GetMapping(value = "job/{id}")
    public SystemResponse<JobQueryVO> getJob(@PathVariable Integer id) {
        Job job = this.jobService.findById(id);

        if (job == null)
            return SystemResponse.success(null);

        Datasource fromDs = this.datasourceService.findById(job.getFromDatasource());
        Datasource toDs = this.datasourceService.findById(job.getToDatasource());

        JobQueryVO vo = new JobQueryVO(job);
        vo.setFromDatasourceType(fromDs.getType());
        vo.setToDatasourceType(toDs.getType());
        return SystemResponse.success(vo);
    }

    /**
     * 更新任务
     *
     * @param id 任务编号
     * @param vo 更新内容
     * @return 更新记录数
     */
    @PutMapping(value = "job/{id}")
    public SystemResponse<Integer> update(@PathVariable Integer id, @Valid @RequestBody JobSaveVO vo) {
        Job job = vo.toJob();
        job.setId(id);
        int cnt = this.jobService.update(job);
        return SystemResponse.success(cnt);
    }


    /**
     * 删除任务
     *
     * @param id 任务编号
     * @return 删除记录数
     */
    @DeleteMapping(value = "job/{id}")
    public SystemResponse<Integer> delete(@PathVariable Integer id) {
        int cnt = this.jobService.deleteById(id);
        return SystemResponse.success(cnt);
    }

    /**
     * 查询任务列表
     *
     * @param pageNum  页码
     * @param pageSize 单页大小
     * @param name     任务名称
     * @return
     */
    @GetMapping(value = "jobs")
    public SystemResponse<PageInfo<Job>> list(@RequestParam Integer pageNum,
                                              @RequestParam Integer pageSize,
                                              @RequestParam(required = false) String name,
                                              @RequestParam(required = false) Integer scheduleStatus) {
        PageHelper.startPage(pageNum, pageSize);
        JobQueryDTO params = new JobQueryDTO(name, scheduleStatus);
        List<Job> jobs = this.jobService.listByParams(params);
        return SystemResponse.success(new PageInfo<>(jobs));
    }
}
