import request from '@/utils/request'

// 获取所有类别
export function getCategories() {
  return request({
    url: '/categories',
    method: 'get',
  })
}

// 获取所有类别
export function getTags() {
  return request({
    url: '/articles/tags',
    method: 'get',
  })
}

//增加标签
export function increaseTag(tagName) {
  return request({
    url: '/articles/tags',
    method: 'post',
    data:{
      tagName:tagName
    }
  })
}

//获取笔记详细内容
export function getArticleDetail(articleId) {
  return request({
    url: `/articles/info/${articleId}`,
    method: 'get',
  })
}

//评论
export function doComment(data) {
  return request({
    url: '/articles/comment',
    method: 'post',
    data:data
  })
}
//删除评论
export function deleteComment(comment) {
  return request({
    url: '/articles/unComment',
    method: 'delete',
    data:comment
  })
}
//点赞
export function doLike(likeInfo) {
  return request({
    url: '/articles/like',
    method: 'post',
    data:likeInfo
  })
}
//取消点赞
export function undoLike(unlikeInfo) {
  return request({
    url: '/articles/unlike',
    method: 'delete',
    data:unlikeInfo
  })
}
//收藏
export function doCollection(collectionInfo) {
  return request({
    url: '/articles/collection',
    method: 'post',
    data:collectionInfo
  })
}
//取消收藏
export function undoCollection(uncollectionInfo) {
  return request({
    url: '/articles/unCollection',
    method: 'delete',
    data:uncollectionInfo
  })
}

//获取点赞、收藏状态
export function getLACStatus(userId,articleId) {
  return request({
    url: `/articles/getLACStatus/${userId}/${articleId}`,
    method: 'get',
  })
}

//根据标签ID获取文章列表
export function getArticlesByTagId(tagId) {
  return request({
    url: `/articles/tag/${tagId}`,
    method: 'get',
  })
}

//获取所有文章
export function getAllArticles(){
  return request({
    url: '/articles',
    method: 'get',
  })
}

//搜索
export function search(keyword){
  return request({
    url:'/articles/search',
    method:'get',
    params:{
      keyword:keyword,
    }
  })
}