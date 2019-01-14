package com.oneorange.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by admin on 2016/6/14.
 */
public class UserPrefenceManager {
    /**
     * 保存Preference的name
     */
    public static final String PREFERENCE_NAME = "userinfo";
    private static SharedPreferences mSharedPreferences;
    private static UserPrefenceManager mPreferencemManager;
    private static SharedPreferences.Editor editor;

    private String USER_TOKEN = "token";
    private String REFESH_TOKEN = "refresh_token";//刷新token
    private String USER_CURRENT_NICKNAME = "nickname";
    private String USER_CURRENT_AVATAR = "avatar";
    private String USER_PHONE = "phone";
    private String USER_PSD = "password";
    private String USER_HXID = "huanxinid";
    private String USER_EXTRA_PSD="extra_psd";
    private String USER_GENDER = "gender";

    private UserPrefenceManager(Context cxt) {
        mSharedPreferences = cxt.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
    }


    public static synchronized void init(Context cxt) {
        if (mPreferencemManager == null) {
            mPreferencemManager = new UserPrefenceManager(cxt);
        }
    }

    /**
     * 单例模式，获取instance实例
     *
     * @return
     */
    public synchronized static UserPrefenceManager getInstance() {
        if (mPreferencemManager == null) {
            throw new RuntimeException("please init first!");
        }

        return mPreferencemManager;
    }


    public void setToken(String token) {
        editor.putString(USER_TOKEN, token);
        editor.commit();
    }

    public String getToken() {
        return mSharedPreferences.getString(USER_TOKEN, null);
    }


    public void setRefeshToken(String refresh_token) {
        editor.putString(REFESH_TOKEN, refresh_token);
        editor.commit();
    }

    public String getRefeshToken() {
        return mSharedPreferences.getString(REFESH_TOKEN, null);
    }

    public void setCurrentNickName(String nickname) {
        editor.putString(USER_CURRENT_NICKNAME, nickname);
        editor.commit();
    }

    public String getCurrentNickName() {
        return mSharedPreferences.getString(USER_CURRENT_NICKNAME, null);
    }

    public void setCurrentAvatar(String avatar) {
        editor.putString(USER_CURRENT_AVATAR, avatar);
        editor.commit();
    }

    public String getCurrentAvatar() {
        return mSharedPreferences.getString(USER_CURRENT_AVATAR, null);
    }

    public void setUserPhone(String phone) {
        editor.putString(USER_PHONE, phone);
        editor.commit();
    }

    public String getUserPhone() {
        return mSharedPreferences.getString(USER_PHONE, null);
    }

    public void setUserPsd(String psd) {
        editor.putString(USER_PSD, psd);
        editor.commit();
    }


    public String getUserPsd() {
        return mSharedPreferences.getString(USER_PSD, null);
    }


    public void setUserGender(String gender) {
        editor.putString(USER_GENDER, gender);
        editor.commit();
    }


    public String getUserGender() {
        return mSharedPreferences.getString(USER_GENDER, null);
    }

    /**
     * 环信id
     *
     * @param hxid 暂时定为橙id
     */
    public void setHxid(String hxid) {
        editor.putString(USER_HXID, hxid);
        editor.commit();
    }

    public String getHxid() {
        return mSharedPreferences.getString(USER_HXID, null);
    }

    public void setExtrapsd(String extrapsd){
        editor.putString(USER_EXTRA_PSD,extrapsd);
        editor.commit();
    }

    public String getExtrapsd(){
        return mSharedPreferences.getString(USER_EXTRA_PSD, null);
    }

    /**
     * @param token    token
     * @param nickName nickname
     * @param avatar   avatar
     * @param phone    手机号（帐号）
     * @param psd      密码
     */
    public void setUserInfo(String token, String nickName, String avatar, String phone, String psd) {
        setToken(token);
        setCurrentNickName(nickName);
        setCurrentAvatar(avatar);
        setUserPhone(phone);
        setUserPsd(psd);
    }

    public void cleanUserInfo() {
        setToken("");
        setCurrentNickName("");
        setCurrentAvatar("");
        setUserPhone("");
        setUserPsd("");
        setHxid("");
    }
}
