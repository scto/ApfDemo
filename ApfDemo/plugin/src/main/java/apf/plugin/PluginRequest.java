package apf.plugin;

public class PluginRequest {

    public String identify;
    public String content;

    public String toString() {
        return identify + "|" + content;
    }

    public boolean invalid() {
        if (identify == null || identify.length() == 0) {
            return true;
        }
        return false;
    }

    public Runnable firstRunnable;
    public Runnable lastRunnable;

    public void doFirst() {
        if (firstRunnable != null) {
            firstRunnable.run();
        }
    }

    public void doLast() {
        if (lastRunnable != null) {
            lastRunnable.run();
        }
    }
}
