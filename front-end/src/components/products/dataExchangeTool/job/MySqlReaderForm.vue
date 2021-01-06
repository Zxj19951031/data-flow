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
  name: "MysqlReader",
  props: {
    id: String,
    data: Object
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
        queryTimeout: 60,
        loginTimeout: 10,
        columns: []
      },
      schemaSelectData: [],
      tableSelectData: []
    }
  },
  mounted() {
    this.handleSchemaQuery();
    if (this.$props.data) {
      this.formData = this.$props.data
      this.handleSchemaSelectChange()
      this.handleTableSelectChange()
    }
  },
  methods: {
    //查询Schema列表，组件初始化后回去获取该数据源下的所有Schema
    handleSchemaQuery() {
      let path = {id: this.$props.id}
      this.$http.getDatasourceSchemas({path}).then(resp => {
        this.schemaSelectData = resp.data;
      })
    },
    //处理Schema变化，编辑页面进入时也会调用该方法
    handleSchemaSelectChange() {
      let path = {id: this.$props.id}
      let params = {schema: this.formData.schema}
      this.$http.getDatasourceTables({path, params}).then(resp => {
        this.tableSelectData = resp.data;
      })
    },
    /**
     *  处理Table变化
     *  编辑页面进入后也会调用该方法，对字段的呈现方式会有所不同
     *  情况1：存在于历史的column中，但不存在于当前查到的column中，说明该字段被删除或重命名了那么将其从column中踢掉
     *  情况2：不存在于历史的column中，但存在于当前查到的column中，说明该字段被新增了，则将其加入到column中
     *  情况3：两边都存在的字段，不做操作
     */
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
