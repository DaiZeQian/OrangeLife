package com.oneorange.utils;

import android.app.Dialog;

/**
 * Created by admin on 2016/5/31.
 * <p>
 * dialog 管理类
 */
public class DialogUtils {










    /**
     * 设置隐藏
     *
     * @param dialog
     */
    public static void setDialogDismiss(Dialog dialog) {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    /**
     * 设置 显示
     *
     * @param dialog
     */
    public static void setDiaLogShow(Dialog dialog) {
        if (dialog != null) {
            if (!dialog.isShowing()) {
                dialog.show();
            }
        }
    }
}
