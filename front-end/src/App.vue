<template>
  <el-container id="body-container">
    <el-header v-if="this.$route.fullPath!=='/login'">
      <el-row type="flex" justify="space-between">
        <!--        logo-->
        <el-col>
          <el-image id="logo-image" :src="this.headerIcon.url"></el-image>
        </el-col>
        <!--        头部导航-->
        <el-col>
          <Navigation class="header-line-navigation" :items="this.navigation" mode="horizontal" style="float: right;"/>
        </el-col>
      </el-row>
    </el-header>
    <!--    正文-->
    <el-main style="height: 100%;overflow-y: hidden">
      <router-view/>
    </el-main>

  </el-container>


</template>

<script>

import Navigation from "@/components/navigation/Navigation";

export default {
  name: 'app',
  components: {
    Navigation,
  },
  data() {
    return {
      navigation: [
        {
          id: 1,
          title: "数据交换工具",
          path: "/products/dataExchangeTool/rule"
        }, {
          id: 2,
          title: "流程引擎",
          path: "/products/flowableEngine"
        }, {
          id: 4,
          title: "控制台",
          path: "/console",
          children: [{
            id: 3,
            title: "更新日志",
            path: "/console/changeLog"
          }]
        }, {
          id: 5,
          title: "个人中心",
          path: "/aboutMe"
        }],
      headerIcon: {
        url: '',
      }
    }
  }
}
</script>

<style>
body, html {
  margin: 0;
  height: 100%;
  overflow: hidden;
}

/*导航项最小宽度*/
.el-menu {
  min-width: 100px !important;
}

#body-container {
  margin: 0;
  height: 100%;
  overflow: hidden;
}

#body-container > .el-header {
  width: 100%;
}

#body-container > .el-header .el-row {
  width: 100%;
  border-bottom-width: 1px;
  border-bottom-style: solid;
  border-bottom-color: #dedede;
}

.el-header .el-image {
  width: 100px;
  height: 50px;
  margin-top: 5px;
}

.expression-row div:first-child .el-input-group__append {
  border-top-right-radius: 0;
  border-bottom-right-radius: 0;
}

.expression-row input:nth-child(1) {
  border-radius: 0;
  border-left: 0;
}

.week-from-to-input .el-input__inner, .week-last-of-month-input .el-input__inner {
  border-radius: 0;
}

.col-tb tr {
  height: 60px;
}
</style>
