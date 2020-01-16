package com.atguigu.gmall.auth.service;

import com.atguigu.core.bean.Resp;
import com.atguigu.core.exception.UmsException;
import com.atguigu.core.utils.JwtUtils;
import com.atguigu.gmall.auth.config.JwtProperties;
import com.atguigu.gmall.auth.feign.GmallUmsClient;
import com.atguigu.gmall.ums.entity.MemberEntity;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


import javax.annotation.Resource;

/**
 * @author wxy
 * @create 2020-01-15 20:20
 */
@Service
@EnableConfigurationProperties(JwtProperties.class)
public class AuthService {

    @Resource
    private GmallUmsClient gmallUmsClient;

    @Resource
    private  JwtProperties jwtProperties;

    public String accredit(String username, String password) {
        //远程调用feign
        Resp<MemberEntity> memberEntityResp = this.gmallUmsClient.queryUser(username, password);
        MemberEntity memberEntity = memberEntityResp.getData();
        //判断用户是否为空
        if (memberEntity == null) {
            throw new UmsException("lll");
        }
        //生成jwt
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("id", memberEntity.getId());
            map.put("username", memberEntity.getUsername());
            return JwtUtils.generateToken(map, this.jwtProperties.getPrivateKey(), this.jwtProperties.getExpireTime());

            //放入cookie中

        } catch (Exception e) {
            e.printStackTrace();
        }

      return  null;
    }
}
