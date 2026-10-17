"use strict";
const common_vendor = require("../../common/vendor.js");
const common_assets = require("../../common/assets.js");
const pages_utils_request = require("../utils/request.js");
if (!Array) {
  const _easycom_uni_easyinput2 = common_vendor.resolveComponent("uni-easyinput");
  const _easycom_uni_forms_item2 = common_vendor.resolveComponent("uni-forms-item");
  const _easycom_uni_forms2 = common_vendor.resolveComponent("uni-forms");
  const _easycom_uni_data_checkbox2 = common_vendor.resolveComponent("uni-data-checkbox");
  const _easycom_uni_icons2 = common_vendor.resolveComponent("uni-icons");
  (_easycom_uni_easyinput2 + _easycom_uni_forms_item2 + _easycom_uni_forms2 + _easycom_uni_data_checkbox2 + _easycom_uni_icons2)();
}
const _easycom_uni_easyinput = () => "../../uni_modules/uni-easyinput/components/uni-easyinput/uni-easyinput.js";
const _easycom_uni_forms_item = () => "../../uni_modules/uni-forms/components/uni-forms-item/uni-forms-item.js";
const _easycom_uni_forms = () => "../../uni_modules/uni-forms/components/uni-forms/uni-forms.js";
const _easycom_uni_data_checkbox = () => "../../uni_modules/uni-data-checkbox/components/uni-data-checkbox/uni-data-checkbox.js";
const _easycom_uni_icons = () => "../../uni_modules/uni-icons/components/uni-icons/uni-icons.js";
if (!Math) {
  (_easycom_uni_easyinput + _easycom_uni_forms_item + _easycom_uni_forms + _easycom_uni_data_checkbox + _easycom_uni_icons)();
}
const _sfc_main = /* @__PURE__ */ common_vendor.defineComponent({
  __name: "login",
  setup(__props) {
    const form = common_vendor.reactive({
      username: "",
      password: ""
    });
    const rules = common_vendor.reactive({
      username: {
        rules: [{ required: true, errorMessage: "请输入账号" }]
      },
      password: {
        rules: [
          { required: true, errorMessage: "请输入密码" },
          { minLength: 6, errorMessage: "密码至少6位" }
        ]
      }
    });
    const remember = common_vendor.ref([]);
    const loading = common_vendor.ref(false);
    const formRef = common_vendor.ref(null);
    const checkboxData = [
      { text: "记住密码", value: "remember" }
    ];
    async function onLogin() {
      try {
        await formRef.value.validate();
      } catch (e) {
        return;
      }
      loading.value = true;
      try {
        const res = await pages_utils_request.request.post(
          "/login",
          {
            username: form.username,
            password: form.password,
            remember: remember.value.includes("remember") ? 1 : 0
          },
          {
            loading: false
          }
        );
        if (res == null ? void 0 : res.token)
          common_vendor.index.setStorageSync("token", res.token);
        common_vendor.index.__f__("log", "at pages/login/login.vue:136", "token " + common_vendor.index.getStorageSync("token"));
        if (remember.value.includes("remember")) {
          common_vendor.index.setStorageSync("savedAccount", {
            username: form.username,
            password: form.password
          });
        }
        common_vendor.index.showToast({ title: "登录成功", icon: "success" });
        setTimeout(() => {
          common_vendor.index.reLaunch({ url: "/pages/index/index" });
        }, 1e3);
      } catch (e) {
        common_vendor.index.__f__("log", "at pages/login/login.vue:151", "登录失败", e);
      } finally {
        loading.value = false;
      }
    }
    function iconClick(type) {
      common_vendor.index.showToast({
        title: `点击了${type === "prefix" ? "左侧" : "右侧"}图标`,
        icon: "none"
      });
    }
    function onForget() {
      common_vendor.index.showToast({ title: "请联系管理员重置", icon: "none" });
    }
    function onRegister() {
      common_vendor.index.navigateTo({ url: "/pages/login/register" });
    }
    return (_ctx, _cache) => {
      return {
        a: common_assets._imports_0,
        b: common_vendor.o(iconClick, "fa"),
        c: common_vendor.o(($event) => form.username = $event, "19"),
        d: common_vendor.p({
          prefixIcon: "person",
          clearable: true,
          placeholder: "请输入账号",
          modelValue: form.username
        }),
        e: common_vendor.p({
          name: "username"
        }),
        f: common_vendor.o(($event) => form.password = $event, "41"),
        g: common_vendor.p({
          type: "password",
          prefixIcon: "locked",
          placeholder: "请输入密码",
          modelValue: form.password
        }),
        h: common_vendor.p({
          name: "password"
        }),
        i: common_vendor.sr(formRef, "e4e4508d-0", {
          "k": "formRef"
        }),
        j: common_vendor.p({
          modelValue: form,
          rules
        }),
        k: common_vendor.o(($event) => remember.value = $event, "9d"),
        l: common_vendor.p({
          localdata: checkboxData,
          multiple: true,
          animation: false,
          modelValue: remember.value
        }),
        m: common_vendor.o(onForget, "d9"),
        n: common_vendor.t(loading.value ? "登录中..." : "登 录"),
        o: loading.value,
        p: common_vendor.o(onLogin, "e5"),
        q: common_vendor.p({
          type: "weixin",
          size: "36",
          color: "#07c160"
        }),
        r: common_vendor.p({
          type: "qq",
          size: "36",
          color: "#1ca4fa"
        }),
        s: common_vendor.p({
          type: "weibo",
          size: "36",
          color: "#f50"
        }),
        t: common_vendor.o(onRegister, "40")
      };
    };
  }
});
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-e4e4508d"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/login/login.js.map
