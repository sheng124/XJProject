<template>
  <header class="header has-background-white has-text-black">
    <b-navbar class="is-white" :fixed-top="true">
      <template slot="brand">
        <b-navbar-item tag="router-link" :to="{ path: '/' }">
          <img :src="noteImg" alt="logo" />
        </b-navbar-item>
      </template>
      <template slot="start">
        <b-navbar-item tag="router-link" :to="{ path: '/' }">
          <i class="el-icon-s-home">首页</i>
        </b-navbar-item>
      </template>

      <template slot="end">
        <el-autocomplete
          style="width: 500px;margin:0 100px 0px 50px;"
          class="mt-1"
          popper-class="my-autocomplete"
          v-model="keywords"
          :fetch-suggestions="querySearch"
          placeholder="输入文章标题或内容..."
          @select="handleSelect"
          :trigger-on-focus="false"
        >
          <i
            class="el-icon-search el-input__icon"
            slot="suffix"
            ><!-- 放搜索图标 -->
          </i>
          <template slot-scope="{ item }">
            <div v-html="item.articleTitle"></div>
            <span class="is-size-7" v-html="item.articleContent"></span>
            <el-divider class="my-0"></el-divider>
          </template>
        </el-autocomplete>

        <!-- <el-autocomplete
          class="inline-input"
          v-model="keywords"
          :fetch-suggestions="querySearch"
          placeholder="搜索..."
          @select=""
        ></el-autocomplete>
        
        <v-icon>mdi-magnify</v-icon> -->
        <!-- <div class="search-result-wrapper">
        <hr class="divider"/>
        <ul>
          <li v-for="item of articleList" :key="item.id" class="search-reslut">
        
            <a @click="goTo(item.id)" v-html="item.articleTitle"/>
   
            <p
              class="search-reslut-content text-justify"
              v-html="item.articleContent"
            />
          </li>
        </ul>

        <div
            v-show="flag && articleList.length == 0"
            style="font-size:0.875rem"
        >
         找不到您查询的内容：{{ keywords }}
        </div>
      </div> -->
        <!-- <b-navbar-item tag="router-link" :to="{}"> 🔍搜索 </b-navbar-item> -->
        <!-- <b-navbar-item tag="router-link" :to="{ path: '/chat' }">
          聊天1
        </b-navbar-item> -->
        <b-navbar-item @click="openChatDialog"
          ><i class="el-icon-chat-dot-round">聊天</i></b-navbar-item
        >
        <b-navbar-item
          tag="router-link"
          :to="{ name: 'publish_center' }"
          v-show="token != null"
        >
          <i class="el-icon-edit-outline">发布中心</i>
        </b-navbar-item>
        <b-navbar-item tag="div">
          <b-switch v-model="darkMode" passive-type="is-warning" type="is-dark">
            {{ darkMode ? "夜" : "日" }}
          </b-switch>
        </b-navbar-item>
        <b-navbar-item v-if="token == null || token === ''" tag="div">
          <div class="buttons">
            <b-button
              class="is-light"
              tag="router-link"
              :to="{ path: '/register' }"
            >
              注册
            </b-button>

            <b-button
              class="is-info"
              outlined
              tag="router-link"
              :to="{ path: '/login' }"
            >
              登录
            </b-button>
          </div>
        </b-navbar-item>
        <!-- <b-navbar-item v-else tag="div">
          <el-dropdown class="avatar-container" trigger="click">
            <div class="avatar-wrapper">
              <img
                :src="user.userAvatar"
                class="user-avatar"
              />
              <i class="el-icon-caret-bottom" />
            </div>
            <el-dropdown-menu slot="dropdown" class="user-dropdown">
              <router-link to="/">
                <el-dropdown-item> 个人信息 </el-dropdown-item>
              </router-link>
              <a target="_blank" href="https://www.baidu.com">
                <el-dropdown-item>发布中心</el-dropdown-item>
              </a>
              <el-dropdown-item divided @click.native="logout">
                <span style="display: block">退出登录</span>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </b-navbar-item> -->
        <div v-else>
          <el-dropdown class="avatar-container" trigger="click">
            <div class="avatar-wrapper">
              <img :src="user.userAvatar" class="user-avatar" />
              <i class="el-icon-caret-bottom" />
            </div>
            <el-dropdown-menu slot="dropdown" class="user-dropdown">
              <router-link
                :to="{
                  name: 'user_info',
                  params: {
                    userId: user.userId,
                  },
                }"
              >
                <el-dropdown-item
                  ><v-icon small class="mb-1">mdi-account</v-icon
                  >个人中心</el-dropdown-item
                >
              </router-link>
              <!-- <router-link
                :to="{
                  name: 'publish_center',
                }"
              >
                <el-dropdown-item> 发布中心 </el-dropdown-item>
              </router-link> -->
              <el-dropdown-item divided @click.native="logout">
                <span style="display: block"
                  ><v-icon small class="mb-1">mdi-logout</v-icon>退出登录</span
                >
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
        <!-- <b-navbar-dropdown :label="user.username" :right="true">
          <img :src="user.userAvatar" class="" alt="User Avatar" />
          <b-navbar-item
            tag="router-link"
            :to="{ path: `/member/${user.username}/home` }"
          >
            🧘 个人中心
          </b-navbar-item>
          <hr class="dropdown-divider" />
          <b-navbar-item
            tag="router-link"
            :to="{ path: `/member/${user.username}/setting` }"
          >
            发布中心
          </b-navbar-item>
          <hr class="dropdown-divider" />
          <b-navbar-item tag="a" @click="logout"> 👋 退出登录 </b-navbar-item>
        </b-navbar-dropdown> -->
      </template>
    </b-navbar>
    <!-- 聊天弹窗 -->
    <div data-app="true">
      <v-dialog
        v-model="chatDialogVisible"
        max-width="1200px"
        style="height: 800px"
      >
        <chat-model @close="closeChatDialog"></chat-model>
      </v-dialog>
    </div>
    <!-- 文章内容弹窗 -->
    <div data-app="true">
      <v-dialog
        v-model="selectedArticleVisible"
        max-width="1000px"
        style="height: 800px"
      >
        <article-model
          :currentArticleId="selectedArticleId"
          @close="closeArticleDialog"
        ></article-model>
      </v-dialog>
    </div>
  </header>
</template>
  
  <script>
import {
  disable as disableDarkMode,
  enable as enableDarkMode,
} from "darkreader";
import { mapGetters } from "vuex";
import ChatModel from "@/components/Chat/ChatModel.vue";
import ArticleModel from "@/components/Article/ArticleModel.vue"; //导入子组件文章模型
import { removeAll, getDarkMode, setDarkMode } from "@/utils/js_cookie";
import { search } from "@/api/article";
export default {
  name: "Header",
  components: {
    //声明组件
    ChatModel,
    ArticleModel,
  },
  data() {
    return {
      logoUrl: require("@/assets/logo.png"),
      noteImg: require("@/assets/image/note.png"),
      darkMode: false,
      chatDialogVisible: false,

      selectedArticleId: -1,
      selectedArticleVisible: false, //查看文章详细内容的对话框
      keywords: "",
    };
  },
  computed: {
    ...mapGetters(["token", "user"]),
  },
  watch: {
    keywords(val) {},
    // 监听Theme模式
    darkMode(val) {
      if (val) {
        enableDarkMode({});
      } else {
        disableDarkMode();
      }
      setDarkMode(this.darkMode);
    },
  },
  created() {
    console.log(this.user);
    // 获取cookie中的夜间还是白天模式
    this.darkMode = getDarkMode();
    if (this.darkMode) {
      enableDarkMode({});
    } else {
      disableDarkMode();
    }
  },
  methods: {
    openChatDialog() {
      this.chatDialogVisible = true;
    },
    closeChatDialog() {
      this.chatDialogVisible = false;
    },
    async logout() {
      this.$store.dispatch("user/userLogout").then(() => {
        this.$message.info("退出登录成功");
        setTimeout(() => {
          if (this.$route.path !== "/") {
            this.$router.push({ path: this.redirect || "/" });
          }
        }, 500);
      });
    },
    querySearch(keywords, cb) {
      search(keywords)
        .then((response) => {
          const { data } = response;
          cb(data);
        })
        .catch((error) => {
          console.log("找不到");
        });
    },
    handleSelect(item) {
      this.selectedArticleId = item.articleId;
      this.selectedArticleVisible = true;
    },
    closeArticleDialog() {
      this.selectedArticleVisible = false;
      this.selectedArticleId = null;
    },
    handleIconSearch() {},
  },
};
</script>
  
<style scoped>
input {
  width: 80%;
  height: 86%;
}
.menu-btn {
  text-shadow: 0.05rem 0.05rem 0.1rem rgba(0, 0, 0, 0.3);
}

.avatar-container {
  margin-right: 10px;
}

.avatar-wrapper {
  margin-top: 5px;
  position: relative;
}

.user-avatar {
  cursor: pointer;
  border-radius: 50%;
  width: 40px; /* 根据需要调整头像的宽度 */
  height: 40px; /* 根据需要调整头像的高度 */
}

.el-icon-caret-bottom {
  cursor: pointer;
  position: absolute;
  right: -20px;
  top: 25px;
  font-size: 12px;
}
</style>