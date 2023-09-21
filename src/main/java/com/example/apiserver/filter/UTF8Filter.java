package com.example.apiserver.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@WebFilter(urlPatterns = {"/*"})  //** 작동하려면 KingApplication에 @ServletComponentScan를 추가함
@Log4j2
public class UTF8Filter implements Filter{

   @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        //HttpServletRequest req = (HttpServletRequest) request; // 책에는 이렇게, 어차피 ServletRequest의 method이나 상관 없을것 같음

        request.setCharacterEncoding("utf-8");

        chain.doFilter(request, response);
    }

}
