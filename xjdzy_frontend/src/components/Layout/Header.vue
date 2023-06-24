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
          <i class="iconfont iconzhuye" /> 首页
        </b-navbar-item>
      </template>

      <template slot="end">
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
              class="is-light"
              tag="router-link"
              :to="{ path: '/login' }"
            >
              登录
            </b-button>
          </div>
        </b-navbar-item>
        <b-navbar-item v-else tag="div">
          <!-- <button @click="showDropdown">
            <img :src="user.avatar" class="user-avatar" />
          </button> -->
          <b-navbar-dropdown :label="user.username">
            <img :src="user.avatar" class="user-avatar" alt="User Avatar" />
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
          </b-navbar-dropdown>
        </b-navbar-item>
      </template>
    </b-navbar>
  </header>
</template>
  
  <script>
/*   import { disable as disableDarkMode, enable as enableDarkMode } from 'darkreader'
  import { getDarkMode, setDarkMode } from '@/utils/js_cookie' */
import { mapGetters } from "vuex";
import { removeToken } from "@/utils/js_cookie";
export default {
  name: "Header",
  data() {
    return {
      logoUrl: require("@/assets/logo.png"),
      doubaoImg: require("@/assets/image/doubao.png"),
      /* dropdownVisible: false, */
    };
  },
  computed: {
    ...mapGetters(["token", "user"]),
  },
  created() {},
  methods: {
    logout() {
      removeToken();
      this.$store.commit("user/logout", null);
      this.$message.info("退出登录成功");
      setTimeout(() => {
        this.$router.push({ path: this.redirect || "/" }).catch((err) => {
          console.log("输出报错", err);
          this.$forceUpdate();
        });
      }, 500);
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
    showDropdown() {
      this.dropdownVisible = !this.dropdownVisible;
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
.user-avatar {
  cursor: pointer;
  border-radius: 50%;
  width: 30px; /* 根据需要调整头像的宽度 */
  height: 30px; /* 根据需要调整头像的高度 */
}
</style>