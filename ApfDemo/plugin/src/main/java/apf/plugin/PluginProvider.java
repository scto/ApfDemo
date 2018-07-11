package apf.plugin;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

public class PluginProvider extends ContentProvider {

    private static Gson mGson = new Gson();

    @Override
    public boolean onCreate() {
        if (mGson == null) {
            mGson = new Gson();
        }
        return true;
    }

    @Override
    public Bundle call(String method, String arg, Bundle extras) {
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
        Log.d("PPP", getClass().getSimpleName() + "|handleAll|authority|" + PluginUtil.CONTENT_AUTHORITY);
        if (PluginMethod.METHOD_GET_PLUGIN_INFOS.equals(method)) {
            action = extras.getString(PluginAction.NAME);
            resultBean = getPluginInfosByAction(context, action);
        } else {
            // TODO: other methods
        }
        Log.d("PPP", getClass().getSimpleName() + "|handleAll|method|" + method + "|extras|" + extras.toString() + "|end|" + System.currentTimeMillis());
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

    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
