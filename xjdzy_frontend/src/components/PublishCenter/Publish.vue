<template>
  <div>
    <el-card class="column is-10 is-offset-1">
      <p class="is-size-4 has-text-weight-bold mb-4">发布笔记</p>
      <!-- 文章数据 -->
      <el-form
        ref="articleForm"
        :model="articleForm"
        label-width="80px"
        size="medium"
      >
        <!-- 文章标题 -->
        <el-form-item label="文章标题">
          <el-input
            v-model="articleForm.articleTitle"
            placeholder="请填写文章标题"
          ></el-input>
        </el-form-item>
        <!-- 文章内容 -->
        <el-form-item label="文章内容">
          <el-input
            type="textarea"
            autosize
            v-model="articleForm.articleContent"
            placeholder="请输入文章内容"
          ></el-input>
        </el-form-item>
        <!-- 文章分类 -->
        <!-- <el-form-item label="文章分类">
          <el-input
            v-model="articleForm.categoryName"
            placeholder="分类"
          ></el-input>
        </el-form-item> -->
        <el-form-item label="文章分类">
          <el-tag
            v-show="articleForm.categoryName"
            :closable="true"
            style="margin: 0 1rem 0 0"
            type="success"
            @close="removeCategory"
          >
            {{ articleForm.categoryName }}
          </el-tag>
          <!-- 分类选项 -->
          <el-popover
            v-if="!articleForm.categoryName"
            placement="bottom-start"
            trigger="click"
            width="460"
          >
            <div class="popover-title">分类</div>
            <!-- 搜索框 -->
            <el-autocomplete
              v-model="categoryName"
              :fetch-suggestions="searchCategories"
              :trigger-on-focus="true"
              placeholder="请输入分类名搜索"
              style="width: 100%"
              @select="handleSelectCategories"
            >
              <template slot-scope="{ item }">
                <div>{{ item.categoryName }}</div>
              </template>
            </el-autocomplete>
            <!-- 分类 -->
            <div class="popover-container">
              <div
                v-for="item of categoryList"
                :key="item.categoryId"
                class="category-item"
                @click="addCategory(item)"
              >
                {{ item.categoryName }}
              </div>
            </div>
            <el-button slot="reference" plain size="small" type="success">
              添加分类
            </el-button>
          </el-popover>
        </el-form-item>
        <!-- 文章标签 -->
        <el-form-item label="文章标签">
          <el-tag
            v-for="(item, index) of articleForm.tagList"
            :key="index"
            :closable="true"
            style="margin: 0 1rem 0 0"
            @close="removeTag(item)"
          >
            {{ item }}
          </el-tag>
          <!-- 标签选项 -->
          <el-popover
            v-if="articleForm.tagList.length < 3"
            placement="bottom-start"
            trigger="click"
            width="300"
            class="poppver"
          >
            <div class="popover-title">标签</div>
            <!-- 搜索框 -->
            <el-autocomplete
              class="inline-input"
              v-model="tagName"
              :fetch-suggestions="searchTags"
              :trigger-on-focus="false"
              placeholder="请输入标签名搜索，enter可添加自定义标签"
              style="width: 100%"
              @select="handleSelectTag"
              @keyup.enter.native="saveTag"
            >
              <template slot-scope="{ item }">
                <div>{{ item.tagName }}</div>
              </template>
            </el-autocomplete>
            <!-- 标签 -->
            <div class="popover-container">
              <div style="margin-bottom: 1rem">添加标签</div>
              <el-tag
                v-for="item of tagList"
                :key="item.tagId"
                :class="tagClass(item)"
                @click="addTag(item)"
                class="mx-1 my-1"
                style="cursor: pointer;"
              >
                {{ item.tagName }}
              </el-tag>
            </div>
            <el-button slot="reference" plain size="small" type="primary">
              添加标签
            </el-button>
          </el-popover>
        </el-form-item>
        <!-- 上传封面 -->
        <el-form-item label="上传封面">
          <el-upload
            :auto-upload="false"
            :on-change="checkType1"
            :show-file-list="false"
            action="none"
            class="upload-cover"
            drag
          >
            <i v-if="articleForm.articleCover == ''" class="el-icon-upload" />
            <div v-if="articleForm.articleCover == ''" class="el-upload__text">
              将文件拖到此处，或<em>点击上传</em>
            </div>
            <img v-else :src="articleCoverUrl" height="180px" width="360px" />
          </el-upload>
        </el-form-item>
        <!-- 上传文章图片 -->
        <el-form-item label="上传图片">
          <el-upload
            action="none"
            :auto-upload="false"
            :on-change="checkType2"
            list-type="picture-card"
            :on-preview="handlePictureCardPreview"
            :on-remove="handleRemove"
          >
            <i class="el-icon-plus"></i>
          </el-upload>
          <!-- 预览图片对话框 -->
          <el-dialog :visible.sync="dialogVisible">
            <img width="100%" :src="dialogImageUrl" alt="" />
          </el-dialog>
        </el-form-item>
        <el-form-item>
          <el-button @click="cancel = false">取 消</el-button>
          <el-button type="danger" @click="publishArticle"> 发 表 </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { userPublishArticle } from "@/api/user";
import { getCategories, getTags, increaseTag } from "@/api/article";

export default {
  name: "Publish",
  components: {},
  data() {
    return {
      categoryName: "",
      //数据库中的分类列表
      categoryList: [],
      tagName: "",
      //数据库中的标签列表
      tagList: [],
      articleCoverUrl: "",
      articleImageUrlList: [],
      dialogImageUrl: "",
      dialogVisible: false,
      //发布笔记表单
      articleForm: {
        articleTitle: "",
        articleContent: "",
        categoryName: "",
        tagList: [],
        articleCover: "",
        articleImages: [],
      },
    };
  },
  computed: {
    ...mapGetters(["user", "token"]),
    tagClass() {
      return function (item) {
        const index = this.articleForm.tagList.indexOf(item.tagName);
        return index != -1 ? "tag-item-select" : "tag-item";
      };
    },
  },
  created() {},
  mounted() {
    console.log("createTime:" + this.getNowTime());
    getCategories().then((response) => {
      const { data } = response;
      this.categoryList = data;
    });
    getTags().then((response) => {
      const { data } = response;
      this.tagList = data;
    });
  },
  methods: {
    handleSelectCategories(item) {
      this.addCategory({
        categoryName: item.categoryName,
      });
    },
    addCategory(item) {
      this.articleForm.categoryName = item.categoryName;
    },
    removeCategory() {
      this.articleForm.categoryName = null;
    },
    searchCategories(keywords, cb) {
      var categoryList = this.categoryList;
      var results = keywords
        ? categoryList.filter(this.createFilter1(keywords))
        : categoryList;
      // 调用 callback 返回建议列表的数据
      cb(results);
    },
    createFilter1(keywords) {
      return (item) => {
        return item.categoryName.toLowerCase().indexOf(keywords.toLowerCase()) === 0;
      };
    },
    createFilter2(keywords) {
      return (item) => {
        return item.tagName.toLowerCase().indexOf(keywords.toLowerCase()) === 0;
      };
    },

    removeTag(item) {
      const index = this.article.tagNameList.indexOf(item);
      this.article.tagNameList.splice(index, 1);
    },
    searchTags(keywords, cb) {
      var tagList = this.tagList;
      var results = keywords
        ? tagList.filter(this.createFilter2(keywords))
        : tagList;
      // 调用 callback 返回建议列表的数据
      cb(results);
    },
    handleSelectTag(item) {
      this.addTag({
        tagName: item.tagName,
      });
    },
    saveTag() {
      if (this.tagName.trim() != "") {
        this.addTag({
          tagName: this.tagName,
        });
        //增加标签接口
        increaseTag(this.tagName).then((response) => {
          const { data } = response;
          this.$message({
            message: "添加标签成功",
            type: "success",
            duration: 2000,
          });
          getTags().then((response) => {
            const { data } = response;
            this.tagList = data;
          });
        });
        this.tagName = "";
      }
    },
    addTag(item) {
      if (this.articleForm.tagList.indexOf(item.tagName) == -1) {
        this.articleForm.tagList.push(item.tagName);
      }
    },
    removeTag(item) {
      const index = this.articleForm.tagList.indexOf(item);
      this.articleForm.tagList.splice(index, 1);
    },

    checkType1(file, fileList) {
      //截取文件类型
      let fileType = file.name.substring(file.name.lastIndexOf(".") + 1);
      fileList = [];
      if (fileType !== "jpeg" && fileType !== "jpg" && fileType !== "png") {
        this.$message.error("封面格式不正确");
        return false;
      }
      if (file.size / 1024 / 1024 > 2) {
        this.$message.error("上传头像不能超过2MB");
        return false;
      }
      this.articleForm.articleCover = file.raw;
      this.articleCoverUrl = URL.createObjectURL(file.raw);
      console.log("封面：" + this.articleForm.articleCover);
      console.log("封面url", this.articleCoverUrl);
    },
    checkType2(file, fileList) {
      console.log("checkType2工作---");
      //截取文件类型
      let fileType = file.name.substring(file.name.lastIndexOf(".") + 1);
      fileList = [];
      if (fileType !== "jpeg" && fileType !== "jpg" && fileType !== "png") {
        this.$message.error("图片格式不正确");
        return false;
      }
      if (file.size / 1024 / 1024 > 2) {
        this.$message.error("上传头像不能超过2MB");
        return false;
      }
      this.articleForm.articleImages.push(file.raw);
      /* this.articleImageUrlList.push(URL.createObjectURL(file.raw)); */
      console.log("checktype2处理结果:" + this.articleForm.articleImages);
      /* console.log(this.articleImageUrlList); */
    },
    handleRemove(file, fileList) {
      console.log(file);
      console.log(fileList);
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    publishArticle() {
      console.log("publishArticle中~~~~~~~~");
      //构建formData
      let formData = new FormData();
      //文章信息
      let articleInfo = {
        userId: this.user.userId,
        articleTitle: this.articleForm.articleTitle,
        articleContent: this.articleForm.articleContent,
        categoryId: this.getCategoryId(this.articleForm.categoryName),
        createTime: this.getNowTime(),
        tagList: this.getTagIdList(this.articleForm.tagList),
      };
      console.log("articleInfo", articleInfo);
      //这里包装 可以直接转换成对象
      formData.append(
        "articleInfo",
        new Blob([JSON.stringify(articleInfo)], { type: "application/json" })
      );
      //封面
      console.log("封面:" + this.articleForm.articleCover);
      formData.append("articleCover", this.articleForm.articleCover);

      //文章图片
      console.log("文章图片：" + this.articleForm.articleImages);
      for (var i = 0; i < this.articleForm.articleImages.length; i++) {
        formData.append("articleImages", this.articleForm.articleImages[i]);
      }
      console.log("formData", formData);
      userPublishArticle(formData).then((response) => {
        const { data } = response;
        this.$message({
          message: "发布笔记成功",
          type: "success",
          duration: 2000,
        });
        this.$router.push({
          name: "user_info",
          params: {
            params: {
              userId: this.user.userId,
            },
          },
        });
      });
    },
    cancel() {},
    getNowTime: function () {
      let dateTime;
      let yy = new Date().getFullYear();
      let mm =
        new Date().getMonth() + 1 < 10
          ? "0" + (new Date().getMonth() + 1)
          : new Date().getMonth() + 1;
      let dd =
        new Date().getDate() < 10
          ? "0" + new Date().getDate()
          : new Date().getDate();
      let hh =
        new Date().getHours() < 10
          ? "0" + new Date().getHours()
          : new Date().getHours();
      let mf =
        new Date().getMinutes() < 10
          ? "0" + new Date().getMinutes()
          : new Date().getMinutes();
      let ss =
        new Date().getSeconds() < 10
          ? "0" + new Date().getSeconds()
          : new Date().getSeconds();
      dateTime =
        yy + "-" + mm + "-" + dd + "T" + hh + ":" + mf + ":" + ss + ".000Z";
      console.log(dateTime);
      return dateTime;
    },
    getCategoryId(categoryName) {
      for (var i = 0; i < this.categoryList.length; i++) {
        if (categoryName == this.categoryList[i].categoryName) {
          return this.categoryList[i].categoryId;
        }
      }
    },
    getTagIdList(tagNameList) {
      return tagNameList
        .map((tagName) => {
          const tag = this.tagList.find((tag) => tag.tagName === tagName);
          return tag ? { tagId: tag.tagId.toString() } : null;
        })
        .filter((tagId) => tagId !== null);
    },
  },
};
</script>

<style>
.popover-title {
  margin-bottom: 1rem;
  text-align: center;
}
.popover-container {
  margin-top: 1rem;
  height: 260px;
  overflow-y: auto;
}
.category-item {
  cursor: pointer;
  padding: 0.6rem 0.5rem;
}
</style>