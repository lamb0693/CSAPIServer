package com.example.apiserver.filter;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.swing.tree.ExpandVetoException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

@Log4j2
public class LoginFilter extends AbstractAuthenticationProcessingFilter {
    public LoginFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        log.info("######## in LoginFilter");

        if(request.getMethod().equalsIgnoreCase("GET")){
            log.info("##### Get method -> rejecting");
            return null;
        }

        Gson gson = new Gson();
        Map<String, String> jsonData = null;
        try{
            Reader reader = new InputStreamReader(request.getInputStream());
            jsonData = gson.fromJson(reader, Map.class);
        } catch(Exception e){
            log.error("##### json parsing error");
            return null;
        }

        log.info(jsonData);

        // 기본 AuthenticationManager를 SecurityConfig에서 설정 -> MemberUserDetaiols를 이용한 authentication이 일어남
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(
                        jsonData.get("tel"), jsonData.get("password"));

        Authentication authentication =  getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
        log.info("attemptAuthentication@AbstractAuthenticationProcessingFilter : authentication {}", authentication);
        return authentication;
    }
}
