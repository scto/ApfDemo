package intent.compact;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

public class IntentCompact {

    public static boolean checkIntentHasHandle(Context ctx, Intent intent) {
        try {
            boolean hasHandle = true;
            if (ctx.getPackageManager().resolveActivity(intent,
                    PackageManager.MATCH_DEFAULT_ONLY) == null) {
                if (ctx.getPackageManager().resolveService(intent,
                        PackageManager.MATCH_DEFAULT_ONLY) == null) {
                    hasHandle = false;
                }
            }
            return hasHandle;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String convertIntentToString(Intent intent) {
        try {
            return intent.toUri(Intent.URI_INTENT_SCHEME);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Intent convertStringToIntent(String string) {
        try {
            return Intent.parseUri(string, Intent.URI_INTENT_SCHEME);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
