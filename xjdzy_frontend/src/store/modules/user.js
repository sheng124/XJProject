import { login } from '@/api/auth/auth'
import { getToken, setToken } from '@/utils/auth'

//定义全局状态数据
const state = {
    token: getToken(), // token
    user: '', // 用户对象
  }

const mutations = {
    setTokenState(state, token){
        state.token = token
    }
}

//actions解决异步请求（指请求数据后不等待数据返回，直接去做别的事，当数据返回时，再通过回调函数）
const actions = {
    // 用户登录的方法
    userLogin({ commit }, userInfo) {		//这里的userLogin是给dispatch调用的（dispatch("user/userLogin", this.ruleForm)）
      console.log(userInfo)
      const { username, password } = userInfo
      return new Promise((resolve, reject) => {
        login({ username: username.trim(), password: password }).then(response => {//这里的login是'@/api/auth/auth'里的登录接口
          const { data } = response
          commit('setTokenState', data.token)	//存到store.state里
          setToken(data.token)		//存到cookie里
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
  }

  /* 疑问：登录接口的响应数据应该要增加头像，或者就是注册的时候选择头像，还是，先注册登录，然后在个人中心里再去修改头像 */