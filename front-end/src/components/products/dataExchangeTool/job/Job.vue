<template>
  <div>
    <!--    搜索行-->
    <el-row>
      <el-form :inline="true" :model="searchForm" ref="searchForm">
        <el-form-item label="任务名称" prop="name">
          <el-input v-model="searchForm.name" placeholder="请输入要搜索的任务名称" size="small"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="small" @click="handleSearch"></el-button>
        </el-form-item>
        <el-form-item>
          <el-button icon="el-icon-refresh" size="small" @click="handleRefresh"></el-button>
        </el-form-item>
        <el-form-item style="float: right">
          <el-button type="primary" icon="el-icon-plus" size="small" @click="preHandleAddOrEdit">新增</el-button>
        </el-form-item>
      </el-form>
    </el-row>
    <!--记录行-->
    <el-row>
      <el-table :data="tableData" style="width: 100%">
        <el-table-column type="index" label="序号" width="50"/>
        <el-table-column prop="name" label="传输名称" width="350"/>
        <el-table-column prop="statusStr" label="任务状态" width="180"/>
        <el-table-column prop="createTime" label="创建时间"/>
        <el-table-column prop="updateTime" label="修改时间"/>
        <el-table-column label="操作" align="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="preHandleEdit(scope.row)">
              <i class="el-icon-setting"></i>配置
            </el-button>
            <el-button type="text" size="small" @click="preHandleInstanceQuery(scope.row)">
              <i class="el-icon-tickets"></i>查看实例
            </el-button>
            <el-button
                type="text"
                size="small"
                @click="onRegister(scope.row)"
                v-if="scope.row.registered===0"
            >
              <i class="el-icon-video-play"></i>运行
            </el-button>
            <el-button
                type="text"
                size="small"
                @click="onCancel(scope.row)"
                v-if="scope.row.registered===1"
            >
              <i class="el-icon-video-pause"></i>停止
            </el-button>
            <el-button type="text" size="small">
              <i class="el-icon-delete"></i>删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "Job",
  data() {
    return {
      searchForm: {
        name: null,
      },
      tableData: []
    }
  },
  mounted() {
    this.handleTableDataQuery()
  },
  methods: {
    //处理搜索
    handleSearch() {
    },
    //处理搜索重置
    handleRefresh() {
    },
    //预处理编辑按扭
    preHandleEdit(record) {
      console.log(record)
    },
    //查询列表数据
    handleTableDataQuery() {
      this.tableData = [{
        id: 1,
        name: '同步任务1',
        status: 0,
        statusStr: '调度中',
        createTime: '2020-12-08 00:00:00',
        updateTime: '2020-12-08 00:00:00'
      }]
    },
    //预处理实例查询
    preHandleInstanceQuery(record) {
      console.log(record)
    },
    //预处理新增编辑
    preHandleAddOrEdit(record) {
      if (record) {
        this.$router.push({
          path: "/products/dataExchangeTool/job/add"
        })
      } else {
        this.$router.push({
          path: "/products/dataExchangeTool/job/edit",
          query: {id: record.id},
        })
      }
    }
  }
}
</script>

<style scoped>

</style>
