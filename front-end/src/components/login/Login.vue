<template>
  <div>
    <el-row type="flex" justify="center">
      Data Flow
    </el-row>
    <el-row type="flex" justify="center">
      <el-col :span="6">
        <el-form v-model="formData" ref="ruleForm" label-position="left">
          <el-form-item prop="username">
            <el-input v-model="formData.username" placeholder="请输入用户名">
              <template slot="prepend"><span class="el-icon-user"></span></template>
            </el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input type="password" v-model="formData.password" placeholder="请输入密码" autocomplete="off">
              <template slot="prepend"><span class="el-icon-lock"></span></template>
            </el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" style="width: 100%" @click="this.login">登录</el-button>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
  </div>

</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      formData: {
        username: "",
        password: "",
      }
    }
  },
  methods: {
    login() {
      let params = this.formData;
      this.$http.login({params}).then(resp => {
        localStorage.token = resp.data.token;
        this.$router.push('/')
      })
    }
  }
}
</script>

<style scoped>
.el-form {
  padding: 20px 5px;
}
</style>
