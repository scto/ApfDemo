package apf.plugin;

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

public class PluginProvider extends ContentProvider {

    public static final String CONTENT_AUTHORITY = "apf.plugin.test_plugin_provider";
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
        Log.d("PPP", "handleAll|method|" + method + "|extras|" + extras.toString() + "|start|" + System.currentTimeMillis());
        if (PluginMethod.METHOD_GET_PLUGIN_INFOS.equals(method)) {
            action = extras.getString(PluginAction.NAME);
            resultBean = getPluginInfosByAction(context, action);
        } else {
            // TODO: other methods
        }
        Log.d("PPP", "handleAll|method|" + method + "|extras|" + extras.toString() + "|end|" + System.currentTimeMillis());
        return resultBean;
    }

    private final ResultBean getPluginInfosByAction(Context context, String action) {
        ResultBean resultBean = null;
        if (PluginAction.ACTION_GET_IDENTIFY.equals(action)) {
            resultBean = getPluginIdentify(context);
        } else {
            // TODO: other actions
        }
        return resultBean;
    }

    private final ResultBean getPluginIdentify(Context context) {
        ResultBean resultBean = null;
        try {
            String packageName = context.getPackageName();
            if (packageName != null && packageName.length() > 0) {
                resultBean = new ResultBean();
                resultBean.identify = packageName;
                resultBean.timestamp = System.currentTimeMillis();
            }
        } catch (Exception e) {
            e.printStackTrace();
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
