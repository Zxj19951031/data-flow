<template>
  <div>
    <el-form-item label="选择库">
      <el-select v-model="formData.schema" placeholder="请选择（Schema）" @change="handleSchemaSelectChange">
        <el-option v-for="(item,index) in schemaSelectData" :key="index" :label="item" :value="item"></el-option>
      </el-select>
    </el-form-item>
    <el-form-item>
      <el-switch v-model="formData.ifQuery" active-color="#13ce66" inactive-color="#13ce66" active-text="自定义"
                 inactive-text="选择表"></el-switch>
    </el-form-item>
    <div v-if="!formData.ifQuery">
      <el-form-item label="选择表">
        <el-select v-model="formData.table" placeholder="请选择（Table）" @change="handleTableSelectChange">
          <el-option v-for="(item,index) in tableSelectData" :key="index" :label="item.split('.')[1]"
                     :value="item"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="查询条件">
        <el-input v-model="formData.where" placeholder="请输入Where内容，例如 id = 1"></el-input>
      </el-form-item>
    </div>
    <div v-if="formData.ifQuery">
      <el-form-item label="查询设置">
        <el-input v-model="formData.querySql" placeholder="请输入Select内容，例如 SELECT * FROM table WHERE id = 1"></el-input>
      </el-form-item>
    </div>
    <el-form-item label="预处理Sql">
      <el-input v-model="formData.preSql" placeholder="请输入预处理Sql，此脚本将在执行查询前操作"></el-input>
    </el-form-item>
    <el-form-item label="结束处理Sql">
      <el-input v-model="formData.postSql" placeholder="请输入结束处理Sql，此脚本将在执行查询结束后操作"></el-input>
    </el-form-item>
    <el-form-item label="切分主键">
      <el-input v-model="formData.splitKey" placeholder="请输入切分主键字段，目前只支持整型数据类型的字段"></el-input>
    </el-form-item>
    <el-form-item label="查询超时时间(秒）">
      <el-input-number v-model="formData.queryTimeout" size="mini"></el-input-number>
    </el-form-item>
    <el-form-item label="登录超时时间(秒）">
      <el-input-number v-model="formData.loginTimeout" size="mini"></el-input-number>
    </el-form-item>
  </div>
</template>

<script>
export default {
  name: "MySqlReaderForm",
  props: {
    id: String
  },
  data() {
    return {
      formData: {
        schema: "",
        ifQuery: false,
        table: "",
        where: "",
        querySql: "",
        preSql: "",
        postSql: "",
        splitKey: "",
        queryTimeout: "",
        loginTimeout: ""
      },
      schemaSelectData: [],
      tableSelectData: []
    }
  },
  mounted() {
    this.handleSchemaQuery();
  },
  methods: {
    //查询Schema列表
    handleSchemaQuery() {
      this.schemaSelectData = ['z-flowable']
    },
    //处理Schema变化
    handleSchemaSelectChange() {
      this.tableSelectData = ['z-flowable.ACT_APP_APPDEF']
    },
    //处理Table变化
    handleTableSelectChange() {
      const col = [{
        name:"id",
        type:"int(11)"
      },{
        name:"name",
        type:"varchar(255)"
      },]
      this.$emit('columnCallBack',col)
    }
  }
}
</script>

<style scoped>

</style>
