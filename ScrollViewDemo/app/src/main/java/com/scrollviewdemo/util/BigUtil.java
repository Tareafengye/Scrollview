package com.scrollviewdemo.util;

import android.text.TextUtils;
import android.util.Log;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/10/11 0011.
 */

public class BigUtil {
    /**
     * 大小写转换
     *
     * @param map
     * @return
     */
    public static String getUrlParamsByMap(HashMap<String, Object> map) {
        if (map == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Object[] key_arr = map.keySet().toArray();
        Arrays.sort(key_arr);
        Log.d("StringBuilderarres", key_arr.toString());
        for (Object key : key_arr) {
            Object value = map.get(key.toString());
            sb.append(key).append("=").append(value);
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = s.substring(0, s.length() - 1);
        }
        Log.d("getUrlParamsByMap", s);
        return s;
    }

    /**
     * 排序
     *
     * @param param
     * @return
     */
    public static HashMap<String, Object> getUrlParams(String param) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (TextUtils.isEmpty(param)) {
            return map;
        }
        String[] params = param.split("&");
        for (int i = 0; i < params.length; i++) {
            String[] p = params[i].split("=");
            if (p.length == 2) {
                map.put(p[0], p[1]);
            }
        }
        Log.d("getUrlParams", map.toString());
        return map;
    }

    /**
     * 排序
     *
     * @param param
     * @return
     */
    public static HashMap<String, Object> getUrl(String param) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (TextUtils.isEmpty(param)) {
            return map;
        }
        String[] params = param.split("&");
        for (int i = 0; i < params.length; i++) {
            String[] p = params[i].split("=");
            if (p.length == 2) {
                map.put(p[0], p[1]);
            }
        }
        return map;
    }
}
