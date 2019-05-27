package apf.host;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.limpoxe.fairy.core.FairyGlobal;
import com.limpoxe.fairy.core.PluginFilter;
import com.limpoxe.fairy.core.PluginLoader;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        FairyGlobal.onlyUseStandAlonePlugins();
        FairyGlobal.disableActivityManagerProxyInMainProcess();
        FairyGlobal.disableNotificationManagerProxyInMainProcess();
        FairyGlobal.disablePackageManagerProxyInMainProcess();
        FairyGlobal.setNeedVerifyPlugin(false);
        FairyGlobal.setAllowDowngrade(false);
        if (!FairyGlobal.hasPluginFilter()) {
            FairyGlobal.setPluginFilter(new PluginFilter() {
                @Override
                public boolean accept(String packageName) {
                    Log.d("PPP", "PluginFilter->accept|" + packageName);
                    if (TextUtils.isEmpty(packageName)) {
                        return false;
                    }
                    return packageName.contains("tv.zhenjing.vitamin") || packageName.endsWith("plugin");
                }
            });
        }
        try {
            PluginLoader.initLoader(this);
        } catch (Exception e) {
        }
    }

    @Override
    public Context getBaseContext() {
        return PluginLoader.fixBaseContextForReceiver(super.getBaseContext());
    }
}
