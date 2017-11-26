package apf.plugin;

import android.app.Activity;
import android.compact.impl.*;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;

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
        final Button btn6 = (Button) findViewById(R.id.btn6);
        final TextView btn6ret = (TextView) findViewById(R.id.btn6_ret);
        final Button btn7 = (Button) findViewById(R.id.btn7);
        final TextView btn7ret = (TextView) findViewById(R.id.btn7_ret);

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
                intent.setPackage("apf.plugin");
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
//                PluginTaskImpl instance = null;
//                try {
//                    Class classType = Class.forName("apf.plugin.PluginTask");
//                    Object obj = classType.newInstance();
//                    instance = (PluginTaskImpl) obj;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                if (instance != null) {
//                    PluginRequest pluginRequest = new PluginRequest();
//                    pluginRequest.identify = "version1";
//                    instance.run(view.getContext(), pluginRequest, new PluginTaskCallback() {
//                        @Override
//                        public void onSuccess(PluginRequest request) {
//                            Log.d("PPP", "pluginTaskSuccess|" + request.toString());
//                        }
//
//                        @Override
//                        public void onFailure(PluginRequest request) {
//                            Log.d("PPP", "pluginTaskFailure|" + request.toString());
//                        }
//                    });
//                }

                TaskImpl instance = null;
                try {
                    Class classType = Class.forName("apf.plugin.PluginTask");
                    Object obj = classType.newInstance();
                    instance = (TaskImpl) obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (instance != null) {
                    TaskPayload payload = new TaskPayload();
                    payload.identify = "version1";
                    instance.run(view.getContext(), payload, new TaskCallback() {
                        @Override
                        public void onResult(TaskPayload taskPayload) {
                            if (1 == taskPayload.state) {
                                Log.d("PPP", "TaskSuccess|" + taskPayload.identify + "|" + taskPayload.content);
                            } else {
                                Log.d("PPP", "TaskFailure|" + taskPayload.identify + "|" + taskPayload.content);
                            }
                            btn5ret.setText(taskPayload.identify + "|" + taskPayload.content);
                        }
                    });
                }
            }
        });

        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("apf.plugin.action.PLUGIN_INTENT_SERVICE");
                intent.setPackage("apf.plugin");
                startService(intent);
                bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
            }
        });

        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("apf.plugin.action.PLUGIN_INTENT_SERVICE");
                intent.setPackage("apf.plugin");
                TaskPayload payload = new TaskPayload();
                payload.identify = "version1";
                intent.putExtra("taskpayload", (Parcelable) payload);
                startService(intent);
            }
        });
    }

    private boolean mBound = false;
    private Messenger mMessenger = null;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mMessenger = new Messenger(iBinder);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mMessenger = null;
            mBound = false;
        }
    };

    private Messenger msg = new Messenger(new SendingHandler());

    class SendingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBound) {
            try {
                unbindService(mServiceConnection);
            } catch (Exception e) {
            }
            mBound = false;
        }
    }
}
