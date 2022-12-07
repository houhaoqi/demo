<template>
  <div class="m-content">
      <div class="block">
        <a href="http://124.223.92.156:8090/"><el-avatar :size="100" :src="user.avatar"></el-avatar></a>
        
      </div>
      <div>{{user.username}}</div>
      <div class="maction">
        <el-row>
          <el-button type="success"  round @click="home">主 页</el-button>
          <el-button type="warning" round @click="blogadd">发表博客</el-button>&nbsp;
          <span v-show="!hasLogin">
            <el-button type="success" round @click="login">登 录</el-button>
          </span> 
          <span v-show="hasLogin">
            <el-button type="info" round @click="logout">退 出</el-button>
          </span>
        </el-row>
      </div>
      <h1>随想小记</h1>
  </div>
</template>

<script>
export default {
    name: 'Header',
    data(){
        return {
            hasLogin: '',
            user: {
                username: '请先登录',
                avatar: '../assets/logo.png'
            },
            blogs: {},
            currentpage: 1,
            total: 0
        }
    },
    methods: {
        //退出操作
        logout(){
            const _this = this
            //首先调用后端logout接口(因该接口需要认证权限,所以需要传入token)
            //其次调用$store清除用户信息及token
            this.$axios.get('/logout',{
                headers: {
                    "Authorization": localStorage.getItem("token")
                }
            }).then(res => {
                _this.$store.commit("REMOVE_INFO")
                _this.$router.push('/login')
            })
        },
        //跳转登录操作
        login(){
            this.$router.push('/login')
        },
        //新增博客
        blogadd(){
            this.$router.push('/blog/add')
        },
        //返回主页
        home(){
            this.$router.push('/blogs')
        }
    },
    //页面创建时即会调用渲染,进而获取用户信息
    created () {
      console.log("=======获取用户信息======")
      console.log(this.$store.getters.getUser)
      if (this.$store.getters.getUser.username) {
        //如果username不为空
        this.user.username = this.$store.getters.getUser.username
        this.user.avatar = this.$store.getters.getUser.avatar 
        //判断是登录状态还是非登录显示 退出按钮或者登录按钮
        this.hasLogin = true;
      }
    }
  }
</script>

<style>
  .m-content {
    height: 400px;
    padding: 0;
    margin: 0 auto;
    line-height: 40px;
    text-align: center;
  }
  .block{
    padding: 100px 0 0 0;
  }
  .el-avatar{
    -webkit-transition: all 250ms cubic-bezier(0.02, 0.01, 0.47, 1);
    transition: all 250ms cubic-bezier(.02, .01, .47, 1);
  }
  .el-avatar:hover{
    box-shadow: 0 1px 6px 0 rgba(0, 0, 0, .2);
    border-color: #eee;
    transition: all .2s ease-in-out;
    box-shadow: 0 16px 32px 0 rgba(48, 55, 66, 0.15);
    transform: translate(0,-5px);
    transition-delay: 0s !important;
  }
/* 导航栏 */
  .el-menu-demo{
      background-color: aqua;
      text-align: center;
  }
</style>