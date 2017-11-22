package apf.plugin;

public class ResultBean {
    String identify;
    String content;
    String ex;
    long timestamp = 0L;

    @Override
    public String toString() {
        return hashCode() + "|" + identify + "|" + timestamp + "|" + content + "|" + ex;
    }
}
