package apf.host;

import android.compact.impl.TaskPayload;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;

public class HostProvider extends ContentProvider {

    public static final String CONTENT_AUTHORITY = "apf.host.test_host_provider";
    private static Gson mGson = new Gson();

    @Override
    public boolean onCreate() {
        if (mGson == null) {
            mGson = new Gson();
        }
        return true;
    }

    @Nullable
    @Override
    public Bundle call(@NonNull String method, @Nullable String arg, @Nullable Bundle extras) {
        Bundle ret = new Bundle();
        ResultBean result = handleAll(getContext(), method, arg, extras);
        if (result != null) {
            ret.putString("result_json", mGson.toJson(result));
        } else {
            ret = super.call(method, arg, extras);
        }
        return ret;
    }

    private final ResultBean handleAll(Context context, String method, String arg, Bundle extras) {
        String action = null;
        ResultBean resultBean = null;
        Log.d("PPP", getClass().getSimpleName() + "|handleAll|method|" + method + "|extras|" + extras.toString() + "|start|" + System.currentTimeMillis());
        if (PluginMethod.METHOD_GET_PLUGIN_INFOS.equals(method)) {
            action = extras.getString(PluginAction.NAME);
            resultBean = getPluginInfosByAction(context, action, extras);
        } else if (PluginMethod.METHOD_SEND_MESSAGES_TO_HOST.equals(method)) {
            action = extras.getString(PluginAction.NAME);
            resultBean = getPluginMessages(context, action, extras);
        } else {
            // TODO: other methods
        }
        Log.d("PPP", getClass().getSimpleName() + "|handleAll|method|" + method + "|extras|" + extras.toString() + "|end|" + System.currentTimeMillis());
        return resultBean;
    }

    private final ResultBean getPluginInfosByAction(Context context, String action, Bundle extras) {
        ResultBean resultBean = null;
        if (PluginAction.ACTION_GET_IDENTIFY.equals(action)) {
//            resultBean = PluginUtil.getProviderIdentifyByResultBean(context);

            resultBean = new ResultBean();
            resultBean.identify = context.getPackageName();
            resultBean.timestamp = System.currentTimeMillis();
        } else {
            // TODO: other actions
        }
        return resultBean;
    }

    private final ResultBean getPluginMessages(Context context, String action, Bundle extras) {
        ResultBean resultBean = null;
        if (PluginAction.ACTION_SET_TASK_PAYLOAD.equals(action)) {
            String strJson = extras.getString("extra_json");
            TaskPayload payload = mGson.fromJson(strJson, TaskPayload.class);
            Log.d("PPP", "HOST|getPluginMessages|TASK_PAYLOAD|" + payload.identify + "|" + payload.content + "|" + payload.state + "|" + payload.timestamp);

            resultBean = new ResultBean();
            resultBean.identify = context.getPackageName();
            resultBean.timestamp = System.currentTimeMillis();
        } else {
            // TODO: other actions
        }
        return resultBean;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}