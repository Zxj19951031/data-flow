<template>
  <div>
    <!--搜索行-->
    <el-row>
      <el-form :inline="true" :model="searchForm" ref="formSearch">
        <el-form-item label="规则名称" prop="name">
          <el-input v-model="searchForm.name" placeholder="请输入要搜索的规则名称" size="small">
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="small" @click="handleTableDataQuery"></el-button>
        </el-form-item>
        <el-form-item>
          <el-button icon="el-icon-refresh" size="small" @click="handleRefresh"></el-button>
        </el-form-item>
        <el-form-item style="float: right">
          <el-button type="primary" icon="el-icon-plus" size="small" @click="preHandleDrawerOpen(null)">新增</el-button>
        </el-form-item>
      </el-form>
    </el-row>
    <!--记录行-->
    <el-row>
      <el-table :data="tableData" style="width: 100%">
        <el-table-column type="index" label="序号" width="50"/>
        <el-table-column prop="name" label="规则名称" width="300"/>
        <el-table-column prop="expression" label="规则表达式"/>
        <el-table-column prop="createTime" label="创建时间" width="165"/>
        <el-table-column prop="updateTime" label="修改时间" width="165"/>
        <el-table-column label="操作" align="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="preHandleDrawerOpen(scope.row)">
              <i class="el-icon-edit"></i>编辑
            </el-button>
            <el-button type="text" size="small" @click="handleDelete(scope.row)">
              <i class="el-icon-delete"></i>删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-row>
    <!--分页行-->
    <el-row type="flex" justify="center">
      <el-pagination
          @size-change="handlePageSizeChange"
          @current-change="handlePageNumChange"
          :page-sizes="pageParam.pageSizes"
          :page-size="pageParam.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pageParam.total"
      ></el-pagination>
    </el-row>
    <!--抽屉-->
    <el-drawer :visible.sync="drawerData.visible" :direction="drawerData.direction" :before-close="preHandleDrawerClose"
               @closed="handleDrawerClose" :with-header="false" :modal="drawerData.model" ref="drawer" size="38%">
      <el-container style="height: 100%">
        <el-main>
          <el-form :model="formData" label-width="120px" ref="drawerForm">
            <el-form-item label="规则名称" prop="name">
              <el-input placeholder="请输入规则名称" v-model="formData.name"></el-input>
            </el-form-item>
            <el-divider><i class="el-icon-timer"></i></el-divider>
            <div style="max-height: 600px;overflow: scroll;">
              <!--表达式 秒操作-->
              <el-form-item label="秒" prop="sec">
                <el-select v-model="drawerData.config.sec.type" size="small" style="width:250px"
                           @change="handleExpressionChange">
                  <el-option v-for="(item,index) in drawerData.config.sec.types" :key="index" :label="item.name"
                             :value="index"></el-option>
                </el-select>
                <!--周期从N-M秒执行-->
                <el-row class="expression-row" v-if="drawerData.config.sec.type===1" type="flex">
                  <el-input v-model="drawerData.config.sec.types[1].from" size="small" type="number" :min="0" :max="59"
                            controls-position="right" @change="handleExpressionChange">
                    <template slot="prepend">从</template>
                    <template slot="append">至</template>
                  </el-input>
                  <el-input v-model="drawerData.config.sec.types[1].to" size="small" type="number"
                            :min="drawerData.config.sec.types[1].from" :max="59" controls-position="right"
                            @change="handleExpressionChange">
                    <template slot="append">秒执行</template>
                  </el-input>
                </el-row>
                <!--从N秒开始，每隔M秒执行一次-->
                <el-row class="expression-row" v-if="drawerData.config.sec.type===2" type="flex">
                  <el-input v-model="drawerData.config.sec.types[2].from" size="small" type="number" :min="0" :max="59"
                            controls-position="right"
                            @change="handleExpressionChange">
                    <template slot="prepend">从</template>
                    <template slot="append">开始，间隔</template>
                  </el-input>
                  <el-input v-model="drawerData.config.sec.types[2].gap" size="small" type="number" :min="1" :max="59"
                            controls-position="right"
                            @change="handleExpressionChange">
                    <template slot="append">秒执行</template>
                  </el-input>
                </el-row>
                <!--指定某几秒执行-->
                <el-row v-if="drawerData.config.sec.type===3">
                  <el-checkbox-group v-model="drawerData.config.sec.types[3].checkList"
                                     @change="handleExpressionChange">
                    <el-checkbox v-for="cnt in 60" :key="cnt-1" :label="(cnt-1).toString()" :value="cnt-1"
                                 style="width: 40px;margin-right: 5px;"></el-checkbox>
                  </el-checkbox-group>
                </el-row>
              </el-form-item>
              <!--表达式 分操作-->
              <el-form-item label="分钟" prop="min">
                <el-select v-model="drawerData.config.min.type" size="small" style="width:250px"
                           @change="handleExpressionChange">
                  <el-option v-for="(item,index) in drawerData.config.min.types" :key="index" :label="item.name"
                             :value="index"></el-option>
                </el-select>
                <!--周期从N-M分钟执行-->
                <el-row class="expression-row" v-if="drawerData.config.min.type===1" type="flex">
                  <el-input v-model="drawerData.config.min.types[1].from" type="number" size="small" :min="0"
                            :max="59" controls-position="right" @change="handleExpressionChange">
                    <template slot="prepend">从</template>
                    <template slot="append">至</template>
                  </el-input>
                  <el-input v-model="drawerData.config.min.types[1].to" type="number" size="small" :max="59"
                            :min="drawerData.config.min.types[1].from" controls-position="right"
                            @change="handleExpressionChange">
                    <template slot="append">分执行</template>
                  </el-input>
                </el-row>
                <!--从N分钟开始，间隔M分钟执行一次-->
                <el-row class="expression-row" v-if="drawerData.config.min.type===2" type="flex">
                  <el-input v-model="drawerData.config.min.types[2].from" type="number" size="small" :min="0" :max="59"
                            controls-position="right" @change="handleExpressionChange">
                    <template slot="prepend">从</template>
                    <template slot="append">开始，间隔</template>
                  </el-input>
                  <el-input v-model="drawerData.config.min.types[2].gap" type="number" size="small" :max="59" :min="1"
                            controls-position="right" @change="handleExpressionChange">
                    <template slot="append">分钟执行</template>
                  </el-input>
                </el-row>
                <!--指定某几分钟执行-->
                <el-row v-if="drawerData.config.min.type===3">
                  <el-checkbox-group v-model="drawerData.config.min.types[3].checkList"
                                     @change="handleExpressionChange">
                    <el-checkbox v-for="cnt in 60" :key="cnt-1" :label="(cnt-1).toString()" :value="cnt-1"
                                 style="width: 40px;margin-right: 5px;"></el-checkbox>
                  </el-checkbox-group>
                </el-row>
              </el-form-item>
              <!--表达式 时操作-->
              <el-form-item label="小时" prop="hour">
                <el-select v-model="drawerData.config.hour.type" size="small" style="width:250px"
                           @change="handleExpressionChange">
                  <el-option v-for="(item,index) in drawerData.config.hour.types" :key="index" :label="item.name"
                             :value="index"></el-option>
                </el-select>
                <!--周期从N-M小时执行-->
                <el-row class="expression-row" v-if="drawerData.config.hour.type===1" type="flex">
                  <el-input v-model="drawerData.config.hour.types[1].from" type="number" size="small" :min="0" :max="23"
                            controls-position="right" @change="handleExpressionChange">
                    <template slot="prepend">从</template>
                    <template slot="append">至</template>
                  </el-input>
                  <el-input v-model="drawerData.config.hour.types[1].to" type="number" size="small" :max="23"
                            :min="drawerData.config.hour.types[1].from" controls-position="right"
                            @change="handleExpressionChange">
                    <template slot="append">时执行</template>
                  </el-input>
                </el-row>
                <!--从N时开始，每隔M时执行一次-->
                <el-row class="expression-row" v-if="drawerData.config.hour.type===2" type="flex">
                  <el-input v-model="drawerData.config.hour.types[2].from" type="number" size="small" :max="23"
                            controls-position="right" @change="handleExpressionChange">
                    <template slot="prepend">从</template>
                    <template slot="append">时开始，间隔</template>
                  </el-input>
                  <el-input v-model="drawerData.config.hour.types[2].gap" type="number" size="small" :max="23" :min="1"
                            controls-position="right" @change="handleExpressionChange">
                    <template slot="append">小时执行</template>
                  </el-input>
                </el-row>
                <!--指定某几小时执行-->
                <el-row v-if="drawerData.config.hour.type===3">
                  <el-checkbox-group v-model="drawerData.config.hour.types[3].checkList"
                                     @change="handleExpressionChange">
                    <el-checkbox v-for="cnt in 24" :key="cnt-1" :label="(cnt-1).toString()" :value="cnt-1"
                                 style="width: 40px;margin-right: 5px;"></el-checkbox>
                  </el-checkbox-group>
                </el-row>
              </el-form-item>
              <!--表达式 日操作-->
              <el-form-item label="日" prop="day">
                <el-select v-model="drawerData.config.day.type" size="small" style="width:250px"
                           @change="handleExpressionChange">
                  <el-option v-for="(item,index) in drawerData.config.day.types" :key="index" :label="item.name"
                             :value="index"></el-option>
                </el-select>
                <!--周期从N-M日执行-->
                <el-row class="expression-row" v-if="drawerData.config.day.type===1" type="flex">
                  <el-input v-model="drawerData.config.day.types[1].from" type="number" size="small" :min="1" :max="31"
                            controls-position="right" @change="handleExpressionChange">
                    <template slot="prepend">从</template>
                    <template slot="append">至</template>
                  </el-input>
                  <el-input v-model="drawerData.config.day.types[1].to" type="number" size="small" :max="31"
                            :min="drawerData.config.day.types[1].from" controls-position="right"
                            @change="handleExpressionChange">
                    <template slot="append">日执行</template>
                  </el-input>
                </el-row>
                <!--从N日开始，每隔M日执行一次-->
                <el-row class="expression-row" v-if="drawerData.config.day.type===2" type="flex">
                  <el-input v-model="drawerData.config.day.types[2].from" type="number" size="small" :min="1" :max="31"
                            controls-position="right" @change="handleExpressionChange">
                    <template slot="prepend">从</template>
                    <template slot="append">日开始，间隔</template>
                  </el-input>
                  <el-input v-model="drawerData.config.day.types[2].gap" type="number" size="small" :max="31" :min="1"
                            controls-position="right" @change="handleExpressionChange">
                    <template slot="append">日执行</template>
                  </el-input>
                </el-row>
                <!--指定某几日执行-->
                <el-row v-if="drawerData.config.day.type===4">
                  <el-checkbox-group v-model="drawerData.config.day.types[4].checkList"
                                     @change="handleExpressionChange">
                    <el-checkbox v-for="cnt in 31" :key="cnt" :label="cnt" :value="cnt"
                                 style="width: 40px; margin-right: 5px;"></el-checkbox>
                  </el-checkbox-group>
                </el-row>
              </el-form-item>
              <!--表达式 月操作-->
              <el-form-item label="月" prop="mon">
                <el-select v-model="drawerData.config.mon.type" size="small" style="width:250px"
                           @change="handleExpressionChange">
                  <el-option v-for="(item,index) in drawerData.config.mon.types" :key="index" :label="item.name"
                             :value="index"></el-option>
                </el-select>
                <!--周期从N-M月执行-->
                <el-row class="expression-row" v-if="drawerData.config.mon.type===1" type="flex">
                  <el-input v-model="drawerData.config.mon.types[1].from" type="number" size="small" :min="1" :max="12"
                            controls-position="right" @change="handleExpressionChange">
                    <template slot="prepend">从</template>
                    <template slot="append">至</template>
                  </el-input>
                  <el-input v-model="drawerData.config.mon.types[1].to" type="number" size="small" :max="12"
                            :min="drawerData.config.mon.types[1].from" controls-position="right"
                            @change="handleExpressionChange">
                    <template slot="append">月执行</template>
                  </el-input>
                </el-row>
                <!--从N月开始，每隔M月执行一次-->
                <el-row class="expression-row" v-if="drawerData.config.mon.type===2" type="flex">
                  <el-input v-model="drawerData.config.mon.types[2].from" type="number" size="small" :max="12"
                            controls-position="right" @change="handleExpressionChange">
                    <template slot="prepend">从</template>
                    <template slot="append">月开始，间隔</template>
                  </el-input>
                  <el-input v-model="drawerData.config.mon.types[2].gap" type="number" size="small" :max="12" :min="1"
                            controls-position="right" @change="handleExpressionChange">
                    <template slot="append">月执行</template>
                  </el-input>
                </el-row>
                <!--指定某几月执行-->
                <el-row v-if="drawerData.config.mon.type===3">
                  <el-checkbox-group v-model="drawerData.config.mon.types[3].checkList"
                                     @change="handleExpressionChange">
                    <el-checkbox v-for="cnt in 12" :key="cnt" :label="cnt" :value="cnt"
                                 style="width: 40px;margin-right: 5px;"></el-checkbox>
                  </el-checkbox-group>
                </el-row>
              </el-form-item>
              <!--表达式 周操作-->
              <el-form-item label="周" prop="week">
                <el-select v-model="drawerData.config.week.type" size="small" style="width:250px"
                           @change="handleExpressionChange">
                  <el-option v-for="(item,index) in drawerData.config.week.types" :key="index" :label="item.name"
                             :value="index"></el-option>
                </el-select>
                <!--从周N至周M执行-->
                <el-row v-if="drawerData.config.week.type===1" type="flex">
                  <div class="week-from-to-input">
                    <div class="el-input-group__prepend" style="border-right: 0">从周</div>
                    <el-select class="from-select" v-model="drawerData.config.week.types[1].from" size="small"
                               @change="handleExpressionChange">
                      <el-option v-for="item in drawerData.config.week.weeks" :key="item.value" :label="item.name"
                                 :value="item.value"></el-option>
                    </el-select>
                    <div class="el-input-group__append"
                         style="border-right: 0;border-bottom-right-radius: 0; border-top-right-radius: 0">至周
                    </div>
                    <el-select class="to-select" v-model="drawerData.config.week.types[1].to" size="small"
                               @change="handleExpressionChange">
                      <el-option v-for="item in drawerData.config.week.weeks" :key="item.value" :label="item.name"
                                 :value="item.value"></el-option>
                    </el-select>
                    <div class="el-input-group__append">执行</div>
                  </div>
                </el-row>
                <!--从本月的最后一个周N执行-->
                <el-row v-if="drawerData.config.week.type===2" type="flex">
                  <div class="week-last-of-month-input">
                    <div class="el-input-group__prepend" style="border-right: 0">本月最后一个周</div>
                    <el-select v-model="drawerData.config.week.types[2].last" size="small"
                               @change="handleExpressionChange">
                      <el-option v-for="item in drawerData.config.week.weeks" :key="item.value" :label="item.name"
                                 :value="item.value"></el-option>
                    </el-select>
                    <div class="el-input-group__append">执行</div>
                  </div>
                </el-row>
                <!--指定几周执行-->
                <el-row v-if="drawerData.config.week.type===3">
                  <el-checkbox-group v-model="drawerData.config.week.types[3].checkList"
                                     @change="handleExpressionChange">
                    <el-checkbox v-for="cnt in drawerData.config.week.weeks" :key="cnt.value" :label="cnt.name"
                                 :value="cnt.value" style="width: 40px;margin-right: 5px;"></el-checkbox>
                  </el-checkbox-group>
                </el-row>
              </el-form-item>
              <!--表达式 年操作-->
              <el-form-item label="年" prop="year">
                <el-select v-model="drawerData.config.year.type" size="small" style="width:250px"
                           @change="handleExpressionChange">
                  <el-option v-for="(item,index) in drawerData.config.year.types" :key="index" :label="item.name"
                             :value="index"></el-option>
                </el-select>
                <el-row class="expression" v-if="drawerData.config.year.type===1" type="flex">
                  <el-input v-model="drawerData.config.year.types[1].from" type="number" size="small"
                            controls-position="right"
                            @change="handleExpressionChange">
                    <template slot="prepend">从</template>
                    <template slot="append">年至</template>
                  </el-input>
                  <el-input v-model="drawerData.config.year.types[1].to" type="number" size="small"
                            controls-position="right"
                            @change="handleExpressionChange">
                    <template slot="append">年执行</template>
                  </el-input>
                </el-row>
              </el-form-item>
            </div>
            <el-divider><i class="el-icon-timer"></i></el-divider>
            <el-form-item label="规则表达式" prop="expression">
              <el-input v-model="formData.expression" :readonly="true">
                <el-button slot="append" @click="handleTestButtonClick">运行</el-button>
              </el-input>
            </el-form-item>
            <el-form-item label="近十次运行时间">
              <el-input
                  type="textarea"
                  :rows="2"
                  :autosize="{ minRows: 4, maxRows: 4}"
                  v-model="drawerData.times"
                  resize="none"
                  :readonly="true"
              ></el-input>
            </el-form-item>
          </el-form>
        </el-main>
        <el-footer>
          <el-row type="flex" justify="end">
            <el-button type="primary" v-if="drawerData.type==='add'" @click="handleRecordAdd">保存新增</el-button>
            <el-button type="primary" v-if="drawerData.type==='edit'" @click="handleRecordEdit">保存编辑</el-button>
          </el-row>
        </el-footer>
      </el-container>
    </el-drawer>
  </div>
</template>

<script>
export default {
  name: "DataToolRuleManager",
  data() {
    return {
      searchForm: {
        name: null,
      },
      tableData: [],
      pageParam: {
        pageNum: 1,
        pageSize: 30,
        pageSizes: [30, 50, 100],
        total: 0,
      },
      drawerData: {
        visible: false,
        direction: "rtl",
        model: true,
        type: "add",
        times: null,
        config: {
          sec: {
            type: 0,
            types: [
              {name: "每秒执行", express: "*"},
              {name: "周期从N-M秒执行", from: 0, to: 59},
              {name: "间隔从N秒开始，每M秒执行一次", from: 0, gap: 1},
              {name: "指定某几秒执行", checkList: ["0"]}
            ],
          },
          min: {
            type: 0,
            types: [
              {name: "每分钟执行", express: "*"},
              {name: "周期从N-M分钟执行", from: 0, to: 59},
              {name: "间隔从N分钟开始，每M分钟执行一次", from: 0, gap: 1},
              {name: "指定某几分钟执行", checkList: ["0"]},
            ],
          },
          hour: {
            type: 0,
            types: [
              {name: "每小时执行", express: "*"},
              {name: "周期从N-M小时执行", from: 0, to: 23},
              {name: "间隔从N时开始，每M小时执行一次", from: 0, gap: 1},
              {name: "指定某几小时执行", checkList: ["0"]},
            ],
          },
          day: {
            type: 0,
            types: [
              {name: "每日执行", express: "*"},
              {name: "周期从N-M日执行", from: 1, to: 31},
              {name: "间隔从N日开始，每M日执行一次", from: 1, gap: 1},
              {name: "本月最后一天", express: "L"},
              {name: "指定某几日执行", checkList: [1]},
              {name: "不指定", express: "?"},
            ],
          },
          mon: {
            type: 0,
            types: [
              {name: "每月执行", express: "*"},
              {name: "周期从N-M月执行", from: 1, to: 12},
              {name: "间隔从N月开始，每M个月执行一次", from: 1, gap: 1},
              {name: "指定某几月执行", checkList: [1]},
            ],
          },
          week: {
            type: 0,
            weeks: [
              {name: "日", value: "1",},
              {name: "一", value: "2",},
              {name: "二", value: "3",},
              {name: "三", value: "4",},
              {name: "四", value: "5",},
              {name: "五", value: "6",},
              {name: "六", value: "7",},
            ],
            types: [
              {name: "不指定", express: '?'},
              {name: "周期从周N-周M执行", from: "1", to: "2"},
              {name: "本月最后一个周N", last: "1"},
              {name: "指定执行", checkList: ["1"]},
            ],
          },
          year: {
            type: 0,
            types: [
              {name: "每年", express: '*'},
              {name: "周期从N-M年执行", from: "2000", to: "2020",},
            ],
          },
        },
      },
      formData: {
        id: "",
        name: "",
        expression: "* * * * * ? *"
      },
    }
  },
  mounted() {
    this.handleTableDataQuery();
  },
  methods: {
    //处理单页大小变动
    handlePageSizeChange(pageSize) {
      this.pageParam.pageSize = pageSize;
      this.handleTableDataQuery()
    },
    //处理页码变动
    handlePageNumChange(pageNum) {
      this.pageParam.pageNum = pageNum;
      this.handleTableDataQuery()
    },
    //查询列表数据
    handleTableDataQuery() {
      let params = {
        name: this.searchForm.name,
        pageNum: this.pageParam.pageNum,
        pageSize: this.pageParam.pageSize
      }
      this.$http.getCrons({params})
          .then(response => {
            this.tableData = response.data.list
            this.pageParam.total = response.data.total
          })
    },
    //查询某条记录
    handleRecordQuery(id) {
      let path = {id: id};
      this.$http.getCron({path}).then(response => {
        if (response && response.data) {
          this.formData.id = response.data.id;
          this.formData.name = response.data.name;
          this.formData.expression = response.data.expression;
          this.drawerData.config = JSON.parse(response.data.metadata);
        }
      })
    },
    //处理删除操作
    handleDelete(row) {
      let path = {id: row.id}
      // eslint-disable-next-line no-unused-vars
      this.$http.deleteCron({path}).then(response => {
        this.$message.success('删除成功')
        this.handleTableDataQuery();
      })
    },
    //处理新增
    handleRecordAdd() {
      let params = {
        name: this.formData.name,
        expression: this.formData.expression,
        metadata: JSON.stringify(this.drawerData.config)
      }
      // eslint-disable-next-line no-unused-vars
      this.$http.saveCron({params}).then(response => {
        this.$message({message: '新增成功', type: 'success'});
        this.$refs.drawer.closeDrawer();
      });
    },
    //处理编辑
    handleRecordEdit() {
      let params = {
        name: this.formData.name,
        expression: this.formData.expression,
        metadata: JSON.stringify(this.drawerData.config)
      }
      let path = {id: this.formData.id}
      // eslint-disable-next-line no-unused-vars
      this.$http.updateCron({params, path}).then(response => {
        this.$message({message: '编辑成功', type: 'success'});
        this.$refs.drawer.closeDrawer();
      });

    },
    //处理表达式变更
    handleExpressionChange() {
      let expression = [];

      //秒表达式
      let secExpress = '';
      if (this.drawerData.config.sec.type === 0) {
        secExpress = this.drawerData.config.sec.types[0].express;
      } else if (this.drawerData.config.sec.type === 1) {
        secExpress = this.drawerData.config.sec.types[1].from + "-" + this.drawerData.config.sec.types[1].to;
      } else if (this.drawerData.config.sec.type === 2) {
        secExpress = this.drawerData.config.sec.types[2].from + "/" + this.drawerData.config.sec.types[2].gap;
      } else {
        secExpress = this.drawerData.config.sec.types[3].checkList.join(",");
      }
      expression.push(secExpress);

      //分表达式
      let minExpress = '';
      if (this.drawerData.config.min.type === 0) {
        minExpress = this.drawerData.config.min.types[0].express;
      } else if (this.drawerData.config.min.type === 1) {
        minExpress = this.drawerData.config.min.types[1].from + "-" + this.drawerData.config.min.types[1].to;
      } else if (this.drawerData.config.min.type === 2) {
        minExpress = this.drawerData.config.min.types[2].from + "/" + this.drawerData.config.min.types[2].gap;
      } else {
        minExpress = this.drawerData.config.min.types[3].checkList.join(",");
      }
      expression.push(minExpress);


      //时表达式
      let hourExpress = '';
      if (this.drawerData.config.hour.type === 0) {
        hourExpress = this.drawerData.config.hour.types[0].express
      } else if (this.drawerData.config.hour.type === 1) {
        hourExpress = this.drawerData.config.hour.types[1].from + "-" + this.drawerData.config.hour.types[1].to;
      } else if (this.drawerData.config.hour.type === 2) {
        hourExpress = this.drawerData.config.hour.types[2].from + "/" + this.drawerData.config.hour.types[2].gap;
      } else {
        hourExpress = this.drawerData.config.hour.types[3].checkList.join(",");
      }
      expression.push(hourExpress);

      //日表达式
      let dayExpress = '';
      if (this.drawerData.config.day.type === 1) {
        dayExpress = this.drawerData.config.day.types[1].from + "-" + this.drawerData.config.day.types[1].to;
      } else if (this.drawerData.config.day.type === 2) {
        dayExpress = this.drawerData.config.day.types[2].from + "/" + this.drawerData.config.day.types[2].gap;
      } else if (this.drawerData.config.day.type === 4) {
        dayExpress = this.drawerData.config.day.types[4].checkList.join(",");
      } else {
        dayExpress = this.drawerData.config.day.types[this.drawerData.config.day.type].express;
      }
      expression.push(dayExpress);

      //月表达式
      let monExpress = '';
      if (this.drawerData.config.mon.type === 0) {
        monExpress = this.drawerData.config.mon.types[0].express;
      } else if (this.drawerData.config.mon.type === 1) {
        monExpress = this.drawerData.config.mon.types[1].from + "-" + this.drawerData.config.mon.types[1].to;
      } else if (this.drawerData.config.mon.type === 2) {
        monExpress = this.drawerData.config.mon.types[2].from + "/" + this.drawerData.config.mon.types[2].gap;
      } else {
        monExpress = this.drawerData.config.mon.types[3].checkList.join(",");
      }
      expression.push(monExpress);


      let weekExpress = '';
      if (this.drawerData.config.week.type === 0) {
        weekExpress = this.drawerData.config.week.types[0].express;
      } else if (this.drawerData.config.week.type === 1) {
        weekExpress = this.drawerData.config.week.types[1].from + "-" + this.drawerData.config.week.types[1].to;
      } else if (this.drawerData.config.week.type === 2) {
        weekExpress = this.drawerData.config.week.types[2].last + "L";
      } else {
        weekExpress = this.drawerData.config.week.types[3].checkList.join(",");
      }
      expression.push(weekExpress);


      if (this.drawerData.config.year.type === 1) this.drawerData.config.year.value = "*";
      else
        this.drawerData.config.year.value =
            this.drawerData.config.year.from + "-" + this.drawerData.config.year.to;

      this.formData.expression = expression.join(" ");
    },
    //抽屉打开前
    preHandleDrawerOpen(row) {
      this.drawerData.visible = true;
      if (row) {
        this.handleRecordQuery(row.id)
        this.drawerData.type = "edit";
      } else {
        this.drawerData.type = "add";
      }
    },
    //抽屉关闭前
    preHandleDrawerClose(done) {
      //关闭窗口
      done();
    },
    //处理抽屉关闭事件
    handleDrawerClose() {
      //重置data
      this.$refs.drawerForm.resetFields();
      Object.assign(this.drawerData, this.$options.data().drawerData);
      //刷新列表
      this.handleTableDataQuery();
    },
    //处理点击运行按扭显示进10次调度时间
    handleTestButtonClick() {
      let params = {
        cron: this.formData.expression,
      }
      this.$http.getCronTestResult({params}).then(response => {
        this.drawerData.times = response.data.join("\n")
      })
    },
    //处理重置搜索
    handleRefresh() {
      this.$refs.formSearch.resetFields();
      this.handleTableDataQuery()
    },
  }
}
</script>

<style scoped>
.week-from-to-input {
  width: 100%;
  line-height: normal;
  display: inline-table;
  font-size: 13px;
}

.week-last-of-month-input {
  line-height: normal;
  display: inline-table;
  font-size: 13px;
}

</style>
