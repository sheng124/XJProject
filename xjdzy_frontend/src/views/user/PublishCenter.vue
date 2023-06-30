<template>
  <!-- <div class="columns">
    <div class="column is-2">
      <el-menu :default-active="activeTab" class="el-menu-vertical-demo">
        <el-submenu index="1">
          <template slot="title">
            <i class="el-icon-location"></i>
            <span>发布</span>
          </template>
          <el-menu-item-group>
            <el-menu-item index="1-1" @click="navigateTo('/publish')">发布笔记</el-menu-item>
            <el-menu-item index="1-2" @click="navigateTo('/edit')">修改笔记</el-menu-item>
          </el-menu-item-group>
        </el-submenu>
        <el-menu-item index="2" @click="navigateTo('/data')">
          <i class="el-icon-data-line"></i>
          <span slot="title">笔记数据</span>
        </el-menu-item>
      </el-menu>
    </div>
    <div class="column has-background-white-bis">
      <Publish v-show="activeTab === 1"></Publish>
      <ArticleData v-show="activeTab === 2"></ArticleData>
    </div>
  </div> -->
  <el-row class="tac">
    <el-col :span="5">
      <el-menu :default-active="activeTab" class="el-menu-vertical-demo">
        <el-submenu index="1">
          <template slot="title">
            <i class="el-icon-location"></i>
            <span>发布</span>
          </template>
          <el-menu-item-group>
            <el-menu-item index="1-1" @click="navigateTo('/publish')">发布笔记</el-menu-item>
            <el-menu-item index="1-2" @click="navigateTo('/edit')">修改笔记</el-menu-item>
          </el-menu-item-group>
        </el-submenu>
        <el-menu-item index="2" @click="navigateTo('/data')">
          <i class="el-icon-data-line"></i>
          <span slot="title">笔记数据</span>
        </el-menu-item>
      </el-menu>
    </el-col>
    <el-col :span="19" class="has-background-white-bis">
      <Publish v-show="activeTab === 1"></Publish>
      <ArticleData v-show="activeTab === 2"></ArticleData>
    </el-col>
  </el-row>
</template>

<script>
import Publish from "@/components/PublishCenter/Publish";
import ArticleData from "@/components/PublishCenter/ArticleData.vue";
export default {
  name: "PublishCenter",
  components: { Publish, ArticleData },
  data() {
    return {
      activeTab: 1,
    };
  },
  methods: {
    navigateTo(routePath) {
      if (routePath === "/publish") {
        this.activeTab = 1;
      } else if (routePath === "/data") {
        this.activeTab = 2;
      }
    },
  },
};
</script>

<style>
</style>