package apf.plugin;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
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

    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d("PPP", "IncomingHandler|" + msg.toString());
        }
    }

    final Messenger messenger = new Messenger(new IncomingHandler());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }
}
