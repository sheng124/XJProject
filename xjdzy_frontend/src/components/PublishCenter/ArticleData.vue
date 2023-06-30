<template>
  <div>
    笔记数据
    <el-button @click="edit">编辑</el-button>
  </div>
</template>

<script>
export default {
  name: "ArticleData",

  data() {
    return {
      selectToEditArticleId: 2,
    };
  },
  methods: {
    edit() {
      this.$store.commit(
        "user/setEditArticleIdState",
        this.selectToEditArticleId
      );//需要向父组件传值
    },
  },
};
</script>

<style>
</style>