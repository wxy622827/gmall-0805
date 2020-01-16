package com.atguigu.gmall.auth.config;

import com.atguigu.core.utils.RsaUtils;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


import javax.annotation.PostConstruct;

/**
 * @author wxy
 * @create 2020-01-15 19:57
 */
@ConfigurationProperties(prefix="jwt.token")
@Data
public class JwtProperties {
    private  String pubKeyPath;
    private  String priKeyPath;
    private  String secret;
    private  String cookieName;
    private  Integer expireTime;

    private PublicKey publicKey;
    private PrivateKey privateKey;

    @PostConstruct
    public  void init() throws Exception {
        File pubFile = new File(pubKeyPath);
        File priFile = new File(priKeyPath);
        if(!pubFile.exists() || !priFile.exists()){
            RsaUtils.generateKey(pubKeyPath,priKeyPath,secret);
        }
        publicKey=RsaUtils.getPublicKey(pubKeyPath);
        privateKey=RsaUtils.getPrivateKey(priKeyPath);
    }

}
