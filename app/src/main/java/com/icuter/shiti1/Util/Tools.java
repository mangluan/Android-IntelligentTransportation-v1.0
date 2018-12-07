package com.icuter.shiti1.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by 50834 on 2018/12/4.
 */

public class Tools {

    public static String IP = "http://47.106.75.2:8080/transportservice/action/";

    public static boolean NetTool(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.isConnected())
            return true;
        else
            return false;
    }


}
