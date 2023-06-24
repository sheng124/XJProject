import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import '@/permission'

// Buefy
import Buefy from 'buefy'
import 'buefy/dist/buefy.css'//导入样式
// ElementUI
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';//导入样式

Vue.use(Buefy)//引用，之后可以在所有地方使用Buefy、ElementUI
Vue.use(ElementUI);

Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
