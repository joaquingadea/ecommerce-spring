package com.api.ecommerce.shared.security.cookie;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class CookieService {
    public void addCookie(String cookieName, Object cookieValue, HttpServletResponse response, int maxAge) {
        ResponseCookie cookie = ResponseCookie.from(cookieName,cookieValue.toString())
                .secure(true)
                .httpOnly(true)
                .sameSite("None")
                .maxAge(maxAge)
                .path("/")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE,cookie.toString());
    }
    public void deleteCookie(String cookieName,HttpServletResponse response){
        ResponseCookie cookie = ResponseCookie.from(cookieName,null)
                .secure(true)
                .httpOnly(true)
                .maxAge(0)
                .sameSite("None")
                .path("/")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE,cookie.toString());
    }

}
