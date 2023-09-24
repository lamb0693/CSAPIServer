package com.example.apiserver.test;

import com.example.apiserver.util.JwtUtil;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
@Log4j2
public class JwtUtilTest {
    @Autowired
    private JwtUtil jwtUtil;
    private final String TAG = "JwtUtilTest :";

    @Test
    public void testGenerateToken() {
        Map<String, Object> claimMap = Map.of("testid", "testString");
        String token = jwtUtil.generateToken(claimMap, 5);
        log.info(TAG + "Generated Token {}", token);
    }

    @Test
    public void testParseToken(){
        Map<String, Object> claimMap = Map.of("testid", "testString");
        String token = jwtUtil.generateToken(claimMap, 5);
        Map<String, Object> result = jwtUtil.validateToken(token);
        log.info(TAG+"result : {}", result);
    }
}
