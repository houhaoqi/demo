package com.haoqi.filter;

import com.alibaba.fastjson.JSON;
import com.haoqi.common.BaseContext;
import com.haoqi.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * f1.使用过滤器或者拦截器，在过滤器或者拦截器中判断用户是否已经完成登录，
 * 如果没有登录则跳转到登录页面
 * @author haoqi
 * @Date 2022/7/22 - 11:49
 */
// 自定义过滤器: 检查用户是否已经完成登录
//  urlPatterns = "/*": 拦截所有请求
@WebFilter(filterName = "LoginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    //匹配路径支持通配符
    public static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        log.info("拦截请求：{}",request.getRequestURI());
        //1.获取请求的uri
        String requestURI = request.getRequestURI();
        //2.预定义不需处理的路径，初始访问等界面
        String[] urls = new String[]{
                "/employee/login",//登录 放行
                "/employee/logout",
                "/backend/**",  //后端静态资源
                "/front/**",     //前端静态资源
                "/common/**",
                "/user/sendMsg",//移动端发送短息
                "/user/login"   //移动端登录
        };
        //3.判断请求是否需要处理，否则直接放行
        boolean check = check(urls,requestURI);
        //放行
        if(check){
            log.info("不需处理本次请求：{}",requestURI);
            filterChain.doFilter(request,response);
            return;
        }
        //4-1、后台判断 登录状态(session中含有employee的登录信息)，如果已经登录，则直接放行
        Long empId = (Long)request.getSession().getAttribute("employee");
        if (empId != null){
            log.info("已登录用户employeeID：{}",empId);
            //调用BaseContext.java获取employeeId
            BaseContext.setCurrentId(empId);
            //在MyMetaObjectHandler的方法中调用BaseContext获取登录用户的id
            filterChain.doFilter(request,response);
            return;
        }
        // 4-2、前台判断 移动端(消费者端)登录状态(session中含有user的登录信息)，如果已经登录，则直接放行
        Long userId = (Long)request.getSession().getAttribute("user");
        if (userId != null){
            log.info("已经登录userID:{}",userId);
            // 自定义元数据对象处理器 MyObjectHandler中需要使用 登录用户id
            // 通过ThreadLocal set和get用户id
            BaseContext.setCurrentId(userId);
            filterChain.doFilter(request,response);
            return;
        }

        //5.未登录，则返回登录页面，利用输出流向页面响应数据
        // 返回结果需要是 backend/js/request.js 中写好的返回结果
        log.info("未登录用户");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }
    //路径匹配，检查本次请求是否需要放行
    private boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url,requestURI);
            if (match){
                return true;
            }
        }
        return false;
    }
}
