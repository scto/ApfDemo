package apf.plugin;

import android.content.Context;

public class PluginTask implements PluginTaskImpl {

    public void run(Context context, PluginRequest request, PluginTaskCallback callback) {
        if (context == null || request == null || request.invalid()) {
            return;
        }

        boolean success = false;
        request.doFirst();
        // TODO
        request.content = context.getPackageName();
        success = true;

        request.doLast();

        if (success) {
            if (callback != null) {
                callback.onSuccess(request);
            }
        } else {
            if (callback != null) {
                callback.onFailure(request);
            }
        }
    }
}
