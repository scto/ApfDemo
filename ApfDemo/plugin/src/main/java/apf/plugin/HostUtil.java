package apf.plugin;

import android.compact.impl.TaskPayload;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.Gson;

public class HostUtil {
    public static final String CONTENT_AUTHORITY = "apf.host.test_host_provider";

    public static void sendMessageWithTaskPayload(Context context, TaskPayload payload) {
        Bundle extras = new Bundle();
        extras.putString(PluginAction.NAME, PluginAction.ACTION_SET_TASK_PAYLOAD);
        extras.putString("extra_json", new Gson().toJson(payload));
        Uri uri = Uri.parse("content://" + CONTENT_AUTHORITY);
        try {
            context.getApplicationContext().getContentResolver()
                    .call(uri, PluginMethod.METHOD_SEND_MESSAGES_TO_HOST, null, extras);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static String getProviderIdentify(Context context) {
        long startTime = System.currentTimeMillis();
        String identify = null;
        Bundle result = null;
        String resultJson = null;
        ResultBean resultBean = null;
        Bundle extras = new Bundle();
        extras.putString(PluginAction.NAME, PluginAction.ACTION_GET_IDENTIFY);
        Uri uri = Uri.parse("content://" + CONTENT_AUTHORITY);
        try {
            result = context.getApplicationContext().getContentResolver()
                    .call(uri, PluginMethod.METHOD_GET_PLUGIN_INFOS, null, extras);
        } catch (Throwable t) {
            result = null;
        }
        if (result != null) {
            resultJson = result.getString("result_json");
        }
        if (resultJson != null && resultJson.length() > 0) {
            resultBean = new Gson().fromJson(resultJson, ResultBean.class);
        }
        long endTime = System.currentTimeMillis();
        if (resultBean != null) {
            Log.d("PPP", "ResultBean|" + resultBean.toString() + "|cost|" + (endTime - startTime));
            identify = resultBean.identify;
        }
        return identify;
    }
}
