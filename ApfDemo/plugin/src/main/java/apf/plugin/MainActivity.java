package apf.plugin;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView btn1ret = (TextView) findViewById(R.id.btn1_ret);
        Button btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String identify = getProviderIdentify(view.getContext());
                Log.d("PPP", "getProviderIdentify|" + identify);
                btn1ret.setText(identify);
            }
        });

    }

    private String getProviderIdentify(Context context) {
        long startTime = System.currentTimeMillis();
        String identify = null;
        Bundle result = null;
        String resultJson = null;
        ResultBean resultBean = null;
        Bundle extras = new Bundle();
        extras.putString(PluginAction.NAME, PluginAction.ACTION_GET_IDENTIFY);
        Uri uri = Uri.parse("content://" + PluginProvider.CONTENT_AUTHORITY);
        try {
            result = context.getApplicationContext().getContentResolver()
                    .call(uri, PluginMethod.METHOD_GET_PLUGIN_INFOS, null, extras);
        } catch (Throwable t) {
            result = null;
        }
        if (result != null) {
            resultJson = result.getString("result_json");
        }
        if (resultJson != null && resultJson.length() > 0) {
            resultBean = new Gson().fromJson(resultJson, ResultBean.class);
        }
        long endTime = System.currentTimeMillis();
        if (resultBean != null) {
            Log.d("PPP", "ResultBean|" + resultBean.toString() + "|cost|" + (endTime - startTime));
            identify = resultBean.identify;
        }
        return identify;
    }
}
