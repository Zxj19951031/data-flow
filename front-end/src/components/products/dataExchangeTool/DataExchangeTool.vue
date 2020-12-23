<template>
  <el-container id="aside-main-container">
    <!--    左侧导航-->
    <el-aside width="200px">
      <Navigation :items="this.navigation"/>
    </el-aside>
    <!--    分割线-->
    <el-divider id="aside-main-divider" direction="vertical"></el-divider>
    <!--    右侧内容-->
    <el-main>
      <router-view/>
    </el-main>
  </el-container>
</template>

<script>
import Navigation from "@/components/navigation/Navigation";

export default {
  name: "DataExchangeTool",
  components: {
    Navigation,
  },
  data() {
    return {
      navigation: [{
        id: 2,
        title: "调度规则",
        path: "/products/dataExchangeTool/rule"
      }, {
        id: 3,
        title: "数据源",
        path: "/products/dataExchangeTool/datasource"
      }, {
        id: 4,
        title: "交换任务",
        path: "/products/dataExchangeTool/job"
      }],
    }
  }
}
</script>

<style scoped>

#aside-main-divider {
  height: auto;
}

#aside-main-container {
  height: 100%;
}
</style>
