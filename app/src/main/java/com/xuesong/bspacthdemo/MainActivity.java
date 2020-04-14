package com.xuesong.bspacthdemo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView version = findViewById(R.id.version);
        //
        version.setText("fffffffffffffff"+BuildConfig.VERSION_NAME);

        requestPermission();

    }


    public void update(View view) {
        //1、合成 apk
        //先从服务器下载到差分包

        new ApkUpdateTask().execute();
    }




    class ApkUpdateTask extends AsyncTask<Void, Void, Boolean> {


        @Override
        protected Boolean doInBackground(Void... params) {

            Log.d(TAG,"开始下载 。。。");

            File patchFile = new File(Environment.getExternalStorageDirectory()+"/apk.patch");
            Log.d(TAG,"下载完成 。。。");

            String oldfile = ApkUtils.getSourceApkPath(MainActivity.this, getPackageName());

            String newFile = Contants.NEW_APK_PATH;
            if(patchFile.exists()){
                Log.e(TAG,"zai");
            }else {
                Log.e(TAG,"buzai");
            }

            String patchFileString = patchFile.getAbsolutePath();

            Log.d(TAG,"开始合并");
            int ret = MainActivity.bspatch(oldfile, newFile,patchFileString);
            Log.d(TAG,"开始完成");

            if (ret == 0) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                Log.d(TAG,"合并成功 开始安装新apk");
                ApkUtils.installApk(MainActivity.this, Contants.NEW_APK_PATH);
            }
        }
    }

    public void requestPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, "申请权限", Toast.LENGTH_SHORT).show();

            // 申请 相机 麦克风权限
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,

                    Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }
    }
    public static String TAG = "Tim_MainActivity";
    /**
     *
     * @param oldapk 当前运行的apk
     * @param patch  差分包
     * @param output 合成后的新的apk输出到
     */
    public static native int bspatch(String oldapk,String patch,String output);
}
