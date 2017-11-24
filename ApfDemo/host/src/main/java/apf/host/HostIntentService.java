package apf.host;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import intent.compact.IntentCompact;

public class HostIntentService extends IntentService {

    public HostIntentService() {
        this("HostIntentService");
    }

    public HostIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("PPP", "HostIntentService|onHandleIntent|" + IntentCompact.convertIntentToString(intent));
    }
}
