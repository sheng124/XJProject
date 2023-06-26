<template>
  <div class="container">
    文章列表
  </div>
</template>

<script>
import { mapGetters } from "vuex";
export default {
  name: "ArticleList",

  computed: {
    ...mapGetters(["token", "user"]),
  }
}
</script>

<style>

</style>