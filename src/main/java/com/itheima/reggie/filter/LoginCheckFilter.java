package com.itheima.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否已经完成登录
 */
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //1、获取本次请求的URI
        String requestURI = request.getRequestURI();
        //定义一些请求的路径(不需要处理的请求)
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**"
        };
        //2、判断本次请求是否需要进行处理(其实就是检查登录的状态)
        boolean check = check(urls, requestURI);

        //3、如果不需要进行处理，则直接放行
        if (check){
            filterChain.doFilter(request, response);
            return;
        }
        //4、判断登录的状态，如果已经登录则直接放行
        if (request.getSession().getAttribute("employee") != null){
            filterChain.doFilter(request, response);
            return;
        }
        //5、如果未登录则返回登录的结果,通过输出流的方式向着客户端页面响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * 1、判断本次请求是否需要进行放行
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls,String requestURI) {
        for (String url : urls){
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {return true;}
        }
        return false;
    }

}
