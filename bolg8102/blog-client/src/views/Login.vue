<template>
  <div class="header">
    <el-container>
      <el-header></el-header>
      <el-main>
        <img class="mlogo" src="../assets/header.jpg" alt="logo图片" />
        <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
          <el-form-item label="用户姓名" prop="username">
            <el-input v-model="ruleForm.username"></el-input>
          </el-form-item>
          <el-form-item label="用户密码" prop="password">
            <el-input v-model="ruleForm.password"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitForm('ruleForm')">登录</el-button>
            <el-button @click="resetForm('ruleForm')">重置</el-button>
          </el-form-item>
        </el-form>
      </el-main>
    </el-container>
  </div>
</template>
<script>
  //博客登录页面
  
  export default {
    name: "Login",
    data () {
      return {
        ruleForm: {
          username: 'haoqi',
          password: '111111'
        },
        rules: {
          username: [
            { required: true, message: '请输入用户名称', trigger: 'blur' },
            { min: 3, max: 15, message: '长度在 3 到 15 个字符', trigger: 'blur' }

          ],
          password: [
            { required: true, message: '请选择密码', trigger: 'blur' }
          ]
        }
      };
    },
    methods: {
      submitForm (formName) {

          //获取整个vue的this
          const _this = this
          console.log('校验成功')

          this.$refs[formName].validate((valid) => {
          if (valid) {
              //alert('submit!');
              //提交逻辑
              this.$axios.post('/login',this.ruleForm).then((res)=>{
                  console.log(res)
                  //存储(共享)全局变量jwt和userInfo
                  const jwt = res.headers['authorization']
                  const userInfo = res.data.data
                  _this.$store.commit('SET_TOKEN', jwt)
                  _this.$store.commit('SET_USERINFO', userInfo)
                  _this.$router.push("/blogs") //跳转blogs页面
              })
                  
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      resetForm (formName) {
        this.$refs[formName].resetFields();
      }
    },
    mounted(){
      this.$notify({
          title: 'hello !',
          message: '还没登录哦！',
          duration: 1500
      });
    }
  }
</script>

<style scoped>

.header{
  height: 1000px;
}
.el-main {
  background-color: #e5fffd7c;
  color: rgb(199, 0, 0);
  text-align: center;
  line-height: 50px;
  width: 30%;
  height: 100%;
  margin: 100px auto;
  border-radius: 5%;
}
.mlogo {
  /* padding: 10px 0; */
  margin: 0 auto;
  height: 25%;
  width: 25%;
  border-radius: 50%;
}

</style>
