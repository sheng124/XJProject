<template>
  <div>
    <!-- 文章内容 -->
    <v-card elevation="1" max-width="1500px" style="border-radius: 20px">
      <!-- <v-toolbar color="gray" dark flat>
        <v-toolbar-title>Chat</v-toolbar-title>
      </v-toolbar> -->
      <div class="level-left">
        <v-navigation-drawer permanent expand-on-hover class="is-align-self-flex-start">
          <v-list-item class="px-2">
            <v-list-item-avatar>
              <v-img :src="user.userAvatar"></v-img>
            </v-list-item-avatar>
            <v-list-item-title>{{ user.username }}</v-list-item-title>
          </v-list-item>
          <v-divider class="my-1"></v-divider>
          <v-list nav>
            <v-list-item link @click="navigateTo('/messages')">
              <v-list-item-icon>
                <v-icon>mdi-chat</v-icon>
              </v-list-item-icon>
              <v-list-item-title>Messages</v-list-item-title>
            </v-list-item>
            <v-list-item link @click="navigateTo('/accounts')">
              <v-list-item-icon>
                <v-icon>mdi-account-group</v-icon>
              </v-list-item-icon>
              <v-list-item-title>联系人</v-list-item-title>
            </v-list-item>
          </v-list>
        </v-navigation-drawer>
        <div class="is-align-self-flex-start">
          <!-- <v-toolbar
            flat
            absolute
            width="260px"
            height="61px"
            color="grey lighten-5"
          >
            <span style="margin: 0 40%">Chat</span>
          </v-toolbar> -->
          <v-list max-width="260px" flat>
            <v-subheader>CHATS</v-subheader>
            <v-list-item-group v-model="selectedItem" color="primary">
              <v-list-item>
                <v-list-item-avatar>
                  <v-img :src="user.userAvatar"></v-img>
                </v-list-item-avatar>
                <v-list-item-content>
                  <v-list-item-title>是打算电话</v-list-item-title>
                  <v-list-item-subtitle>分身乏术东方说？</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
              <v-list-item>
                <v-list-item-avatar>
                  <v-img :src="user.userAvatar"></v-img>
                </v-list-item-avatar>
                <v-list-item-content>
                  <v-list-item-title>测试</v-list-item-title>
                  <v-list-item-subtitle>你吃啥？</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
              <v-list-item>
                <v-list-item-avatar>
                  <v-img :src="user.userAvatar"></v-img>
                </v-list-item-avatar>
                <v-list-item-content>
                  <v-list-item-title>测试</v-list-item-title>
                  <v-list-item-subtitle>你吃啥？</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
              <v-list-item>
                <v-list-item-avatar>
                  <v-img :src="user.userAvatar"></v-img>
                </v-list-item-avatar>
                <v-list-item-content>
                  <v-list-item-title>测试</v-list-item-title>
                  <v-list-item-subtitle>你吃啥？</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
              <v-list-item>
                <v-list-item-avatar>
                  <v-img :src="user.userAvatar"></v-img>
                </v-list-item-avatar>
                <v-list-item-content>
                  <v-list-item-title>测试</v-list-item-title>
                  <v-list-item-subtitle>你吃啥？</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
              <v-list-item>
                <v-list-item-avatar>
                  <v-img :src="user.userAvatar"></v-img>
                </v-list-item-avatar>
                <v-list-item-content>
                  <v-list-item-title>测试</v-list-item-title>
                  <v-list-item-subtitle>你吃啥？</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
            </v-list-item-group>
          </v-list>
        </div>
        <div>
          聊天界面
        </div>
      </div>
    </v-card>
  </div>
</template>

<script>
import { mapGetters } from "vuex";

export default {
  name: "ChatModel",
  props: {},
  data() {
    return {
      activeTab: 1,
      selectedItem: 1,
    };
  },
  methods: {
    formatDate1(dateString) {
      const datePart = dateString.split("T")[0];
      return datePart;
    },
    formatDate2(dateString) {
      const date = dateString.replace("T", " ");
      return date;
    },
    navigateTo(routePath) {
      if (routePath === "/messages") {
        this.activeTab = 1;
        console.log("选中的分栏：",this.activeTab,"聊天")
      } else {
        this.activeTab = 2;
        console.log("选中的分栏：",this.activeTab,"联系人")
      }
      
    },
    getNowTime: function () {
      let dateTime;
      let yy = new Date().getFullYear();
      let mm =
        new Date().getMonth() + 1 < 10
          ? "0" + (new Date().getMonth() + 1)
          : new Date().getMonth() + 1;
      let dd =
        new Date().getDate() < 10
          ? "0" + new Date().getDate()
          : new Date().getDate();
      let hh =
        new Date().getHours() < 10
          ? "0" + new Date().getHours()
          : new Date().getHours();
      let mf =
        new Date().getMinutes() < 10
          ? "0" + new Date().getMinutes()
          : new Date().getMinutes();
      let ss =
        new Date().getSeconds() < 10
          ? "0" + new Date().getSeconds()
          : new Date().getSeconds();
      dateTime =
        yy + "-" + mm + "-" + dd + "T" + hh + ":" + mf + ":" + ss + ".000Z";
      console.log(dateTime);
      return dateTime;
    },
  },
  computed: {
    ...mapGetters(["user", "token"]),
  },
  watch: {
    // 监听模式
    selectedItem(val){
      console.log("选中的聊天：",val)
    }
    
  },
  mounted() {
    this.getArticle();
    /* this.scroll = new IScroll(this.$refs.wrapper, {
      mouseWheel: true, // 允许鼠标滚轮
    }); */
  },
};
</script>

<style scoped>
.user-avatar-article-detail {
  cursor: pointer;
  border-radius: 50%;
  width: 40px; /* 根据需要调整头像的宽度 */
  height: 40px; /* 根据需要调整头像的高度 */
}
.image-container {
  height: 745px;
  display: flex;
  justify-content: center;
  align-items: center;
}
.responsive-image {
  width: auto;
  height: auto;
  max-height: 100%;
  max-width: 100%;
}
.video-container {
  height: 700px;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
}

.responsive-video {
  position: relative;
  padding-bottom: 56.25%; /* 16:9 aspect ratio (9 / 16 * 100) */
  width: 100%;
  height: 0;
}

.responsive-video iframe {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.comment-user-avatar {
  cursor: pointer;
  border-radius: 50%;
  width: 35px; /* 根据需要调整头像的宽度 */
  height: 32px; /* 根据需要调整头像的高度 */
}
.router-link-active {
  text-decoration: none;
  color: gray;
}
a {
  text-decoration: none;
  color: gray;
}
/*.wrapper {
  position: relative;
  height: 600px;
  overflow: hidden;
}*/
</style>