"use strict";
const common_vendor = require("../../common/vendor.js");
const pages_utils_http = require("../utils/http.js");
const _sfc_main = {
  __name: "index",
  setup(__props) {
    const isLogin = common_vendor.ref(false);
    const userItem = common_vendor.ref(null);
    const loading = common_vendor.ref(false);
    common_vendor.onMounted(() => {
      checkLogin();
    });
    function checkLogin() {
      const token = common_vendor.index.getStorageSync("token");
      if (token) {
        isLogin.value = true;
      } else {
        common_vendor.index.showToast({ title: "请先登录", icon: "none" });
        setTimeout(() => {
          common_vendor.index.navigateTo({ url: "/pages/login/login" });
        }, 1e3);
      }
    }
    async function getUserItem() {
      loading.value = true;
      try {
        const res = await pages_utils_http.request.get("/getUserItem", { userid: 1 });
        userItem.value = res.data || res;
        common_vendor.index.__f__("log", "at pages/index/index.vue:64", "用户数据", userItem.value);
      } catch (e) {
        common_vendor.index.__f__("log", "at pages/index/index.vue:66", "请求失败", e);
      } finally {
        loading.value = false;
      }
    }
    function goLogin() {
      common_vendor.index.navigateTo({ url: "/pages/login/login" });
    }
    function onLogout() {
      common_vendor.index.removeStorageSync("token");
      isLogin.value = false;
      userItem.value = null;
      common_vendor.index.showToast({ title: "已退出登录", icon: "none" });
    }
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.t(isLogin.value ? "已登录，欢迎回来！" : "正在跳转登录..."),
        b: isLogin.value
      }, isLogin.value ? {
        c: common_vendor.o(onLogout, "e6")
      } : {
        d: common_vendor.o(goLogin, "53")
      }, {
        e: isLogin.value
      }, isLogin.value ? {
        f: common_vendor.t(loading.value ? "请求中..." : "获取用户数据"),
        g: common_vendor.o(getUserItem, "cb")
      } : {}, {
        h: userItem.value
      }, userItem.value ? {
        i: common_vendor.f(userItem.value, (value, key, i0) => {
          return {
            a: common_vendor.t(key),
            b: common_vendor.t(value),
            c: key
          };
        })
      } : loading.value ? {} : {}, {
        j: loading.value
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-1cf27b2a"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/index/index.js.map
