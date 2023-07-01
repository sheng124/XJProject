<template>
  <div>
    <div class="container">
    <b-tag icon="tag" class="is-size-3 mb-4 is-primary is-light"> {{tagName}}</b-tag>
    <el-divider></el-divider>
    <article-list :articles="articles"></article-list>
  </div>
  </div>
</template>

<script>
import ArticleList from "@/components/Article/ArticleList.vue"
import {getArticlesByTagId} from "@/api/article"
export default {
  name:"ArticlesOfTag",
  components: {
    //声明组件
    ArticleList,
  },
  data(){
    return{
      tagId:this.$route.params.tagId,
      tagName:null,
      articles:[],
    }
  },
  mounted(){
    this.init();
  },
  methods:{
    init(){
      getArticlesByTagId(this.tagId).then((response=>{
        const {data}=response;
        this.articles=data;
        console.log("根据tagId获取到的文章:",this.articles)
        console.log(this.articles[0].tagList.filter(tag=>tag.tagId == this.tagId))
        let tag=this.articles[0].tagList.filter(tag=>tag.tagId == this.tagId)
        this.tagName=tag[0].tagName
        console.log("当前标签：",this.tagName)
      }))
    }
  }

}
</script>

<style>

</style>