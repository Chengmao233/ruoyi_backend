package com.ruoyi.web.controller.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.http.*;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.utils.http.HttpUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Author dw
 * @ClassName WeChatUtil
 * @Description
 * @Date 2020/8/28 10:56
 * @Version 1.0
 */
public class WeChatUtil {



    public static JSONObject getSessionKeyOrOpenId(String code) {
        String requestUrl = "https://api.weixin.qq.com/sns/jscode2session";
        HashMap<String, Object> requestUrlParam = new HashMap<>();
        //小程序appId
        requestUrlParam.put("appid", "wx7ee586d59cbc9355");
        //小程序secret
        requestUrlParam.put("secret", "3326582281da7fccc30e124e96fdb346");
        //小程序端返回的code
        requestUrlParam.put("js_code", code);
        //默认参数
        requestUrlParam.put("grant_type", "authorization_code");
        //发送post请求读取调用微信接口获取openid用户唯一标识
        String result = HttpUtil.get(requestUrl, requestUrlParam);
        JSONObject jsonObject = JSONUtil.parseObj(result);
        return jsonObject;
    }

    public static String storeAccessToken() {
        String url = "https://api.weixin.qq.com/cgi-bin/token";
        HashMap<String, Object> requestUrlParam = new HashMap<>();
        //小程序appId
        requestUrlParam.put("appid", "wx7ee586d59cbc9355");
        //小程序secret
        requestUrlParam.put("secret", "3326582281da7fccc30e124e96fdb346");
        //默认参数
        requestUrlParam.put("grant_type", "client_credential");
        //发送post请求读取调用微信接口获取openid用户唯一标识
        String result = HttpUtil.get(url, requestUrlParam);
        JSONObject jsonObject = JSONUtil.parseObj(result);
        String sucessKey = "access_token";
        if (jsonObject.containsKey(sucessKey)) {
            return jsonObject.get("access_token").toString();
        } else {
            return null;
        }
    }

    public static JSONObject getPhoneNumber(String code, String accessToken) {
        String result = null;
        try {
            // 接口调用凭证：accessToken
            String baseUrl = "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=" + accessToken;
            HashMap<String, Object> requestParam = new HashMap<>();
            // 手机号调用凭证
            requestParam.put("code", code);
            // 发送post请求读取调用微信接口获取openid用户唯一标识
            String jsonStr = JSONUtil.toJsonStr(requestParam);
            HttpResponse response = HttpRequest.post(baseUrl)
                    .header(Header.CONTENT_ENCODING, "UTF-8")
                    // 发送json数据需要设置contentType
                    .header(Header.CONTENT_TYPE, "application/x-www-form-urlencoded")
                    .body(jsonStr)
                    .execute();
            if (response.getStatus() == HttpStatus.HTTP_OK) {
                result = response.body();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSONUtil.parseObj(result);
    }

    public static JSONObject getUserInfo(String encryptedData, String sessionKey, String iv) {
        // 被加密的数据
        byte[] dataByte = Base64.decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.decode(sessionKey);
        // 偏移量
        byte[] ivByte = Base64.decode(iv);
        try {
            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            // 初始化
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                return JSONUtil.parseObj(result);
            }else{
                System.out.println("null");
                System.out.println("null");
                System.out.println("null");
                System.out.println("null");
                System.out.println("null");
            }
        } catch (Exception e) {

        }
        return null;
    }
}
