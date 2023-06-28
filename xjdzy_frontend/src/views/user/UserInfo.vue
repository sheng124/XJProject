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
      <div class="card">
        <div class="card-content">
          <div class="media">
            <div class="media-left">
              <img :src="user.userAvatar" class="user-avatar" />
            </div>
            <div class="media-content ml-3">
              <p class="title is-4 mx-4 mt-4">{{ user.username }}</p>
              <p>
                <i
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
                ><span class="has-text-weight-light">关注</span
                ><span class="has-text-weight-semibold ml-2"
                  >{{ userData.followers }} </span
                ><span class="has-text-weight-light">粉丝</span
                ><span class="has-text-weight-semibold ml-2"
                  >{{ likesANDcollection }} </span
                ><span class="has-text-weight-light">获赞与收藏</span>
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- 此用户发布、收藏、点赞的笔记列表 -->
    <section>
      <!-- 标签栏 -->
      <b-tabs
        type="is-toggle-rounded"
        position="is-centered"
        v-model="activeTab"
        class="mt-3"
      >
        <template v-for="(tab, index) in tabs">
          <b-tab-item :key="index" :label="tab">
            <div class="columns is-desktop">
              <div
                class="column"
                v-for="(item, index1) in allDivideArticles[index]"
                :key="index1"
              >
                <!-- 关于笔记作者相关的路由还需更改 -->
                <el-card
                  :body-style="{ padding: '0px' }"
                  v-for="(article, index2) in allDivideArticles[index][index1]"
                  :key="index2"
                  class="my-2"
                  style="border-radius: 20px"
                >
                  <!-- 笔记封面 -->
                  <!-- 可以试着采用类似小红书，点击笔记，弹出窗口显示文章内容 -->
                  <div style="cursor: pointer;">
                    <v-hover v-slot="{ hover }" >
                      <img
                        :src="article.articleCover"
                        :class="{ 'image-hover': hover, image: !hover }"
                        @click="openArticleDialog(article.articleId)"
                      />
                    </v-hover>
                  </div>
                  <div style="padding: 14px;cursor: pointer;" @click="openArticleDialog(articleId)">
                      <div class="mb-2 has-text-black has-text-weight-semibold">
                        {{ article.articleTitle }}
                      </div>
                    <!-- 笔记作者头像，名称 -->
                    <router-link
                      :to="{
                        name: 'user_info',
                        params: { userId: user.userId },
                      }"
                    >
                      <div class="level-left">
                        <img
                          :src="user.userAvatar"
                          class="user-avatar-article mr-1"
                        />
                        {{ user.username }}
                      </div>
                    </router-link>
                    <!-- 发表时间 -->
                    <time class="time"
                      ><v-icon small class="mb-1 mr-1">mdi-clock-outline</v-icon
                      >{{ article.createTime }}</time
                    >
                    <!-- 笔记分类 -->
                    <router-link
                      :to="{
                        name: 'categorie',
                        params: {
                          categoryId: article.categoryId,
                        },
                      }"
                      style="float: right"
                    >
                      <v-icon>mdi-bookmark</v-icon>
                      {{ article.categoryName }}
                    </router-link>
                    <!-- 笔记标签 -->
                    <b-taglist>
                      <b-tag
                        rounded
                        type="is-info is-light"
                        v-for="tag in article.tagList"
                        :key="tag.tagId"
                      >
                        <router-link
                          :to="{
                            name: 'tag',
                            params: { tagId: tag.tagId },
                          }"
                          ><span class="has-text-info">#{{ tag.tagName }}</span>
                        </router-link></b-tag
                      >
                    </b-taglist>
                  </div>
                </el-card>
              </div>
            </div>
          </b-tab-item>
        </template>
      </b-tabs>
    </section>
    <!-- 文章内容弹窗 -->
    <div data-app="true">
      <v-dialog v-model="selectedArticleVisible" max-width="1000px" style="height:800px">
        <article-model
          :currentArticleId="selectedArticleId"
          @close="closeArticleDialog"
        ></article-model>
      </v-dialog>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import {
  getUserData,
  uploadUserAvatar,
  modifyUsername,
  modifyPassword,
  getWrLiCoArticles,
} from "@/api/user";
import ArticleModel from "@/components/Article/ArticleModel.vue";
export default {
  name: "UserInfo",
  components: {
    ArticleModel,
  },
  data() {
    return {
      carousels: [
        { text: "Slide 1", color: "primary" },
        { text: "Slide 2", color: "info" },
        { text: "Slide 3", color: "success" },
        { text: "Slide 4", color: "warning" },
        { text: "Slide 5", color: "danger" },
      ],
      userData: {
        following: 0,
        followers: 0,
        likesNum: 0,
        collectionNum: 0,
      },
      likesANDcollection: 0,
      dialogVisible: false,
      dialog: false, //测试
      form: {
        username: "",
        password: "",
      },
      rules: {
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
      imageFile: null,
      imageUrl: "",
      activeTab: "",
      tabs: [],
      wrArticles: [],
      liArticles: [],
      coArticles: [],
      articles: [],
      wrDivideArticles: [],
      liDivideArticles: [],
      coDivideArticles: [],
      allDivideArticles: [],
      currentDate: new Date(),

      //选择的文章
      selectedArticleId: -1,
      selectedArticleVisible: false,
    };
  },
  computed: {
    ...mapGetters(["user", "token"]),
  },
  mounted() {
    this.init();
  },
  methods: {
    init() {
      getUserData(this.user.userId).then((response) => {
        const { data } = response;
        this.userData = data;
        this.likesANDcollection = data.likesNum + data.collectionNum;
        this.form.username = this.user.username;
        this.form.password = this.user.password;
        this.imageUrl = this.user.userAvatar;
      });
      this.activeTab = "笔记";
      this.tabs = ["笔记", "收藏", "点赞"];
      getWrLiCoArticles(this.user.userId).then((response) => {
        const { data } = response;
        console.log("收到的所有相关笔记数据", data, data.length);
        //已发布：1，已收藏:2，已喜欢：3
        for(var i=0;i<data.length;i++){
          data[i].createTime=this.replaceTWithSpace(data[i].createTime);
                  console.log("笔记"+i+"的类型为：",data[i].type)
                  if(data[i].type==1){
                    this.wrArticles.push(data[i]);
                  }
                  else if(data[i].type==2){
                    this.coArticles.push(data[i]);
                  }
                  else{
                    this.liArticles.push(data[i]);
                  }
                }
        // 分类为 type 为 1 的文章
        //this.wrArticles = data.filter((article) => article.type === 1);
        // 分类为 type 为 2 的文章
        //this.coArticles = data.filter((article) => article.type === 2);
        // 分类为 type 为 3 的文章
        //this.liArticles = data.filter((article) => article.type === 3);
        console.log("已发布笔记：", this.wrArticles);
        console.log("已收藏笔记：", this.coArticles);
        console.log("已喜欢笔记：", this.liArticles);
        this.articles.push(this.wrArticles);
        this.articles.push(this.coArticles);
        this.articles.push(this.liArticles);
        console.log("所有该用户相关的笔记：", this.articles);
        this.wrDivideArticles = this.divideArticle2(this.wrArticles);
        this.liDivideArticles = this.divideArticle2(this.coArticles);
        this.coDivideArticles = this.divideArticle2(this.liArticles);
        this.allDivideArticles.push(this.wrDivideArticles);
        this.allDivideArticles.push(this.coDivideArticles);
        this.allDivideArticles.push(this.liDivideArticles);
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
    //打开文章弹窗
    openArticleDialog(articleId) {
      this.selectedArticleVisible = true;
      this.selectedArticleId = articleId;
      console.log(
        "打开对话框：",
        this.selectedArticleVisible,
        this.selectedArticleId
      );
    },
    closeArticleDialog() {
      this.electedArticleVisible = false;
      this.selectedArticleId = null;
    },
    replaceTWithSpace(str) {
      return str.replace("T", " ");
    },
  },
};
</script>

<style>
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

.my-wrapper {
  text-align: center;
}
.time {
  font-size: 13px;
  color: #999;
}

.bottom {
  margin-top: 13px;
  line-height: 12px;
}

.button {
  padding: 0;
  float: right;
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

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both;
}
.user-avatar-article {
  cursor: pointer;
  border-radius: 50%;
  width: 20px; /* 根据需要调整头像的宽度 */
  height: 20px; /* 根据需要调整头像的高度 */
}
.router-link-active {
  text-decoration: none;
  color: black;
}
a {
  text-decoration: none;
  color: grey;
}
</style>