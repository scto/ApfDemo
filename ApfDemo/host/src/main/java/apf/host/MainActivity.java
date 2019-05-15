package apf.host;

import android.compact.impl.TaskCallback;
import android.compact.impl.TaskImpl;
import android.compact.impl.TaskPayload;
import android.compact.utils.FileCompactUtil;
import android.compact.utils.IntentCompactUtil;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.limpoxe.fairy.content.PluginDescriptor;
import com.limpoxe.fairy.core.FairyGlobal;
import com.limpoxe.fairy.core.PluginIntentResolver;
import com.limpoxe.fairy.manager.PluginManagerHelper;
import com.limpoxe.fairy.util.RefInvoker;

import java.io.File;
import java.util.List;

/**
 * 调研结果：
 * 1. host不能调用plugin的ContentProvider，但是plugin可以成功调用host的ContentProvider
 * 2. host可以成功调起plugin的IntentService，plugin也可以成功调起host的IntentService
 * 3. host无法Class.forName(插件中类)，所以host中newInstance插件中类的方案无法完成
 * 4. host可以发送info到PluginIntentService，plugin处理完毕，通过HostContentProvider返回结果
 */
public class MainActivity extends AppCompatActivity {

    private TextView tvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView tvTest = (TextView) findViewById(R.id.tv_test);
        final Button btnTest = (Button) findViewById(R.id.btn_test);
        final Button btnTest2 = (Button) findViewById(R.id.btn_test2);
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


        btn1.setEnabled(false);
        btnTest.setEnabled(false);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    boolean exist = false;
                    Intent intent = null;
                    intent = new Intent("android.plugin.framework.launch_test_action");
                    exist = IntentCompactUtil.checkIntentHasHandle(view.getContext(), intent);
                    Log.d("PPP", "installPlugin|" + IntentCompactUtil.convertIntentToString(intent) + "|exist|" + exist);

                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("mifg://android_plugin_framework/test?id=0"));

                    exist = IntentCompactUtil.checkIntentHasHandle(view.getContext(), intent);
                    // Intent intent = Intent.parseUri(strIntent, Intent.URI_INTENT_SCHEME);
                    Log.d("PPP", "installPlugin|" + IntentCompactUtil.convertIntentToString(intent) + "|exist|" + exist);

                    if (exist) {
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                }
            }
        });
        btnTest2.setEnabled(false);
        btnTest2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    for (PluginDescriptor pd : PluginManagerHelper.getPlugins()) {
                        if (pd != null && pd.getPackageName() != null) {
                            Log.d("PPP", "pd|" + pd.getPackageName() + "|" + pd.getInstalledPath() + "|" + pd.getVersion());
                            // 以下是自启动实验
                            Intent launchIntent = getPackageManager().getLaunchIntentForPackage(pd.getPackageName());
                            if (launchIntent == null && !FairyGlobal.isEnablePackageManagerProxyInMainProcess()) {
                                Log.d("PPP", "create a intent to handle");
                                launchIntent = IntentCompactUtil.convertStringToIntent("intent:#Intent;action=android.intent.action.MAIN;category=android.intent.category.LAUNCHER;launchFlags=0x10000000;package=tv.zhenjing.vitamin;component=tv.zhenjing.vitamin/com.kuaiest.video.SplashActivity;end");
                            }
                            if (launchIntent != null) {
                                Log.d("PPP", "installPlugin|" + IntentCompactUtil.convertIntentToString(launchIntent) + "|" + launchIntent.getPackage());
                                try {
                                    boolean hasPluginFilter = FairyGlobal.hasPluginFilter();
                                    boolean matchFilter = FairyGlobal.filterPlugin(launchIntent);
                                    Log.d("PPP", "checkPluginFilter->hasPluginFilter|" + hasPluginFilter + "|matchFilter|" + matchFilter);
                                    if (hasPluginFilter && matchFilter) {
                                        PluginIntentResolver.resolveActivity(launchIntent);
                                    }
                                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(launchIntent);
                                } catch (Exception e) {
                                }
                            }
                            // 以下是scheme调起实验
//                            Intent intent = new Intent(Intent.ACTION_VIEW);
//                            intent.setPackage(pd.getPackageName());
//                            intent.setData(Uri.parse("zhenjing://detail?id=361354494851338244")); // scheme调用方式
//                            try {
//                                boolean hasPluginFilter = FairyGlobal.hasPluginFilter();
//                                boolean matchFilter = FairyGlobal.filterPlugin(intent);
//                                Log.d("PPP", "checkPluginFilter->hasPluginFilter|" + hasPluginFilter + "|matchFilter|" + matchFilter);
//                                if (hasPluginFilter && matchFilter) {
//                                    PluginIntentResolver.resolveActivity(intent);
//                                }
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                            } catch (Exception e) {
//                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
        });

        String patchDirPath = FileCompactUtil.getPatchDirPath(this);
        FileCompactUtil.copyAssetsBySuffixToDir(this, ".apk", patchDirPath);

        File[] files = FileCompactUtil.listDirFilesBySuffix(patchDirPath, ".apk");
        for (File file : files) {
            int resultCode = PluginManagerHelper.installPlugin(file.getAbsolutePath());
            String message = getPluginErrMsg(resultCode);
            Log.d("PPP", "installPlugin|" + file.getName() + "|" + message);
        }

        List<PluginDescriptor> plugins = PluginManagerHelper.getPlugins();
        if (plugins != null && plugins.size() > 0) {
            tvTest.setText("有插件");
            btn1.setEnabled(true);
            btnTest.setEnabled(true);
            btnTest2.setEnabled(true);
        }

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
                String identify = HostUtil.getProviderIdentify(view.getContext());
                Log.d("PPP", "getProviderIdentify|" + identify);
                btn2ret.setText(identify);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("apf.plugin.action.PLUGIN_INTENT_SERVICE");
                intent.setPackage("apf.plugin");
                startService(intent);
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
                try {
                    Class classType = RefInvoker.forName("apf.plugin.PluginObject");
                    Object obj = classType.newInstance();
                    Log.d("PPP", "instance|" + obj);
                } catch (Exception e) {
                    Log.d("PPP", "Exception|" + e.getMessage());
                    e.printStackTrace();
                }

                TaskImpl instance = null;
                try {
                    Class classType = Class.forName("apf.plugin.PluginTask");
                    Object obj = classType.newInstance();
                    instance = (TaskImpl) obj;
                    Log.d("PPP", "instance|" + instance);
                } catch (Exception e) {
                    Log.d("PPP", "Exception|" + e.getMessage());
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
                TaskPayload payload = new TaskPayload();
                payload.identify = "version1";
                intent.putExtra("taskpayload", (Parcelable) payload);
                startService(intent);
            }
        });
    }

    private static String getPluginErrMsg(int code) {
        if (code == PluginManagerHelper.SUCCESS) {
            return "成功";
        } else if (code == PluginManagerHelper.SRC_FILE_NOT_FOUND) {
            return "失败: 安装文件未找到";
        } else if (code == PluginManagerHelper.COPY_FILE_FAIL) {
            return "失败: 复制安装文件到安装目录失败";
        } else if (code == PluginManagerHelper.SIGNATURES_INVALIDATE) {
            return "失败: 安装文件验证失败";
        } else if (code == PluginManagerHelper.VERIFY_SIGNATURES_FAIL) {
            return "失败: 插件和宿主签名串不匹配";
        } else if (code == PluginManagerHelper.PARSE_MANIFEST_FAIL) {
            return "失败: 插件Manifest文件解析出错";
        }
        if (code == PluginManagerHelper.FAIL_BECAUSE_SAME_VER_HAS_LOADED) {
            return "失败: 同版本插件已加载,无需安装";
        } else if (code == PluginManagerHelper.MIN_API_NOT_SUPPORTED) {
            return "失败: 当前系统版本过低,不支持此插件";
        } else if (code == PluginManagerHelper.PLUGIN_NOT_EXIST) {
            return "失败: 插件不存在";
        } else if (code == PluginManagerHelper.REMOVE_FAIL) {
            return "失败: 删除插件失败";
        } else if (code == PluginManagerHelper.HOST_VERSION_NOT_SUPPORT_CURRENT_PLUGIN) {
            return "失败: 插件要求的宿主版本和当前宿主版本不匹配";
        } else {
            return "失败: 其他 code=" + code;
        }
    }
}
