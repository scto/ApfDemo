package apf.plugin;

import android.app.IntentService;
import android.compact.impl.TaskCallback;
import android.compact.impl.TaskPayload;
import android.compact.utils.IntentCompactUtil;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

public class PluginIntentService extends IntentService {

    public PluginIntentService() {
        this("PluginIntentService");
    }

    public PluginIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("PPP", "PluginIntentService|onHandleIntent|" + IntentCompactUtil.convertIntentToString(intent));
        Bundle extra = intent.getExtras();
        if (extra != null) {
            Object obj = extra.get("taskpayload");
            Log.d("PPP", "obj|" + obj);
            if (obj instanceof TaskPayload) {
                TaskPayload payload = (TaskPayload) obj;
                final PluginTask task = new PluginTask();
                task.run(getApplicationContext(), payload, new TaskCallback() {
                    @Override
                    public void onResult(TaskPayload taskPayload) {
                        Log.d("PPP", "onResult|" + taskPayload.state + "|" + taskPayload.identify + "|" + taskPayload.content);
                        HostUtil.sendMessageWithTaskPayload(getApplicationContext(), taskPayload);
                    }
                });
            }
        } else {
            Log.d("PPP", "extra|no");
        }
    }

    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d("PPP", "IncomingHandler|" + msg.toString());
        }
    }

    final Messenger messenger = new Messenger(new IncomingHandler());

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }
}
