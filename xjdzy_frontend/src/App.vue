<template>
  <v-app>
    <div class="mb-5">
      <Header></Header>
    </div>
    <div class="container is-fluid context">
        <router-view :key="this.$route.fullPath"></router-view>
    </div>
  </v-app>
</template>

<script>
import Header from "@/components/Layout/Header";

export default {
  name: "App",
  components: { Header },
};
</script>

<style>
</style>
