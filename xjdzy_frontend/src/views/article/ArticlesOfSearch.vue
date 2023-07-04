<template>
  <div>
    <section>
      <v-tabs v-model="activeTab">
        <v-tabs-slider color="purple darken-3"></v-tabs-slider>
        <v-tab class="is-size-6" href="#笔记">
          {{ category }}
        </v-tab>
      </v-tabs>
    </section>
    <v-tabs-items class="mt-3" v-model="activeTab">
      <v-tab-item value="笔记">
        <article-list :articles="dividedArticles[index]"></article-list>
      </v-tab-item>
    </v-tabs-items>
  </div>
</template>

<script>
import ArticleList from "@/components/Article/ArticleList.vue";
export default {
  name: "ArticlesOfSearch",
  components: {
    //声明组件
    ArticleList,
  },
  data() {
    return {
      activeTab: 1,
      searchArticleList: this.$route.params.searchArticleList,
      articleList: [],
    };
  },
};
</script>

<style>
</style>