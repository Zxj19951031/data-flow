<template>
  <div>
    <!--搜索行-->
    <el-row>
      <el-form :inline="true" :model="searchData" class="demo-form-inline" ref="searchData">
        <el-form-item label="数据源名称" prop="name">
          <el-input v-model="searchData.name" placeholder="请输入" size="small"></el-input>
        </el-form-item>
        <el-form-item label="数据源类型" prop="type">
          <el-select v-model="searchData.type" placeholder="请选择" size="small">
            <el-option v-for="item in searchData.types" :key="item.value" :label="item.label"
                       :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="small" @click="handleSearch"></el-button>
        </el-form-item>
        <el-form-item>
          <el-button icon="el-icon-refresh" size="small" @click="handleRefresh"></el-button>
        </el-form-item>
        <el-form-item style="float: right">
          <el-button type="primary" @click="preHandleDrawerOpen(null)" icon="el-icon-plus" size="small">新增</el-button>
        </el-form-item>
      </el-form>
    </el-row>
    <!--记录行-->
    <el-row>
      <el-table :data="tableData" style="width: 100%" height="618">
        <el-table-column type="index" label="序号"/>
        <el-table-column prop="name" label="数据源名称" width="300"/>
        <el-table-column prop="typeStr" label="数据源类型" width="90 ">
          <template slot-scope="scope">
            <img class="database-icon" :src="datasourceType[scope.row.type - 1].src" height="20" width="20"/>
            <span>{{ datasourceType[scope.row.type - 1].name }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="host" label="主机地址" width="300"/>
        <el-table-column prop="createTime" label="创建时间" width="165"/>
        <el-table-column prop="updateTime" label="修改时间" width="165"/>
        <el-table-column label="操作" align="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleConnectionCheck(scope.row)"><i class="el-icon-link"></i>连通测试
            </el-button>
            <el-button type="text" size="small" @click="preHandleDrawerOpen(scope.row)"><i class="el-icon-edit"></i>编辑
            </el-button>
            <el-button type="text" size="small" @click="handleDelete(scope.row)"><i class="el-icon-delete"></i>删除
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
    <!--抽屉-->
    <el-drawer :visible.sync="drawerData.visible" :direction="drawerData.direction" :modal="drawerData.model"
               ref="drawer" @closed="handleDrawerClose" @opened="handleDrawerOpened">
      <el-container style="height: 100%">
        <el-main>
          <el-form :model="formData" label-width="100px" style="margin-right: 20px" ref="drawerForm">
            <el-form-item label="数据源名称" prop="name">
              <el-input placeholder="请输入数据源名称" v-model="formData.name"></el-input>
            </el-form-item>
            <el-form-item label="数据源类型" placeholder="请选择" prop="type">
              <el-select v-model="formData.type" :disabled="drawerData.type==='edit'">
                <el-option v-for="item in searchData.types" :key="item.value" :label="item.label"
                           :value="item.value"></el-option>
              </el-select>
            </el-form-item>
            <!--mysql 数据源表单-->
            <MySqlForm v-if="formData.type===1" :ref='searchData.types[formData.type - 1].label'></MySqlForm>
          </el-form>
        </el-main>
        <el-footer>
          <el-row type="flex" justify="end">
            <el-button @click="handleConnectionCheck(null)">测试连通性</el-button>
            <el-button type="primary" v-if="drawerData.type==='add'" @click="handleSave">保存新增</el-button>
            <el-button type="primary" v-if="drawerData.type==='edit'" @click="handleUpdate">保存编辑</el-button>
          </el-row>
        </el-footer>
      </el-container>
    </el-drawer>
  </div>
</template>
<script>
import MySqlForm from "@/components/products/dataExchangeTool/datasource/MySqlForm";
import MysqlPng from "@/assets/mysql.png";

export default {
  name: "Datasource",
  components: {MySqlForm},
  data() {
    return {
      searchData: {
        name: null,
        type: null,
        types: [{
          label: "MySql",
          value: 1,
        }]
      },
      tableData: [],
      pageParam: {
        pageNum: 1,
        pageSize: 10,
        pageSizes: [10, 50, 100],
        total: 0,
      },
      drawerData: {
        visible: false,
        direction: "rtl",
        model: true,
        type: "add",
      },
      formData: {
        name: null,
        type: null,
      },
      datasourceType: [
        {name: 'MySql', src: MysqlPng},
      ]
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
        pageNum: this.pageParam.pageNum,
        pageSize: this.pageParam.pageSize,
        name: this.searchData.name,
        type: this.searchData.type
      }
      this.$http.getDatasources({params}).then(response => {
        this.tableData = response.data.list
        this.pageParam.total = response.data.total
      })
    },
    //处理列表搜索
    handleSearch() {
      this.handleTableDataQuery();
    },
    //处理列表刷新
    handleRefresh() {
      this.$refs.searchData.resetFields();
      this.handleTableDataQuery();
    },
    //预处理抽屉打开
    preHandleDrawerOpen(record) {
      this.drawerData.visible = true;
      if (record) {
        this.handleRecordQuery(record.id)
        this.drawerData.type = "edit";
      } else {
        this.drawerData.type = "add";
      }
    },
    //处理抽屉打开后的动作 由于抽屉内容为懒加载内容所以对于子组件的赋值操作要等到开启结束
    handleDrawerOpened() {
      if (this.formData.type) {
        this.$refs[this.searchData.types[this.formData.type - 1].label].handleFormData(this.formData)
      }
    },
    //处理删除
    handleDelete(record) {
      let path = {id: record.id}
      // eslint-disable-next-line no-unused-vars
      this.$http.deleteDatasource({path}).then(resp => {
        this.$message.success("删除成功")
        this.handleTableDataQuery()
      })
    },
    //处理测试连接
    handleConnectionCheck(row) {
      let params = {};
      if (row) {
        let path = {id: row.id}
        this.$http.getDatasource({path}).then(resp => {
          params = resp.data;
          // eslint-disable-next-line no-unused-vars
          this.$http.testConnection({params}).then(resp => {
            this.$message.success('Successfully')
          })
        })
      } else {
        Object.assign(params, this.formData);
        Object.assign(params, this.$refs[this.searchData.types[this.formData.type - 1].label].formData)
        console.log(params)
        // eslint-disable-next-line no-unused-vars
        this.$http.testConnection({params}).then(resp => {
          this.$message.success('Successfully')
        })
      }


    },
    //处理抽屉关闭
    handleDrawerClose() {
      //重置data
      this.$refs.drawerForm.resetFields();
    },
    //处理保存新增按扭
    handleSave() {
      let params = {};
      Object.assign(params, this.formData);
      Object.assign(params, this.$refs[this.searchData.types[this.formData.type - 1].label].formData)
      // eslint-disable-next-line no-unused-vars
      this.$http.saveDatasource({params}).then(resp => {
        this.$message.success('新增成功')
        this.$refs.drawer.closeDrawer();
        this.handleTableDataQuery()
      })
    },
    //处理保存编辑按扭
    handleUpdate() {
      let params = {};
      Object.assign(params, this.formData);
      Object.assign(params, this.$refs[this.searchData.types[this.formData.type - 1].label].formData)
      let path = {id: this.formData.id}
      // eslint-disable-next-line no-unused-vars
      this.$http.updateDatasource({params, path}).then(resp => {
        this.$message.success('编辑成功')
        this.$refs.drawer.closeDrawer();
        this.handleTableDataQuery()
      })
    },
    //查询记录
    handleRecordQuery(id) {
      let path = {id: id}
      this.$http.getDatasource({path}).then(resp => {
        this.formData = resp.data;
      })
    }
  }
}
</script>

<style scoped>

</style>
