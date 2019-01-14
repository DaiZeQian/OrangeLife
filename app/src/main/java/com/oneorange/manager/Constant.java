package com.oneorange.manager;

/**
 * Created by admin on 2016/5/31.
 */
public class Constant {


    public static final String ACCOUNT_REMOVED = "account_removed";  //帐号被移除
    public static final String ACCOUNT_CONFLICT = "conflict";    //帐号被顶替


   /* //==好友
    *//**被邀请*//*
    BEINVITEED,
    *//**被拒绝*//*
    BEREFUSED,
    *//**对方同意*//*
    BEAGREED,
    //==群组
    *//**对方申请进入群*//*
    BEAPPLYED,
    *//**我同意了对方的请求*//*
    AGREED,
    *//**我拒绝了对方的请求*//*
    REFUSED,

    //==群邀请
    *//**收到对方的群邀请**//*
    GROUPINVITATION,
    *//**收到对方同意群邀请的通知**//*
    GROUPINVITATION_ACCEPTED,
    */
    /**
     * 收到对方拒绝群邀请的通知*
     *//*
    GROUPINVITATION_DECLINED*/
    public static final String BEINVITEED = "1";
    public static final String BEREFUSED = "2";
    public static final String BEAGREED = "3";

    public static final String BEAPPLYED = "4";
    public static final String AGREED = "5";
    public static final String REFUSED = "6";

    public static final String GROUPINVITATION = "7";
    public static final String GROUPINVITATION_ACCEPTED = "8";
    public static final String GROUPINVITATION_DECLINED = "9";

}
