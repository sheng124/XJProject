<template>
  <div>
    <!-- 文章内容 -->
    <v-card elevation="1" shaped max-width="1000px">
      <div class="columns">
        <div class="column">
          <b-carousel>
        <b-carousel-item v-for="(carousel, i) in carousels" :key="i">
            <section :class="`hero is-medium is-${carousel.color}`">
                <div class="hero-body has-text-centered">
                    <h1 class="title">{{carousel.text}}</h1>
                </div>
            </section>
        </b-carousel-item>
    </b-carousel>
        </div>
        <div class="column">

        </div>
        
      </div>
    </v-card>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
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
                { text: 'Slide 1', color: 'primary' },
                { text: 'Slide 2', color: 'info' },
                { text: 'Slide 3', color: 'success' },
                { text: 'Slide 4', color: 'warning' },
                { text: 'Slide 5', color: 'danger' }
            ]
    };
  },
  methods: {},
  computed: {
    ...mapGetters(["user", "token"]),
  },
  watch: {
    // 监听Theme模式
    currentArticleId(val) {
      console.log("currentArticleId", this.currentArticleId);
    },
  },
  mounted() {},
};
</script>

<style>
</style>