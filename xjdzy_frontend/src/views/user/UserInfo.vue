<template>
  <div>
    <div class="column is-half is-offset-one-quarter">
      <!-- <el-card>
        <div>
          <img :src="user.userAvatar" class="user-avatar" />
          {{ userData }}
        </div>
      </el-card> -->
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
    <el-dialog title="编辑资料" :visible.sync="dialogVisible" width="50%">
      <el-form :model="form" :rules="rules" ref="form">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" autocomplete="off"></el-input>
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
        <b-button @click="dialogVisible = false" class="mx-1">取 消</b-button>
        <b-button type="is-primary" @click="submitForm('form')" class="mx-1"
          >确定</b-button
        >
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import {
  getUserData,
  uploadUserAvatar,
  modifyUsername,
  modifyPassword,
} from "@/api/user";
export default {
  name: "UserInfo",
  data() {
    return {
      userData: {
        following: 0,
        followers: 0,
        likesNum: 0,
        collectionNum: 0,
      },
      likesANDcollection: 0,
      dialogVisible: false,
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
    };
  },
  computed: {
    ...mapGetters(["user", "token"]),
  },
  created() {
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
        this.imageUrl=this.user.userAvatar;
      });
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
          message: "编辑信息成功",
          type: "success",
          duration: 2000,
        });
        this.$store.dispatch("user/getInfo");
      });
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
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
          modifyUsername(modifyUsernameForm).then((response) => {
            console.log(response.data);
          }).catch(() => {
            });
          modifyPassword(
            modifyPasswordForm
          ).then((response) => {
            console.log(response.data);
          })/* .catch(() => {
            }); */
          if(this.imageUrl!=this.user.userAvatar){
            this.uploadAvatar();
          }
          this.dialogVisible=false;
          this.$store.dispatch("user/getInfo");
        } else {
          return false;
        }
      });
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
</style>