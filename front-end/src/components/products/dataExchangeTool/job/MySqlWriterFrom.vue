<template>
  <div>
    <el-form-item label="选择库">
      <el-select v-model="formData.schema" placeholder="请选择（Schema）" @change="handleSchemaSelectChange">
        <el-option v-for="(item,index) in schemaSelectData" :key="index" :label="item" :value="item"></el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="选择表">
      <el-select v-model="formData.table" placeholder="请选择（Table）" @change="handleTableSelectChange">
        <el-option v-for="(item,index) in tableSelectData" :key="index" :label="item.split('.')[1]"
                   :value="item"></el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="选择写入方式">
      <el-radio-group v-model="formData.method">
        <el-radio label="insert">INSERT</el-radio>
        <el-radio label="update">UPDATE</el-radio>
        <el-radio label="replace">REPLACE</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="预处理Sql">
      <el-input v-model="formData.preSql" placeholder="请输入Sql"></el-input>
    </el-form-item>
    <el-form-item label="结束处理Sql">
      <el-input v-model="formData.postSql" placeholder="请输入Sql"></el-input>
    </el-form-item>
    <el-form-item label="查询超时时间(秒）">
      <el-input-number v-model="formData.queryTimeout"></el-input-number>
    </el-form-item>
    <el-form-item label="登录超时时间(秒）">
      <el-input-number v-model="formData.loginTimeout"></el-input-number>
    </el-form-item>
    <el-form-item label="最大缓冲记录数(条）">
      <el-input-number v-model="formData.bufferSize"></el-input-number>
    </el-form-item>
  </div>
</template>

<script>
export default {
  name: "MySqlWriterFrom",
  props: {
    id: String
  },
  data() {
    return {
      formData: {
        schema: "",
        table: "",
        method: "insert",
        preSql: "",
        postSql: "",
        queryTimeout: "",
        loginTimeout: "",
        bufferSize: ""
      },
      schemaSelectData: [],
      tableSelectData: []
    }
  },
  mounted() {
    this.handleSchemaQuery()
  },
  methods: {
    //查询Schema列表
    handleSchemaQuery() {
      this.schemaSelectData = ['z-flowable']
    },
    //处理Schema变化
    handleSchemaSelectChange() {
      this.tableSelectData = ['z-flowable.ACT_APP_DATABASECHANGELOG']
    },
    //处理Table变化
    handleTableSelectChange() {
      const col = [{
        name: "id",
        type: "int(11)"
      }, {
        name: "name",
        type: "varchar(255)"
      },]
      this.$emit('columnCallBack', col)
    }
  }
}
</script>

<style scoped>

</style>
