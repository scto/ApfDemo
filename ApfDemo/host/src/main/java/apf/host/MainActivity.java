package apf.host;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.limpoxe.fairy.content.PluginDescriptor;
import com.limpoxe.fairy.manager.PluginManagerHelper;

import java.io.File;
import java.util.List;

import file.compact.FileCompactUtil;

public class MainActivity extends AppCompatActivity {

    private TextView tvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView tvTest = (TextView) findViewById(R.id.tv_test);
        final Button btnTest = (Button) findViewById(R.id.btn_test);
        final TextView btn1ret = (TextView) findViewById(R.id.btn1_ret);
        final Button btn1 = (Button) findViewById(R.id.btn1);
        final TextView btn2ret = (TextView) findViewById(R.id.btn2_ret);
        final Button btn2 = (Button) findViewById(R.id.btn2);
        btn1.setEnabled(false);
        btnTest.setEnabled(false);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    boolean exist = false;
                    Intent intent = null;
                    intent = new Intent("android.plugin.framework.launch_test_action");
                    exist = checkIntentHasHandle(view.getContext(), intent);
                    Log.d("PPP", "installPlugin|" + convertIntentToString(intent) + "|exist|" + exist);

                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("mifg://android_plugin_framework/test?id=0"));

                    exist = checkIntentHasHandle(view.getContext(), intent);
                    // Intent intent = Intent.parseUri(strIntent, Intent.URI_INTENT_SCHEME);
                    Log.d("PPP", "installPlugin|" + convertIntentToString(intent) + "|exist|" + exist);

                    if (exist) {
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
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
    }

    private static String getPluginErrMsg(int code) {
        if(code== PluginManagerHelper.SUCCESS) {
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
        } if (code == PluginManagerHelper.FAIL_BECAUSE_SAME_VER_HAS_LOADED) {
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

    boolean checkIntentHasHandle(Context ctx, Intent intent) {
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

    String convertIntentToString(Intent intent) {
        try {
            return intent.toUri(Intent.URI_INTENT_SCHEME);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    Intent convertStringToIntent(String string) {
        try {
            return Intent.parseUri(string, Intent.URI_INTENT_SCHEME);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
