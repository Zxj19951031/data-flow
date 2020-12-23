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
          <el-button type="primary" @click="preHandleDrawerOpen" icon="el-icon-plus" size="small">新增</el-button>
        </el-form-item>
      </el-form>
    </el-row>
    <!--记录行-->
    <el-row>
      <el-table :data="tableData" style="width: 100%">
        <el-table-column type="index" label="序号"/>
        <el-table-column prop="name" label="数据源名称" width="300"/>
        <el-table-column prop="typeStr" label="数据源类型">
          <template slot-scope="scope">
            <img class="database-icon" v-if="scope.row.type===1" src="@/assets/mysql.png" height="20"
                 width="20"/>
            <span>{{ scope.row.typeStr }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="host" label="主机地址" width="450"/>
        <el-table-column prop="createTime" label="创建时间" width="165"/>
        <el-table-column prop="updateTime" label="修改时间" width="165"/>
        <el-table-column label="操作" align="right">
          <template slot-scope="scope">
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
                     layout="total, sizes, prev, pager, next, jumper" :total="pageParam.total"></el-pagination>
    </el-row>
    <!--抽屉-->
    <el-drawer :visible.sync="drawerData.visible" :direction="drawerData.direction" :modal="drawerData.model"
               ref="drawer" @closed="handleDrawerClose">
      <el-container style="height: 100%">
        <el-main>
          <el-form :model="formData" label-width="100px" style="margin-right: 20px" ref="drawerForm">
            <el-form-item label="数据源名称" prop="name">
              <el-input placeholder="请输入数据源名称" v-model="formData.name"></el-input>
            </el-form-item>
            <el-form-item label="数据源类型" placeholder="请选择" prop="type">
              <el-select v-model="formData.type">
                <el-option v-for="item in searchData.types" :key="item.value" :label="item.label"
                           :value="item.value"></el-option>
              </el-select>
            </el-form-item>
            <!--mysql 数据源表单-->
            <MySqlForm v-if="formData.type===1" :ref="searchData.types[formData.type-1].label"></MySqlForm>
          </el-form>
        </el-main>
        <el-footer>
          <el-row type="flex" justify="end">
            <el-button @click="handleConnectionCheck">测试连通性</el-button>
            <el-button type="primary" v-if="drawerData.type==='add'">保存新增</el-button>
            <el-button type="primary" v-if="drawerData.type==='edit'">保存编辑</el-button>
          </el-row>
        </el-footer>
      </el-container>
    </el-drawer>
  </div>
</template>
<script>
import MySqlForm from "@/components/products/dataExchangeTool/datasource/MySqlForm";

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
          icon: "../../assets/mysql.png"
        }, {
          label: "Oracle",
          value: 2,
        }, {
          label: "Sql Server",
          value: 3
        }]
      },
      tableData: [],
      pageParam: {
        pageSize: 30,
        pageSizes: [30, 50, 100],
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
      }
    }
  },
  mounted() {
    this.handleTableDataQuery();
  },
  methods: {
    //查询列表数据
    handleTableDataQuery() {
      this.tableData = [{
        id: 1,
        name: '测试数据源',
        type: 1,
        typeStr: 'MySql',
        host: '127.0.0.1',
        createTime: '2020-12-08 00:00:00',
        updateTime: '2020-12-08 00:00:00'
      }]
    },
    //处理列表搜索
    handleSearch() {

    },
    //处理列表刷新
    handleRefresh() {

    },
    //预处理抽屉打开
    preHandleDrawerOpen(record) {
      this.drawerData.visible = true;
      if (record) {
        this.drawerData.type = "add";
      } else {
        this.drawerData.type = "edit";
      }
    },
    //处理删除
    handleDelete(record) {
      console.log(record)
    },
    //处理测试连接
    handleConnectionCheck() {

    },
    //处理抽屉关闭
    handleDrawerClose() {
      //重置data
      this.$refs.drawerForm.resetFields();
    }
  }
}
</script>

<style scoped>

</style>
