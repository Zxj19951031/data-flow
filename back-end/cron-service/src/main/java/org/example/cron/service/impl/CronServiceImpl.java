package org.example.cron.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.cron.exceptions.QuartzError;
import org.example.cron.exceptions.SystemException;
import org.example.cron.mapper.CronMapper;
import org.example.cron.model.Cron;
import org.example.cron.service.CronService;
import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author zhuxj
 * @since 2020/12/16
 */
@Slf4j
@Service
public class CronServiceImpl implements CronService {
    @Resource
    private CronMapper cronMapper;

    @Override
    public int save(Cron cron) {
        this.cronMapper.insert(cron);
        log.info("新增调度规则记录\n{}", cron);
        return cron.getId();
    }

    @Override
    public Cron getCron(Integer id) {
        return this.cronMapper.selectOne(id);
    }

    @Override
    public List<Cron> getCrons(String name) {
        return this.cronMapper.selectList(name);
    }

    @Override
    public int update(Cron cron) {
        int cnt = this.cronMapper.updateByPrimaryKeySelective(cron);
        log.info("更新调度规则记录\n条数={},{}", cnt, cron);
        return cnt;
    }

    @Override
    public int delete(Integer id) {
        int cnt = this.cronMapper.deleteByPrimaryKey(id);
        log.info("删除调度规则记录\n条数={},id={}", cnt, id);
        return cnt;
    }

    @Override
    public List<Date> testCron(String cron, Integer cnt) {
        if (!CronExpression.isValidExpression(cron)) {
            log.error("不符合规则的调度表达式\n{}", cron);
            throw SystemException.newException(QuartzError.CRON_EXPRESSION_ERROR, "不符合规则的调度表达式");
        }
        if (cnt == null || cnt <= 0) {
            cnt = 10;
        }

        Trigger trigger = TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();

        List<Date> results = new ArrayList<>();
        Date offset = Calendar.getInstance().getTime();
        for (int i = 0; i < cnt; i++) {
            results.add(trigger.getFireTimeAfter(offset));
            offset = results.get(i);
        }
        return results;
    }
}
