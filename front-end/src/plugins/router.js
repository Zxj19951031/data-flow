import Vue from 'vue'
import VueRouter from 'vue-router'
import DataExchangeTool from "@/components/products/dataExchangeTool/DataExchangeTool";
import DataToolStatistic from "@/components/products/dataExchangeTool/statistic/Statistic";
import DataToolRuleManager from "@/components/products/dataExchangeTool/rule/Rule";
import DataToolDatasource from "@/components/products/dataExchangeTool/datasource/Datasource";
import DataToolJob from "@/components/products/dataExchangeTool/job/Job";
import DataToolJobForm from "@/components/products/dataExchangeTool/job/JobForm";
import Login from "@/components/login/Login";


Vue.use(VueRouter)


export default new VueRouter({
  routes: [
    {
      path: '/login',
      component: Login
    },
    {
      path: "/products/dataExchangeTool/rule",
      component: DataExchangeTool,
      children: [{
        path: "/products/dataExchangeTool/statistic",
        component: DataToolStatistic
      }, {
        path: "/products/dataExchangeTool/rule",
        component: DataToolRuleManager
      }, {
        path: "/products/dataExchangeTool/datasource",
        component: DataToolDatasource
      }, {
        path: "/products/dataExchangeTool/job",
        component: DataToolJob
      }, {
        path: "/products/dataExchangeTool/job/:action",
        component: DataToolJobForm
      }]
    }, {
      path: "/products/flowableEngine",
    }, {
      path: "/console/changeLog",
    }, {
      path: "/aboutMe",
    }, {
      path: "/",
      redirect: "/products/dataExchangeTool/rule",
    },]
})
