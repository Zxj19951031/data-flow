<!--导航栏-->
<template>
  <el-menu id="navigation" :mode="this.mode" :router="true" :default-active="this.$route.path">
    <template v-for="item in this.items">
      <template v-if="item.children">
        <el-submenu :index="item.path" :key="'key_'+item.id">
          <template slot="title">
            <span slot="title">{{ item.title }}</span>
          </template>
          <Navigation :items="item.children"></Navigation>
        </el-submenu>
      </template>
      <template v-else>
        <el-menu-item :index="item.path" :key="'key_'+item.id">
          <span slot="title">{{ item.title }}</span>
        </el-menu-item>
      </template>
    </template>
  </el-menu>
</template>

<script>
export default {
  name: "Navigation",
  props: {
    items: Array,
    mode: String,
  },
  methods: {

  }
}
</script>

<style scoped>
#navigation {
  border: none;
}
</style>
