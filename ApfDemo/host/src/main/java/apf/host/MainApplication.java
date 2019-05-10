package apf.host;

import android.app.Application;
import android.content.Context;

import com.limpoxe.fairy.core.PluginLoader;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        PluginLoader.initLoader(this);
    }

    @Override
    public Context getBaseContext() {
        return PluginLoader.fixBaseContextForReceiver(super.getBaseContext());
    }
}
