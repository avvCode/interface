package com.vv.common.utils;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTHeader;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.RegisteredPayload;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
@Slf4j
@Component
public class TokenUtils {
    /**
     * 加盐
     */
    private final String SALT = "API.VV.TOKEN";

    /**
     * 签名
     */
    private final JWTSigner signer = JWTSignerUtil.hs512(SALT.getBytes());

    public  String getToken(int userId,String userEmail){
        DateTime now = DateTime.now();
        DateTime newTime = now.offsetNew(DateField.HOUR, 720); //过期时间720小时
        Map<String, Object> map = new HashMap<String, Object>() {
            private static final long serialVersionUID = 1L;
            {
                put("userId", userId);
                put("userEmail", userEmail);
                //签发时间
                put(RegisteredPayload.ISSUED_AT, now);
                //过期时间
                put(RegisteredPayload.EXPIRES_AT, newTime);
            }
        };
        return JWTUtil.createToken(map,signer);
    }

    /**
     * 验证Token是否过期
     * @param token
     * @return
     */
    public boolean verifyToken(String token){
        try {
            JWT jwt = JWTUtil.parseToken(token);
            String algorithm = jwt.getAlgorithm();
            String SIGN_ALGORITHM = "HS512";
            if (!SIGN_ALGORITHM.equals(algorithm)){
                return false;
            }
            return jwt.setSigner(signer)
                    .verify();
        }catch (Exception e){
            log.error("verifyKey方法error--->{}",String.valueOf(e));
            return false;
        }
    }

    /**
     * 验证token是否过期
     * @param token
     * @return
     */
    public  boolean verifyTime (String token){
        JWT jwt = JWTUtil.parseToken(token);
        boolean verifyTime = jwt.validate(0);
        return !verifyTime;
    }
}
