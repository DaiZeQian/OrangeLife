package com.oneorange.manager;

/**
 * Created by admin on 2016/6/14.
 * 服务器url
 */
public class UrlConfig {

    //公司测试地址
    public static final String HOSTDOMAIN = "http://192.168.1.141:82/";
    public static final String API_BASE = HOSTDOMAIN + "api/";

    /**
     * 用户
     */
    public static final String USER = API_BASE + "?m=usr";

    /**
     * 发送验证码
     */
    public static final String SENDVERIFY_URL = API_BASE + "?f=sendCodeSMS";
    /**
     * 验证码登录或者注册
     */
    public static final String REGISITER_URL = HOSTDOMAIN + "oauth/access_token";

    /**
     * 获取验证码
     */
    public static final String GETCODE_URL = API_BASE + "sendCodeSMS";
    /**
     * 注册
     */
    public static final String REGISITRER_URL = API_BASE + "signUp";
    /**
     * 上传图片
     */
    public static final String UPDATEIMAGE_URL = API_BASE + "uploadGroupeAvatar";
    /**
     * token修改密码
     */
    public static final String CHANGEPSDBYTOKEN_ULR = API_BASE + "byToken";
    /**
     * 重设密码  oldpsd  与psd
     */
    public static final String CHANGEPSDMYOLD_URL=API_BASE+"byProfile";
    /**
     * 密码登录
     */
    public static final String LOGINBYPSD_URL = USER + "&f=loginByPwd";
    /**
     * 获取用户信息 （不同于获取自己的信息）
     */
    public static final String GETFRIENDINFO_URL = API_BASE + "friendInfo";
    /**
     * 获取好友列表
     */
    public static final String GETCONTACT_URL = API_BASE + "friendlist";
    /**
     * 搜索用户列表
     */
    public static final String SEARCHUSER_URL = API_BASE + "searchUser";
    /**
     * 获取用户信息
     */
    public static final String GETUSERINFO_URL = API_BASE + "userInfo";
    /**
     * 修改用户信息
     */
    public static final String UPDATEUSERINFO_URL = API_BASE + "saveUserInfo";


}
