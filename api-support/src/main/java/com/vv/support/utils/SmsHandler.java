package com.vv.support.utils;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import com.vv.common.model.to.SmsTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//导入可选配置类
// 导入对应SMS模块的client
// 导入要请求接口对应的request response类

/**
 * Tencent Cloud Sms Sendsms
 *
 */
@Slf4j
@Component
public class SmsHandler {

    @Value("${tencent.secretId}")
    private String secretId;
    @Value("${tencent.secretKey}")
    private String secretKey;
    @Value("${tencent.smsSdkAppId}")
    private String smsSdkAppId;
    @Value("${tencent.endpoint}")
    private String endpoint;
    @Value("${tencent.region}")
    private String region;
    @Value("${tencent.templateId}")
    private String templateId;
    @Value("${tencent.signName}")
    private String signName;

    public SendSmsResponse smsSend(SmsTo smsTo) throws TencentCloudSDKException {
        // 实例化一个请求对象，根据调用的接口和实际情况，可以进一步设置请求参数
        SendSmsRequest req = new SendSmsRequest();
        //利用自己开通的密钥创建一个客户端
        Credential cred = new Credential(secretId, secretKey);

        // 实例化一个http选项，可选，没有特殊需求可以跳过
        HttpProfile httpProfile = new HttpProfile();

        httpProfile.setReqMethod("POST");

        httpProfile.setConnTimeout(60);
        /* 指定接入地域域名，默认就近地域接入域名为 sms.tencentcloudapi.com ，也支持指定地域域名访问，例如广州地域的域名为 sms.ap-guangzhou.tencentcloudapi.com */
        httpProfile.setEndpoint(endpoint);

        /* 非必要步骤:
         * 实例化一个客户端配置对象，可以指定超时时间等配置 */
        ClientProfile clientProfile = new ClientProfile();
        /* SDK默认用TC3-HMAC-SHA256进行签名
         * 非必要请不要修改这个字段 */
        clientProfile.setSignMethod("HmacSHA256");

        clientProfile.setHttpProfile(httpProfile);

        SmsClient client = new SmsClient(cred, region,clientProfile);

        /* 短信应用ID: 短信SdkAppId在 [短信控制台] 添加应用后生成的实际SdkAppId，示例如1400006666 */
        req.setSmsSdkAppId(smsSdkAppId);
        /* 短信签名内容: 使用 UTF-8 编码，必须填写已审核通过的签名 */
        req.setSignName(signName);
        /* 模板 ID: 必须填写已审核通过的模板 ID */
        req.setTemplateId(templateId);
        /* 模板参数: 模板参数的个数需要与 TemplateId 对应模板的变量个数保持一致，若无模板参数，则设置为空 */
        String[] templateParamSet = {smsTo.getCode()};
        req.setTemplateParamSet(templateParamSet);
        /* 下发手机号码，采用 E.164 标准，+[国家或地区码][手机号]*/
        String[] phoneNumberSet = {"+86" + smsTo.getPhone()};
        req.setPhoneNumberSet(phoneNumberSet);

        SendSmsResponse res = client.SendSms(req);

        log.info(SendSmsResponse.toJsonString(res));

        return res;
    }
}
