<template>
  <div>
    <!-- 文章内容 -->
    <v-card elevation="1" max-width="1200px" style="border-radius: 20px">
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
              <v-list-item-title>消息</v-list-item-title>
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
            <v-subheader @click="windowOfUserId = -1">聊天</v-subheader>
            <v-list-item-group
              v-model="selectedItem"
              color="primary"
              style="overflow-y: scroll; overflow-x: hidden; height: 700px"
            >
              <v-list-item
                v-for="(item, index) in handledMessageList"
                :key="index"
                @click="showChatWindow(item[0].userId)"
              >
                <v-list-item-avatar>
                  <v-img :src="item[0].userAvatar"></v-img>
                </v-list-item-avatar>
                <v-list-item-content>
                  <v-list-item-title>{{ item[0].username }}</v-list-item-title>
                  <v-list-item-subtitle v-if="item[item.length-1].code==2">{{item[item.length-1].username}}: {{ item[item.length-1].content }}</v-list-item-subtitle>
                  <v-list-item-subtitle v-else>{{user.username}}: {{ item[item.length-1].content }}</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
            </v-list-item-group>
          </v-list>
          <v-list width="260px" flat v-else>
            <v-subheader>关注的人</v-subheader>
            <v-list-item-group
              v-model="selectedItem"
              color="primary"
              style="overflow-y: scroll; overflow-x: hidden; height: 700px"
            >
              <v-list-item
                v-for="(Onefollowing, index) in FollowingDataList"
                :key="index"
                @click="showChatUserCard(Onefollowing.userId)"
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
        <div
          class="is-align-self-flex-start"
          style="width: 75%"
          v-if="windowOfUserId != -1 && cardOfUserId == -1"
        >
          <!-- <v-text class="is-size-4">{{ handledMessageList[windowOfUserId][0].username }}</v-text> -->
          <div class="level mb-0">
            <v-toolbar height="55px" color="grey lighten-5" flat>
              <v-toolbar-title>{{ cardUser.username }}</v-toolbar-title>
            </v-toolbar>
            <v-btn class="ma-2" text icon color="red" @click="windowOfUserId = -1">
              <v-icon>mdi-close-circle</v-icon>
            </v-btn>
          </div>

          <el-divider class="my-0"></el-divider>
          <div
            class="px-2 py-1"
            style="height: 500px; overflow-y: scroll; overflow-x: hidden"
          >
            <!-- 聊天内容 -->
            <div
              v-for="(item, index) in handledMessageList[windowOfUserId]"
              :key="index"
              class="mt-3"
            >
              <div v-if="item.code == 2" class="my-2">
                <b-tooltip
                  :label="item.content"
                  position="is-right"
                  always
                  type="is-light"
                >
                  <v-avatar size="36">
                    <img :src="item.userAvatar" :alt="item.username" />
                  </v-avatar>
                </b-tooltip>
              </div>
              <div style="display: flex; justify-content: flex-end" v-else>
                <b-tooltip
                  type="is-info"
                  class="is-align-self-flex-end"
                  :label="item.content"
                  position="is-left"
                  always
                >
                  <v-avatar size="36">
                    <img :src="user.userAvatar" :alt="user.username" />
                  </v-avatar>
                </b-tooltip>
              </div>
              <div
                style="
                  display: flex;
                  justify-content: flex-end;
                  margin-right: 50px;
                "
                v-if="item.code == 1"
              >
                <div class="is-size-7 has-text-grey" v-if="item.read == true">
                  已读
                </div>
                <div class="is-size-7 has-text-warning-dark" v-else>未读</div>
              </div>
            </div>
          </div>
          <el-divider class="my-0"></el-divider>
          <div class="mx-2">
            <!-- 输入框 -->
            <el-popover
              placement="bottom"
              title="标题"
              width="500"
              height="700"
              trigger="click"
              v-model="emojiShow"
            >
              <b-button slot="reference" type="is-light">😀</b-button>
              <div class="browBox">
                <ul class="ul_in_browBox">
                  <li
                    class="li_in_browBox"
                    v-for="(item, index) in faceList"
                    :key="index"
                    @click="getBrow(index)"
                  >
                    {{ item }}
                  </li>
                </ul>
              </div>
            </el-popover>

            <el-input
              :rows="5"
              type="textarea"
              placeholder="请输入内容"
              v-model="content"
            >
            </el-input>
            <el-button
              class="submit-btn"
              type="primary"
              @click="sendMessage(windowOfUserId, content)"
              :disabled="content == ''"
              >发送
            </el-button>
          </div>
        </div>
        <div
          v-if="windowOfUserId == -1 && cardOfUserId != -1"
          style="width: 75%; display: flex; justify-content: center"
        >
          <v-card
            width="450"
            style="
              display: flex;
              flex-direction: column;
              align-items: center;
              justify-content: center;
              height: 100%;
            "
            class="py-5"
          >
            <div>
              <v-avatar size="200">
                <img :src="cardUser.userAvatar" :alt="cardUser.username" />
              </v-avatar>
            </div>
            <div class="my-3">{{ cardUser.username }}</div>
            <div>
              <span class="is-size-7">笔记</span
              ><v-avatar
                size="40"
                class="mx-1"
                tile
                v-for="(item, index) in wrArticlesOfCardUser"
                :key="index"
              >
                <img :src="item.articleCover" />
              </v-avatar>
            </div>
            <div class="mt-4">
              <v-btn color="success" @click="createChat(cardOfUserId)">
                发消息
                <v-icon right> mdi-chat-processing </v-icon>
              </v-btn>
            </div>
          </v-card>
        </div>
      </div>
    </v-card>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import {
  getFollowingList,
  getFollowerList,
  getUserInfo,
  getWrLiCoArticles,
} from "@/api/user";

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
      windowOfUserId: -1, //关于关闭聊天，初步设置在点击Chat后

      cardOfUserId: -1,
      cardUser: null,
      wrArticlesOfCardUser: [],
      count: 0,
      chatUsername: "",

      //聊天输入内容
      content: "",
      //表情框是否展示
      emojiShow: false,
      //表情列表
      faceList: [],
      //表情文本
      getBrowString: "",
    };
  },
  created() {
    this.loadEmojis();
    //初始化websocket
    this.ws = new WebSocket("ws://localhost:8080/chat/" + this.user.userId);
    this.wsInit();
    this.init();
  },
  computed: {
    ...mapGetters(["user", "token"]),
  },
  methods: {
    //加载表情，存放到表情列表中
    loadEmojis() {
      const appData = require("@/assets/image/emojis.json");
      for (let i in appData) {
        this.faceList.push(appData[i].char);
      }
    },
    getBrow(index) {
      for (let i in this.faceList) {
        if (index == i) {
          this.getBrowString = this.faceList[index];
          this.content += this.getBrowString;
        }
      }
      this.emojiShow = false;
    },
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
      /* console.log("handledMessageList[2]:", this.handledMessageList[2]);
      console.log("handledMessageList[4]:", this.handledMessageList[4]);
      console.log(
        "handledMessageList[2]的用户名：",
        this.handledMessageList[2][0].username
      ); */
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
      this.messageList = [];
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
          console.log("收到的控制消息：", msgInfo);
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
          if (this.windowOfUserId != -1 && msgInfo.read == false) {
            this.sendControlMessage(this.windowOfUserId);
            console.log(
              "当前收到新消息，已发送控制消息给用户",
              this.windowOfUserId
            );
          }
        }
        console.log("每收到服务器一条消息后的messageList", this.messageList);
        this.handleMessages();
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
        code: 0,
        userId: chatUserId,
      };
      this.ws.send(JSON.stringify(msg));
    },
    sendMessage(userId, message) {
      var msg = {
        code: 1,
        userId: Number(userId),
        userAvatar: this.cardUser.userAvatar,
        username: this.cardUser.username,
        content: message,
        contentType: 0,
        read: false,
        sendTime: this.getNowTime(),
      };
      this.ws.send(JSON.stringify(msg));
      this.messageList.push(msg);
      console.log("每发送一条消息后的messageList", this.messageList);
      this.handleMessages();
      //清空输入框
      this.content = "";
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
      this.cardOfUserId = -1;
      this.windowOfUserId = chatUserId;
      console.log("对话的用户ID", this.windowOfUserId);
      getUserInfo(this.windowOfUserId).then((response) => {
        const { data } = response;
        this.cardUser = data;
        console.log("选中消息的联系人信息", this.cardUser);
      });
      this.sendControlMessage(this.windowOfUserId);
      console.log("已发送控制消息给用户", this.windowOfUserId);
    },
    showChatUserCard(chatUserId) {
      this.windowOfUserId = -1;
      this.cardOfUserId = chatUserId;
      getUserInfo(this.cardOfUserId).then((response) => {
        const { data } = response;
        this.cardUser = data;
        console.log("选中的联系人", this.cardUser);
        getWrLiCoArticles(this.cardOfUserId).then((response) => {
          const { data } = response;
          console.log("收到的所有卡片用户相关笔记数据", data, data.length);
          this.wrArticlesOfCardUser = []; //先清空已发布数组
          this.count = 0;
          //已发布：1，已收藏:2，已喜欢：3
          for (var i = 0; i < data.length; i++) {
            console.log("笔记" + i + "的类型为：", data[i].type);
            if (data[i].type == 1 && this.count < 6) {
              //只展示6篇笔记
              //处理数据
              this.wrArticlesOfCardUser.push(data[i]);
              this.count++;
            }
          }
          console.log(
            "卡片用户：",
            this.cardOfUserId,
            "的展示笔记：",
            this.wrArticlesOfCardUser
          );
        });
      });
    },
    createChat(cardOfUserId) {
      this.windowOfUserId = cardOfUserId;
      this.cardOfUserId = -1;
      this.activeTab = 1;
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
    /* this.scroll = new IScroll(this.$refs.wrapper, {
      mouseWheel: true, // 允许鼠标滚轮
    }); */
  },
};
</script>

<style scoped>
.user-avatar-chat {
  cursor: pointer;
  border-radius: 50%;
  width: 50px; /* 根据需要调整头像的宽度 */
  height: 50px; /* 根据需要调整头像的高度 */
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

.browBox {
  width: 100%;
  height: 200px;
  background: #e6e6e6;
  position: absolute;
  z-index: 100;
  bottom: 0px;
  overflow: scroll;
}
.ul_in_browBox {
  display: flex;
  flex-wrap: wrap;
  padding: 10px;
}
.li_in_browBox {
  cursor: pointer;
  width: 10%;
  font-size: 26px;
  list-style: none;
  text-align: center;
}

.submit-btn {
  margin: 0 15px 10px 0;
  float: right;
}
</style>