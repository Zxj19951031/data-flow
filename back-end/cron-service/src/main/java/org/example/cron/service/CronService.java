package org.example.cron.service;

import org.example.cron.model.Cron;

import java.util.Date;
import java.util.List;

/**
 * @author zhuxj
 * @since 2020/12/16
 */
public interface CronService {

    /**
     * 新增记录
     *
     * @param cron Cron
     * @return id of new record
     */
    int save(Cron cron);

    /**
     * 查询一条记录
     *
     * @param id id
     * @return Cron
     */
    Cron getCron(Integer id);

    /**
     * 条件查询记录
     *
     * @param name 调度名称
     * @return list of cron(s)
     */
    List<Cron> getCrons(String name);

    /**
     * 更新一条记录
     *
     * @param cron Cron
     * @return 受影响的记录数
     */
    int update(Cron cron);

    /**
     * 删除一条记录
     *
     * @param id 记录编号
     * @return 受影响的记录数
     */
    int delete(Integer id);

    /**
     * 测试调度表达式
     *  @param cron 表达式
     * @param cnt  返回近cnt次结果
     * @return
     */
    List<Date> testCron(String cron, Integer cnt);
}
