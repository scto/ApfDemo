package apf.plugin;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import intent.compact.IntentCompact;

public class PluginIntentService extends IntentService {

    public PluginIntentService() {
        this("PluginIntentService");
    }

    public PluginIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("PPP", "PluginIntentService|onHandleIntent|" + IntentCompact.convertIntentToString(intent));
    }
}
