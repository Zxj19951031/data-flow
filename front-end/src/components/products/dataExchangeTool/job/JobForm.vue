<template>
  <div style="height: 100%;overflow-y: hidden">
    <el-row style="height: 40px">
      <el-page-header @back="handleBack" :content="this.$route.params.id===undefined?'新增任务':'编辑任务'"></el-page-header>
    </el-row>
    <el-row class="steps-row">
      <el-steps :active="stepData.active">
        <template v-for="(step,index) in stepData.list">
          <el-step :title="step.title" :description="step.description" :icon="step.icon" :key="index"></el-step>
        </template>
      </el-steps>
    </el-row>
    <el-row class="form-row">
      <el-form :model="formData" label-width="160px" label-position="left" pre="formData" size="small">
        <!--基础信息-->
        <div @mouseenter="handleCardHover(0)">
          <el-divider content-position="left"><i class="el-icon-edit-outline"></i></el-divider>
          <el-form-item label="任务名称" prop="name">
            <el-input type="text" placeholder="请输入任务名称" v-model="formData.name"></el-input>
          </el-form-item>
        </div>
        <!--源端数据源配置-->
        <div @mouseenter="handleCardHover(1)">
          <el-divider content-position="left"><i class="el-icon-s-fold"></i></el-divider>
          <el-form-item label="源端数据源">
            <el-select v-model="formData.source" placeholder="请选择数据源" @change="handleSourceSelectChange" ref="source">
              <el-option v-for="(item,index) in datasourceSelectData" :key="index" :label="item.name"
                         :value="item.type+'-'+item.id"></el-option>
            </el-select>
          </el-form-item>
          <!--mysql 读取配置-->
          <MySqlReaderForm v-if="formData.source.startsWith('1-')"
                           :id="formData.source.split('-')[1]"
                           v-on:columnCallBack="handleSourceCol"></MySqlReaderForm>
        </div>
        <!--目的数据源配置-->
        <div @mouseenter="handleCardHover(2)">
          <el-divider content-position="left"><i class="el-icon-s-unfold"></i></el-divider>
          <el-form-item label="目的数据源">
            <el-select v-model="formData.target" placeholder="请选择数据源" @change="handleTargetSelectChange" ref="target">
              <el-option v-for="(item,index) in datasourceSelectData" :key="index" :label="item.name"
                         :value="item.type+'-'+item.id"></el-option>
            </el-select>
          </el-form-item>
          <!--mysql 写入配置  -->
          <MySqlWriterFrom v-if="formData.target.startsWith('1-')"
                           :id="formData.target.split('-')[1]"
                           v-on:columnCallBack="handleTargetCol"></MySqlWriterFrom>
        </div>
        <!--列映射配置-->
        <div @mouseenter="handleCardHover(3)">
          <el-divider content-position="left"><i class="el-icon-guide"></i></el-divider>
          <el-row>
            <el-col :span="10">
              <el-table id="source-col-tb" class="col-tb" :data="formData.source_col"
                        :header-cell-style="()=>{return 'text-align: center'}"
                        :cell-style="()=>{return 'text-align: center'}" empty-text="尚未配置源端信息">
                <el-table-column prop="name" label="字段名称"/>
                <el-table-column prop="type" label="字段类型"/>
              </el-table>
            </el-col>
            <el-col :span="4">
              <el-table class="col-tb" :data="formData.line" :header-cell-style="()=>{return 'text-align: center'}"
                        :cell-style="()=>{return 'text-align: center;cursor: pointer;'}" empty-text=" "
                        @cell-click="handleLineClick">
                <el-table-column label="映射">
                  <template slot-scope="scope">
                    <el-progress :percentage="scope.row.value" :show-text="false"></el-progress>
                  </template>
                </el-table-column>
              </el-table>
            </el-col>
            <el-col :span="10">
              <el-table id="target-col-tb" class="col-tb" :data="formData.target_col"
                        :header-cell-style="()=>{return 'text-align: center'}"
                        :cell-style="()=>{return 'text-align: center'}" empty-text="尚未配置目的信息">
                <el-table-column prop="name" label="字段名称"/>
                <el-table-column prop="type" label="字段类型"/>
              </el-table>
            </el-col>
          </el-row>
        </div>
        <!--定时器配置-->
        <div @mouseenter="handleCardHover(4)">
          <el-divider content-position="left"><i class="el-icon-date"></i></el-divider>
          <el-form-item label="定时器">
            <el-select v-model="formData.rule_id" placeholder="请选择调度规则">
              <el-option v-for="(item,index) in ruleSelectData" :key="index" :label="item.name"
                         :value="item.id"></el-option>
            </el-select>
          </el-form-item>
        </div>
        <!--高级参数配置-->
        <div @mouseenter="handleCardHover(5)">
          <el-divider content-position="left"><i class="el-icon-s-operation"></i></el-divider>
          <el-row>
            <el-col :span="8">
              <el-form-item label="并发任务数量（个）">
                <el-input-number v-model="formData.job.setting.speed.channel" size="mini"></el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="日志汇报间隔（毫秒）">
                <el-input-number v-model="formData.job.setting.report.interval" size="mini"></el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="最大缓冲记录数（条）">
                <el-input-number v-model="formData.job.tunnel.bufferSize" size="mini"></el-input-number>
              </el-form-item>
            </el-col>
          </el-row>
        </div>
      </el-form>
    </el-row>
    <el-row type="flex" justify="end">
      <el-button @click="handleCancelClick">取消</el-button>
      <el-button type="primary">
        {{ this.$route.params.id === undefined ? '确认新增' : '保存编辑' }}
      </el-button>
    </el-row>
  </div>
</template>

<script>
import MySqlReaderForm from "@/components/products/dataExchangeTool/job/MySqlReaderForm";
import MySqlWriterFrom from "@/components/products/dataExchangeTool/job/MySqlWriterFrom";

export default {
  name: "JobForm",
  components: {MySqlWriterFrom, MySqlReaderForm},
  data() {
    return {
      stepData: {
        active: 0,
        list: [{
          title: '基础配置',
          description: '设置任务基本信息',
          icon: 'el-icon-edit-outline'
        }, {
          title: '配置源端数据源',
          description: '选择一个数据源进行源端配置',
          icon: 'el-icon-s-fold'
        }, {
          title: '配置目标数据源',
          description: '选择一个数据源进行目标配置',
          icon: 'el-icon-s-unfold'
        }, {
          title: '字段映射',
          description: '选择若干字段进行映射',
          icon: 'el-icon-guide'
        }, {
          title: '绑定调度规则',
          description: '选择一个调度规则规定任务周期',
          icon: 'el-icon-date'
        }, {
          title: '高级配置',
          description: '配置高级参数',
          icon: 'el-icon-s-operation'
        },]
      },
      formData: {
        name: null,
        source: "",
        target: "",
        source_col: [],
        target_col: [],
        line: [],
        rule_id: null,
        job: {
          setting: {
            speed: {
              channel: 1
            },
            report: {
              interval: 10000
            },
          },
          tunnel: {
            bufferSize: 1024
          }
        }
      },
      datasourceSelectData: [],
      ruleSelectData: [{
        id: -1,
        name: '手动触发'
      }]
    }
  },
  mounted() {
    this.handleDatasourceQuery();
  },
  methods: {
    //处理返回上一页
    handleBack() {
      this.$router.back();
    },
    //处理鼠标划过卡片事件
    handleCardHover(index) {
      this.stepData.active = index;
    },
    //查询已有的数据源
    handleDatasourceQuery() {
      this.datasourceSelectData = [{
        id: 1,
        name: '测试数据源',
        type: 1,
        typeStr: 'MySql',
        host: '127.0.0.1',
        createTime: '2020-12-08 00:00:00',
        updateTime: '2020-12-08 00:00:00'
      }]
    },
    //处理源端数据源下拉变化
    handleSourceSelectChange(item) {
      console.log(item);
    },
    //处理目的数据源下拉变化
    handleTargetSelectChange(item) {
      console.log(item);

    },
    //处理源端表的变化，构建字段
    handleSourceCol(col) {
      this.formData.source_col = col;
      let minLineCount = Math.min(
          this.formData.source_col.length,
          this.formData.target_col.length
      );
      for (let index = 0; index < minLineCount; index++) {
        this.formData.line.push({value: 100, index: index});
      }
    },
    //处理目的表变化，构建字段
    handleTargetCol(col) {
      this.formData.target_col = col;
      let minLineCount = Math.min(
          this.formData.source_col.length,
          this.formData.target_col.length
      );
      for (let index = 0; index < minLineCount; index++) {
        this.formData.line.push({value: 100});
      }
    },
    //处理连线点击事件
    handleLineClick(row) {
      row.value = Math.abs(row.value - 100)
    },
    //处理取消按扭操作
    handleCancelClick() {

    }
  }
}
</script>

<style scoped>
.el-page-header {
  margin-bottom: 20px;
}

.steps-row {
  height: 100px;
}

.form-row {
  height: calc(100% - 180px);
  overflow: scroll;
}
</style>
