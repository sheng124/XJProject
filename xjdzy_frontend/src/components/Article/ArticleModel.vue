<template>
  <div>
    <!-- 文章内容 -->
    <v-card elevation="1" max-width="1000px" style="border-radius: 20px">
      <el-row>
        <el-col :span="12">
          <!-- 文章左侧图片、视频 -->
          <b-carousel :autoplay="false">
            <!-- <b-carousel-item v-for="(carousel, i) in carousels" :key="i" >
              <section :class="`hero is-medium is-${carousel.color}`">
                <div class="hero-body has-text-centered">
                  <h1 class="title">{{ carousel.text }}</h1>
                </div>
              </section> -->
            <b-carousel-item v-if="article.videoUrl!=null">
              <figure class="image is-2by3">
                <iframe
                  class="has-ratio"
                  width="100%"
                  :src="article.videoUrl"
                  frameborder="0"
                  allowfullscreen
                ></iframe
                ><!-- 去掉了height="360" -->
              </figure>
            </b-carousel-item>
            <!-- <b-carousel-item>
              <div class="video-container">
                <div class="responsive-video">
                <iframe
                  width="100%"
                  height="100%"
                  src="http://121.36.202.123:8080/assert/c574a573bdb0491ba42809c842d3cda3.mp4"
                  frameborder="0"
                  allowfullscreen
                ></iframe>
                </div>
              </div>
            </b-carousel-item> -->
            <b-carousel-item
              v-for="(img, index) in article.articleImages"
              :key="index"
            >
              <div class="image-container has-background-light">
                <!-- <b-image class="image" :src="img"></b-image> -->
                <img :src="img" alt="Image" class="responsive-image" />
              </div>
            </b-carousel-item>
          </b-carousel>
        </el-col>
        <el-col :span="12">
          <!-- 文章右侧内容 -->
          <!-- 笔记作者头像，名称 -->
          <div class="level ml-2 mb-1">
            <router-link
              :to="{
                name: 'user_info',
                params: { userId: article.userInfo.userId },
              }"
            >
              <div class="level-left mx-2 my-2">
                <img
                  :src="article.userInfo.userAvatar"
                  class="user-avatar-article-detail mr-1"
                />
                <span class="ml-2 is-size-4">{{ article.userInfo.username }}</span>
              </div>
            </router-link>
            <span v-if="user.userId != article.userInfo.userId">
              <v-btn
                color="red"
                @click="clickToFollow"
                dark
                rounded
                class="mr-4"
                v-if="followFlag == false"
              >
                <v-icon dark left> mdi-account-multiple-plus </v-icon>关注
              </v-btn>
              <v-btn
                color="red"
                @click="clickToUnfollow"
                outlined
                dark
                rounded
                class="mr-4"
                v-else
              >
                已关注
              </v-btn>
            </span>
          </div>
          <div style="overflow-y: scroll; overflow-x: hidden; height: 600px">
            <!-- <div class="wrapper" ref="wrapper"> -->
            <div>
              <!-- 标题 -->
              <v-card-title>{{ article.articleTitle }}</v-card-title>
              <v-card-text style="white-space: pre-wrap;" class="is-size-6">{{
                article.articleContent
              }}</v-card-text>
              <v-card-text class="py-1">
                <b-taglist>
                  <b-tag
                    rounded
                    type="is-info is-light"
                    v-for="tag in article.tagList"
                    :key="tag.tagId"
                  >
                    <router-link
                      :to="{
                        name: 'articles_of_tag',
                        params: { tagId: tag.tagId },
                      }"
                      ><span class="has-text-info">#{{ tag.tagName }}</span>
                    </router-link></b-tag
                  >
                </b-taglist>
              </v-card-text>
              <v-card-text class="py-2 has-text-grey"
                >{{ article.createTime }}
              </v-card-text>
              <v-card-text class="my-1 py-1"
                ><v-divider class="my-1"></v-divider
              ></v-card-text>
              <v-card-text class="pt-0 pb-2 has-text-grey"
                >共 {{ article.commentNum }} 条评论</v-card-text
              >
              <v-card-text
                v-for="(comment, index) in article.candUDtoList"
                :key="index"
                class="py-2"
              >
                <!-- 评论用户的头像，名称 -->
                <div style="display: flex" class="mx-2">
                  <!-- 跳转到评论者的个人首页 -->
                  <div>
                    <router-link
                      :to="{
                        name: 'user_info',
                        params: { userId: comment.userId },
                      }"
                    >
                      <img
                        :src="comment.userAvatar"
                        class="comment-user-avatar"
                      />
                    </router-link>
                  </div>
                  <v-card-text
                    class="px-0 py-0 ml-2"
                    @mouseover="
                      checkCurrentUser(comment.userId, comment.commentId)
                    "
                    @mouseleave="clearHover()"
                  >
                    <router-link
                      :to="{
                        name: 'user_info',
                        params: { userId: comment.userId },
                      }"
                      ><span class="is-size-6">{{ comment.userName }}</span>
                    </router-link>

                    <v-card-text class="py-0 px-0">{{
                      comment.comment
                    }}</v-card-text>
                    <p
                      style="display: flex; justify-content: space-between"
                      class="mb-1"
                    >
                      <span class="has-text-grey-light py-2">{{
                        comment.createTime
                      }}</span
                      ><span style="margin-left: auto">
                        <v-btn
                          text
                          icon
                          color="red"
                          @click.native="
                            clickToDeleteComment(comment.commentId)
                          "
                          v-if="
                            isCurrentUser(comment.userId, comment.commentId)
                          "
                          ><v-icon>mdi-trash-can</v-icon></v-btn
                        >
                        <!-- <b-icon
                          @click.native="
                            clickToDeleteComment(comment.commentId)
                          "
                          v-if="
                            isCurrentUser(comment.userId, comment.commentId)
                          "
                          icon="trash-can"
                          type="is-danger"
                        >
                        </b-icon
                      > -->
                      </span>
                    </p>
                    <p>
                      <v-divider class="my-1" light></v-divider>
                    </p>
                  </v-card-text>

                  <!-- <v-card-text class="has-text-right py-0">{{ comment.createTime }}</v-card-text>
                  <v-card-text class="py-0" style="margin-left:28px"><v-divider class="my-1"></v-divider></v-card-text> -->
                </div>
              </v-card-text>
            </div>
          </div>
          <el-divider class="my-0"></el-divider>
          <v-card-text class="py-1"
            ><span
              ><v-btn
                text
                icon
                @click="clickToLike"
                v-if="this.likesFlag == false"
                ><v-icon class="is-size-3"
                  >mdi-cards-heart-outline</v-icon
                ></v-btn
              ><v-btn text icon color="red" @click="clickToUnlike" v-else
                ><v-icon class="is-size-3">mdi-heart</v-icon></v-btn
              ><span class="ml-1 mr-3">{{ article.likesNum }}</span></span
            ><span
              ><v-btn
                text
                icon
                @click="clickToCollect"
                v-if="this.collectionFlag == false"
                ><v-icon class="is-size-3">mdi-star-outline</v-icon></v-btn
              ><v-btn
                text
                icon
                color="yellow darken-2"
                @click="clickToUncollect"
                v-else
                ><v-icon class="is-size-3">mdi-star</v-icon></v-btn
              ><span class="ml-1 mr-3">{{ article.collectionNum }}</span></span
            ><span
              ><v-btn text icon
                ><v-icon class="is-size-3" @click="clickToComment"
                  >mdi-chat-processing-outline</v-icon
                ></v-btn
              ><span class="ml-1 mr-3">{{ article.commentNum }}</span></span
            ></v-card-text
          >
          <b-input
            placeholder="说点什么"
            rounded
            icon="chat"
            ref="comment"
            @keyup.enter.native="handleComment"
            v-model="commentText"
            class="mx-2"
          ></b-input>
        </el-col>
      </el-row>
    </v-card>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import {
  getArticleDetail,
  doComment,
  deleteComment,
  doLike,
  undoLike,
  doCollection,
  undoCollection,
  getLACStatus,
} from "@/api/article";
import { doFollow, undoFollow, getFollowStatus } from "@/api/user";
/* import IScroll from "iscroll"; // 普通版 */
export default {
  name: "ArticleModel",
  props: {
    currentArticleId: {
      type: Number,
      required: true,
    },
  },
  data() {
    return {
      article: null,
      scroll: null,
      commentText: "",
      newCommentNum: 0,
      hoveredUserId: null,
      hoveredCommentId: null,
      likesFlag: false,
      collectionFlag: false,
      followFlag: false,
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
    getArticle() {
      getArticleDetail(this.currentArticleId).then((response) => {
        const { data } = response;
        console.log("response", data);
        this.article = data;
        console.log("this.article.candUDtoList", this.article.candUDtoList);
        this.article.createTime = this.formatDate1(this.article.createTime);
        for (var i = 0; i < this.article.candUDtoList.length; i++) {
          this.article.candUDtoList[i].createTime = this.formatDate2(
            this.article.candUDtoList[i].createTime
          );
        }
        /* this.article.candUDtoList = this.article.candUDtoList.sort((a, b) => {
          const dateA = new Date(a);
          const dateB = new Date(b);
          return dateB - dateA;
        }); */
        console.log("处理后的文章详细内容：", this.article);
        //查看关注，点赞，收藏状态
        this.getStatus(
          this.user.userId,
          this.article.userInfo.userId,
          this.article.articleId
        );
      });
    },
    //查看关注，点赞，收藏状态
    getStatus(userId, fUserId, articleId) {
      getFollowStatus(userId, fUserId).then((response) => {
        const { data } = response;
        this.followFlag = data;
        console.log("查看当前关注状态：", this.followFlag);
      });
      getLACStatus(userId, articleId).then((response) => {
        const { data } = response;
        this.likesFlag = data.like;
        this.collectionFlag = data.collection;
        console.log("查看当前点赞、收藏状态：", this.followFlag);
      });
    },
    handleComment() {
      const commentData = {
        userId: this.user.userId,
        articleId: this.currentArticleId,
        comment: this.commentText,
        createTime: this.getNowTime(),
      };
      console.log(commentData);
      doComment(commentData).then((response) => {
        this.commentText = "";
        this.$message({
          message: "评论成功",
          type: "success",
          duration: 2000,
        });
        this.newCommentNum++;
      });
    },
    clickToComment() {
      this.$refs.comment.focus();
    },
    clickToDeleteComment(selectedCommentId) {
      const comment = {
        commentId: selectedCommentId,
      };
      console.log("删除的评论：", comment);
      deleteComment(comment).then((response) => {
        this.$message({
          message: "删除评论成功",
          type: "success",
          duration: 2000,
        });
        this.newCommentNum--;
      });
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
    checkCurrentUser(userId, commentId) {
      this.hoveredUserId = userId;
      this.hoveredCommentId = commentId;
      console.log(
        "鼠标悬浮的评论用户ID和评论ID",
        this.hoveredUserId,
        this.hoveredCommentId
      );
    },
    isCurrentUser(userId, commentId) {
      return (
        this.hoveredUserId === userId &&
        this.user.userId === userId &&
        this.hoveredCommentId === commentId
      );
    },
    clearHover(){
      this.hoveredUserId=null;
      this.hoveredCommentId=null;
    },
    clickToFollow() {
      const data = {
        userId: this.user.userId,
        followingUserId: this.article.userInfo.userId,
      };
      doFollow(data).then((response) => {
        this.$message({
          message: "关注成功",
          type: "success",
          duration: 2000,
        });
        this.followFlag = true;
      });
    },
    clickToUnfollow() {
      const data = {
        userId: this.user.userId,
        followingUserId: this.article.userInfo.userId,
      };
      undoFollow(data).then((response) => {
        this.$message({
          message: "已取消关注",
          type: "success",
          duration: 2000,
        });
        this.followFlag = false;
      });
    },
    clickToLike() {
      const like = {
        userId: this.user.userId,
        articleId: this.currentArticleId,
      };
      console.log("点赞信息：", like);
      doLike(like).then((response) => {
        this.$message({
          message: "点赞成功",
          type: "success",
          duration: 2000,
        });
      });
      this.likesFlag = true;
    },
    clickToUnlike() {
      const unlike = {
        userId: this.user.userId,
        articleId: this.currentArticleId,
      };
      console.log("取消点赞信息：", unlike);
      undoLike(unlike).then((response) => {
        this.$message({
          message: "已取消收藏",
          type: "success",
          duration: 2000,
        });
      });
      this.likesFlag = false;
    },
    clickToCollect() {
      const collection = {
        userId: this.user.userId,
        articleId: this.currentArticleId,
      };
      console.log("收藏信息：", collection);
      doCollection(collection).then((response) => {
        this.$message({
          message: "收藏成功",
          type: "success",
          duration: 2000,
        });
      });
      this.collectionFlag = true;
    },
    clickToUncollect() {
      const uncollection = {
        userId: this.user.userId,
        articleId: this.currentArticleId,
      };
      console.log("取消收藏信息：", uncollection);
      undoCollection(uncollection).then((response) => {
        this.$message({
          message: "已取消收藏",
          type: "success",
          duration: 2000,
        });
      });
      this.collectionFlag = false;
    },
    //向父组件传值
    sendValuesToParent() {
      const values = {
        Lflag: this.likesFlag,
        Cflag: this.collectionFlag,
      }; // 将多个值封装到对象中
      this.$emit("getLACstatus", values); // 触发自定义事件，并传递值对象
      console.log("子组件ArticleModel向父组件传值",values)
    },
  },
  computed: {
    ...mapGetters(["user", "token"]),
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
      this.sendValuesToParent();
    },
    collectionFlag(val) {
      console.log("collectionFlag", this.collectionFlag);
      this.getArticle();
      this.sendValuesToParent();
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
  max-width:100%
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