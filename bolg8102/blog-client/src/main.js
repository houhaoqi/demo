// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
// 存储
import store from './store'
// 路由
import router from './router'
// 引入element-ui依赖
import Element from 'element-ui'
import "element-ui/lib/theme-chalk/index.css"

// 引入axios依赖
import axios from 'axios'   

// 引入自定义axios.js
import "./axios.js"
import './permission.js' // 路由拦截

//mavonEditor
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'



//引用全局
Vue.prototype.$axios = axios 

// use
Vue.use(mavonEditor)
Vue.use(Element)
Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
