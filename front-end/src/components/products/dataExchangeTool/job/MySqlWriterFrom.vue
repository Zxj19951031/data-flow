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
  name: "MysqlWriter",
  props: {
    id: String,
    data: Object
  },
  data() {
    return {
      formData: {
        schema: "",
        table: "",
        method: "insert",
        preSql: "",
        postSql: "",
        queryTimeout: 60,
        loginTimeout: 10,
        bufferSize: 1000,
        columns: []
      },
      schemaSelectData: [],
      tableSelectData: []
    }
  },
  mounted() {
    this.handleSchemaQuery()
    if (this.$props.data) {
      this.formData = this.$props.data
      this.handleSchemaSelectChange()
      this.handleTableSelectChange()
    }
  },
  methods: {
    //查询Schema列表
    handleSchemaQuery() {
      let path = {id: this.$props.id}
      this.$http.getDatasourceSchemas({path}).then(resp => {
        this.schemaSelectData = resp.data;
      })
    },
    //处理Schema变化
    handleSchemaSelectChange() {
      let path = {id: this.$props.id}
      let params = {schema: this.formData.schema}
      this.$http.getDatasourceTables({path, params}).then(resp => {
        this.tableSelectData = resp.data;
      })
    },
    //处理Table变化
    handleTableSelectChange() {
      let path = {id: this.$props.id}
      let params = {
        schema: this.formData.schema,
        table: this.formData.table
      }
      this.$http.getDatasourceColumns({path, params}).then(resp => {
        let allColumns = [];
        for (let col of resp.data) {
          allColumns.push({
            name: col.split('[')[0],
            type: col.split('[')[1].replace(']', ''),
            selected: false
          });
        }

        let fixed = []
        if (this.formData.columns) {
          //踢掉情况1的字段，记做已选的字段
          for (let column of this.formData.columns) {
            for (let allColumn of allColumns) {
              if (allColumn.name === column) {
                allColumn.selected = true
                fixed.push(allColumn)
              }
            }
          }

          //保留情况2的字段，记做未选的字段
          for (let column of allColumns) {
            let unselected = true;
            for (let selectedColumn of fixed) {
              if (selectedColumn.name === column.name) {
                unselected = false
                break
              }
            }
            if (unselected) {
              fixed.push(column)
            }
          }
        }else{
          fixed = allColumns
        }
        this.$emit('columnCallBack', fixed)
      })
    }
  }
}
</script>

<style scoped>

</style>
