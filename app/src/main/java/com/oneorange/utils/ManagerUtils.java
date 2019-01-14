package com.oneorange.utils;

/**
 * Created by admin on 2015/11/26.
 */
public class ManagerUtils {

    // 设置级别为2    level为层级  1代表当前 上级调用 2代表上级在往上级调用
    public static String getCurrentMethodName() {//获取当前运行的方法名称
        int level = 2;
        StackTraceElement[] stacks = new Throwable().getStackTrace();
        String methodName = stacks[level].getMethodName();
        return methodName;
    }

    public static String getCurrentClassName() {//获取当前运行的类名
        int level = 2;
        StackTraceElement[] stacks = new Throwable().getStackTrace();
        String className = stacks[level].getClassName();
        return className;
    }


}
