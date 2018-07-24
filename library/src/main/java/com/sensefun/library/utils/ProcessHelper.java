package com.sensefun.library.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * 进程 Utils
 * <p>
 * Created by Jinjia on 2017/7/1.
 */
public final class ProcessHelper {

    private ProcessHelper() {
    }

    /**
     * 获取当前进程名称
     *
     * @return 进程名
     */
    public static String getCurrentProcessName(Context context) {
        ActivityManager mgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list = mgr.getRunningAppProcesses();
        final int pid = android.os.Process.myPid();

        for (ActivityManager.RunningAppProcessInfo progress : list) {
            if (progress.pid == pid) {
                return progress.processName;
            }
        }
        return null;
    }

    /**
     * 当前进程是否是主进程
     *
     * @param context 上下文
     * @return 是否是主进程
     */
    public static boolean isMainProgress(Context context) {
        return context.getPackageName().equals(ProcessHelper.getCurrentProcessName(context));
    }

}
