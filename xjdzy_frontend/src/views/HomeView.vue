<template>
  <div>
    <section>
      <!-- 标签栏 -->
      <!--<b-tabs size="is-medium" v-model="activeTab" class="mt-3">
        <template v-for="(category, index) in categoryNames"
          >
          <b-tab-item :key="index" :label="category">
            <article-list :articles="dividedArticles[index]"></article-list>
          </b-tab-item>
        </template>
      </b-tabs>-->
      <v-tabs v-model="activeTab">
        <v-tabs-slider color="purple darken-3"></v-tabs-slider>
        <v-tab
          class="is-size-6"
          v-for="(category, index) in categoryNames"
          :key="index"
          :href="`#${category}`"
        >
          {{ category }}
        </v-tab>
      </v-tabs>
    </section>
    <v-tabs-items class="mt-3" v-model="activeTab">
      <v-tab-item
        v-for="(category, index) in categoryNames"
        :key="index"
        :value="`${category}`"
      >
        <article-list :articles="dividedArticles[index]"></article-list>
      </v-tab-item>
    </v-tabs-items>
  </div>
</template>

<script>
// @ is an alias to /src

import { getAllArticles, getCategories } from "@/api/article";
import ArticleList from "@/components/Article/ArticleList.vue";
export default {
  name: "HomeView",
  components: {
    //声明组件
    ArticleList,
  },
  data() {
    return {
      tab: null,
      billboard: "版本更新",
      activeTab: null, //当前选中的标签栏
      articles: [],
      categories: [], //分类数组：存放【{ID，分类名}】
      categoryNames: [],
      dividedArticles: [], //分好类的文章
      categoryId: this.$route.params.categoryId,
    };
  },
  mounted() {
    this.init();
    
  },
  watch: {
    categoryId(val) {
      for (var i = 0; i < this.categories; i++) {
        if (this.categories[i].categoryId == val) {
          this.activeTab = this.categories[i].categoryName;
          console.log("路由收到的参数：", this.activeTab);
        }
      }
    },
  },
  methods: {
    goToCategory(){
      for (var i = 0; i < this.categories.length; i++) {
          if (this.categories[i].categoryId == this.categoryId) {
            this.activeTab = this.categories[i].categoryName;
            console.log("路由收到的参数：", this.activeTab);
          }
        }
    },
    init() {
      console.log("路由收到的参数:", this.$route.params);
      console.log("需要显示的分类ID", this.categoryId);

      getCategories().then((response) => {
        const { data } = response;
        this.categories = data;
        console.log("所有分类：", this.categories);
        this.categoryNames = this.categories.map(
          (category) => category.categoryName
        );
        console.log("分类名数组：", this.categoryNames);
        this.goToCategory();
      });

      getAllArticles().then((response) => {
        const { data } = response;
        this.articles = data;
        console.log("所有文章：", this.articles);
        //将所有文章按照分类进行划分成数组
        for (var i = 0; i < this.categories.length; i++) {
          this.dividedArticles.push(
            this.articles.filter(
              (article) =>
                article.category.categoryId == this.categories[i].categoryId
            )
          );
        }
        console.log("划分好的数组：", this.dividedArticles);
        
      });
    },
  },
};
</script>