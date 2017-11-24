package apf.plugin;

import android.content.Context;

public interface PluginTaskImpl {
    void run(Context context, PluginRequest request, PluginTaskCallback callback);
}
