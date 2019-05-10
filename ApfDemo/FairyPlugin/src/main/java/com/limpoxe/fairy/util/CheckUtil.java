package com.limpoxe.fairy.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.limpoxe.fairy.content.PluginDescriptor;
import com.limpoxe.fairy.manager.PluginManagerHelper;

public class CheckUtil {

    /**
     * 综合检查
     */
    public static boolean checkBeforeStartIntent(Context ctx, Intent intent) {
        return checkBeforeStartIntent(ctx, intent, intent.getComponent());
    }

    /**
     * 综合检查
     */
    public static boolean checkBeforeStartIntent(Context ctx, Intent intent, ComponentName componentName) {
        if (componentName != null) {
            return checkBeforeStartIntent(ctx, intent, componentName.getPackageName(), componentName.getClassName());
        }
        return false;
    }

    /**
     * 综合检查
     */
    public static boolean checkBeforeStartIntent(Context ctx, Intent intent, String packageName, String clazzName) {
        return checkIntentHasHandle(ctx, intent) && checkPluginInstalled(packageName) && checkPluginReadyByClassName(clazzName);
    }

    /**
     * 是否已安装
     */
    public static boolean checkPluginInstalled(String pluginPackageName) {
        if (pluginPackageName == null || pluginPackageName.length() == 0) {
            return false;
        }
        try {
            if (PluginManagerHelper.isInstalled(pluginPackageName)) {
                boolean found = false;
                for (PluginDescriptor plugin : PluginManagerHelper.getPlugins()) {
                    if (plugin != null && pluginPackageName.equals(plugin.getPackageName())) {
                        found = true;
                        break;
                    }
                }
                return found;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 是否已加载
     */
    public static boolean checkPluginReadyByClassName(String pluginClazzName) {
        try {
            return PluginManagerHelper.getPluginDescriptorByClassName(pluginClazzName) != null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 是否有接收方
     */
    public static boolean checkIntentHasHandle(Context ctx, Intent intent) {
        try {
            boolean hasHandle = true;
            if (ctx.getApplicationContext().getPackageManager().resolveActivity(intent,
                    PackageManager.MATCH_DEFAULT_ONLY) == null) {
                if (ctx.getApplicationContext().getPackageManager().resolveService(intent,
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
}
