<template>
  <div>
    聊天界面
  </div>
</template>

<script>
export default {
  name: "ChatWindow",
  props: {

  },
  data() {
    return {
      MessageList:[],
    };
  },
  computed: {
    ...mapGetters(["user", "token"]),
  },
  created() {
  },
  
}
</script>

<style>

</style>