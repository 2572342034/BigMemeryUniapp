<template>
  <view class="login-container">
    <!-- Logo -->
    <view class="logo-box">
      <image src="/static/logo.png" mode="aspectFit" class="logo" />
      <text class="welcome">欢迎登录</text>
      <text class="desc">请使用账号密码登录</text>
    </view>

    <!-- 表单卡片 -->
    <view class="form-card">
      <uni-forms
        ref="formRef"
        :modelValue="form"
        :rules="rules"
      >
        <!-- 账号 -->
        <uni-forms-item name="username">
          <uni-easyinput
            v-model="form.username"
            prefixIcon="person"
            :clearable="true"
            placeholder="请输入账号"
            @iconClick="iconClick"
          />
        </uni-forms-item>

        <!-- 密码 -->
        <uni-forms-item name="password">
          <uni-easyinput
            v-model="form.password"
            type="password"
            prefixIcon="locked"
            placeholder="请输入密码"
          />
        </uni-forms-item>
      </uni-forms>

      <!-- 记住密码 & 忘记密码 -->
      <view class="form-extra">
        <uni-data-checkbox
          v-model="remember"
          :localdata="checkboxData"
          multiple
          :animation="false"
          style="display:inline-block"
        />
        <text class="forget" @click="onForget">忘记密码？</text>
      </view>

      <!-- 登录按钮 -->
      <button class="login-btn" :disabled="loading" @click="onLogin">
        {{ loading ? '登录中...' : '登 录' }}
      </button>

      <!-- 分隔线 -->
      <view class="divider">
        <view class="line"></view>
        <text class="divider-text">其他方式登录</text>
        <view class="line"></view>
      </view>

      <!-- 第三方登录图标 -->
      <view class="oauth">
        <uni-icons type="weixin" size="36" color="#07c160" />
        <uni-icons type="qq" size="36" color="#1ca4fa" />
        <uni-icons type="weibo" size="36" color="#f50" />
      </view>
    </view>

    <!-- 底部注册 -->
    <view class="footer">
      <text>还没有账号？</text>
      <text class="link" @click="onRegister">立即注册</text>
    </view>
  </view>
</template>

<script setup lang="ts" name="login">
import { reactive, ref } from 'vue'
import request from '@/pages/utils/request.js'

// 表单数据
const form = reactive({
  username: '',
  password: ''
})

// 校验规则
const rules = reactive({
  username: {
    rules: [{ required: true, errorMessage: '请输入账号' }]
  },
  password: {
    rules: [
      { required: true, errorMessage: '请输入密码' },
      { minLength: 6, errorMessage: '密码至少6位' }
    ]
  }
})

const remember = ref([])
const loading = ref(false)
const formRef = ref(null)

const checkboxData = [
  { text: '记住密码', value: 'remember' }
]

// 登录
async function onLogin() {
  // 1. 表单校验
  try {
    await formRef.value.validate()
  } catch (e) {
    return
  }

  loading.value = true
  try {
    // 2. 用 request 发请求
    const res = await request.post(
      '/login',
      {
        username: form.username,
        password: form.password,
        remember: remember.value.includes('remember') ? 1 : 0
      },
      { loading: false
	  }
    )

    // 3. 能走到这里就是成功（request 已判断 code === 0）
    // 存 token
    if (res?.token) uni.setStorageSync('token', res.token)
	console.log("token " +  uni.getStorageSync("token"))
    // 记住密码
    if (remember.value.includes('remember')) {
      uni.setStorageSync('savedAccount', {
        username: form.username,
        password: form.password
      })
    }

    uni.showToast({ title: '登录成功', icon: 'success' })
    setTimeout(() => {
      uni.reLaunch({ url: '/pages/index/index' })
    }, 1000)
  } catch (e) {
    // request.js 已统一弹错误提示，这里只做日志
    console.log('登录失败', e)
  } finally {
    loading.value = false
  }
}

// 输入框图标点击
function iconClick(type) {
  uni.showToast({
    title: `点击了${type === 'prefix' ? '左侧' : '右侧'}图标`,
    icon: 'none'
  })
}

function onForget() {
  uni.showToast({ title: '请联系管理员重置', icon: 'none' })
}

function onRegister() {
  uni.navigateTo({ url: '/pages/login/register' })
}
</script>

<style lang="scss" scoped>
.login-container {
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
  padding-top: 140rpx;
  padding-bottom: 60rpx;
}
.logo {
  width: 150rpx;
  height: 150rpx;
  border-radius: 30rpx;
  background: #fff;
  box-shadow: 0 8rpx 30rpx rgba(0, 0, 0, 0.1);
}
.welcome {
  margin-top: 30rpx;
  font-size: 44rpx;
  font-weight: bold;
  color: #fff;
}
.desc {
  margin-top: 12rpx;
  font-size: 26rpx;
  color: rgba(255, 255, 255, 0.8);
}

/* 表单卡片 */
.form-card {
  background: #fff;
  border-radius: 32rpx;
  padding: 50rpx 40rpx;
  box-shadow: 0 10rpx 40rpx rgba(0, 0, 0, 0.06);
}

/* 记住密码 & 忘记密码 */
.form-extra {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 10rpx 0 40rpx;
  font-size: 26rpx;
  color: #666;
}
.forget {
  color: #4a90ff;
}

/* 登录按钮 */
.login-btn {
  width: 100%;
  height: 90rpx;
  line-height: 90rpx;
  background: #4a90ff;
  color: #fff;
  font-size: 34rpx;
  border-radius: 45rpx;
  border: none;
}
.login-btn[disabled] {
  opacity: 0.6;
}

/* 分隔线 */
.divider {
  display: flex;
  align-items: center;
  margin: 40rpx 0 30rpx;
  gap: 20rpx;
}
.line {
  flex: 1;
  height: 2rpx;
  background: #ddd;
}
.divider-text {
  font-size: 24rpx;
  color: #999;
  white-space: nowrap;
}

/* 第三方登录 */
.oauth {
  display: flex;
  justify-content: center;
  gap: 50rpx;
}

/* 底部注册 */
.footer {
  text-align: center;
  margin-top: 60rpx;
  font-size: 26rpx;
  color: #0055ff;
}
.link {
  color: #0055ff;
  font-weight: bold;
  margin-left: 10rpx;
  text-decoration: underline;
}
</style>