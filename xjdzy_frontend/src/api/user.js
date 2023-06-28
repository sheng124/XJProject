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

//获取已发布、已收藏、已点赞文章
export function getWrLiCoArticles(userId) {
  return request({
    url: `/user/articles/${userId}`,
    method: 'get',
  })
}

//上传视频
export function uploadVideo(formData,fn) {
  return request({
    url: '/user/uploadVideo',
    method: 'post',
    data:formData,
    onUploadProgress: fn, // `onUploadProgress` 允许为上传处理进度事件
    //onDownloadProgress: fn
  })
}
