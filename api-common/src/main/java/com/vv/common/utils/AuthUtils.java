package com.vv.common.utils;

import cn.hutool.crypto.digest.Digester;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.vv.common.constant.AuthConstant;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class AuthUtils {

    //生成accessKey
    private final JWTSigner signer = JWTSignerUtil.hs512(AuthConstant.AUTH_KEY.getBytes());


    /**
     * 生成 accessKey
     *
     * @return
     */
    public String accessKey(String phone) {
        Digester digester = new Digester(AuthConstant.ALGORITHM);
        return digester.digestHex(phone);
    }

    /**
     * 生成 secretKey
     *
     * @param userEmail
     * @return
     */
    public String secretKey(String phone,String userEmail) {
        String s = phone + userEmail;
        UUID uuid = UUID.nameUUIDFromBytes(s.getBytes());
        String uid = uuid.toString().replaceAll("-", "");
        Map<String, Object> secMap = new HashMap<String, Object>() {
            private static final long serialVersionUID = 1L;

            {
                put("uuid", uid);
            }
        };
        return JWTUtil.createToken(secMap, signer);
    }

    public String token(String phone, String userEmail, String accessKey, String secretKey) {
        Map<String, Object> map = new HashMap<String, Object>() {
            private static final long serialVersionUID = 1L;
            {
                put("phone", phone);
                put("userEmail", userEmail);
                put("accessKey", accessKey);
                put("secretKey", secretKey);
            }
        };
        //生成token，并设置签名
        return JWTUtil.createToken(map, signer);
    }
}
