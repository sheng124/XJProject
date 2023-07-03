<template>
  <div>
    <!-- 文章内容 -->
    <v-card elevation="1" max-width="1500px" style="border-radius: 20px">
      <!-- <v-toolbar color="gray" dark flat>
        <v-toolbar-title>Chat</v-toolbar-title>
      </v-toolbar> -->
      <div class="level-left">
        <v-navigation-drawer
          permanent
          expand-on-hover
          class="is-align-self-flex-start"
        >
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
          <v-list width="260px" flat v-if="activeTab == 1">
            <v-subheader @click="windowOfUserId = -1">CHATS</v-subheader>
            <v-list-item-group v-model="selectedItem" color="primary">
              <v-list-item
                v-for="(item, index) in handledMessageList"
                :key="index"
                @click="showChatWindow(item[0].userId)"
              >
                <v-list-item-avatar>
                  <v-img :src="user.userAvatar"></v-img>
                </v-list-item-avatar>
                <v-list-item-content>
                  <v-list-item-title>{{ item }}</v-list-item-title>
                  <v-list-item-subtitle>...........</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
            </v-list-item-group>
          </v-list>
          <v-list width="260px" flat v-else>
            <v-subheader>ACCOUNTS</v-subheader>
            <v-list-item-group v-model="selectedItem" color="primary">
              <v-list-item
                v-for="(Onefollowing, index) in FollowingDataList"
                :key="index"
              >
                <v-list-item-avatar>
                  <v-img :src="Onefollowing.userAvatar"></v-img>
                </v-list-item-avatar>

                <v-list-item-content>
                  <v-list-item-title>{{
                    Onefollowing.username
                  }}</v-list-item-title>
                </v-list-item-content>
              </v-list-item>
            </v-list-item-group>
          </v-list>
        </div>
        <div v-if="windowOfUserId != -1">聊天界面</div>
      </div>
    </v-card>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { getFollowingList, getFollowerList, getUserInfo } from "@/api/user";

export default {
  name: "ChatModel",
  props: {},
  data() {
    return {
      activeTab: 1, // 选中显示消息列表还是联系人列表
      selectedItem: -1, //选中具体一条消息或联系人
      FollowingDataList: [], // 当前用户的关注列表
      FollowerDataList: [], // 当前用户的粉丝列表

      ws: null,
      messageList: [],
      sendMsg: "",
      sendUserId: -1,

      handledMessageList: [],
      windowOfUserId: -1, //关于关闭聊天，初步设置在点击头像后
    };
  },
  created() {
    //初始化websocket
    this.ws = new WebSocket("ws://localhost:8080/chat/" + this.user.userId);
    this.wsInit();
    this.init();
  },
  computed: {
    ...mapGetters(["user", "token"]),
  },
  methods: {
    init() {
      getFollowingList(this.user.userId).then((response) => {
        const { data } = response;
        this.FollowingDataList = data;
        console.log("获取用户关注列表", this.FollowingDataList);
      });
      getFollowerList(this.user.userId).then((response) => {
        const { data } = response;
        this.FollowerDataList = data;
        console.log("获取用户粉丝列表", this.FollowerDataList);
      });
    },
    handleMessages() {
      this.handledMessageList = this.messageList.reduce((result, message) => {
        const userId = message.userId;
        if (!result[userId]) {
          result[userId] = [];
        }
        result[userId].push(message);
        return result;
      }, {});
      console.log("handledMessageList:", this.handledMessageList);
      console.log("handledMessageList[2]:", this.handledMessageList[2]);
      console.log("handledMessageList[4]:", this.handledMessageList[4]);
      console.log("循环遍历handledMessageList");
      //重置数组
      for (const userId in this.handledMessageList) {
        const messages = this.handledMessageList[userId];
        // 在这里对每个用户的消息进行操作
        console.log(`用户 ${userId} 的消息数量：${messages.length}`);
        messages.forEach((message) => {
          console.log(`消息内容：${message.content}`);
        });
      }
      const keys = Object.keys(this.handledMessageList);
      console.log("键：", keys);
    },
    wsInit() {
      this.ws.onopen = () => {
        console.log("服务器连接");
        console.log(this.ws.readyState);
      };
      this.ws.onclose = () => {
        console.log("服务器关闭");
        console.log(this.ws.readyState);
      };
      this.ws.onmessage = (message) => {
        console.log("收到服务器消息");
        var msgInfo = JSON.parse(message.data);
        console.log(msgInfo);
        console.log(this.ws.readyState);
        if (msgInfo.code === 0) {
          // 修改已经收到的消息状态
          for (var i = 0; i < this.messageList.length; i++) {
            if (
              this.messageList[i].code === 1 &&
              this.messageList[i].userId === msgInfo.userId
            ) {
              this.messageList[i].read = true;
            }
          }
        } else {
          this.messageList.push(msgInfo);
        }
        console.log("每收到服务器一条消息后的messageList", this.messageList);
        this.handleMessages();
        if(this.windowOfUserId!=-1){
          this.sendControlMessage(this.windowOfUserId)
          console.log("已发送控制消息给用户",this.windowOfUserId);
        }
      };
      this.ws.onerror = (error) => {
        console.log("websocket错误!");
        console.log(error);
        console.log(this.ws.readyState);
      };
    },
    // 进入聊天界面要发一个控制消息，聊天界面打开状态，每收到一条消息，都会发一个控制消息
    sendControlMessage(chatUserId) {
      var msg = {
        code:0,
        userId:chatUserId
      };
      this.ws.send(JSON.stringify(msg));
    },
    sendMessage(userId, message) {
      var msg = {
        code: 1,
        userId: Number(userId),
        content: message,
        contentType: 0,
        read: false,
        sendTime: this.getNowTime(),
      };
      this.ws.send(JSON.stringify(msg));
      this.messageList.push(msg);
      console.log("每发送一条消息后的messageList", this.messageList);
      this.handleMessages();
    },
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
        console.log("选中的分栏：", this.activeTab, "聊天");
      } else {
        this.activeTab = 2;
        console.log("选中的分栏：", this.activeTab, "联系人");
      }
    },
    showChatWindow(chatUserId) {
      this.windowOfUserId = chatUserId;
      console.log("对话的用户ID", this.windowOfUserId);
      this.sendControlMessage(this.windowOfUserId);
      console.log("已发送控制消息给用户",this.windowOfUserId);
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

  watch: {
    // 监听模式
    selectedItem(val) {
      console.log("选中的聊天：", val);
    },
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