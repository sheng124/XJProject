const getters = {
    token(state){
        return state.user.token;   // token
    },
    user(state){
        return state.user.user;     // 用户对象
    }
  }
  export default getters