import request from '@/utils/request'

//获取用户信息
export function getUserInfo(userId) {
  return request({
    url: `/user/info/${userId}`,
    method: 'get',
  })
}
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
// 修改文章
export function userEditArticle(data) {
  return request({
    url: '/user/articles/edit',
    method: 'post',
    data:data,
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
//上传图片
export function uploadImage(formData) {
  return request({
    url: '/user/uploadImage',
    method: 'post',
    data:formData,
  })
}

//关注
export function doFollow(data) {
  return request({
    url: '/user/follow',
    method: 'post',
    data:data,
  })
}

//取消关注
export function undoFollow(data) {
  return request({
    url: '/user/unfollow',
    method: 'delete',
    data:data,
  })
}

//查看是否关注
export function getFollowStatus(userId,fUserId) {
  return request({
    url: `/user/getFollowStatus/${userId}/${fUserId}`,
    method: 'get',
  })
}