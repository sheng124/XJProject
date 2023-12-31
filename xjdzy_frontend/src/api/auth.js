import request from '@/utils/request'

// 前台用户注册
export function register(user) {
  return request({
    url: '/register',
    method: 'post',
    data: user
  })
}
// 前台用户登录
export function login(data) {
  return request({
    url: '/login',
    method: 'post',
    data
  })
}

// 登录后获取前台用户信息
export function getUserInfo(userId) {
  return request({
    url: `/user/info/${userId}`,
    method: 'get'
  })
}

// 前台用户退出登录
export function logout() {
  return request({
    url: '/logout',
    method:'delete'
  })
}