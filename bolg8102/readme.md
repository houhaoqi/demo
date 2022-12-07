# 简易博客项目
(springboot+jwt+shiro+vue+elementUI+axios+redis+mysql)
前端blog-client：vue，elementui，axios
后端blog-server：springboot，shiro，jwt，mysql，mybatisplus，redis，docker
## 搭建文档

[搭建文档1](https://juejin.cn/post/6844903823966732302#heading-23)
[搭建文档2](https://blog.csdn.net/weixin_43247803/article/details/113666136)
> 段落引用
## 一、后端 blog-server

### 1.创建springboot项目"vueblog-demo-8102"

1. 构建项目架构，创建数据库，导入数据
2. 引入pom.xml文件，配置properties.yml文件
3. 使用mybatisplus插件
4. 配置自动生成代码工具CodeGenerator.java
5. 测试查询结果
6. 封装结果集Result.java，测试5
7. shiro整合jwt用redis存储
[1.shiro安全框架参考](https://blog.csdn.net/bbxylqf126com/article/details/110501155?ops_request_misc=%257B%2522request%255Fid%2522%253A%2522166255896216782428628821%2522%252C%2522scm%2522%253A%252220140713.130102334..%2522%257D&request_id=166255896216782428628821&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~top_positive~default-1-110501155-null-null.142^v47^body_digest,201^v3^add_ask&utm_term=shiro&spm=1018.2226.3001.4187)
[2.jwt安全校验参考](https://blog.csdn.net/weixin_45070175/article/details/118559272?ops_request_misc=%257B%2522request%255Fid%2522%253A%2522166255794916800180658396%2522%252C%2522scm%2522%253A%252220140713.130102334..%2522%257D&request_id=166255794916800180658396&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~top_positive~default-1-118559272-null-null.142^v47^body_digest,201^v3^add_ask&utm_term=jwt&spm=1018.2226.3001.4187)


## 二、前端 blog-client

### 1.创建项目
安装依赖
```js
# 切换到项目根目录
cd vueblog-vue

# 安装element-ui
cnpm install element-ui --save

# 安装axios
cnpm install axios --save

# 安装 markdown 插件
cnpm install mavon-editor --save
# 用于解析md文档
cnpm install markdown-it --save
# md样式
cnpm install github-markdown-css

```

引入依赖main.js
```js
// 引入element ui
import Element from 'element-ui'
import "element-ui/lib/theme-chalk/index.css"
Vue.use(Element)

// 引入axios
import axios from 'axios'
Vue.prototype.$axios = axios //

// 全局注册githubmarkdown
import Vue from 'vue'
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'

// use
Vue.use(mavonEditor)

```







