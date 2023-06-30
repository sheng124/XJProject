<!--
 * @Author: wyh 1370804207@qq.com
 * @Date: 2023-06-27 20:04:11
 * @LastEditors: wyh 1370804207@qq.com
 * @LastEditTime: 2023-06-28 17:03:32
 * @FilePath: \xjdzy_frontend\src\components\PublishCenter\ArticleData.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template>
  <div>
    <el-card style="margin: 0 3%; margin-bottom: 20px">
      <div class="div1">
        <div class="avatar-wrapper">
          <img :src="user.userAvatar" class="user-avatar" />
        </div>
        <div class="div2">
          <div class="div3">
            <p style="font-size: 19px">{{ userName }}</p>
          </div>
          <div class="div4">
            <div style="margin-left: 2px; position: absolute; width: 50px">
              <p style="font-size: 16px">{{ user_following }} 关注</p>
            </div>
            <div style="margin-left: 60px; position: absolute; width: 50px">
              <p style="font-size: 16px">{{ user_followers }} 粉丝</p>
            </div>
            <div style="margin-left: 120px; position: absolute; width: 150px">
              <p style="font-size: 16px">{{ likesANDcollection }} 获赞与收藏</p>
            </div>
          </div>
        </div>
      </div>
    </el-card>

    <el-card style="margin: 0 1%; margin-bottom: 20px">
      <p class="is-size-4 has-text-weight-bold mb-4">数据总览</p>

      <div class="biaoqian1">
        <el-card class="biaoqian2">
          <p class="title1">发表文章总数</p>
          <p class="data1">{{ allArticleNum }}</p>
        </el-card>
        <el-card class="biaoqian2">
          <p class="title1">文章访问量</p>
          <p class="data1">{{ allViewsNum }}</p>
        </el-card>
        <el-card class="biaoqian2">
          <p class="title1">文章点赞数</p>
          <p class="data1">{{ allLikesNum }}</p>
        </el-card>
        <el-card class="biaoqian2">
          <p class="title1">文章收藏数</p>
          <p class="data1">{{ allCollectionNum }}</p>
        </el-card>
      </div>

      <p class="is-size-4 has-text-weight-bold mb-4" style="margin-top: 30px">
        最新发布文章数据
      </p>
      <article-list-in-data :articles="wrArticles"></article-list-in-data>
    </el-card>
  </div>
</template>

<script>
import ArticleListInData from '@/components/Article/ArticleListInData.vue';
import { getUserData,getWrLiCoArticles } from "@/api/user"
import { mapGetters } from "vuex";

export default {
  components: { ArticleListInData },
  name: "ArticleData",
  data() {
    return {
      //选择要编辑的笔记ID
      //selectToEditArticleId: -1,
      value1: 2222,
      wrArticles: [],
      allArticleNum: 0,
      allViewsNum: 0,
      allLikesNum: 0,
      allCollectionNum: 0,
      userName: null,
      userData: {
        following: 0,
        followers: 0,
        likesNum: 0,
        collectionNum: 0,
      },
      likesANDcollection: 0,
      user_following: 0,
      user_followers: 0,
    };
  },
  computed: {
    ...mapGetters(["token", "user"]),
  },
  mounted() {
    this.init();
  },
  watch: {
    // 监听模式
  },
  methods: {
    init() {
      getUserData(this.user.userId).then((response) => {
        const { data } = response;
        this.userData = data;
        this.likesANDcollection = data.likesNum + data.collectionNum;
        this.user_following = data.following;
        this.user_followers = data.followers;
        this.userName = this.user.username;
      });
      getWrLiCoArticles(this.user.userId).then((response) => {
        const { data } = response;
        console.log("收到的所有相关笔记数据", data, data.length);
        //已发布：1，已收藏:2，已喜欢：3
        for (var i = 0; i < data.length; i++) {
          console.log("笔记" + i + "的类型为：", data[i].type);
          if (data[i].type == 1) {
            //处理数据
            this.wrArticles.push(data[i]);
            this.allArticleNum = this.allArticleNum + 1;
            this.allViewsNum = this.allViewsNum + data[i].viewsNum;
            console.log("笔记的访问量为：", data[i].viewsNum);
            this.allLikesNum = this.allLikesNum + data[i].likesNum;
            this.allCollectionNum =
              this.allCollectionNum + data[i].collectionNum;
          }
        }
      });
    },
    
  },
};
</script>

<style>
.div1 {
  width: 500px;
  height: 80px;
  position: relative;
}
.div2 {
  margin-left: 100px;
  width: 500px;
  position: absolute;
}
.div3 {
  margin-top: 10px;
  position: absolute;
}
.div4 {
  margin-top: 60px;
  position: absolute;
}
.biaoqian1 {
  text-align: center;
  width: 100%;
}
.biaoqian2 {
  display: inline-block;
  background-color: #fbfbfb;

  margin-left: 10px;
  width: 23%;
  height: 115px;
}
.title1 {
  font-size: 16px;
  font-family: "Hiragino Sans GB";
  color: #959595;
}
.data1 {
  font-size: 30px;
  font-family: "San Francisco UI";
  margin-top: 7px;
}

.user-avatar {
  cursor: pointer;
  border-radius: 50%;
  width: 80px; /* 根据需要调整头像的宽度 */
  height: 80px; /* 根据需要调整头像的高度 */
}
.el-icon-caret-bottom {
  cursor: pointer;
  position: absolute;
  right: -20px;
  top: 25px;
  font-size: 12px;
}
.avatar-wrapper {
  margin-top: 5px;
  position: absolute;
}
</style>