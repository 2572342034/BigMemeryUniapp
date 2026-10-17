"use strict";
const common_vendor = require("../../common/vendor.js");
const common_assets = require("../../common/assets.js");
const pages_utils_request = require("../utils/request.js");
if (!Array) {
  const _easycom_uni_easyinput2 = common_vendor.resolveComponent("uni-easyinput");
  const _easycom_uni_forms_item2 = common_vendor.resolveComponent("uni-forms-item");
  const _easycom_uni_forms2 = common_vendor.resolveComponent("uni-forms");
  (_easycom_uni_easyinput2 + _easycom_uni_forms_item2 + _easycom_uni_forms2)();
}
const _easycom_uni_easyinput = () => "../../uni_modules/uni-easyinput/components/uni-easyinput/uni-easyinput.js";
const _easycom_uni_forms_item = () => "../../uni_modules/uni-forms/components/uni-forms-item/uni-forms-item.js";
const _easycom_uni_forms = () => "../../uni_modules/uni-forms/components/uni-forms/uni-forms.js";
if (!Math) {
  (_easycom_uni_easyinput + _easycom_uni_forms_item + _easycom_uni_forms)();
}
const _sfc_main = /* @__PURE__ */ common_vendor.defineComponent({
  __name: "register",
  setup(__props) {
    const form = common_vendor.reactive({
      username: "",
      password: "",
      confirmPassword: "",
      email: "",
      phoneNumber: ""
    });
    const rules = common_vendor.reactive({
      username: {
        rules: [
          { required: true, errorMessage: "请输入用户名" },
          { minLength: 3, errorMessage: "用户名至少3位" },
          { maxLength: 20, errorMessage: "用户名最多20位" }
        ]
      },
      email: {
        rules: [
          { required: true, errorMessage: "请输入邮箱" },
          { format: "email", errorMessage: "邮箱格式不正确" }
        ]
      },
      phoneNumber: {
        rules: [
          { required: true, errorMessage: "请输入手机号" },
          {
            validateFunction: (rule, value, data, callback) => {
              const reg = /^1[3-9]\d{9}$/;
              if (!reg.test(value)) {
                callback("手机号格式不正确");
              } else {
                callback();
              }
            }
          }
        ]
      },
      password: {
        rules: [
          { required: true, errorMessage: "请输入密码" },
          { minLength: 6, errorMessage: "密码至少6位" },
          { maxLength: 20, errorMessage: "密码最多20位" }
        ]
      },
      confirmPassword: {
        rules: [
          { required: true, errorMessage: "请再次输入密码" },
          {
            validateFunction: (rule, value, data, callback) => {
              if (value !== data.password) {
                callback("两次输入的密码不一致");
              } else {
                callback();
              }
            }
          }
        ]
      }
    });
    const loading = common_vendor.ref(false);
    const formRef = common_vendor.ref(null);
    async function onRegister() {
      try {
        await formRef.value.validate();
      } catch (e) {
        return;
      }
      loading.value = true;
      try {
        const res = await pages_utils_request.request.post(
          "/register",
          {
            username: form.username,
            password: form.password,
            email: form.email,
            phoneNumber: form.phoneNumber
          },
          { loading: false }
        );
        common_vendor.index.showToast({ title: "注册成功", icon: "success" });
        setTimeout(() => {
          common_vendor.index.redirectTo({ url: "/pages/login/login" });
        }, 1e3);
      } catch (e) {
        common_vendor.index.__f__("log", "at pages/login/register.vue:184", "注册失败", e);
      } finally {
        loading.value = false;
      }
    }
    function onLogin() {
      common_vendor.index.navigateTo({ url: "/pages/login/login" });
    }
    return (_ctx, _cache) => {
      return {
        a: common_assets._imports_0,
        b: common_vendor.o(($event) => form.username = $event, "8e"),
        c: common_vendor.p({
          placeholder: "请输入用户名",
          prefixIcon: "person",
          clearable: true,
          modelValue: form.username
        }),
        d: common_vendor.p({
          name: "username"
        }),
        e: common_vendor.o(($event) => form.email = $event, "99"),
        f: common_vendor.p({
          placeholder: "请输入邮箱",
          prefixIcon: "email",
          clearable: true,
          modelValue: form.email
        }),
        g: common_vendor.p({
          name: "email"
        }),
        h: common_vendor.o(($event) => form.phoneNumber = $event, "c4"),
        i: common_vendor.p({
          placeholder: "请输入手机号",
          prefixIcon: "phone",
          clearable: true,
          type: "number",
          maxlength: "11",
          modelValue: form.phoneNumber
        }),
        j: common_vendor.p({
          name: "phoneNumber"
        }),
        k: common_vendor.o(($event) => form.password = $event, "e9"),
        l: common_vendor.p({
          type: "password",
          placeholder: "请输入密码",
          prefixIcon: "locked",
          modelValue: form.password
        }),
        m: common_vendor.p({
          name: "password"
        }),
        n: common_vendor.o(($event) => form.confirmPassword = $event, "d9"),
        o: common_vendor.p({
          type: "password",
          placeholder: "请再次输入密码",
          prefixIcon: "locked",
          modelValue: form.confirmPassword
        }),
        p: common_vendor.p({
          name: "confirmPassword"
        }),
        q: common_vendor.sr(formRef, "838b72c9-0", {
          "k": "formRef"
        }),
        r: common_vendor.p({
          modelValue: form,
          rules
        }),
        s: common_vendor.t(loading.value ? "注册中..." : "注 册"),
        t: loading.value,
        v: common_vendor.o(onRegister, "6d"),
        w: common_vendor.o(onLogin, "cc")
      };
    };
  }
});
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-838b72c9"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/login/register.js.map
