<!--统计中心-->
<template>
  <div>
    <el-row type="flex" justify="space-around" style="margin-bottom: 20px">
      <el-col :span="23">
        <el-card shadow="never" header="任务分布">
          <el-card class="job-distribution-card" v-for="index of 300" :key="'key'+index" shadow="hover"></el-card>
        </el-card>
      </el-col>
    </el-row>
    <el-row type="flex" justify="space-around">
      <el-col :span="11">
        <el-card shadow="never" :header="'接入数据源总数 :1442'">
          <div id="datasource-cnt-line-chart" ref="chart1"></div>
        </el-card>
      </el-col>
      <el-col :span="11">
        <el-card shadow="never" :header="'任务总数:1442'">
          <div id="job-cnt-line-chart" ref="chart2"></div>
        </el-card>
      </el-col>
    </el-row>

  </div>
</template>

<script>
export default {
  name: "Statistic",
  data() {
    return {
      jobDistribution: []
    }
  },
  mounted() {
    this.datasourceCntLineChart()
    this.jobCntLineChart()
  },
  methods: {
    datasourceCntLineChart() {
      let instance = require('echarts').init(this.$refs.chart1);
      instance.setOption({
        xAxis: {
          type: 'category',
          data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          data: [820, 932, 901, 934, 1290, 1330, 1320],
          type: 'line'
        }, {
          data: [0, 100, 22, 93, 129, 133, 132],
          type: 'line'
        }]
      });
    },
    jobCntLineChart() {
      let instance = require('echarts').init(this.$refs.chart2);
      instance.setOption({
        xAxis: {
          type: 'category',
          data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          data: [820, 932, 901, 934, 1290, 1330, 1320],
          type: 'line'
        }, {
          data: [0, 100, 22, 93, 129, 133, 132],
          type: 'line'
        }]
      });
    }
  }
}
</script>

<style scoped>
#datasource-cnt-line-chart {
  height: 250px;
}

#job-cnt-line-chart {
  height: 250px;
}

.job-distribution-card {
  width: 2.5%;
  height: 20px;
  float: left;
}
</style>
