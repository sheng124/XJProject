<template>
  <div>
    <div class="box">🔔{{billboard}}</div>
  </div>
</template>

<script>
// @ is an alias to /src


export default {
  name: 'HomeView',
  components: {
  },
  data(){
    return{
      billboard:'版本更新'
    }
  }
}
</script>
