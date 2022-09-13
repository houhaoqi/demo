import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/views/Login' 
import Blogs from '@/views/Blogs' 
import BlogEdit from '@/views/BlogEdit' 
import BlogDetail from '@/views/BlogDetail' 

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Index',
      redirect:{name : "Blogs"}
    },
    {
      path: '/blogs',
      name: 'Blogs',
      component: Blogs
    },{
    path: '/Login',
    name: 'Login',
    component: Login
    },{
      path: '/blog/add',
      name: 'BlogAdd',
      component: BlogEdit,
      meta: {
        requireAuth: true
      }
    },{
      path: '/blog/:blogid',
      name: 'BlogDetail',
      component: BlogDetail
    } ,{
      path: '/blog/:blogid/edit',
      name: 'BlogEdit', 
      component: BlogEdit,
      meta: {
        requireAuth: true
      }
    }
]})
