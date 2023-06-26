import request from '@/utils/request'

// 获取用户关注数、粉丝数、获赞与收藏数
export function getUserData(userId) {
  return request({
    url: `/user/data/${userId}`,
    method: 'get',
  })
}

//修改用户名
export function modifyUsername(form) {
  return request({
    url: '/user/username',
    method: 'post',
    data:form
  })
}
//修改密码
export function modifyPassword(form) {
  return request({
    url: '/user/password',
    method: 'post',
    data:form
  })
}


// 上传、修改头像
export function uploadUserAvatar(formData) {
  return request({
    url: '/user/avatar',
    method: 'post',
    data:formData,
  })
}

// 发布文章
export function userPublishArticle(formData) {
  return request({
    url: '/user/articles',
    method: 'post',
    data:formData,
  })
}


