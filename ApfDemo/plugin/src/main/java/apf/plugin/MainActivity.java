package apf.plugin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import intent.compact.IntentCompact;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btn1 = (Button) findViewById(R.id.btn1);
        final TextView btn1ret = (TextView) findViewById(R.id.btn1_ret);
        final Button btn2 = (Button) findViewById(R.id.btn2);
        final TextView btn2ret = (TextView) findViewById(R.id.btn2_ret);
        final Button btn3 = (Button) findViewById(R.id.btn3);
        final TextView btn3ret = (TextView) findViewById(R.id.btn3_ret);
        final Button btn4 = (Button) findViewById(R.id.btn4);
        final TextView btn4ret = (TextView) findViewById(R.id.btn4_ret);
        final Button btn5 = (Button) findViewById(R.id.btn5);
        final TextView btn5ret = (TextView) findViewById(R.id.btn5_ret);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String identify = PluginUtil.getProviderIdentify(view.getContext());
                Log.d("PPP", "getProviderIdentify|" + identify);
                btn1ret.setText(identify);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("apf.plugin.action.PLUGIN_INTENT_SERVICE");
//                boolean handled = IntentCompact.checkIntentHasHandle(view.getContext(), intent);
//                Log.d("PPP", "handled|" + handled);
//                intent.putExtra(PluginMethod.NAME, PluginMethod.METHOD_GET_PLUGIN_INFOS);
//                intent.putExtra(PluginAction.NAME, PluginAction.ACTION_GET_IDENTIFY);
                intent.setPackage(getPackageName());
                startService(intent);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String identify = HostUtil.getProviderIdentify(view.getContext());
                Log.d("PPP", "getProviderIdentify|" + identify);
                btn3ret.setText(identify);
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("apf.host.action.HOST_INTENT_SERVICE");
                intent.setPackage("apf.host");
                startService(intent);
            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 常规写法
//                PluginTask pluginTask = new PluginTask();
//                PluginRequest pluginRequest = new PluginRequest();
//                pluginRequest.identify = "N/A";
//                PluginTaskImpl impl = (PluginTaskImpl) pluginTask;
//                impl.run(view.getContext(), pluginRequest, new PluginTaskCallback() {
//                    @Override
//                    public void onSuccess(PluginRequest request) {
//                        Log.d("PPP", "pluginTaskSuccess|" + request.toString());
//                    }
//
//                    @Override
//                    public void onFailure(PluginRequest request) {
//                        Log.d("PPP", "pluginTaskFailure|" + request.toString());
//                    }
//                });

                // 反射写法
                PluginTaskImpl instance = null;
                try {
                    Class classType = Class.forName("apf.plugin.PluginTask");
                    Object obj = classType.newInstance();
                    instance = (PluginTaskImpl) obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (instance != null) {
                    PluginRequest pluginRequest = new PluginRequest();
                    pluginRequest.identify = "version1";
                    instance.run(view.getContext(), pluginRequest, new PluginTaskCallback() {
                        @Override
                        public void onSuccess(PluginRequest request) {
                            Log.d("PPP", "pluginTaskSuccess|" + request.toString());
                        }

                        @Override
                        public void onFailure(PluginRequest request) {
                            Log.d("PPP", "pluginTaskFailure|" + request.toString());
                        }
                    });
                }
            }
        });
    }
}
