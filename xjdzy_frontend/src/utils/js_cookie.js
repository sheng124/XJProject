import Cookies from 'js-cookie'

const uToken = 'u_token'
const darkMode = 'dark_mode';
const uId='u_id'

// 获取Token
export function getToken() {
    return Cookies.get(uToken);
}

// 设置Token，0.1天,与后端同步
export function setToken(token) {
    return Cookies.set(uToken, token, {expires: 0.1})
}

// 删除Token
export function removeToken() {
    return Cookies.remove(uToken)
}

// 获取用户id
export function getId() {
    return Cookies.get(uId);
}

// 设置用户id，0.1天,与后端同步
export function setId(id) {
    return Cookies.set(uId, id, {expires: 0.1})
}

// 删除用户id
export function removeId() {
    return Cookies.remove(uId)
}

//设置黑夜白天模式
export function setDarkMode(mode) {
    return Cookies.set(darkMode, mode, {expires: 365})
}

export function getDarkMode() {
    return !(undefined === Cookies.get(darkMode) || 'false' === Cookies.get(darkMode));
}

export function removeAll() {
    return Cookies.Cookies.removeAll()
}