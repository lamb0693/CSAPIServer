package com.example.apiserver.filter;

import com.example.apiserver.exception.AccessTokenException;
import com.example.apiserver.service.MemberUserDetailsService;
import com.example.apiserver.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

@Log4j2
@AllArgsConstructor
public class JwtTokenCheckFilter extends OncePerRequestFilter {
    private JwtUtil jwtUtil;
    MemberUserDetailsService memberUserDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String TAG = "#### doFilterInternal@JwtTokenCheckFilter : ";
        String path = request.getRequestURI();
        log.info("{} : path = {}", TAG, path);

        if(!path.startsWith("/api/")){
            filterChain.doFilter(request, response);
            log.info("{} : not start with /api - passing filter", TAG);
            return;
        }

        log.info("{} entered", TAG);

        try{
            Map<String, Object> strParseResult = validateAccessToken(request);
            log.info("#### doFilterInternal@JwtTokenCheckFilter result of parse Token : {}", strParseResult);

            //***
            // 인증 mechanism 추가 해야
            // ROle도 추가

            if(SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = memberUserDetailsService.loadUserByUsername(strParseResult.get("tel").toString());
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, "", userDetails.getAuthorities()
                );
                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                log.info("doFilterInternal@JwtTokenCheckFilter : authentication {}", authentication);

                if(!authentication.isAuthenticated()) {
                    log.error("pass Jwt Parsing with no error but not authenticated");
                }

            }
            //***

            filterChain.doFilter(request, response);
        } catch(AccessTokenException accessTokenException ){
            accessTokenException.sendResponseError(response);
        }
    }

    private Map<String, Object> validateAccessToken(HttpServletRequest request) throws AccessTokenException{
        String strHeader = request.getHeader("Authorization");
        log.info("#### entering validateAccessToken");
        if(strHeader == null || strHeader.length() < 8){
            throw new AccessTokenException(AccessTokenException.TokenError.UNACCEPT);
        }

        String tokenType = strHeader.substring(0,6);
        String strToken = strHeader.substring(7);
        if(!tokenType.equalsIgnoreCase("Bearer")){
            throw new AccessTokenException(AccessTokenException.TokenError.BADTYPE);
        }

        try{
            Map<String, Object> values = jwtUtil.validateToken(strToken);
            log.info("#### validationToken success {}", values);
            return values;
        } catch(MalformedJwtException malformedJwtException){
            throw new AccessTokenException(AccessTokenException.TokenError.MALFORM);
        } catch(SignatureException signatureException){
            throw new AccessTokenException(AccessTokenException.TokenError.BADSIGN);
        } catch(ExpiredJwtException expiredJwtException){
            throw new AccessTokenException(AccessTokenException.TokenError.EXPIRED);
        }
    }
}
