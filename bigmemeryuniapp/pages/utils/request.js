// utils/request.js

// ============ 基础配置 ============
const BASE_URL = 'http://localhost:8080'  // 改成你的后端地址
const TIMEOUT = 15000                     // 超时时间
const WHITE_LIST = ['/login']             // 白名单：不携带 token 的接口

// ============ Loading 引用计数（解决并发请求 loading 闪烁） ============
let loadingCount = 0
function showLoading(title = '加载中') {
  if (loadingCount === 0) {
    uni.showLoading({ title, mask: true })
  }
  loadingCount++
}
function hideLoading() {
  loadingCount--
  if (loadingCount <= 0) {
    loadingCount = 0
    uni.hideLoading()
  }
}

// ============ 生成请求唯一 key（防重复提交） ============
function generateReqKey(options) {
  const { url, method, data } = options
  return [url, method, JSON.stringify(data)].join('&')
}
const pendingRequest = new Map()

// ============ 请求主体 ============
export default function request(options = {}) {
  const {
    url,
    method = 'GET',
    data = {},
    header = {},
    loading = true,
    silent = false,
    ...rest
  } = options

  // 1. 防重复请求
  const reqKey = generateReqKey({ url, method, data })
  if (pendingRequest.has(reqKey)) {
    return Promise.reject('请勿重复请求')
  }
  pendingRequest.set(reqKey, true)

  // 2. 请求头 + Token 注入
  const token = uni.getStorageSync('token')
  const needAuth = !WHITE_LIST.some(item => url.includes(item))
  const finalHeader = {
    'Content-Type': 'application/json',
    ...header
  }
  if (needAuth && token) {
    finalHeader.Authorization = `Bearer ${token}`
  }

  // 3. Loading
  if (loading) showLoading()

  return new Promise((resolve, reject) => {
    uni.request({
      url: BASE_URL + url,
      method,
      data,
      header: finalHeader,
      timeout: TIMEOUT,
      ...rest,

      success: (res) => {
        const { statusCode, data: resData } = res

        // HTTP 层面正常
        if (statusCode >= 200 && statusCode < 300) {
          // ★ 按你的返回格式判断：code === 200 成功
          if (resData.code === 200) {
            resolve(resData.data)           // 只返回业务数据
          } else if (resData.code === 401) {
            handleLogout()
            reject(resData)
          } else {
            // ★ 失败提示用 message 字段
            !silent && uni.showToast({ title: resData.message || '请求失败', icon: 'none' })
            reject(resData)
          }
        } else if (statusCode === 401) {
          handleLogout()
          reject(resData)
        } else {
          !silent && uni.showToast({ title: `服务异常(${statusCode})`, icon: 'none' })
          reject(res)
        }
      },

      fail: (err) => {
        const msg = err.errMsg.includes('timeout') ? '请求超时' : '网络连接失败'
        !silent && uni.showToast({ title: msg, icon: 'none' })
        reject(err)
      },

      complete: () => {
        pendingRequest.delete(reqKey)
        if (loading) hideLoading()
      }
    })
  })
}

// ============ 登录过期统一处理 ============
function handleLogout() {
  uni.removeStorageSync('token')
  uni.showStorageSync  // 占位，避免空函数（可删）
  uni.showToast({ title: '登录已过期，请重新登录', icon: 'none' })
  setTimeout(() => {
    uni.reLaunch({ url: '/pages/login/login' })
  }, 1000)
}

// ============ 便捷方法 ============
request.get = (url, data, options = {}) =>
  request({ url, method: 'GET', data, ...options })

request.post = (url, data, options = {}) =>
  request({ url, method: 'POST', data, ...options })

request.put = (url, data, options = {}) =>
  request({ url, method: 'PUT', data, ...options })

request.delete = (url, data, options = {}) =>
  request({ url, method: 'DELETE', data, ...options })