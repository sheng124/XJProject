const getters = {
    token(state){
        return state.user.token;   // token
    },
    user(state){
        return state.user.user;     // 用户对象
    },
    editArticleId(state){
        return state.user.editArticleId
    },
    deleteArticleId(state){
        return state.user.deleteArticleId
    },
    Lflag(state){
        return state.user.Lflag
    },
    Cflag(state){
        return state.user.Cflag
    }
  }
  export default getters