"use strict";
const common_vendor = require("../../common/vendor.js");
const BASE_URL = "http://localhost:8080";
const TIMEOUT = 15e3;
const WHITE_LIST = ["/login"];
let loadingCount = 0;
function showLoading(title = "加载中") {
  if (loadingCount === 0) {
    common_vendor.index.showLoading({ title, mask: true });
  }
  loadingCount++;
}
function hideLoading() {
  loadingCount--;
  if (loadingCount <= 0) {
    loadingCount = 0;
    common_vendor.index.hideLoading();
  }
}
function generateReqKey(options) {
  const { url, method, data } = options;
  return [url, method, JSON.stringify(data)].join("&");
}
const pendingRequest = /* @__PURE__ */ new Map();
function request(options = {}) {
  const {
    url,
    method = "GET",
    data = {},
    header = {},
    loading = true,
    silent = false,
    ...rest
  } = options;
  const reqKey = generateReqKey({ url, method, data });
  if (pendingRequest.has(reqKey)) {
    return Promise.reject("请勿重复请求");
  }
  pendingRequest.set(reqKey, true);
  const token = common_vendor.index.getStorageSync("token");
  const needAuth = !WHITE_LIST.some((item) => url.includes(item));
  const finalHeader = {
    "Content-Type": "application/json",
    ...header
  };
  if (needAuth && token) {
    finalHeader.Authorization = `Bearer ${token}`;
  }
  if (loading)
    showLoading();
  return new Promise((resolve, reject) => {
    common_vendor.index.request({
      url: BASE_URL + url,
      method,
      data,
      header: finalHeader,
      timeout: TIMEOUT,
      ...rest,
      success: (res) => {
        const { statusCode, data: resData } = res;
        if (statusCode >= 200 && statusCode < 300) {
          if (resData.code === 200) {
            resolve(resData.data);
          } else if (resData.code === 401) {
            handleLogout();
            reject(resData);
          } else {
            !silent && common_vendor.index.showToast({ title: resData.message || "请求失败", icon: "none" });
            reject(resData);
          }
        } else if (statusCode === 401) {
          handleLogout();
          reject(resData);
        } else {
          !silent && common_vendor.index.showToast({ title: `服务异常(${statusCode})`, icon: "none" });
          reject(res);
        }
      },
      fail: (err) => {
        const msg = err.errMsg.includes("timeout") ? "请求超时" : "网络连接失败";
        !silent && common_vendor.index.showToast({ title: msg, icon: "none" });
        reject(err);
      },
      complete: () => {
        pendingRequest.delete(reqKey);
        if (loading)
          hideLoading();
      }
    });
  });
}
function handleLogout() {
  common_vendor.index.removeStorageSync("token");
  common_vendor.index.showStorageSync;
  common_vendor.index.showToast({ title: "登录已过期，请重新登录", icon: "none" });
  setTimeout(() => {
    common_vendor.index.reLaunch({ url: "/pages/login/login" });
  }, 1e3);
}
request.get = (url, data, options = {}) => request({ url, method: "GET", data, ...options });
request.post = (url, data, options = {}) => request({ url, method: "POST", data, ...options });
request.put = (url, data, options = {}) => request({ url, method: "PUT", data, ...options });
request.delete = (url, data, options = {}) => request({ url, method: "DELETE", data, ...options });
exports.request = request;
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/utils/request.js.map
