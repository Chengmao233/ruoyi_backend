package com.ruoyi.system.domain;

import java.util.Map;

public class WechatMinAppLoginVO {
    //"用户唯一标识"
    private String openid;

    //"会话密钥"
    private String session_key;

    //"用户在开放平台的唯一标识符，若当前小程序已绑定到微信开放平台帐号下会返回，详见 UnionID 机制说明。")
    private String unionid;

    //"获取到的凭证")
    private String access_token;

    //"凭证有效时间，单位：秒。目前是7200秒之内的值")
    private int expires_in;

    //"接收电话号码")
    private Map<String,Object> phone_info;

    //"错误码")
    private int errcode;

    //"错误信息")
    private String errmsg;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getSession_key() {
        return session_key;
    }

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public Map<String, Object> getPhone_info() {
        return phone_info;
    }

    public void setPhone_info(Map<String, Object> phone_info) {
        this.phone_info = phone_info;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}
