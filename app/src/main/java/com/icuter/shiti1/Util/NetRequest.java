package com.icuter.shiti1.Util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Set;


public class NetRequest implements Runnable {

    private Context mContext;
    private URL mURL;
    private Handler mHandler;
    private Map<String, String> params;
    private boolean isLoop = false;
    private long LoopTime = 5000;

    public NetRequest(Context context, String url, Handler handler) throws MalformedURLException {
        mContext = context;
        mURL = new URL(url);
        mHandler = handler;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public void setLoop(boolean loop) {
        isLoop = loop;
    }

    public void setLoopTime(long loopTime) {
        LoopTime = loopTime;
    }

    @Override
    public void run() {
        if (Tools.NetTool(mContext)) {
            do {
                String result = RequestData();
                if (result != null && !result.equals("")) {
                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    mHandler.sendMessage(message);
                }
                if (isLoop) {
                    try {
                        Thread.sleep(LoopTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } while (isLoop);
        } else {
            Message message = new Message();
            message.what = -1;
            mHandler.sendMessage(message);
        }
    }

    private String RequestData() {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) mURL.openConnection();
            connection.setConnectTimeout(10000);
            if (params == null) {
                //GET
                connection.setRequestMethod("GET");
            } else {
                //POST
                connection.setRequestMethod("POST");
                StringBuffer sb = new StringBuffer();
                Set<String> keys = params.keySet();
                for (String key : keys) {
                    sb.append(key).append("=").append(params.get(key)).append("&");
                }
                sb.deleteCharAt(sb.length() - 1);
                DataOutputStream stream = new DataOutputStream(connection.getOutputStream());
                stream.writeBytes(sb.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}
