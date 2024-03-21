package com.example.bypassjava.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter("/*")
public class MyFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException {

        try {
            if(servletRequest.getContentLength() >= 1145) {
                servletResponse.getWriter().println("It's tool long!!!!");
            } else {
                filterChain.doFilter(servletRequest, servletResponse);
            }
        } catch (Exception e) {
            servletResponse.getWriter().println(e.getMessage());
        }
    }

    @Override
    public void destroy() {
    }
}
