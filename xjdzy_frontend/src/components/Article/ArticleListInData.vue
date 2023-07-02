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
            <div
              style="padding: 14px; cursor: pointer"
              @mouseover="getHoverArticle(article.articleId)"
              @mouseleave="hoveredArticleId = null"
            >
              <div
                class="mb-2 has-text-black has-text-weight-semibold"
                @click="openArticleDialog(article.articleId)"
              >
                {{ article.articleTitle }}
              </div>
              <!-- 笔记作者头像，名称 -->

              <div class="level-left">
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
                  <span class="my-2">{{ article.userInfo.username }}</span>
                </div>
                </router-link>
                <v-btn
                  text
                  icon
                  @click="edit(article.articleId)"
                  v-show="hoveredArticleId == article.articleId"
                  ><v-icon>mdi-pencil-circle</v-icon></v-btn
                >
                <el-popconfirm title="确定要删除这篇笔记吗？" @confirm="clickToDelete(article.articleId)">
                <v-btn
                slot="reference"
                  text
                  icon
                  v-show="hoveredArticleId == article.articleId"
                  ><v-icon>mdi-delete-circle</v-icon></v-btn
                ></el-popconfirm>
              </div>

              <!-- 发表时间 -->
              <time class="time"
                ><v-icon small class="mb-1 mr-1">mdi-clock-outline</v-icon
                >{{ article.createTime }}</time
              >
              <!-- 笔记分类 -->
              <router-link
                :to="{
                  name: 'articles_of_category',
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
                      name: 'articles_of_tag',
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
import ArticleModel from "@/components/Article/ArticleModel.vue"; //导入子组件文章模型
import {deleteArticle} from "@/api/user"
export default {
  name: "ArticleList",
  components: {
    //声明组件
    ArticleModel,
  },
  computed: {
    ...mapGetters(["token", "user", "editArticleId","deleteArticleId"]),
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
      //是否显示编辑、删除图标
      hoveredArticleId: null,
    };
  },
  created() {
    this.init();
  },
  watch: {
    articles(val) {
      console.log("子组件更新当前文章数组:", this.articles);
      this.init();
    },
  },
  methods: {
    reset() {
      this.articles = [];
      this.divideArticles = [];
    },
    init() {
      console.log();
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
    //编辑函数
    edit(selectToEditArticleId) {
      console.log("点击edit按钮后，即将要编辑的笔记：", selectToEditArticleId);
      console.log("edit函数commit mutations前，editArticleId的值：",this.editArticleId)
      this.$store.commit("user/setEditArticleIdState", selectToEditArticleId); //需要向父组件传值
      console.log("edit函数commit mutations后，editArticleId的值：", this.editArticleId)
    },
    getHoverArticle(articleId) {
      this.hoveredArticleId = articleId;
    },
    clickToDelete(articleId){
      console.log("要删除的笔记ID：",articleId)
      deleteArticle(articleId).then((response)=>{
        this.$message({
          message: "删除笔记成功",
          type: "success",
          duration: 2000,
        });
        console.log("clickToDelete函数commit mutations前，deleteArticleId的值：",this.deleteArticleId)
        this.$store.commit("user/setDeleteArticleIdState", articleId);
        console.log("clickToDelete函数commit mutations后，deleteArticleId的值：", this.deleteArticleId)
      })
    }
  },
};
</script>

<style>
.time {
  font-size: 13px;
  color: #999;
}
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
.user-avatar-article {
  cursor: pointer;
  border-radius: 50%;
  width: 20px; /* 根据需要调整头像的宽度 */
  height: 20px; /* 根据需要调整头像的高度 */
}
</style>