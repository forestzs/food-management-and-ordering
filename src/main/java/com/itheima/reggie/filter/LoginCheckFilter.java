package com.itheima.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否登录的过滤器
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    //路径匹配器
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1.获取本次请求的URI
        String requestURI = request.getRequestURI();
        log.info("拦截到请求：{}", requestURI);
        //2.定义直接放行资源
        String[] urls = new String[] {
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg", //短信发送放行
                "/user/login" //验证码登录放行
        };
        //3.判断资源是否匹配urls，若匹配直接放行
        boolean match = check(urls, requestURI);
        //4.资源可以直接放行
        if(match) {
            log.info("本次请求无需处理");
            filterChain.doFilter(request, response);
        }
        //5.1 不能直接放行，检测电脑端登陆状态
        else if(request.getSession().getAttribute("employee") != null) {
            //已登录，放行
            log.info("用户已登录，用户id：{}", request.getSession().getAttribute("employee"));

            //向线程中注入当前会话的用户id
            Long webId = (Long)request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(webId);

            //过滤器放行
            filterChain.doFilter(request, response);
        }
        //5.2 不能直接放行，检测移动端登陆状态
        else if(request.getSession().getAttribute("user") != null) {
            //已登录，放行
            log.info("用户已登录，用户id：{}", request.getSession().getAttribute("user"));

            //向线程中注入当前会话的用户id
            Long mobileId = (Long)request.getSession().getAttribute("user");
            BaseContext.setCurrentId(mobileId);

            //过滤器放行
            filterChain.doFilter(request, response);
        }
        else { //双端均未登录
            log.info("用户未登录");
            response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        }
    }

    /**
     * 检查本次请求路径（requestURI）匹配直接放行（urls）的方法
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls, String requestURI) {
        for(String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match) {
                return true;
            }
        }
        return false;
    }
}