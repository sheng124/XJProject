import { login, getUserInfo,logout } from '@/api/auth'
import { getToken, setToken, setId,getId,removeToken,removeId,removeAll} from '@/utils/js_cookie'

//定义全局状态数据
const state = {
  token: getToken(), // token
  user:{
    userId: getId(), // 用户id
    username: '',  // 用户名
    password: '', //密码
    userAvatar: ''  // 用户头像
  }
  
}

const mutations = {
  setTokenState(state, token) {
    state.token = token
  },
  setUserState(state, user) {
    state.user.userId = user.userId
    state.user.username = user.username
    state.user.password = user.password
    state.user.userAvatar = user.userAvatar
  },
  clearUserState(state){
    state.user.userId = -1
    state.user.username = ''
    state.user.password = ''
    state.user.userAvatar = ''
  },
  setUserIdState(state,userId){
    state.user.userId=userId
  },
  setUsernameState(state,username){
    state.user.username=username
  },
  setPasswordState(state,password){
    state.user.password=password
  },
}

//actions解决异步请求（指请求数据后不等待数据返回，直接去做别的事，当数据返回时，再通过回调函数）
const actions = {
  // 用户登录的方法
  userLogin({ commit,state }, userInfo) {		//这里的userLogin是给dispatch调用的（dispatch("user/userLogin", this.ruleForm)）
    console.log(userInfo)
    const { username, password } = userInfo
    return new Promise((resolve, reject) => {
      login({ username: username.trim(), password: password }).then(response => {//这里的login是'@/api/auth'里的登录接口
        const { data } = response
        console.log(data);
        commit('setTokenState', data.token)	//存到store.state里
        commit('setUserIdState', data.userId)
        setToken(data.token)		//存到cookie里
        setId(data.userId)
        console.log(111111111111111)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },
  // 获取用户信息
  getInfo({ commit,state }) {
    console.log("开始getInfo")
    return new Promise((resolve, reject) => {
      getUserInfo(state.user.userId).then(response => {
        const { data } = response
        console.log(data)
        if (!data) {
          commit('setTokenState', '')
          commit('clearUserState')
          removeAll()
          resolve()
          reject('Verification failed, please Login again.')
        }
        commit('setUserState', data)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },
  // 退出登录
  userLogout({ commit, state }) {
    return new Promise((resolve, reject) => {
      logout(state.token)
        .then((response) => {
          console.log(response);
          commit('setTokenState', '');
          commit('clearUserState');
          removeToken();
          removeId();
        })
        .catch((error) => {
          reject(error);
        });
    });
  },
}

export default {
  namespaced: true,
  state,
  mutations,
  actions,
};
/* 疑问：登录接口的响应数据应该要增加头像，或者就是注册的时候选择头像，还是，先注册登录，然后在个人中心里再去修改头像 */