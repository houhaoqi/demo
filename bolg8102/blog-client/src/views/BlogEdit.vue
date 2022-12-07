<template>
  <div class="blogedit">
    <Header></Header>
    <div class="m-content">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
        <el-form-item>
          <el-button type="primary" @click="submitForm('ruleForm')">发 布</el-button>
          <el-button @click="resetForm('ruleForm')">重 置</el-button>
        </el-form-item>
        
        <el-form-item label="标 题" prop="title">
          <el-input v-model="ruleForm.title"></el-input>
        </el-form-item>

        <el-form-item label="摘 要" prop="description">
          <el-input type="textarea" v-model="ruleForm.description"></el-input>
        </el-form-item>

        <el-form-item label="内 容" prop="content">
          <mavon-editor v-model="ruleForm.content"></mavon-editor>
        </el-form-item>

        
      </el-form>
    </div>
  </div>
</template>

<script>
import Header from "../components/Header";
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'

export default {
  name: "BlogEdit",
  components: { Header },
  data () {
    return {
      ruleForm: {
        id: '',
        title: '',
        description: '',
        content: ''
      },
      rules: {
        title: [
          { required: true, message: '请输入标题', trigger: 'blur' },
          { min: 1, max: 999, message: '标题不为空', trigger: 'blur' }
        ],

        description: [
          { required: true, message: '请输入摘要', trigger: 'blur' },
        ],
        content: [
          { required: true, message: '请输入内容', trigger: 'blur' },
        ]
      }
    };
  },
  methods: {
    submitForm (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          const _this = this
          this.$axios.post('/blog/edit', this.ruleForm, {
            headers: {
              "Authorization": localStorage.getItem("token")
            }
          }).then(res => {
            _this.$alert('操作成功', '提示', {
              confirmButtonText: '确定',
              callback: action => {
                _this.$router.push("/blogs")
              }
            });
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
  created () {
    //获取动态路由的 blogid
    const blogId = this.$route.params.blogid
    const _this = this
    if (blogId) {
      this.$axios.get("/blog/" + blogId).then(res => {
        const blog = res.data.data
        _this.ruleForm.id = blog.id
        _this.ruleForm.title = blog.title
        _this.ruleForm.description = blog.description
        _this.ruleForm.content = blog.content

      })
    }
  },
  mounted(){
    this.$notify({
        title: '编辑 ^=^!',
        message: '随想一下',
        duration: 1500
    });
  }
}
</script>

<style scoped>
  .blogedit{
    width: 70%;
    margin: 0 auto;
    height: 1100px;
  }
</style>