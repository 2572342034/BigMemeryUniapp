<template>
  <view class="register-container">
    <!-- Logo -->
    <view class="logo-box">
      <image src="/static/logo.png" mode="aspectFit" class="logo" />
      <text class="welcome">用户注册</text>
      <text class="desc">请填写以下信息完成注册</text>
    </view>

    <!-- 表单卡片 -->
    <view class="form-card">
      <uni-forms
        ref="formRef"
        :modelValue="form"
        :rules="rules"
      >
        <!-- 用户名 -->
        <uni-forms-item name="username">
          <uni-easyinput
            v-model="form.username"
            placeholder="请输入用户名"
            prefixIcon="person"
            :clearable="true"
          />
        </uni-forms-item>

        <!-- 邮箱 -->
        <uni-forms-item name="email">
          <uni-easyinput
            v-model="form.email"
            placeholder="请输入邮箱"
            prefixIcon="email"
            :clearable="true"
          />
        </uni-forms-item>

        <!-- 手机号 -->
        <uni-forms-item name="phoneNumber">
          <uni-easyinput
            v-model="form.phoneNumber"
            placeholder="请输入手机号"
            prefixIcon="phone"
            :clearable="true"
            type="number"
            maxlength="11"
          />
        </uni-forms-item>

        <!-- 密码 -->
        <uni-forms-item name="password">
          <uni-easyinput
            v-model="form.password"
            type="password"
            placeholder="请输入密码"
            prefixIcon="locked"
          />
        </uni-forms-item>

        <!-- 确认密码 -->
        <uni-forms-item name="confirmPassword">
          <uni-easyinput
            v-model="form.confirmPassword"
            type="password"
            placeholder="请再次输入密码"
            prefixIcon="locked"
          />
        </uni-forms-item>
      </uni-forms>

      <!-- 注册按钮 -->
      <button class="register-btn" :disabled="loading" @click="onRegister">
        {{ loading ? '注册中...' : '注 册' }}
      </button>

      <!-- 登录入口 -->
      <view class="login-entry">
        <text>已有账号？</text>
        <text class="link" @click="onLogin">立即登录</text>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts" name="register">
import { reactive, ref } from 'vue'
import request from '@/pages/utils/request.js'

// 表单数据
const form = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  email: '',
  phoneNumber: ''
})

// 校验规则
const rules = reactive({
  username: {
    rules: [
      { required: true, errorMessage: '请输入用户名' },
      { minLength: 3, errorMessage: '用户名至少3位' },
      { maxLength: 20, errorMessage: '用户名最多20位' }
    ]
  },
  email: {
    rules: [
      { required: true, errorMessage: '请输入邮箱' },
      { format: 'email', errorMessage: '邮箱格式不正确' }
    ]
  },
  phoneNumber: {
    rules: [
      { required: true, errorMessage: '请输入手机号' },
      {
        validateFunction: (rule, value, data, callback) => {
          const reg = /^1[3-9]\d{9}$/
          if (!reg.test(value)) {
            callback('手机号格式不正确')
          } else {
            callback()
          }
        }
      }
    ]
  },
  password: {
    rules: [
      { required: true, errorMessage: '请输入密码' },
      { minLength: 6, errorMessage: '密码至少6位' },
      { maxLength: 20, errorMessage: '密码最多20位' }
    ]
  },
  confirmPassword: {
    rules: [
      { required: true, errorMessage: '请再次输入密码' },
      {
        validateFunction: (rule, value, data, callback) => {
          if (value !== data.password) {
            callback('两次输入的密码不一致')
          } else {
            callback()
          }
        }
      }
    ]
  }
})

const loading = ref(false)
const formRef = ref(null)

// 注册
async function onRegister() {
  // 1. 表单校验
  try {
    await formRef.value.validate()
  } catch (e) {
    return
  }

  loading.value = true
  try {
    // 2. 调用注册接口（只传后端需要的字段）
    const res = await request.post(
      '/register',
      {
        username: form.username,
        password: form.password,
        email: form.email,
        phoneNumber: form.phoneNumber
      },
      { loading: false }
    )

    // 3. 成功
    uni.showToast({ title: '注册成功', icon: 'success' })
    setTimeout(() => {
      // 注册成功后跳登录页
      uni.redirectTo({ url: '/pages/login/login' })
    }, 1000)
  } catch (e) {
    // request.js 已统一弹错误提示
    console.log('注册失败', e)
  } finally {
    loading.value = false
  }
}

// 去登录
function onLogin() {
  uni.navigateTo({ url: '/pages/login/login' })
}
</script>

<style lang="scss" scoped>
.register-container {
  min-height: 100vh;
  padding: 0 50rpx;
  background: linear-gradient(160deg, #4a90ff 0%, #6aa9ff 45%, #f5f7fa 45%);
  box-sizing: border-box;
}

/* Logo */
.logo-box {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-top: 100rpx;
  padding-bottom: 40rpx;
}
.logo {
  width: 130rpx;
  height: 130rpx;
  border-radius: 26rpx;
  background: #fff;
  box-shadow: 0 8rpx 30rpx rgba(0, 0, 0, 0.1);
}
.welcome {
  margin-top: 24rpx;
  font-size: 42rpx;
  font-weight: bold;
  color: #fff;
}
.desc {
  margin-top: 10rpx;
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.8);
}

/* 表单卡片 */
.form-card {
  background: #fff;
  border-radius: 32rpx;
  padding: 40rpx;
  box-shadow: 0 10rpx 40rpx rgba(0, 0, 0, 0.06);
}

/* 注册按钮 */
.register-btn {
  width: 100%;
  height: 90rpx;
  line-height: 90rpx;
  background: #4a90ff;
  color: #fff;
  font-size: 34rpx;
  border-radius: 45rpx;
  border: none;
  margin-top: 20rpx;
}
.register-btn[disabled] {
  opacity: 0.6;
}

/* 登录入口 */
.login-entry {
  text-align: center;
  margin-top: 40rpx;
  font-size: 26rpx;
  color: #666;
}
.link {
  color: #4a90ff;
  font-weight: bold;
  margin-left: 10rpx;
  text-decoration: underline;
}
</style>