package org.example.cron.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.example.cron.commons.SystemResponse;
import org.example.cron.model.Cron;
import org.example.cron.service.CronService;
import org.example.cron.vo.CronSaveVO;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 规则控制器
 *
 * @author zhuxj
 * @since 2020/12/16
 */
@RestController
public class CronController {

    @Resource
    private CronService cronService;

    /**
     * 新增调度表达式
     *
     * @return SystemResponse
     */
    @PostMapping(value = "cron")
    public SystemResponse<Integer> save(@Valid @RequestBody CronSaveVO cronSaveVO) {
        Cron cron = new Cron();
        BeanUtils.copyProperties(cronSaveVO, cron);
        int id = this.cronService.save(cron);
        return SystemResponse.success(id);
    }

    /**
     * 查询调度表达式
     *
     * @return SystemResponse
     */
    @GetMapping(value = "cron/{id}")
    public SystemResponse<Cron> getCron(@PathVariable Integer id) {

        Cron cron = this.cronService.getCron(id);
        return SystemResponse.success(cron);
    }

    /**
     * 查询调度表达式列表
     *
     * @param name     规则名称
     * @param pageNum  页码
     * @param pageSize 单页大小
     * @return SystemResponse
     */
    @GetMapping(value = "crons")
    public SystemResponse<PageInfo<Cron>> getCrons(@RequestParam Integer pageNum,
                                                   @RequestParam Integer pageSize,
                                                   @RequestParam(required = false) String name) {
        PageHelper.startPage(pageNum, pageSize);
        List<Cron> crons = this.cronService.getCrons(name);
        return SystemResponse.success(new PageInfo<>(crons));
    }

    /**
     * 更新记录
     *
     * @param id         记录编号
     * @param cronSaveVO CronSaveVO
     * @return SystemResponse
     */
    @PutMapping(value = "cron/{id}")
    public SystemResponse<Integer> update(@PathVariable Integer id, @Valid @RequestBody CronSaveVO cronSaveVO) {
        Cron cron = new Cron();
        BeanUtils.copyProperties(cronSaveVO, cron);
        cron.setId(id);
        int cnt = this.cronService.update(cron);
        return SystemResponse.success(cnt);
    }

    /**
     * 删除记录
     *
     * @param id 记录编号
     * @return SystemResponse
     */
    @DeleteMapping(value = "cron/{id}")
    public SystemResponse<Integer> delete(@PathVariable Integer id) {
        int cnt = this.cronService.delete(id);
        return SystemResponse.success(cnt);
    }

    /**
     * 测试调度表达式
     *
     * @param cron 表达式
     * @param cnt  次数
     * @return 近cnt次调度触发时间
     */
    @GetMapping(value = "cron/test/result")
    public SystemResponse<List<String>> test(@RequestParam String cron, @RequestParam(defaultValue = "10") Integer cnt) {

        List<Date> dates = this.cronService.testCron(cron, cnt);
        List<String> dateStr = dates.stream().map(SimpleDateFormat.getDateTimeInstance()::format).collect(Collectors.toList());
        return SystemResponse.success(dateStr);
    }
}
