package apf.host;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

public class PluginUtil {
    public static final String CONTENT_AUTHORITY = "apf.plugin.test_plugin_provider";

    public static ResultBean getProviderIdentifyByResultBean(Context context) {
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
            Log.d("PPP", t.getMessage());
            result = null;
        }
        if (result != null) {
            resultJson = result.getString("result_json");
        }
        if (resultJson != null && resultJson.length() > 0) {
            resultBean = new Gson().fromJson(resultJson, ResultBean.class);
        }
        return resultBean;
    }

    public static String getProviderIdentify(Context context) {
        String identify = null;
        long startTime = System.currentTimeMillis();
        ResultBean resultBean = getProviderIdentifyByResultBean(context);
        long endTime = System.currentTimeMillis();
        if (resultBean != null) {
            Log.d("PPP", "ResultBean|" + resultBean.toString() + "|cost|" + (endTime - startTime));
            identify = resultBean.identify;
        }
        return identify;
    }
}
