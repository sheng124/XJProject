<template>
    <div class="columns py-6">
      <div class="column is-half is-offset-one-quarter">
        <el-card shadow="never">
          <div slot="header" class="has-text-centered has-text-weight-bold">
            新用户入驻
          </div>
          <div>
            <el-form
              ref="ruleForm"
              v-loading="loading"
              :model="ruleForm"
              status-icon
              :rules="rules"
              label-width="100px"
              class="demo-ruleForm"
            >
              <el-form-item label="用户名" prop="username">
                <el-input v-model="ruleForm.username" />
              </el-form-item>
  
              <el-form-item label="密码" prop="password">
                <el-input
                  v-model="ruleForm.password"
                  type="password"
                  autocomplete="off"
                />
              </el-form-item>
  
              <el-form-item label="确认密码" prop="passwordConfirm">
                <el-input
                  v-model="ruleForm.passwordConfirm"
                  type="password"
                  autocomplete="off"
                />
              </el-form-item>
  
              <el-form-item>
                <el-button
                  type="primary"
                  @click="submitForm('ruleForm')"
                >立即注册</el-button>
                <el-button @click="resetForm('ruleForm')">重置</el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-card>
      </div>
    </div>
  </template>
  
  <script>
  import { register } from '@/api/auth/auth'
  
  export default {
    name: 'Register',
    data() {
      const validatePass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入密码'))
        } else if (value !== this.ruleForm.pass) {
          callback(new Error('两次输入密码不一致!'))
        } else {
          callback()
        }
      }
      return {
        loading: false,
        ruleForm: {
          username: '',
          password: '',
          passwordConfirm: ''
        },
        rules: {
          username: [
            { required: true, message: '请输入用户名', trigger: 'blur' },
            {
              min: 1,
              max: 20,
              message: '长度在 1 到 20 个字符',
              trigger: 'blur'
            }
          ],
          password: [
            { required: true, message: '请输入密码', trigger: 'blur' },
            {
              min: 6,
              max: 20,
              message: '长度在 6 到 20 个字符',
              trigger: 'blur'
            }
          ],
          passwordConfirm: [
            { required: true, message: '请再次输入密码', trigger: 'blur' },
            { validator: validatePass, trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.loading = true
            register(this.ruleForm)
              .then((value) => {
                const { code, message } = value
                if (code === 1) {
                  this.$message({
                    message: '账号注册成功',
                    type: 'success'
                  })
                  setTimeout(() => {
                    this.loading = false
                    this.$router.push({ path: this.redirect || '/login' })
                  }, 0.1 * 1000)
                } else {
                  this.$message.error('注册失败，' + message)
                }
              })
              .catch(() => {
                this.loading = false
              })
          } else {
            return false
          }
        })
      },
      resetForm(formName) {
        this.$refs[formName].resetFields()
      }
    }
  }
  </script>
  
  <style scoped>
  </style>