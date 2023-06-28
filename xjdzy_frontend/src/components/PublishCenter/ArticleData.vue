<!--
 * @Author: wyh 1370804207@qq.com
 * @Date: 2023-06-27 20:04:11
 * @LastEditors: wyh 1370804207@qq.com
 * @LastEditTime: 2023-06-28 15:38:17
 * @FilePath: \xjdzy_frontend\src\components\PublishCenter\ArticleData.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template>
  <div>
    <el-card class="column is-10 is-offset-1" style="margin-bottom: 20px;">
        <div class="avatar-wrapper">
          <img :src="user.userAvatar" class="user-avatar" />
          <i class="el-icon-caret-bottom" />
        </div>
    </el-card>

    <el-card class="column is-10 is-offset-1">
      <p class="is-size-4 has-text-weight-bold mb-4">数据总览</p>

      <div class="biaoqian1">
        <el-card class="biaoqian2">
          <p class="title1">笔记访问量</p>
          <p class="data1">{{allViewsNum}}</p>
        </el-card>
        <el-card class="biaoqian2">
          <p class="title1">新增粉丝</p>
          <p class="data1">{{value1}}</p>
        </el-card>
        <el-card class="biaoqian2">
          <p class="title1">新增粉丝</p>
          <p class="data1">{{value1}}</p>
        </el-card>
        <el-card class="biaoqian2">
          <p class="title1">新增粉丝</p>
          <p class="data1">{{value1}}</p>
        </el-card>
      </div>


      <p class="is-size-4 has-text-weight-bold mb-4" style="margin-top: 30px;">最新发布文章数据</p>
    
    </el-card>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { getUserData,getWrLiCoArticles } from "@/api/user"
export default {
  data() {
      return {
        value1: 2222,
        wrArticles: [],
        allViewsNum:0,
      };
    },
    computed: {
    ...mapGetters(["token", "user"]),
  },
  mounted() {
    this.init();
  },
  methods:{
    init(){
      getWrLiCoArticles(this.user.userId).then((response) => {
        const { data } = response;
        console.log("收到的所有相关笔记数据", data, data.length);
        //已发布：1，已收藏:2，已喜欢：3
        for(var i=0;i<data.length;i++){
            console.log("笔记"+i+"的类型为：",data[i].type)
            if(data[i].type==1){
                this.wrArticles.push(data[i]);
                this.allViewsNum=this.allViewsNum+data[i].viewsNum;
                console.log("笔记的访问量为：",data[i].viewsNum);
            }      
        }
      });
    },
  }
}
</script>

<style>
 .biaoqian1{
    text-align: center;
    width: 100%;
  }
  .biaoqian2{
    display:inline-block;
    background-color: #FBFBFB	 ;
    
    margin-left: 10px;
    width:23%;
    height: 115px;
  }
  .title1{
    font-size: 16px;
    font-family: "Hiragino Sans GB";
    color: #959595;
  }
  .data1{
    font-size: 30px;
    font-family: "San Francisco UI";
    margin-top: 7px;
  }
  .avatar-wrapper {
  margin-top: 5px;
  position: relative;
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
</style>