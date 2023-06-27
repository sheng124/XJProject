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

