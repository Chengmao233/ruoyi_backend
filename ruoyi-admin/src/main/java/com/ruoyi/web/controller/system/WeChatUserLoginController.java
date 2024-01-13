package com.ruoyi.web.controller.system;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.ProductTypeRobotCode;
import com.ruoyi.system.domain.UseCode;
import com.ruoyi.system.domain.WeChatPhoneInfo;
import com.ruoyi.system.domain.WeChatUserInfo;
import com.ruoyi.system.service.IProductTypeRobotCodeService;
import com.ruoyi.system.service.IUseCodeService;
import com.ruoyi.web.controller.utils.WeChatUtil;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.ruoyi.common.utils.PageUtils.startPage;

/**
 * @Author dw
 * @ClassName WeChatUserLoginController
 * @Description
 * @Date 2020/8/28 14:12
 * @Version 1.0
 */
@RestController
@RequestMapping("/system/wxLogin")
public class WeChatUserLoginController extends BaseController {
//
//    @Resource
//    private IUserService userService;

    @Autowired
    private IUseCodeService useCodeService;

    @Autowired
    private IProductTypeRobotCodeService productTypeRobotCodeService;

    /**
     * 微信用户登录详情
     */
    @PostMapping("/login")
    public AjaxResult user_login(@RequestBody WeChatUserInfo weChatUserInfo) {
        AjaxResult ajaxResult = new AjaxResult();
        // 2.开发者服务器 登录凭证校验接口 appId + appSecret + 接收小程序发送的code
        JSONObject SessionKeyOpenId = WeChatUtil.getSessionKeyOrOpenId(weChatUserInfo.getCode());
        // 3.接收微信接口服务 获取返回的参数
        String openid = SessionKeyOpenId.get("openid", String.class);
        String sessionKey = SessionKeyOpenId.get("session_key", String.class);
//        ajaxResult.put("sessionKey",sessionKey);
        ajaxResult.put("openid",openid);

        // 用户非敏感信息：rawData
        // 签名：signature
//        JSONObject rawDataJson = JSONUtil.parseObj(weChatUserInfo.getRawData());
        // 4.校验签名 小程序发送的签名signature与服务器端生成的签名signature2 = sha1(rawData + sessionKey)
        //   String signature2 = DigestUtils.sha1Hex(weChatUserInfo.getRawData() + sessionKey);
        // if (!weChatUserInfo.getSignature().equals(signature2)) {
        //   return ResultInfo.error( "签名校验失败");
        //}
        //encrypteData比rowData多了appid和openid
//        JSONObject userInfo = WeChatUtil.getUserInfo(weChatUserInfo.getEncryptedData(),
//            sessionKey, weChatUserInfo.getIv());
//        ajaxResult.put("userInfo",userInfo);
        return ajaxResult;
    }
    @PostMapping("/info")
    public AjaxResult info(@RequestBody WeChatUserInfo weChatUserInfo) {
        String token = WeChatUtil.storeAccessToken();
        if(StringUtils.isEmpty(token)){
            return AjaxResult.error("获取token异常");
        }
        JSONObject phoneNumberJson = WeChatUtil.getPhoneNumber(weChatUserInfo.getCode(), token);
        if(StringUtils.isEmpty(phoneNumberJson)){
            return AjaxResult.error("获取手机号异常");
        }
        WeChatPhoneInfo phoneInfo = phoneNumberJson.get("phone_info", WeChatPhoneInfo.class);
        if(phoneInfo==null){
            return AjaxResult.error("获取手机信息异常");
        }
        System.out.println(phoneInfo.getPurePhoneNumber());
        return AjaxResult.success(phoneInfo.getPurePhoneNumber());
    }

    //用户已经扫过的码返回
    @GetMapping("/hasCode")
    public TableDataInfo list(UseCode useCode)
    {
//        if(useCode.getCodeId()==null){
//            return getDataTable(new ArrayList<>());
//        }
        if(StringUtils.isEmpty(useCode.getOpenid())){
            return getDataTable(new ArrayList<>());
        }
        startPage();
        List<UseCode> list = useCodeService.selectUseCodeList(useCode);
        return getDataTable(list);
    }
    //详情返回
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        ProductTypeRobotCode typeRobotCode = productTypeRobotCodeService.selectProductTypeRobotCodeById(id);
        if(typeRobotCode==null){
            return AjaxResult.error("没有码");
        }
        return AjaxResult.success(typeRobotCode);
    }
    //check
    @GetMapping(value = "/check")
    public AjaxResult check(UseCode useCode)
    {
        if(useCode.getCodeId()==null){
            return AjaxResult.error("缺少码id");
        }
        if(StringUtils.isEmpty(useCode.getOpenid())){
            return AjaxResult.error("缺少码openid");
        }
        AjaxResult ajaxResult = new AjaxResult();
        Integer flag = useCodeService.checkUser(useCode);
        if(flag.equals(0)){
            useCodeService.insertUseCode(useCode);
        }
        ajaxResult.put("flag",flag);
        return ajaxResult;
    }
}
