<template>
  <div>
    <div class="column is-half is-offset-one-quarter">
      <!-- <el-card>
        <div>
          <img :src="user.userAvatar" class="user-avatar" />
          {{ userData }}
        </div>
      </el-card> -->

      <!-- 用户卡片 -->
      <div class="card" style="display: flex;
  justify-content: center;">
        <div class="card-content" style="justify-content: center">
          <div class="media">
            <div class="media-left">
              <img :src="currentUser.userAvatar" class="user-avatar" />
            </div>
            <div class="media-content ml-3">
              <div class="level">
                <span class="title is-4 mx-4 mt-4">{{
                  currentUser.username
                }}</span>
                <span v-if="this.currentUserId != this.user.userId">
                  <v-btn
                    color="red"
                    @click="clickToFollow"
                    dark
                    rounded
                    class="mr-4"
                    v-if="followFlag == false"
                  >
                    <v-icon dark left> mdi-account-multiple-plus </v-icon>关注
                  </v-btn>
                  <v-btn
                    color="red"
                    @click="clickToUnfollow"
                    outlined
                    dark
                    rounded
                    class="mr-4"
                    v-else
                  >
                    已关注
                  </v-btn>
                </span>
              </div>
              <p>
                <i
                  v-if="this.currentUserId == this.user.userId"
                  class="el-icon-edit ml-4 is-size-7 has-text-grey"
                  style="cursor: pointer"
                  @click="dialogVisible = true"
                  >编辑资料</i
                >
              </p>
              <el-dialog
                title="编辑资料"
                :visible.sync="dialogVisible"
                width="50%"
              >
                <el-form :model="form" :rules="rules" ref="form">
                  <el-form-item label="用户名" prop="username">
                    <el-input
                      v-model="form.username"
                      autocomplete="off"
                    ></el-input>
                  </el-form-item>
                  <el-form-item label="密码" prop="password">
                    <el-input
                      v-model="form.password"
                      autocomplete="off"
                      show-password
                    ></el-input>
                  </el-form-item>
                  <el-form-item label="用户头像" prop="userAvatar">
                    <el-upload
                      class="avatar-uploader"
                      action="none"
                      :auto-upload="false"
                      :on-change="checkType"
                      :show-file-list="false"
                      accept="image/jpeg,image/png,image/jpg"
                    >
                      <img v-if="imageUrl" :src="imageUrl" class="avatar" />
                      <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                  </el-form-item>
                </el-form>
                <span slot="footer" class="dialog-footer">
                  <b-button @click="dialogVisible = false" class="mx-1"
                    >取 消</b-button
                  >
                  <b-button
                    type="is-primary"
                    @click="submitForm('form')"
                    class="mx-1"
                    >确定</b-button
                  >
                </span>
              </el-dialog>
              <p class="subtitle is-6 mt-6">
                <span class="has-text-weight-semibold ml-2"
                  >{{ userData.following }} </span
                ><span class="has-text-weight-light" @click="showgz">关注</span
                ><span class="has-text-weight-semibold ml-2"
                  >{{ userData.followers }} </span
                ><span class="has-text-weight-light" @click="showfs">粉丝</span
                ><span class="has-text-weight-semibold ml-2"
                  >{{ likesANDcollection }} </span
                ><span class="has-text-weight-light">获赞与收藏</span>
              </p>
              <el-dialog
                title="关注列表"
                :visible.sync="gzVisible"
                width="30%"
                center>
                <v-list subheader>
                  <v-list-item
                  v-for="(Onefollowing, index) in FollowingDataList" 
                  :key="index"
                  >
                    <v-list-item-avatar>
                      <v-img
                        :src="Onefollowing.userAvatar"
                      ></v-img>
                    </v-list-item-avatar>

                    <v-list-item-content>
                      <v-list-item-title v-text="Onefollowing.username"></v-list-item-title>
                    </v-list-item-content>

                    <v-list-item-button>
                      111
                    </v-list-item-button>

                  </v-list-item>
                </v-list>
                <!-- <div v-for="(Onefollowing, index) in FollowingDataList" :key="index">
                  <div>
                    <img :src="Onefollowing.userAvatar" class="user-avatar2" />
                  </div>
                  <div>
                    <p style="font-size: 19px">{{ Onefollowing.username }}</p>
                  </div>
                  <div>
                    <button>111</button>
                  </div>
                </div> -->
                <span slot="footer" class="dialog-footer">
                  <el-button type="primary" @click="gzVisible = false">关 闭</el-button>
                </span>
              </el-dialog>
              <el-dialog
                title="粉丝列表"
                :visible.sync="fsVisible"
                width="30%"
                center>
                <v-list subheader>
                  <v-list-item
                  v-for="(Onefollower, index) in FollowerDataList" 
                  :key="index"
                  >
                    <v-list-item-avatar>
                      <v-img
                        :src="Onefollower.userAvatar"
                      ></v-img>
                    </v-list-item-avatar>

                    <v-list-item-content>
                      <v-list-item-title v-text="Onefollower.username"></v-list-item-title>
                    </v-list-item-content>

                    <v-list-item-button>
                      111
                    </v-list-item-button>

                  </v-list-item>
                </v-list>
                <span slot="footer" class="dialog-footer">
                  <el-button type="primary" @click="fsVisible = false">关 闭</el-button>
                </span>
              </el-dialog>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- 此用户发布、收藏、点赞的笔记列表 -->
    <section><!--   -->
      <!-- 标签栏 -->
      <!-- <b-tabs
        type="is-toggle-rounded"
        position="is-centered"
        v-model="activeTab"
        class="mt-3"
      >
        <template v-for="(tab, index) in tabs"
          >
          <b-tab-item :key="index" :label="tab">
            <article-list :articles="allArticles[index]"></article-list>
          </b-tab-item>
        </template>
      </b-tabs> -->
      <v-tabs v-model="activeTab" centered><!--   -->
        <v-tabs-slider color="purple darken-3"></v-tabs-slider>
        <v-tab v-for="(tab, index) in tabs" :key="index">
          {{ tab }}
        </v-tab>
      </v-tabs>
    </section>
    <v-tabs-items class="mt-3" v-model="activeTab">
      <v-tab-item v-for="(tab, index) in tabs" :key="index">
        <article-list :articles="allArticles[index]"></article-list>
      </v-tab-item>
    </v-tabs-items>
  </div>
</template>

<script>
import { mapGetters } from "vuex"; //store的getter修饰器，用来更方便的拿store里放的数据
import {
  getUserInfo,
  getUserData,
  uploadUserAvatar,
  modifyUsername,
  modifyPassword,
  getWrLiCoArticles,
  doFollow,
  undoFollow,
  getFollowStatus,
  getFollowingList,
  getFollowerList,
} from "@/api/user"; //从“/src/api/user.js”导入发送axios请求的函数

import ArticleList from "@/components/Article/ArticleList.vue";
export default {
  name: "UserInfo",
  components: {
    //声明组件
    ArticleList,
  },
  watch: {
    //监听一些数据的变化，以做出一些处理
    LACstatus(val) {
      this.init();
      console.log("收到子组件传过来的值后，更新文章列表");
    },
  },
  data() {
    //声明数据
    return {
      userData: {
        //用户数据：关注数、粉丝数、点赞数、收藏数
        following: 0,
        followers: 0,
        likesNum: 0,
        collectionNum: 0,
      },
      likesANDcollection: 0, //获赞与收藏数
      dialogVisible: false, //编辑资料对话框是否显示
      dialog: false, //测试
      form: {
        //编辑资料的表单
        username: "",
        password: "",
      },
      rules: {
        //表单的验证规则
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" },
          {
            min: 1,
            max: 20,
            message: "长度在 1 到 20 个字符",
            trigger: "blur",
          },
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          {
            min: 1,
            max: 20,
            message: "长度在 1 到 20 个字符",
            trigger: "blur",
          },
        ],
      },
      imageFile: null, //上传头像所需的参数
      imageUrl: "",
      activeTab: "笔记", //当前选中的标签栏
      tabs: ["笔记", "收藏", "点赞"], //标签栏数组：存放【笔记、收藏、点赞】
      wrArticles: [], //已发布笔记数组
      liArticles: [], //已点赞笔记数组
      coArticles: [], //已收藏数组
      allArticles: [],
      //当前用户ID
      currentUserId: this.$route.params.userId,
      currentUser: null,
      followFlag: false, //当前个人首页的用户是否是我关注的
      gzVisible:false,
      fsVisible:false,
      FollowingDataList:[],  //当前用户的关注列表
      FollowerDataList:[],   //当前用户的粉丝列表
    };
  },
  computed: {
    ...mapGetters(["user", "token","Lflag","Cflag"]),
  },
  created() {
    this.init();
  },
  watch: {
    followFlag(val) {
      getUserData(this.currentUserId).then((response) => {
        const { data } = response;
        this.userData = data;
        console.log("获取用户信息", this.userData);
        this.likesANDcollection = data.likesNum + data.collectionNum;
      });
    },
    activeTab(val) {
      console.log("当前选中的标签栏：", val);
      console.log(this.activeTab);
    },
    Lflag(val){
      console.log("监听Lflag的变化",val)
      this.init();
    },
    Cflag(val){
      console.log("监听Cflag的变化",val)
      this.init();
    }
  },
  methods: {
    reset() {
      this.wrArticles = [];
      this.liArticles = [];
      this.coArticles = [];
      this.allArticles = [];
      this.FollowingDataList=[];
      this.FollowerDataList=[];
    },
    processArticle(data) {
      //这里的data就是当前用户所有相关的文章
      console.log("收到的所有该用户相关笔记数据", data, data.length);
      //已发布：1，已收藏:2，已喜欢：3
      for (var i = 0; i < data.length; i++) {
        console.log("笔记" + i + "的类型为：", data[i].type);
        //将文章划分归类
        if (data[i].type == 1) {
          this.wrArticles.push(data[i]);
        } else if (data[i].type == 2) {
          this.coArticles.push(data[i]);
        } else {
          this.liArticles.push(data[i]);
        }
      }
      this.allArticles.push(this.wrArticles);
      this.allArticles.push(this.coArticles);
      this.allArticles.push(this.liArticles);
      // 分类为 type 为 1 的文章
      //this.wrArticles = data.filter((article) => article.type === 1);
      // 分类为 type 为 2 的文章
      //this.coArticles = data.filter((article) => article.type === 2);
      // 分类为 type 为 3 的文章
      //this.liArticles = data.filter((article) => article.type === 3);
      console.log("已分类好的笔记：", this.allArticles);
    },
    init() {
      console.log("当前选中的标签栏：", this.activeTab);
      this.reset();
      console.log("当前用户ID：", this.currentUserId);
      getUserInfo(this.currentUserId).then((response) => {
        const { data } = response;
        this.currentUser = data;
        console.log("当前用户信息：", this.currentUser);
      });
      getFollowStatus(this.user.userId, this.currentUserId).then((response) => {
        const { data } = response;
        this.followFlag = data;
        console.log("查看当前关注状态：", this.followFlag);
      });
      getUserData(this.currentUserId).then((response) => {
        const { data } = response;
        this.userData = data;
        console.log("获取用户信息", this.userData);
        this.likesANDcollection = data.likesNum + data.collectionNum;
        this.form.username = this.user.username;
        this.form.password = this.user.password;
        this.imageUrl = this.user.userAvatar;
      });
      getWrLiCoArticles(this.currentUserId).then((response) => {
        const { data } = response;
        this.processArticle(data);
      });
      getFollowingList(this.currentUserId).then((response) => {
        const { data } = response;
        this.FollowingDataList = data;
        console.log("获取用户关注列表", this.FollowingDataList);
      });
      getFollowerList(this.currentUserId).then((response) => {
        const { data } = response;
        this.FollowerDataList = data;
        console.log("获取用户粉丝列表", this.FollowerDataList);
      });
    },
    // 将所有该用户相关的笔记分为4列
    divideArticle1(articles) {
      // 定义目标数组数量
      const numArrays = 4;
      // 计算每个数组中的文章数量
      const avgCount = Math.floor(articles.length / numArrays);
      const remainder = articles.length % numArrays;
      // 创建目标数组
      const targetArrays = Array.from({ length: numArrays }, () => []);
      // 分配文章到目标数组
      let currentIndex = 0;
      for (let i = 0; i < numArrays; i++) {
        const count = avgCount + (i < remainder ? 1 : 0);
        targetArrays[i] = articles.slice(currentIndex, currentIndex + count);
        currentIndex += count;
      }
      // 打印结果
      targetArrays.forEach((arr, index) => {
        console.log(`数组${index + 1}:`, arr);
      });
      return targetArrays;
    },
    //方法2
    divideArticle2(articles) {
      // 定义目标数组数量
      const numArrays = 4;
      // 创建目标数组
      const targetArrays = Array.from({ length: numArrays }, () => []);
      // 分配文章到目标数组
      for (var i = 0; i < articles.length; i++) {
        if (i % numArrays == 0) {
          targetArrays[0].push(articles[i]);
        }
        if (i % numArrays == 1) {
          targetArrays[1].push(articles[i]);
        }
        if (i % numArrays == 2) {
          targetArrays[2].push(articles[i]);
        }
        if (i % numArrays == 3) {
          targetArrays[3].push(articles[i]);
        }
      }
      // 打印结果
      targetArrays.forEach((arr, index) => {
        console.log(`数组${index + 1}:`, arr);
      });
      return targetArrays;
    },
    //上传头像的On-change函数
    checkType(file, fileList) {
      //截取文件类型
      let fileType = file.name.substring(file.name.lastIndexOf(".") + 1);
      fileList = [];
      if (fileType !== "jpeg" && fileType !== "jpg" && fileType !== "png") {
        this.$message.error("头像格式不正确");
        return false;
      }
      if (file.size / 1024 / 1024 > 2) {
        this.$message.error("上传头像不能超过2MB");
        return false;
      }
      this.imageFile = file;
      this.imageUrl = URL.createObjectURL(this.imageFile.raw);
      console.log(this.imageFile);
      console.log(this.imageUrl);
      this.$forceUpdate();
    },
    //上传头像函数
    uploadAvatar() {
      let formData = new FormData();
      console.log(this.imageFile);
      formData.append("imageFile", this.imageFile.raw);
      uploadUserAvatar(formData).then((response) => {
        const { data } = response;
        this.$message({
          message: "上传头像成功",
          type: "success",
          duration: 2000,
        });
        this.$store.dispatch("user/getInfo");
      });
    },
    //提交编辑资料的表单
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          //this.$store.dispatch("user/getInfo");
          const modifyUsernameForm = {
            userId: this.user.userId,
            username: this.user.username,
            newUserName: this.form.username,
          };
          const modifyPasswordForm = {
            userId: this.user.userId,
            password: this.user.password,
            newPassword: this.form.password,
          };
          if (this.form.username !== this.user.username) {
            modifyUsername(modifyUsernameForm).then((response) => {
              console.log(modifyUsernameForm);
              console.log(response.data);
              this.$store.commit(
                "user/setUsernameState",
                modifyUsernameForm.newUserName
              );
              this.$message({
                message: "修改用户名成功",
                type: "success",
                duration: 2000,
              });
            }); /* .catch(() => {
                        }); */
          }
          if (this.form.password !== this.user.password) {
            modifyPassword(modifyPasswordForm).then((response) => {
              console.log(modifyPasswordForm);
              console.log(response.data);
              this.$store.commit(
                "user/setPasswordState",
                modifyPasswordForm.newPassword
              );
              this.$message({
                message: "修改密码成功",
                type: "success",
                duration: 2000,
              });
            }); /* .catch(() => {
                        }); */
          }
          if (this.imageUrl != this.user.userAvatar) {
            this.uploadAvatar();
          }
          //this.$store.dispatch("user/getInfo");
          this.dialogVisible = false;
          this.$forceUpdate();
        } else {
          return false;
        }
      });
    },
    //处理子组件传过来的值
    handleChildValuesChange(data) {
      this.LACstatus = data; // 接收子组件传递的值对象
      console.log("父组件收到的值", this.LACstatus);
    },
    //关注、取消关注
    clickToFollow() {
      const data = {
        userId: this.user.userId,
        followingUserId: this.currentUserId,
      };
      doFollow(data).then((response) => {
        this.$message({
          message: "关注成功",
          type: "success",
          duration: 2000,
        });
        this.followFlag = true;
      });
    },
    clickToUnfollow() {
      const data = {
        userId: this.user.userId,
        followingUserId: this.currentUserId,
      };
      undoFollow(data).then((response) => {
        this.$message({
          message: "已取消关注",
          type: "success",
          duration: 2000,
        });
        this.followFlag = false;
      });
    },
    //展示关注列表的函数
    showgz(){
      this.gzVisible=true;

    },
    //展示粉丝列表的函数
    showfs(){
      this.fsVisible=true;
    }
  },
};
</script>

<style scoped>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
.user-avatar {
  cursor: pointer;
  border-radius: 50%;
  width: 170px; /* 根据需要调整头像的宽度 */
  height: 170px; /* 根据需要调整头像的高度 */
}

.user-avatar2 {
  cursor: pointer;
  border-radius: 50%;
  width: 60px; /* 根据需要调整头像的宽度 */
  height: 60px; /* 根据需要调整头像的高度 */
}

.my-wrapper {
  text-align: center;
}
.time {
  font-size: 13px;
  color: #999;
}

.image {
  width: 100%;
  display: block;
}
.image-hover {
  /* 鼠标悬停时的样式 */
  /* 比如可以调整图片亮度、透明度等 */
  filter: brightness(80%);
  width: 100%;
  display: block;
}

/* .clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both;
} */
.user-avatar-article {
  cursor: pointer;
  border-radius: 50%;
  width: 20px; /* 根据需要调整头像的宽度 */
  height: 20px; /* 根据需要调整头像的高度 */
}
.router-link-active {
  text-decoration: none;
  color: gray;
}
a {
  text-decoration: none;
  color: gray;
}
</style>