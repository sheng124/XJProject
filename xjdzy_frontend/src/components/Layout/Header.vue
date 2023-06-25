<template>
  <header class="header has-background-white has-text-black">
    <b-navbar class="is-white has-shadow" :fixed-top="true">
      <template slot="brand">
        <b-navbar-item tag="router-link" :to="{ path: '/' }">
          <img :src="doubaoImg" alt="logo" />
        </b-navbar-item>
      </template>
      <template slot="start">
        <b-navbar-item tag="router-link" :to="{ path: '/' }">
          🏠首页
        </b-navbar-item>
      </template>

      <template slot="end">
        <b-navbar-item tag="router-link" :to="{}"> 🔍搜索 </b-navbar-item>
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
              class="is-info" outlined
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
              <img :src=user.userAvatar class="user-avatar" />
              <i class="el-icon-caret-bottom" />
            </div>
            <el-dropdown-menu slot="dropdown" class="user-dropdown">
              <router-link :to="{
                name:'user_info',
                params:{
                  userId:user.userId
                }
              }">
                <el-dropdown-item> 个人中心 </el-dropdown-item>
              </router-link>
              <router-link :to="{
                name:'publish_center',
              }">
                <el-dropdown-item> 发布中心 </el-dropdown-item>
              </router-link>
              <el-dropdown-item divided @click.native="logout">
                <span style="display: block">退出登录</span>
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
  </header>
</template>
  
  <script>
import {
  disable as disableDarkMode,
  enable as enableDarkMode,
} from "darkreader";
import { mapGetters } from "vuex";
import { removeAll,getDarkMode,setDarkMode } from "@/utils/js_cookie";
export default {
  name: "Header",
  data() {
    return {
      logoUrl: require("@/assets/logo.png"),
      doubaoImg: require("@/assets/image/doubao.png"),
      darkMode: false,
    };
  },
  computed: {
    ...mapGetters(["token", "user"]),
  },
  watch: {
      // 监听Theme模式
      darkMode(val) {
        if (val) {
          enableDarkMode({})
        } else {
          disableDarkMode()
        }
        setDarkMode(this.darkMode)
      }
    },
  created() {
    console.log(this.user);
    // 获取cookie中的夜间还是白天模式
    this.darkMode = getDarkMode()
      if (this.darkMode) {
        enableDarkMode({})
      } else {
        disableDarkMode()
      }
  },
  methods: {
    logout() {
      this.$store.dispatch("user/userLogout").then(() => {
        this.$message.info("退出登录成功");
        setTimeout(() => {
          this.$router.push({ path: this.redirect || '/' });
        }, 500);
      });
    },
    search() {
      console.log(this.token);
      if (this.searchKey.trim() === null || this.searchKey.trim() === "") {
        this.$message.info({
          showClose: true,
          message: "请输入关键字搜索！",
          type: "warning",
        });
        return false;
      }
      this.$router.push({ path: "/search?key=" + this.searchKey });
    },
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