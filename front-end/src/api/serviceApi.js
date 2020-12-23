const GETAWAY_API = {
    //新增调度表达式
    saveCron: {
        method: 'POST',
        url: '/ruleApi/cron',
    },
    //获取调度表达式
    getCron: {
        method: 'GET',
        url: '/ruleApi/cron/{{id}}',
    },
    //获取调度表达式(s)
    getCrons: {
        method: 'GET',
        url: '/ruleApi/crons',
    },
    //更新调度表达式
    updateCron: {
        method: 'PUT',
        url: '/ruleApi/cron/{{id}}',
    },
    //删除调度表达式
    deleteCron: {
        method: 'DELETE',
        url: '/ruleApi/cron/{{id}}'
    },
    //获取近十次调度触发时间
    getCronTestResult: {
        method: 'GET',
        url: '/ruleApi/cron/test/result'
    }
};

export default GETAWAY_API;
