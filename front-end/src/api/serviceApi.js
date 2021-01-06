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
    },
    //新增数据源
    saveDatasource: {
        method: 'POST',
        url: '/jobApi/datasource'
    },
    //查询数据源
    getDatasource: {
        method: 'GET',
        url: '/jobApi/datasource/{{id}}'
    },
    //删除数据源
    deleteDatasource: {
        method: 'DELETE',
        url: '/jobApi/datasource/{{id}}'
    },
    //更新数据源
    updateDatasource: {
        method: 'PUT',
        url: '/jobApi/datasource/{{id}}'
    },
    //获取数据源列表
    getDatasources: {
        method: 'GET',
        url: '/jobApi/datasources'
    },
    //测试数据源
    testConnection: {
        method: 'POST',
        url: '/jobApi/datasource/connectivity'
    },
    //查询数据源模式列表
    getDatasourceSchemas: {
        method: 'GET',
        url: '/jobApi/datasource/{{id}}/schemas'
    },
    //查询数据源表信息
    getDatasourceTables: {
        method: 'GET',
        url: '/jobApi/datasource/{{id}}/tables'
    },
    //查询数据源字段列表
    getDatasourceColumns: {
        method: 'GET',
        url: '/jobApi/datasource/{{id}}/columns'
    },
    //新增任务
    saveJob: {
        method: 'POST',
        url: '/jobApi/job'
    },
    //获取任务列表
    getJobs: {
        method: 'GET',
        url: '/jobApi/jobs'
    },
    //获取单个任务
    getJob: {
        method: 'GET',
        url: '/jobApi/job/{{id}}'
    },
    //更新任务
    updateJob: {
        method: 'PUT',
        url: '/jobApi/job/{{id}}'
    },
    // 删除任务
    deleteJob: {
        method: 'DELETE',
        url: '/jobApi/job/{{id}}'
    }
};

export default GETAWAY_API;
