<template>
  <div>
    <section>
      <div class="columns is-desktop">
        <div
          class="column"
          v-for="(columnArticles, index1) in divideArticles"
          :key="index1"
        >
          <!-- 关于笔记作者相关的路由还需更改 -->
          <el-card
            :body-style="{ padding: '0px' }"
            v-for="(article, index2) in columnArticles"
            :key="index2"
            class="my-2"
            style="border-radius: 20px"
          >
            <!-- 笔记封面 -->
            <!-- 可以试着采用类似小红书，点击笔记，弹出窗口显示文章内容 -->
            <div style="cursor: pointer">
              <v-hover v-slot="{ hover }">
                <img
                  :src="article.articleCover"
                  :class="{ 'image-hover': hover, image: !hover }"
                  @click="openArticleDialog(article.articleId)"
                />
              </v-hover>
            </div>
            <div style="padding: 14px; cursor: pointer">
              <div
                class="mb-2 has-text-black has-text-weight-semibold"
                @click="openArticleDialog(article.articleId)"
              >
                {{ article.articleTitle }}
              </div>
              <!-- 笔记作者头像，名称 -->
              <router-link
                :to="{
                  name: 'user_info',
                  params: { userId: article.userInfo.userId },
                }"
              >
                <div class="level-left">
                  <img
                    :src="article.userInfo.userAvatar"
                    class="user-avatar-article mr-1"
                  />
                  {{ article.userInfo.username }}
                </div>
              </router-link>
              <!-- 发表时间 -->
              <time class="time"
                ><v-icon small class="mb-1 mr-1">mdi-clock-outline</v-icon
                >{{ article.createTime }}</time
              >
              <!-- 笔记分类 -->
              <router-link
                :to="{
                  name: 'categories',
                  params: {
                    categoryId: article.category.categoryId,
                  },
                }"
                style="float: right"
              >
                <v-icon>mdi-bookmark</v-icon>
                {{ article.category.categoryName }}
              </router-link>
              <!-- 笔记标签 -->
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
            </div>
          </el-card>
        </div>
      </div>
    </section>
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
          @getLACstatus="handleChildValuesChange"
        ></article-model>
      </v-dialog>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
export default {
  name: "ArticleList",
  computed: {
    ...mapGetters(["token", "user"]),
  },
  props: {
    //从父组件收到的文章数组
    articles: {
      type: Array,
      required: true,
    },
  },
  data() {
    return {
      divideArticles: [], //所有笔记分为4列后的数组
      //选择的文章
      selectedArticleId: -1, //选择查看的文章ID
      selectedArticleVisible: false, //查看文章详细内容的对话框
      //点赞、收藏状态
      LACstatus: {},
      //当前用户ID
      currentUserId: this.$route.params.userId,
      followFlag: false, //当前个人首页的用户是否是我关注的
    };
  },
  mounted() {
    this.init();
  },
  methods: {
    reset() {
      this.articles = [];
      this.divideArticles = [];
    },
    init() {
      for (var i = 0; i < this.articles.length; i++) {
        this.articles[i].createTime = this.replaceTWithSpace(
          this.articles[i].createTime
        );
      }
      console.log("处理好时间的文章数组：", this.articles);
      this.divideArticles = this.divideArticle2(this.articles);
    },
    divideArticle2(articles) {
      // 定义目标数组数量
      const numArrays = 4;
      // 创建目标数组
      const targetArrays = Array.from({ length: numArrays }, () => []);
      // 分配文章到目标数组
      for (var i = 0; i < articles.length; i++) {
        if (i % numArrays == 0) {
          targetArrays[0].push(articles[i]);
        }
        if (i % numArrays == 1) {
          targetArrays[1].push(articles[i]);
        }
        if (i % numArrays == 2) {
          targetArrays[2].push(articles[i]);
        }
        if (i % numArrays == 3) {
          targetArrays[3].push(articles[i]);
        }
      }
      // 打印结果
      targetArrays.forEach((arr, index) => {
        console.log(`数组${index + 1}:`, arr);
      });
      return targetArrays;
    },
    replaceTWithSpace(str) {
      return str.replace("T", " ");
    },
    //打开文章弹窗
    openArticleDialog(articleId) {
      this.selectedArticleVisible = true;
      this.selectedArticleId = articleId;
      console.log(
        "打开文章详细内容对话框：",
        this.selectedArticleVisible,
        this.selectedArticleId
      );
    },
    closeArticleDialog() {
      this.electedArticleVisible = false;
      this.selectedArticleId = null;
    },
    //处理文章详情子组件传过来的值
    handleChildValuesChange(data) {
      this.LACstatus = data; // 接收子组件传递的值对象
      console.log("父组件收到的值", this.LACstatus);
    },
  },
};
</script>

<style>
.image {
  width: 100%;
  display: block;
}
.image-hover {
  /* 鼠标悬停时的样式 */
  /* 比如可以调整图片亮度、透明度等 */
  filter: brightness(80%);
  width: 100%;
  display: block;
}
</style>