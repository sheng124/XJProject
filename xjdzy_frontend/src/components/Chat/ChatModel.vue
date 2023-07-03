<template>
  <div>
    <!-- 文章内容 -->
    <v-card elevation="1" max-width="1000px" style="border-radius: 20px">
      <v-toolbar color="gray" dark flat>
        <v-toolbar-title>Chat</v-toolbar-title>
      </v-toolbar>
    </v-card>
  </div>
</template>

<script>
import { mapGetters } from "vuex";

export default {
  name: "ChatModel",
  props: {},
  data() {
    return {};
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
    ...mapGetters(["user", "token", "Lflag", "Cflag"]),
  },
  watch: {
    // 监听模式
    currentArticleId(val) {
      console.log("currentArticleId", this.currentArticleId);
      this.getArticle();
    },
    newCommentNum(val) {
      console.log("newCommentNum", this.newCommentNum);
      this.getArticle();
    },
    likesFlag(val) {
      console.log("likesFlag", this.likesFlag);
      this.getArticle();
      console.log("commit之前的Lflag：", this.Lflag);
      if (this.likeFlag == true) {
        this.$store.commit("user/setLflagState", 1);
      } else {
        this.$store.commit("user/setLflagState", -1);
      }
      console.log("commit之后的Lflag：", this.Lflag);
    },
    collectionFlag(val) {
      console.log("collectionFlag", this.collectionFlag);
      this.getArticle();
      console.log("commit之前的Cflag：", this.Cflag);
      if (this.collectionFlag == true) {
        this.$store.commit("user/setCflagState", 1);
      } else {
        this.$store.commit("user/setCflagState", -1);
      }
      console.log("commit之后的Cflag：", this.Cflag);
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