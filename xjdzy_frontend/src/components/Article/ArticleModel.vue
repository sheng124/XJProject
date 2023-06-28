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
            <b-carousel-item>
              <figure class="image is-2by3">
                <iframe
                  class="has-ratio"
                  width="100%"
                  src="http://121.36.202.123:8080/assert/d27794096e604201bd2b9511e8196345.MP4"
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
                params: { userId: user.userId },
              }"
            >
              <div class="level-left mx-2 my-2">
                <img
                  :src="user.userAvatar"
                  class="user-avatar-article-detail mr-1"
                />
                <span class="ml-2">{{ user.username }}</span>
              </div>
            </router-link>
            <b-button type="is-danger" rounded class="mr-2">关注</b-button>
          </div>
          <!-- 标题 -->
          <v-card-title>{{ article.articleTitle }}</v-card-title>
          <v-card-text class="is-size-6">{{
            article.articleContent
          }}</v-card-text>
          <v-card-text>
            <b-taglist>
              <b-tag
                rounded
                type="is-info is-light"
                v-for="tag in article.tagList"
                :key="tag.tagId"
              >
                <router-link
                  :to="{
                    name: 'tag',
                    params: { tagId: tag.tagId },
                  }"
                  ><span class="has-text-info">#{{ tag.tagName }}</span>
                </router-link></b-tag
              >
            </b-taglist>
          </v-card-text>
          <v-card-text>{{ article.createTime }} </v-card-text>
          <!-- <v-divider inset></v-divider> -->
          <v-divider class="my-1" style="width:90%;margin-left: 5%;"></v-divider>
        </el-col>
      </el-row>
    </v-card>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { getArticleDetail } from "@/api/article";
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
      carousels: [
        { text: "Slide 1", color: "primary" },
        { text: "Slide 2", color: "info" },
        { text: "Slide 3", color: "success" },
        { text: "Slide 4", color: "warning" },
        { text: "Slide 5", color: "danger" },
      ],
      article: null,
    };
  },
  methods: {
    formatDate(dateString) {
      const datePart = dateString.split("T")[0];
      return datePart;
    },
    getArticle() {
      getArticleDetail(this.currentArticleId).then((response) => {
        const { data } = response;
        this.article = data;
        this.article.createTime = this.formatDate(this.article.createTime);
        console.log("获取文章详细内容：", this.article);
      });
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
  },
  mounted() {
    this.getArticle();
  },
};
</script>

<style>
.user-avatar-article-detail {
  cursor: pointer;
  border-radius: 50%;
  width: 40px; /* 根据需要调整头像的宽度 */
  height: 40px; /* 根据需要调整头像的高度 */
}
.image-container {
  height: 700px;
  display: flex;
  justify-content: center;
  align-items: center;
}
.responsive-image {
  width: 100%;
  height: auto;
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
</style>