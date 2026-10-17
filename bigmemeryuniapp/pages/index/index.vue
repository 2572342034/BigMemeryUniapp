<template>
  <view class="container">
    <text class="title">首页</text>
    <text class="subtitle">{{ isLogin ? '已登录，欢迎回来！' : '正在跳转登录...' }}</text>

    <button v-if="isLogin" type="primary" @click="onLogout">
      退出登录
    </button>
    <button v-else type="primary" @click="goLogin">
      去登录
    </button>

    <!-- 点击按钮发请求 -->
    <button v-if="isLogin" type="primary" class="req-btn" @click="getUserItem">
      {{ loading ? '请求中...' : '获取用户数据' }}
    </button>

    <!-- 接口返回数据展示 -->
    <view v-if="userItem" class="user-card">
      <text class="card-title">接口返回数据</text>
      <view class="card-content">
        <text v-for="(value, key) in userItem" :key="key" class="card-item">
          {{ key }}：{{ value }}
        </text>
      </view>
    </view>

    <!-- 加载中 -->
    <text v-else-if="loading" class="loading">加载中...</text>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '@/pages/utils/http.js'

const isLogin = ref(false)
const userItem = ref(null)
const loading = ref(false)

onMounted(() => {
  checkLogin()
})

// 检查登录状态
function checkLogin() {
  const token = uni.getStorageSync('token')
  if (token) {
    isLogin.value = true
  } else {
    uni.showToast({ title: '请先登录', icon: 'none' })
    setTimeout(() => {
      uni.navigateTo({ url: '/pages/login/login' })
    }, 1000)
  }
}

// 点击按钮才发请求
async function getUserItem() {
  loading.value = true
  try {
    const res = await request.get('/getUserItem', { userid: 1 })
    userItem.value = res.data || res
    console.log('用户数据', userItem.value)
  } catch (e) {
    console.log('请求失败', e)
  } finally {
    loading.value = false
  }
}

// 手动跳转登录
function goLogin() {
  uni.navigateTo({ url: '/pages/login/login' })
}

// 退出登录
function onLogout() {
  uni.removeStorageSync('token')
  isLogin.value = false
  userItem.value = null  // 清空数据
  uni.showToast({ title: '已退出登录', icon: 'none' })
}
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  padding: 40rpx;
}
.title {
  font-size: 48rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
}
.subtitle {
  font-size: 28rpx;
  color: #999;
  margin-bottom: 60rpx;
}

/* 请求按钮 */
.req-btn {
  margin-top: 20rpx;
}

/* 数据展示卡片 */
.user-card {
  width: 100%;
  max-width: 600rpx;
  background: #fff;
  border-radius: 20rpx;
  padding: 40rpx;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.06);
  margin-top: 40rpx;
}
.card-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
  display: block;
}
.card-content {
  display: flex;
  flex-direction: column;
}
.card-item {
  font-size: 28rpx;
  color: #666;
  padding: 10rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}
.loading {
  margin-top: 40rpx;
  font-size: 28rpx;
  color: #999;
}
</style>