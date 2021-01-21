<template>
  <div>
    <!--搜索行-->
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
        <el-table-column prop="status" label="任务状态" width="180">
          <template slot-scope="scope">
            <span>{{ scheduleStatus[scope.row.scheduleStatus] }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间"/>
        <el-table-column prop="updateTime" label="修改时间"/>
        <el-table-column label="操作" align="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="preHandleAddOrEdit(scope.row)">
              <i class="el-icon-setting"></i>配置
            </el-button>
            <el-button type="text" size="small" @click="preHandleInstanceQuery(scope.row)">
              <i class="el-icon-tickets"></i>查看实例
            </el-button>
            <el-button type="text" size="small" @click="handleRegister(scope.row)" v-if="scope.row.scheduleStatus===0">
              <i class="el-icon-video-play"></i>运行
            </el-button>
            <el-button type="text" size="small" @click="handleCancel(scope.row)" v-if="scope.row.scheduleStatus===1">
              <i class="el-icon-video-pause"></i>停止
            </el-button>
            <el-button type="text" size="small" @click="handleDelete(scope.row)">
              <i class="el-icon-delete"></i>删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-row>
    <!--分页-->
    <el-row type="flex" justify="center">
      <el-pagination :page-sizes="pageParam.pageSizes" :page-size="pageParam.pageSize"
                     @size-change="handlePageSizeChange" @current-change="handlePageNumChange"
                     layout="total, sizes, prev, pager, next, jumper" :total="pageParam.total"></el-pagination>
    </el-row>


    <el-dialog title="任务实例" :visible.sync="instancesDialogVisible">

      <el-row>
        <el-table :data="instancesTableData">
          <el-table-column type="index" label="序号" width="50"/>
          <el-table-column property="startTime" label="开始时间" width="200"></el-table-column>
          <el-table-column property="endTime" label="结束时间" width="200"></el-table-column>
          <el-table-column property="readCnt" label="读取记录数"></el-table-column>
          <el-table-column property="writeCnt" label="写入记录数"></el-table-column>
          <el-table-column property="status" label="实例状态">
            <template slot-scope="scope">
              <div v-html="instancesStatus[scope.row.status]"></div>
            </template>
          </el-table-column>
        </el-table>
      </el-row>

      <el-row type="flex" justify="center">
        <el-pagination :page-sizes="instancesPageParam.pageSizes" :page-size="instancesPageParam.pageSize"
                       @size-change="handleInstancesPageSizeChange" @current-change="handleInstancesPageNumChange"
                       layout="total, sizes, prev, pager, next, jumper"
                       :total="instancesPageParam.total"></el-pagination>
      </el-row>
    </el-dialog>
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
      tableData: [],
      pageParam: {
        pageNum: 1,
        pageSize: 30,
        pageSizes: [30, 50, 100],
        total: 0,
      },
      scheduleStatus: ['未调度', '调度中'],
      instancesDialogVisible: false,
      instancesJobId: null,
      instancesTableData: [],
      instancesPageParam: {
        pageNum: 1,
        pageSize: 5,
        pageSizes: [5, 50, 100],
        total: 0,
      },
      instancesStatus: {
        0: '<span style="color: lightgreen">Running</span>',
        1: '<span style="color: green">Success</span>',
        2: '<span style="color: yellow">Warning</span>',
        3: '<span style="color: orangered">Error</span>'
      }
    }
  },
  mounted() {
    this.handleTableDataQuery()
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
    //处理实例单页大小变动
    handleInstancesPageSizeChange(pageSize) {
      this.instancesPageParam.pageSize = pageSize;
      this.handleInstanceTableDataQuery(this.instancesJobId)
    },
    //处理实例页码变动
    handleInstancesPageNumChange(pageNum) {
      this.instancesPageParam.pageNum = pageNum;
      this.handleInstanceTableDataQuery(this.instancesJobId)
    },
    //处理搜索
    handleSearch() {
      this.handleTableDataQuery()
    },
    //处理搜索重置
    handleRefresh() {
      this.$refs.searchForm.resetFields();
      this.handleTableDataQuery();
    },
    //查询列表数据
    handleTableDataQuery() {
      let params = {
        pageNum: this.pageParam.pageNum,
        pageSize: this.pageParam.pageSize,
        name: this.searchForm.name,
      }
      this.$http.getJobs({params}).then(resp => {
        this.tableData = resp.data.list
        this.pageParam.total = resp.data.total

      })
    },
    //查询实例
    handleInstanceTableDataQuery(id) {
      let path = {id: id}
      let params = {
        pageNum: this.instancesPageParam.pageNum,
        pageSize: this.instancesPageParam.pageSize,
      }
      this.$http.getJobInstances({path, params}).then(resp => {
        this.instancesTableData = resp.data.list
        this.instancesPageParam.total = resp.data.total
      })
    },
    //预处理实例查询
    preHandleInstanceQuery(record) {
      this.instancesDialogVisible = true
      this.instancesJobId = record.id;
      this.handleInstanceTableDataQuery(record.id)
    },
    //预处理新增编辑
    preHandleAddOrEdit(record) {
      if (record) {
        this.$router.push({
          path: "/products/dataExchangeTool/job/add",
          query: {id: record.id},
        })
      } else {
        this.$router.push({
          path: "/products/dataExchangeTool/job/edit",
        })
      }
    },
    handleDelete(record) {
      let path = {id: record.id}
      this.$http.deleteJob({path}).then(() => {
        this.$message.success('删除成功')
        this.handleTableDataQuery()
      })
    },
    handleRegister(record) {
      let path = {id: record.id}
      this.$http.registerJob({path}).then(resp => {

        this.$message.success('注册任务成功，将于' + resp.data + '启动')
        this.handleTableDataQuery()
      })
    },
    handleCancel(record) {
      let path = {id: record.id}
      this.$http.cancelJob({path}).then(() => {
        this.$message.success('注销任务成功')
        this.handleTableDataQuery()
      })
    }
  }
}
</script>

<style scoped>

</style>
