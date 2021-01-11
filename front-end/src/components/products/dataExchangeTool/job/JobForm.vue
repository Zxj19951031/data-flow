<template>
  <div style="height: 100%;overflow-y: hidden">
    <el-row style="height: 40px">
      <el-page-header @back="handleBack" :content="this.$route.query.id?'编辑任务':'新增任务'"></el-page-header>
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
        <!--定时器配置-->
        <div @mouseenter="handleCardHover(1)">
          <el-divider content-position="left"><i class="el-icon-date"></i></el-divider>
          <el-form-item label="定时器">
            <el-select v-model="formData.rule_id" placeholder="请选择调度规则" clearable>
              <el-option v-for="(item,index) in ruleSelectData" :key="index" :label="item.name"
                         :value="item.id"></el-option>
            </el-select>
          </el-form-item>
        </div>
        <!--高级参数配置-->
        <div @mouseenter="handleCardHover(2)">
          <el-divider content-position="left"><i class="el-icon-s-operation"></i></el-divider>
          <el-row>
            <el-col :span="8">
              <el-form-item label="并发任务数量（个）">
                <el-input-number v-model="formData.pluginConfig.job.setting.speed.channel"
                                 size="mini"></el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="日志汇报间隔（毫秒）">
                <el-input-number v-model="formData.pluginConfig.job.setting.report.interval"
                                 size="mini"></el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="最大缓冲记录数（条）">
                <el-input-number v-model="formData.pluginConfig.job.tunnel.bufferSize" size="mini"></el-input-number>
              </el-form-item>
            </el-col>
          </el-row>
        </div>
        <!--源端数据源配置-->
        <div @mouseenter="handleCardHover(3)">
          <el-divider content-position="left"><i class="el-icon-s-fold"></i></el-divider>
          <el-form-item label="源端数据源">
            <el-select v-model="formData.source" placeholder="请选择数据源" ref="source" clearable>
              <el-option v-for="(item,index) in datasourceSelectData.list" :key="index" :label="item.name"
                         :value="item.type+'-'+item.id"></el-option>
            </el-select>
          </el-form-item>
          <!--mysql 读取配置-->
          <MySqlReaderForm v-if="formData.source&&formData.source.startsWith('1-')"
                           :id="formData.source.split('-')[1]"
                           :data="this.formData.pluginConfig.job.content.reader.parameter"
                           v-on:columnCallBack="handleSourceCol"
                           ref="ReaderForm"></MySqlReaderForm>
        </div>
        <!--目的数据源配置-->
        <div @mouseenter="handleCardHover(4)">
          <el-divider content-position="left"><i class="el-icon-s-unfold"></i></el-divider>
          <el-form-item label="目的数据源">
            <el-select v-model="formData.target" placeholder="请选择数据源" ref="target" clearable>
              <el-option v-for="(item,index) in datasourceSelectData.list" :key="index" :label="item.name"
                         :value="item.type+'-'+item.id"></el-option>
            </el-select>
          </el-form-item>
          <!--mysql 写入配置  -->
          <MySqlWriterFrom v-if="formData.target&&formData.target.startsWith('1-')"
                           :id="formData.target.split('-')[1]"
                           :data="this.formData.pluginConfig.job.content.writer.parameter"
                           v-on:columnCallBack="handleTargetCol"
                           ref="WriterForm"></MySqlWriterFrom>
        </div>
        <!--列映射配置-->
        <div @mouseenter="handleCardHover(5)">
          <el-divider content-position="left"><i class="el-icon-guide"></i></el-divider>
          <el-row>
            <el-col :span="7" :offset="3">
              <el-table id="source-col-tb" class="col-tb" :data="formData.source_col"
                        :header-cell-style="()=>{return 'text-align: right'}"
                        :cell-style="()=>{return 'text-align: right; cursor: all-scroll;'}">
                <el-table-column label="字段名称[字段类型]">
                  <template slot-scope="scope">
                    {{ scope.row.name }} [{{ scope.row.type }}]
                  </template>
                </el-table-column>
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
            <el-col :span="7">
              <el-table id="target-col-tb" class="col-tb" :data="formData.target_col"
                        :cell-style="()=>{return 'cursor: all-scroll;'}"
                        empty-text="尚未配置目的信息">
                <el-table-column label="字段名称[字段类型]">
                  <template slot-scope="scope">
                    {{ scope.row.name }} [{{ scope.row.type }}]
                  </template>
                </el-table-column>
              </el-table>
            </el-col>
          </el-row>
        </div>
      </el-form>
    </el-row>
    <el-row type="flex" justify="end" style="margin-top: 5px">
      <el-button @click="handleCancelClick" size="small">取消</el-button>
      <el-button type="primary" @click="handleConfirm" size="small">
        {{ this.$route.query.id ? '保存编辑' : '确认新增' }}
      </el-button>
    </el-row>
  </div>
</template>

<script>
import MySqlReaderForm from "@/components/products/dataExchangeTool/job/MySqlReaderForm";
import MySqlWriterFrom from "@/components/products/dataExchangeTool/job/MySqlWriterFrom";
import Sortable from 'sortablejs'

export default {
  name: "JobForm",
  // eslint-disable-next-line vue/no-unused-components
  components: {MySqlWriterFrom, MySqlReaderForm},
  data() {
    return {
      //步骤条数据
      stepData: {
        active: 0,
        list: [{
          title: '基础配置',
          description: '设置任务基本信息',
          icon: 'el-icon-edit-outline'
        }, {
          title: '绑定调度规则',
          description: '选择一个调度规则规定任务周期',
          icon: 'el-icon-date'
        }, {
          title: '高级配置',
          description: '配置高级参数',
          icon: 'el-icon-s-operation'
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
        }]
      },
      //表单数据
      formData: {
        name: null,
        source: "",
        target: "",
        source_col: [],
        target_col: [],
        line: [],
        rule_id: null,
        //这里的结构维护成和后台一样便于解析
        pluginConfig: {
          job: {
            content: {
              reader: {
                name: null,
                parameter: null
              },
              writer: {
                name: null,
                parameter: null
              }
            },
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
        }
      },
      //数据源下拉框内容
      datasourceSelectData: {
        name: null,
        list: [],
        noMore: false
      },
      ruleSelectData: []
    }
  },
  mounted() {
    this.handleDatasourceQuery();
    this.handleRuleQuery();
    this.preHandleEdit();
    this.initSortable();
  },
  methods: {
    //处理返回上一页
    handleBack() {
      this.$confirm('此操作将丢失表单内容，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$router.back();
      })
    },
    //处理鼠标划过卡片事件
    handleCardHover(index) {
      this.stepData.active = index;
    },
    //查询已有的数据源
    handleDatasourceQuery() {
      let params = {
        pageNum: 1,
        pageSize: 0,
        name: this.datasourceSelectData.name,
      }
      this.$http.getDatasources({params}).then(response => {
        this.datasourceSelectData.list = response.data.list
        this.datasourceSelectData.noMore = response && response.data.total === this.datasourceSelectData.list.length;
      })
    },
    //处理源端表的变化，构建字段
    handleSourceCol(col) {
      this.formData.source_col = col;
      let minLineCount = Math.min(
          this.formData.source_col.length,
          this.formData.target_col.length
      );
      this.formData.line = [];
      for (let index = 0; index < minLineCount; index++) {
        this.formData.line.push({
          value: this.formData.source_col[index].selected && this.formData.target_col[index].selected ? 100 : 0,
          index: index
        });
      }
    },
    //处理目的表变化，构建字段
    handleTargetCol(col) {
      this.formData.target_col = col;
      let minLineCount = Math.min(
          this.formData.source_col.length,
          this.formData.target_col.length
      );
      this.formData.line = [];
      for (let index = 0; index < minLineCount; index++) {
        this.formData.line.push({
          value: this.formData.source_col[index].selected && this.formData.target_col[index].selected ? 100 : 0,
          index: index
        });
      }
    },
    //处理连线点击事件
    handleLineClick(row) {
      row.value = Math.abs(row.value - 100)
    },
    //处理取消按扭操作
    handleCancelClick() {
      this.$confirm('此操作将丢失表单内容，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$router.back();
      })
    },
    //处理确认按扭点击事件
    handleConfirm() {

      this.formData.pluginConfig.job.content.reader.name = this.$refs.ReaderForm.name;
      this.formData.pluginConfig.job.content.reader.parameter = this.$refs.ReaderForm.formData;
      this.formData.pluginConfig.job.content.reader.parameter.columns = []
      for (let i = 0; i < this.formData.line.length; i++) {
        if (this.formData.line[i].value === 100) {
          this.formData.pluginConfig.job.content.reader.parameter.columns.push(this.formData.source_col[i].name)
        }
      }
      this.formData.pluginConfig.job.content.writer.name = this.$refs.WriterForm.name;
      this.formData.pluginConfig.job.content.writer.parameter = this.$refs.WriterForm.formData;
      this.formData.pluginConfig.job.content.writer.parameter.columns = []
      for (let i = 0; i < this.formData.line.length; i++) {
        if (this.formData.line[i].value === 100) {
          this.formData.pluginConfig.job.content.writer.parameter.columns.push(this.formData.target_col[i].name)
        }
      }

      let params = {
        name: this.formData.name,
        fromDatasource: this.formData.source.split('-')[1],
        toDatasource: this.formData.target.split('-')[1],
        cron: this.formData.rule_id,
        pluginConfig: JSON.stringify(this.formData.pluginConfig)
      }

      let id = this.$route.query.id;
      if (id) {
        let path = {id: id};
        this.$http.updateJob({params, path}).then(() => {
          this.$message.success('编辑任务成功');
          this.$router.back();
        })
      } else {
        this.$http.saveJob({params}).then(() => {
          this.$message.success('新增任务成功');
          this.$router.back();
        })
      }
    },
    //处理规则查询
    handleRuleQuery() {
      let params = {
        pageNum: 1,
        pageSize: 0
      }
      this.$http.getCrons({params})
          .then(response => {
            if (response.data.list && response.data.list.length > 0) {
              for (let rule of response.data.list) {
                this.ruleSelectData.push({id: rule.id, name: rule.name})
              }
            }
          })
    },
    //预处理编辑，回填数据
    preHandleEdit() {
      if (this.$route.query.id) {
        let path = {id: this.$route.query.id}
        this.$http.getJob({path}).then(resp => {
          this.formData.name = resp.data.name;
          this.formData.rule_id = resp.data.cron;
          this.formData.source = resp.data.fromDatasourceType + '-' + resp.data.fromDatasource
          this.formData.target = resp.data.toDatasourceType + '-' + resp.data.toDatasource
          this.formData.pluginConfig = JSON.parse(resp.data.pluginConfig)
        })
      }
    },
    //初始化拖拽
    initSortable() {
      const _this = this;

      const sourceTbBody = document.querySelector("#source-col-tb tbody");
      Sortable.create(sourceTbBody, {
        animation: 500,
        onEnd({newIndex, oldIndex}) {
          const temp = _this.formData.source_col[newIndex];
          _this.formData.source_col[newIndex] = _this.formData.source_col[oldIndex];
          _this.formData.source_col[oldIndex] = temp;
        },
      });

      const targetTbBody = document.querySelector("#target-col-tb tbody");
      Sortable.create(targetTbBody, {
        animation: 500,
        onEnd({newIndex, oldIndex}) {
          const temp = _this.formData.target_col[newIndex];
          _this.formData.target_col[newIndex] = _this.formData.target_col[oldIndex];
          _this.formData.target_col[oldIndex] = temp;
        },
      });
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
