<template>
  <div class="blogs">
    <Header></Header>
    <el-container>
      <!-- <el-aside width="20%">Aside</el-aside> -->

      <el-container>
        <el-main>
          <!-- 走马灯 -->
          <el-carousel :interval="1500" type="card" height="200px">
            <el-carousel-item v-for="item in 3" :key="item">
              <h3 class="medium">{{ item }}</h3>
            </el-carousel-item>
          </el-carousel>
          <!-- 博客条款 -->
          <el-timeline class="timeline">
            <el-timeline-item :timestamp="blog.created" placement="top" v-for="(blog,key) in blogs" :key=key>
              <router-link :to="{  name: 'BlogDetail', params: {blogid: blog.id}}">
                <el-card>
                  <h4>
                    {{blog.title}}
                  </h4>
                  <p>{{blog.description}}</p>
                </el-card>
                </router-link>
            </el-timeline-item>
          </el-timeline>
          <el-pagination class="mage" background layout="prev, pager, next" :current-page="currentPage"
            :page-size="pageSize" :total="total" @current-change=page>
          </el-pagination>
        </el-main>
      </el-container>

      <!-- <el-aside width="20%">Aside</el-aside> -->
    </el-container>
    
    <div class="el-footer">footer</div>
  </div>

  
</template>

<script>
//引入header组件
import Header from "../components/Header";

export default {
  data () {
    return {
      value: new Date(),
      blogs: {},
      currentPage: 1,
      total: 0,
      pageSize: 5
    }
  },
  name: "Blogs",
  components: { Header },

  methods: {
    page (currentPage) {
      const _this = this
      _this.$axios.get("/blogs?currentPage=" + currentPage).then(res => {
        var data = res.data.data
        _this.blogs = data.records
        _this.currentPage = data.current
        _this.pageSize = data.size
        _this.total = data.total

      })
    }
  },
  created () {
    this.page(1)
  },
  mounted(){
    this.$notify({
        title: '欢迎 ^=^ !',
        message: '欢迎回来！',
        duration: 1500
    });
  }
}
</script>

<style scoped>
  .blogs{
    text-decoration: none;
    margin: 0 auto;
    width: 60%;
    height: 100%;
    
  }
  .blogs a{
    text-decoration: none;
  }
  .el-header, .el-footer {
    /* background-color: #B3C0D1; */
    color: #333;
    text-align: center;
    height: 100%;
  }
  
  .el-aside {
    background-color: #fff0bf9f;
    color: #333;
    text-align: center;
    margin: 10px auto;
    border-radius: 2%;
    height: 200px;
    width: 200px;
  }
  
  .el-main {
    background-color: #a6a6a66e;
    color: rgb(5, 5, 5);
    text-align: center;
    border-radius: 1%;
  }
  .el-timeline{
    width: 65%;
    margin: 0 auto;
  }
  /* 时间卡片 */
  .el-card{
    border-radius: 3%;
    background-color: rgba(158, 234, 255, 0.551);
    
  }
  .el-card:hover{
    box-shadow: 0 16px 32px 0 rgba(42, 49, 60, 0.866);
    transform: translate(0,-5px);
    transition-delay: 0s !important;
  }

  /* 走马灯 */
  .el-carousel{
    width: 80%;
    margin: 0 auto;
  }
   .el-carousel__item h3 {
    color: #000000;
    font-size: 14px;
    opacity: 0.75;
    line-height: 200px;
    margin: 0;
    
    background-image: url(../assets/14.jpg);
    background-size: cover;
    background-repeat: no-repeat;
    background-attachment: fixed;
  }
  .el-carousel__item{
    border-radius: 5%;
  }
  .el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
  }
  
  .el-carousel__item:nth-child(2n+1) {
    background-color: #d3dce6;
  }

</style>