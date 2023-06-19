import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'home',
    component: ()=>import('@/views/HomeView')
  },
    // 登录
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/auth/Login'),
      meta: { title: '登录' }
    },
  {
    path: '/register',
    name: 'register',
    component: () => import('@/views/auth/Register'),
    meta: { title: '注册' }
  },
  {
    path: '/404',
    name: '404',
    component: () => import('@/views/error/404'),
    meta: {title: '404-NotFound'}
  },
  {
    path: '*',	//如果用户输了除上面这几个url的路由，就重定向到404.vue
    redirect: '/404',
    hidden: true
  }
]

const router = new VueRouter({
  routes
})

export default router
