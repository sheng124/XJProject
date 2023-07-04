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
      <publish v-show="activeTab === 1"></publish>
      <article-data v-show="activeTab === 2"></article-data>
      <edit v-show="activeTab === 3"></edit>
    </el-col>
  </el-row>
</template>

<script>
import Publish from "@/components/PublishCenter/Publish.vue";
import ArticleData from "@/components/PublishCenter/ArticleData.vue";
import Edit from "@/components/PublishCenter/Edit.vue";
import { mapGetters } from "vuex";

export default {
  name: "PublishCenter",
  components: { Publish, ArticleData,Edit },
  data(){
    return {
      activeTab: 2,
    };
  },
  computed: {
    ...mapGetters(["token", "user","editArticleId"]),
  },
  watch:{
    editArticleId(val){
      console.log("监听到要编辑的笔记ID:",val)
      if(val!=-1){
        this.activeTab=3;
      }
    },
  },
  methods: {
    navigateTo(routePath) {
      if (routePath === "/publish") {
        this.activeTab = 1;
      } else if (routePath === "/data") {
        this.activeTab = 2;
      }else if(routePath === "/edit"){
        this.activeTab=3
      }

    },
  },
};
</script>

<style>
</style>