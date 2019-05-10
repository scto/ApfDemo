package com.limpoxe.fairy.core;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;

import com.limpoxe.fairy.manager.mapping.StubMappingProcessor;
import com.limpoxe.fairy.util.LogUtil;

import java.util.ArrayList;

public class FairyGlobal {
    private static boolean sIsInited;
    private static Application sApplication;
    private static boolean sIsLocalHtmlEnable = false;
    private static int sLoadingResId;
    private static long sMinLoadingTime = 400;
    private static boolean sIsNeedVerifyPluginSign = true;
    private static boolean sSupportRemoteViews = true;
    private static boolean sIsAllowDowngrade = true;
    private static ArrayList<StubMappingProcessor> mappingProcessors = new ArrayList<StubMappingProcessor>();
    private static boolean sOnlyUseStandAlonePlugins = false;
    private static boolean sEnableActivityManagerProxyInMainProcess = true;
    private static boolean sEnableNotificationManagerProxyInMainProcess = true;
    private static boolean sEnablePackageManagerProxyInMainProcess = true;

    public static Application getHostApplication() {
        if (!isInited()) {
            throw new IllegalStateException("not inited yet");
        }
        return sApplication;
    }

    /*package*/ static void setApplication(Application application) {
        sApplication = application;
    }

    /*package*/ static void setIsInited(boolean isInited) {
        sIsInited = isInited;
    }

    /*package*/ static boolean isInited() {
        return sIsInited;
    }

    /**
     * 插件中是否支持使用本地html文件
     * @param isLocalHtmlEnable
     */
    public static void setLocalHtmlenable(boolean isLocalHtmlEnable) {
        sIsLocalHtmlEnable = isLocalHtmlEnable;
    }

    public static boolean isLocalHtmlEnable() {
        return sIsLocalHtmlEnable;
    }

    /**
     * 控制框架日志是否打印
     * @param isLogEnable
     */
    public static void setLogEnable(boolean isLogEnable) {
        LogUtil.setEnable(isLogEnable);
    }

    /**
     * 首次打开插件时，如果是通过Activity打开，会显示一个空白loading页，
     * 通过resId设置loading页ui
     * @param resId
     */
    public static void setLoadingResId(int resId) {
        sLoadingResId = resId;
    }

    public static int getLoadingResId() {
        return sLoadingResId;
    }

    /**
     * 设置loading页最小等待时间，用于在插件较简单，初始化较快时，避免loading页一闪而过
     * 时间设置为0表示无loading页， 默认400ms
     * @param minLoadingTime
     */
    public static void setMinLoadingTime(long minLoadingTime) {
        sMinLoadingTime = minLoadingTime;
    }

    public static long getMinLoadingTime() {
        return sMinLoadingTime;
    }

    public static void setNeedVerifyPlugin(boolean needVerify) {
        sIsNeedVerifyPluginSign = needVerify;
    }

    public static boolean isNeedVerifyPlugin() {
        return sIsNeedVerifyPluginSign;
    }

    public static void setAllowDowngrade(boolean allowDowngrade) {
        sIsAllowDowngrade = allowDowngrade;
    }

    public static boolean isAllowDowngrade() {
        return sIsAllowDowngrade;
    }

    /**
     * 如果两个processor可以处理同一个映射关系，则后添加processor生效，先添加的processor会被忽略
     * @param processor
     */
    public static void registStubMappingProcessor(StubMappingProcessor processor) {
        if (processor == null) {
            return;
        }
        if (mappingProcessors == null) {
            mappingProcessors = new ArrayList<StubMappingProcessor>();
        }
        if (!mappingProcessors.contains(processor)) {
            mappingProcessors.add(processor);
        }
    }

    public static ArrayList<StubMappingProcessor> getStubMappingProcessors() {
        return mappingProcessors;
    }

    public static boolean isSupportRemoteViews() {
        return sSupportRemoteViews;
    }

    public static void setSupportRemoteViews(boolean support){
        sSupportRemoteViews = support;
    }

    private static PluginFilter sPluginFilter;

    public static void setPluginFilter(PluginFilter filter) {
        sPluginFilter = filter;
    }

    public static boolean hasPluginFilter() {
        return sPluginFilter != null;
    }

    public static boolean filterPlugin(String packageName) {
        if (sPluginFilter != null) {
            return sPluginFilter.accept(packageName);
        }
        return false;
    }

    public static boolean filterPlugin(ComponentName componentName) {
        if (sPluginFilter != null) {
            String packageName = componentName != null ? componentName.getPackageName() : null;
            return sPluginFilter.accept(packageName);
        }
        return false;
    }

    public static boolean filterPlugin(Intent intent) {
        if (sPluginFilter != null && intent != null) {
            String packageName1 = intent.getPackage();
            String packageName2 = null;
            ComponentName componentName = intent.getComponent();
            if (componentName != null) {
                packageName2 = componentName.getPackageName();
            }
            return sPluginFilter.accept(packageName1) || sPluginFilter.accept(packageName2);
        }
        return false;
    }

    public static void enableTimeLine() {
        TimeLine.ENABLE = true;
    }

    public static void disableTimeLine() {
        TimeLine.ENABLE = false;
    }

    public static void onlyUseStandAlonePlugins() {
        sOnlyUseStandAlonePlugins = true;
    }

    public static boolean isOnlyUseStandAlonePlugins() {
        return sOnlyUseStandAlonePlugins;
    }

    public static void enableActivityManagerProxyInMainProcess() {
        sEnableActivityManagerProxyInMainProcess = true;
    }

    public static void disableActivityManagerProxyInMainProcess() {
        sEnableActivityManagerProxyInMainProcess = false;
    }

    public static boolean isEnableActivityManagerProxyInMainProcess() {
        return sEnableActivityManagerProxyInMainProcess;
    }

    public static void enableNotificationManagerProxyInMainProcess() {
        sEnableNotificationManagerProxyInMainProcess = true;
    }

    public static void disableNotificationManagerProxyInMainProcess() {
        sEnableNotificationManagerProxyInMainProcess = false;
    }

    public static boolean isEnableNotificationManagerProxyInMainProcess() {
        return sEnableNotificationManagerProxyInMainProcess;
    }

    public static void enablePackageManagerProxyInMainProcess() {
        sEnablePackageManagerProxyInMainProcess = true;
    }

    public static void disablePackageManagerProxyInMainProcess() {
        sEnablePackageManagerProxyInMainProcess = false;
    }

    public static boolean isEnablePackageManagerProxyInMainProcess() {
        return sEnablePackageManagerProxyInMainProcess;
    }
}
