/*
 * Created by 动脑科技-Tim on 17-8-18 下午9:42
 * Copyright (c) 2017. All rights reserved
 *
 * Last modified 17-8-18 下午9:42
 */

package com.xuesong.bspacthdemo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import java.io.File;

public class ApkUtils {

    //获取APK版本号 在公司实际开发中 是根据 key uuid判断（渠道 版本）
    public static int getVersionCode (Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(packageName, 0);
            Log.d("Tim","getVersionCode = "+info.versionCode);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取已安装Apk文件的源Apk文件
     * 如：/data/app/my.apk
     *
     * @param context
     * @param packageName
     * @return
     */
    public static String getSourceApkPath(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName))
            return null;

        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(packageName, 0);
            return appInfo.sourceDir;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 安装Apk
     *
     * @param context
     * @param apkPath
     */
    public static void installApk(Context context, String apkPath) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            boolean hasInstallPermission = isHasInstallPermissionWithO(context);
            if (!hasInstallPermission) {
                context.startActivity(getInstallApkIntent(context,apkPath));
                return;
            }else {
                context.startActivity(getInstallApkIntent(context,apkPath));
            }
        }else {
            context.startActivity(getInstallApkIntent(context,apkPath));
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private static boolean isHasInstallPermissionWithO(Context context){
        if (context == null){
            return false;
        }
        return context.getPackageManager().canRequestPackageInstalls();
    }





    public static Intent getInstallApkIntent(Context context,String filePath){
        File file = new File(filePath);//更新包文件
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= 24)
        {

            // Android7.0及以上版本
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            //参数二:应用包名+".fileProvider"(和步骤二中的Manifest文件中的provider节点下的authorities对应)
            Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName()+ ".provider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        }
        else
        {
            // Android7.0以下版本
            intent.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        Log.e(MainActivity.TAG,filePath);

        return intent;
    }



}
