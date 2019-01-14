package com.oneorange.manager;

import android.content.ContentValues;

import com.oneorange.db.UserFriendsDao;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by admin on 2016/6/17.
 */
public class UserFriendsHelper {


    private static UserFriendsHelper instance;

    public synchronized static UserFriendsHelper getInstance() {
        if (instance == null) {
            instance = new UserFriendsHelper();
        }
        return instance;
    }


    public void saveFriendInfo(UserFriendsDao friendsDao) {
        List<UserFriendsDao> friendsDaos = DataSupport.where("userid = ?", friendsDao.getUserid()).find(UserFriendsDao.class);
        if(friendsDaos.size()<=0){//无数据
            friendsDao.save();
        }else{
            ContentValues values = new ContentValues();
            values.put("nickname",friendsDao.getNickname());
            values.put("avatar",friendsDao.getAvatar());
            DataSupport.updateAll(UserFriendsDao.class, values, "userid = ? ", friendsDao.getUserid());
        }
    }

}
