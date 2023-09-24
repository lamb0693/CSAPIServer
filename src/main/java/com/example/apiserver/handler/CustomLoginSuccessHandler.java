package com.example.apiserver.handler;

import com.example.apiserver.util.JwtUtil;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Map;

@Log4j2
@AllArgsConstructor
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
    private JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String TAG = "##### onAuthenticationSuccess";
        log.info("#### in onAuthenticationSuccess");

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        log.info("{} username : {}", TAG, authentication.getName());
        Map<String, Object> claim = Map.of("tel", authentication.getName(), "authorities", authentication.getAuthorities());
        String accessToken = jwtUtil.generateToken(claim, 2*60*24); //2일
        String refreshToken = jwtUtil.generateToken(claim, 5*60*24); //5일

        Map <String, String> keyMap = Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        );

        Gson gson = new Gson();
        String strJson = gson.toJson(keyMap);

        response.getWriter().println(strJson);
    }
}
