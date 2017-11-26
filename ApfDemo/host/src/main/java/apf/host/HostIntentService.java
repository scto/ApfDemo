package apf.host;

import android.app.IntentService;
import android.compact.utils.IntentCompactUtil;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

public class HostIntentService extends IntentService {

    public HostIntentService() {
        this("HostIntentService");
    }

    public HostIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("PPP", "HostIntentService|onHandleIntent|" + IntentCompactUtil.convertIntentToString(intent));
    }
}
