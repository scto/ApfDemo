package apf.plugin;

import android.compact.impl.TaskCallback;
import android.compact.impl.TaskImpl;
import android.compact.impl.TaskPayload;
import android.content.Context;

public class PluginTask implements TaskImpl {

    public void run(Context context, TaskPayload payload, TaskCallback callback) {
        if (context == null || payload == null || payload.identify == null) {
            return;
        }

        boolean success = false;
        // TODO
        payload.content = context.getPackageName();
        success = true;
        if (success) {
            payload.state = 1;
        } else {
            payload.state = -1;
        }

        if (callback != null) {
            callback.onResult(payload);
        }
    }
}
