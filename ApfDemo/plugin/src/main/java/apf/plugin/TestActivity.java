package apf.plugin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TestActivity extends Activity {
//public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("PPP", "TestActivity-onCreate-----|" + BuildConfig.packagingTime);
        String yes = getApplicationContext().getString(android.R.string.yes);
        Log.d("PPP", "TestActivity-onCreate --- android.R.string.yes|" + yes);
        String name = getApplicationContext().getString(R.string.app_name);
        Log.d("PPP", "TestActivity-onCreate --- R.string.app_name|" + name);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        TextView text = (TextView) findViewById(R.id.text);

        final Button btn1 = (Button) findViewById(R.id.btn1);
        final TextView btn1ret = (TextView) findViewById(R.id.btn1_ret);
        final Button btn2 = (Button) findViewById(R.id.btn2);
        final TextView btn2ret = (TextView) findViewById(R.id.btn2_ret);
        final Button btn3 = (Button) findViewById(R.id.btn3);
        final TextView btn3ret = (TextView) findViewById(R.id.btn3_ret);
        final Button btn4 = (Button) findViewById(R.id.btn4);
        final TextView btn4ret = (TextView) findViewById(R.id.btn4_ret);

        CharSequence content = text.getText();
        text.setText(content + "-TestActivity");

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
    }
}
