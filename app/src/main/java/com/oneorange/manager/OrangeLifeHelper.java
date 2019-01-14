package com.oneorange.manager;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMError;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.model.EaseNotifier;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.oneorange.content.ChatView;
import com.oneorange.db.NewFriendsDao;
import com.oneorange.orangelife.HomeActivity;
import com.oneorange.utils.LogUtil;

import java.util.List;

/**
 * Created by admin on 2016/5/19.
 */
public class OrangeLifeHelper {

    private static OrangeLifeHelper instance;

    private OrangeLifeHelper() {
    }

    private Context appContext;
    //连接监听
    private EMConnectionListener connectionListener;

    //EMEventListener  消息事件监听
    protected EMMessageListener messageListener = null;
    //easeUI组件
    private EaseUI easeUI;
    //联系人和群组是否已经被注册过
    private boolean isRegister;
    //本地广播  可处理消息
    private LocalBroadcastManager broadcastManager;

    /**
     * 创建单例对象
     *
     * @return
     */
    public synchronized static OrangeLifeHelper getInstance() {
        if (instance == null) {
            instance = new OrangeLifeHelper();
        }
        return instance;
    }


    public void init(Context context) {
        EMOptions options = initChatOptions();
        //如果初始化失败 则不可使用环信控件
        if (EaseUI.getInstance().init(context, options)) {
            //设为调试模式，打成正式包时，最好设为false，以免消耗额外的资源
            appContext = context;
            EMClient.getInstance().setDebugMode(true);
            //get easeui instance
            easeUI = EaseUI.getInstance();
            //调用easeui的api设置providers
            setEaseUIProviders();
            //设置全局监听
            setGlobalListeners();

            //本地广播实例化
            broadcastManager = LocalBroadcastManager.getInstance(appContext);
        }
    }


    private EMOptions initChatOptions() {
        Log.d("HuanxinInit", "init HuanXin Options");
        // 获取到EMChatOptions对象
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        // 设置是否需要已读回执
        options.setRequireAck(false);
        // 设置是否需要已送达回执
        options.setRequireDeliveryAck(false);
        return options;

    }

    /**
     * 设置全局 事件监听
     */
    private void setGlobalListeners() {
        //创建全局监听
        connectionListener = new EMConnectionListener() {
            //成功连接到Hyphenate IM服务器时触发
            @Override
            public void onConnected() {

            }

            //和Hyphenate IM服务器断开连接时触发   包含各种问题
            @Override
            public void onDisconnected(final int i) {
                if (i == EMError.USER_REMOVED) {
                    //用户被删除
                    onCurrentAccountRemoved();
                } else if (i == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                    //用户异地登录
                    onConnectionConflict();
                } else {//其他原因

                }
            }
        };

        //注册连接监听
        EMClient.getInstance().addConnectionListener(connectionListener);
        //注册群组和联系人监听
        registerGroupAndContactListener();
        //注册消息事件监听
        registerEventListener();
    }


    /**
     * 全局事件监听
     * 因为可能会有UI页面先处理到这个消息，所以一般如果UI页面已经处理，这里就不需要再次处理
     * activityList.size() <= 0 意味着所有页面都已经在后台运行，或者已经离开Activity Stack
     */
    protected void registerEventListener() {

        messageListener = new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> list) {
                //接受消息接口，在接受到文本消息，图片，视频，语音，地理位置，文件这些消息体的时候，会通过此接口通知用户。
                for (EMMessage message : list) {
                    LogUtil.d("appliction", "onMessageReceived id : " + message.getMsgId());
                    //应用在后台，不需要刷新UI,通知栏提示新消息
                    if (!easeUI.hasForegroundActivies()) {
                        getNotifier().onNewMsg(message);
                    }
                }
            }

            //这个接口只包含命令的消息体，包含命令的消息体通常不对用户展示。
            @Override
            public void onCmdMessageReceived(List<EMMessage> list) {
                //可以设置穿透消息
            }

            @Override
            public void onMessageReadAckReceived(List<EMMessage> list) {

            }

            @Override
            public void onMessageDeliveryAckReceived(List<EMMessage> list) {

            }

            @Override
            public void onMessageChanged(EMMessage emMessage, Object o) {

            }
        };
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    protected void setEaseUIProviders() {
        easeUI.getNotifier().setNotificationInfoProvider(new EaseNotifier.EaseNotificationInfoProvider() {
            @Override
            public String getDisplayedText(EMMessage message) {
                // 设置状态栏的消息提示，可以根据message的类型做相应提示
                String ticker = EaseCommonUtils.getMessageDigest(message, appContext);
                if (message.getType() == EMMessage.Type.TXT) {
                    ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
                }
                return message.getFrom() + ": " + ticker;
            }

            @Override
            public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
                return null;
            }

            @Override
            //修改标题
            public String getTitle(EMMessage message) {
                return "橙云生活";
            }

            @Override
            public int getSmallIcon(EMMessage message) {
                return 0;
            }

            @Override
            public Intent getLaunchIntent(EMMessage message) {
                //设置点击通知栏跳转事件
                Intent intent = new Intent(appContext, ChatView.class);
                EMMessage.ChatType chatType = message.getChatType();
                if (chatType == EMMessage.ChatType.Chat) { // 单聊信息
                    intent.putExtra("userId", message.getFrom());
                    intent.putExtra("chatType", EaseConstant.CHATTYPE_SINGLE);
                } else { // 群聊信息
                    // message.getTo()为群聊id
                    intent.putExtra("userId", message.getTo());
                    intent.putExtra("chatType", EaseConstant.CHATTYPE_GROUP);
                }
                return intent;
            }
        });
    }

    /**
     * 退出登录
     *
     * @param unbindToken 是否解绑设备token(使用GCM才有)
     * @param callback    callback
     */
    public void logout(boolean unbindToken, final EMCallBack callback) {
        LogUtil.d("logout", "logout: " + unbindToken);
        EMClient.getInstance().logout(unbindToken, new EMCallBack() {

            @Override
            public void onSuccess() {
                LogUtil.d("logout", "logout: onSuccess");
                reset();
                if (callback != null) {
                    callback.onSuccess();
                }

            }

            @Override
            public void onProgress(int progress, String status) {
                if (callback != null) {
                    callback.onProgress(progress, status);
                }
            }

            @Override
            public void onError(int code, String error) {
                LogUtil.d("logout", "logout: onSuccess");
                reset();
                if (callback != null) {
                    callback.onError(code, error);
                }
            }
        });
    }


    /**
     * 账号在别的设备登录
     */
    protected void onConnectionConflict() {
        Intent intent = new Intent(appContext, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constant.ACCOUNT_CONFLICT, true);
        appContext.startActivity(intent);
    }

    /**
     * 账号被移除
     */
    protected void onCurrentAccountRemoved() {
        Intent intent = new Intent(appContext, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constant.ACCOUNT_REMOVED, true);
        appContext.startActivity(intent);
    }


    /**
     * 注册群组和联系人监听，由于logout的时候会被sdk清除掉，再次登录的时候需要再注册一下
     */
    public void registerGroupAndContactListener() {
        if (!isRegister) {
            //注册群组变动监听
            EMClient.getInstance().groupManager().addGroupChangeListener(new MyGroupChangeListener());
            //注册联系人变动监听
            EMClient.getInstance().contactManager().setContactListener(new MyContactListener());
            isRegister = true;
        }

    }

    /**
     * 群组变化通知   环信真可恶 竟然不给直接的文档还要自己查看sdk  shit
     */
    class MyGroupChangeListener implements EMGroupChangeListener {
        /**
         * 当前用户收到加入群组邀请
         *
         * @param s  groupid 要加入的群id
         * @param s1 groupname  要加入的群名称
         * @param s2 inviter 邀请人id
         * @param s3 邀请加入的原因
         */
        @Override
        public void onInvitationReceived(String s, String s1, String s2, String s3) {


        }

        /**
         * 用户申请加入群
         *
         * @param s  groupid 要加入的群id
         * @param s1 groupname 要加入的群名称
         * @param s2 applyer 申请人name
         * @param s3 reason 申请加入的renson
         */
        @Override
        public void onApplicationReceived(String s, String s1, String s2, String s3) {

        }

        /**
         * 加群申请被对方接受
         *
         * @param s  groupid 群组id
         * @param s1 groupName  群组名字
         * @param s2 accepter 同意人的username
         */
        @Override
        public void onApplicationAccept(String s, String s1, String s2) {
        }


        /**
         * 加群申请被拒绝
         *
         * @param s  groupId	群组id
         * @param s1 groupName	群组名字
         * @param s2 decliner	拒绝人得username
         * @param s3 reason	拒绝理由
         */
        @Override
        public void onApplicationDeclined(String s, String s1, String s2, String s3) {
        }

        /**
         * 群组邀请被接受
         *
         * @param s  groupId  群组id
         * @param s1 invitee
         * @param s2 reason
         */
        @Override
        public void onInvitationAccpted(String s, String s1, String s2) {
        }

        /**
         * 群组邀请被拒绝
         *
         * @param s  groupId  群组id
         * @param s1 invitee
         * @param s2 reason
         */
        @Override
        public void onInvitationDeclined(String s, String s1, String s2) {
        }


        /**
         * 当前登录用户被管理员移除出群组
         *
         * @param s  groupId   群组id
         * @param s1 groupName 群名称
         */
        @Override
        public void onUserRemoved(String s, String s1) {
        }

        /**
         * 群组被解散。 sdk 会先删除本地的这个群组，之后通过此回调通知应用，此群组被删除了
         *
         * @param s  groupId	群组的ID
         * @param s1 groupName	群组的名称
         */
        @Override
        public void onGroupDestroy(String s, String s1) {
        }

        /**
         * 自动同意加入群组 sdk会先加入这个群组，并通过此回调通知应用
         *
         * @param s  groupId
         * @param s1 inviter
         * @param s2 inviteMessage
         */
        @Override
        public void onAutoAcceptInvitationFromGroup(String s, String s1, String s2) {
        }
    }

    /**
     * 好友变化通知
     */
    class MyContactListener implements EMContactListener {

        /**
         * 增加联系人时回调此方法
         *
         * @param s username	增加的联系人
         */
        @Override
        public void onContactAdded(String s) {
        }

        /**
         * 被删除时回调此方法
         *
         * @param s username
         */
        @Override
        public void onContactDeleted(String s) {
        }

        /**
         * 收到好友邀请
         *
         * @param s  username	发起加为好友用户的名称
         * @param s1 reason	对方发起好友邀请时发出的文字性描述
         */
        @Override
        public void onContactInvited(String s, String s1) {

            NewFriendsDao friendsDao = new NewFriendsDao();
            friendsDao.setReason(s1);
            friendsDao.setUsername(s);
            friendsDao.setStatus(Constant.BEINVITEED);
            notifyNewIviteMessage(friendsDao);
        }

        /**
         * 好友请求被同意
         *
         * @param s username
         */
        @Override
        public void onContactAgreed(String s) {
            NewFriendsDao friendsDao = new NewFriendsDao();
            friendsDao.setUsername(s);
            friendsDao.setStatus(Constant.BEAGREED);
            notifyNewIviteMessage(friendsDao);
        }

        /**
         * 好友请求被拒绝
         *
         * @param s username
         */
        @Override
        public void onContactRefused(String s) {
            notifyNewIviteMessage(null);
            NewFriendsDao friendsDao = new NewFriendsDao();
            friendsDao.setUsername(s);
            friendsDao.setStatus(Constant.BEREFUSED);
            notifyNewIviteMessage(friendsDao);
        }
    }


    /**
     * 保存并提示消息的邀请消息
     *
     * @param msg
     */
    private void notifyNewIviteMessage(NewFriendsDao msg) {
        // 提示有新消息
        if (msg != null) {
            msg.save();
        }
        getNotifier().viberateAndPlayTone(null);
    }


    /**
     * 注销帐号之后的同步
     */
    synchronized void reset() {
        isRegister = false;
    }

    /**
     * 获取消息通知类
     *
     * @return
     */
    public EaseNotifier getNotifier() {
        return easeUI.getNotifier();
    }


    /**
     * 是否登录成功过
     *
     * @return
     */
    public boolean isLoggedIn() {
        return EMClient.getInstance().isLoggedInBefore();
    }
}