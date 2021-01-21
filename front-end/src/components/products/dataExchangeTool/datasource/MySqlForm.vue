<template>
  <div>
    <el-form-item label="主机地址" prop="host">
      <el-input placeholder="请输入主机地址，例如127.0.0.1" v-model="formData.host"></el-input>
    </el-form-item>
    <el-form-item label="端口号" prop="port">
      <el-input type="number" v-model="formData.port"></el-input>
    </el-form-item>
    <el-form-item label="用户名" prop="user">
      <el-input placeholder="请输入用户名，例如root" v-model="formData.username"></el-input>
    </el-form-item>
    <el-form-item label="密码" prop="password">
      <el-input show-password v-model="formData.password" placeholder="请输入密码"
                autocomplete="new-password"></el-input>
    </el-form-item>
  </div>
</template>

<script>
export default {
  name: "MySqlForm",
  data() {
    return {
      formData: {
        host: null,
        port: 3306,
        username: null,
        password: null
      }
    }
  },
  methods: {
    handleFormData(data){
      this.formData.host = data.host;
      this.formData.port = data.port;
      this.formData.username = data.username;
      this.formData.password = data.password;
    }
  }
}
</script>

<style scoped>

</style>
