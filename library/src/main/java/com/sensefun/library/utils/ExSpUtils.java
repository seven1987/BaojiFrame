package com.sensefun.library.utils;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;

import io.reactivex.annotations.NonNull;

/**
 * Sp Utils 扩展
 * <p>
 * Created by Jinjia on 2017/12/8.
 */
public class ExSpUtils {

    public static void saveObject(@NonNull String key, @NonNull Object object) {
        Gson gson = new Gson();
        String json = gson.toJson(object);
        SPUtils.getInstance().put(key, json);
    }

    @Nullable
    public static <T> T getObject(@NonNull String key, @NonNull Class<T> clazz) {
        String json = SPUtils.getInstance().getString(key);
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        return new Gson().fromJson(json, clazz);
    }

}
