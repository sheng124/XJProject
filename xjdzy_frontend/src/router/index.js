import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'home',
    component: () => import('@/views/HomeView'),
    meta: { title: '首页' }
  },
  // 聊天室
  {
    path: '/chat',
    name: 'chat',
    component: () => import('@/views/Chat')
  },
  // 登录
  {
    path: '/login',
    name: 'login',
    component: () => import('@/views/auth/Login'),
    meta: { title: '登录' }
  },
  //注册
  {
    path: '/register',
    name: 'register',
    component: () => import('@/views/auth/Register'),
    meta: { title: '注册' }
  },
  //个人中心
  {
    path: '/user/info/:userId',
    name: 'user_info',
    component: () => import('@/views/user/UserInfo'),
    meta: { title: '个人中心' }
  },
  //发布中心
  {
    path: '/user/publish',
    name: 'publish_center',
    component: () => import('@/views/user/PublishCenter'),
    meta: { title: '发布中心' }
  },
  // 分类文章列表
  {
    path: '/articles/category/:categoryId',
    name: 'articles_of_category',
    component: () => import('@/views/HomeView'),
    meta: { title: '分类文章' }
  },
  // 搜索文章
  {
    path: '/articles/search/:articleId',
    name: 'articles_of_search',
    component: () => import('@/views/article/ArticlesOfSearch'),
    meta: { title: '分类文章' }
  },
  // 标签文章列表
  {
    path: '/articles/tag/:tagId',
    name: 'articles_of_tag',
    component: () => import('@/views/article/ArticlesOfTag'),
    meta: { title: '标签文章' }
  },
  {
    path: '/404',
    name: '404',
    component: () => import('@/views/error/404'),
    meta: { title: '404-NotFound' }
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
