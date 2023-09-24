package com.example.apiserver.exception;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;

import java.awt.*;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

@Log4j2
public class AccessTokenException extends RuntimeException{
    TokenError tokenError;

    @Getter
    public enum TokenError{
        UNACCEPT(401, "Token is null or too short"),
        BADTYPE(401, "not bearer"),
        MALFORM(403, "malformed"),
        BADSIGN(403, "Bead signed"),
        EXPIRED(403, "expired");

        private  int status;
        private String msg;

        TokenError(int status, String msg){
            this.status = status;
            this.msg = msg;
        }
    }

    public AccessTokenException(TokenError tokenError) {
        super(tokenError.name());
        this.tokenError = tokenError;
    }

    public void sendResponseError(HttpServletResponse response){
        log.error("sendResponseError@AccessTokenException : {} {}", tokenError.getStatus(), tokenError.getMsg());
        response.setStatus(tokenError.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Gson gson = new Gson();

        String strErrorResponse = gson.toJson(Map.of("msg", tokenError.getMsg(), "time", new Date()));
        try{
            log.error("sendResponseError@AccessTokenException : {}", strErrorResponse);
            response.getWriter().println(strErrorResponse);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
