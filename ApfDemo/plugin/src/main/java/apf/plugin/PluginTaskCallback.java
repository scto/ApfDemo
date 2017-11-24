package apf.plugin;

public interface PluginTaskCallback {
    void onSuccess(PluginRequest request);
    void onFailure(PluginRequest request);
}
