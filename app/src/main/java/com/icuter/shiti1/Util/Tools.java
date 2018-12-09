package com.icuter.shiti1.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by 50834 on 2018/12/4.
 */

public class Tools {

    public static String getUserName(Context context) {
        SharedPreferences sp = context.getSharedPreferences("Data", Context.MODE_PRIVATE);
        if (!sp.getString("id", "").equals(""))
            return sp.getString("id", "");
        else
            return "-1";
    }

    public static String getIP(String ip){
        return "http://47.106.75.2:8080/transportservice/action/" + ip ;
    }

    public static boolean NetTool(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.isConnected())
            return true;
        else
            return false;
    }


}
