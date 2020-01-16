package com.atguigu.gmall.auth.controller;

import com.atguigu.core.bean.Resp;
import com.atguigu.core.utils.CookieUtils;
import com.atguigu.gmall.auth.config.JwtProperties;
import com.atguigu.gmall.auth.service.AuthService;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wxy
 * @create 2020-01-15 20:14
 */
@RestController
@RequestMapping("accredit")
public class AuthController {
    @Resource
    private AuthService authService;

    @Resource
    private JwtProperties jwtProperties;
     public Resp<Object> accredit(
            @RequestParam("username")String username,
            @RequestParam("password")String password,
            HttpServletRequest request,
            HttpServletResponse reponse
    ){
        String token = this.authService.accredit(username, password);
        if (StringUtils.isNotBlank(token)){
            CookieUtils.setCookie(request,reponse,jwtProperties.getCookieName(),token ,jwtProperties.getExpireTime()*60);

            }
            return Resp.ok(null);
    }
}
