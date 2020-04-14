/*
 * Created by 动脑科技-Tim on 17-8-18 下午9:35
 * Copyright (c) 2017. All rights reserved
 *
 * Last modified 17-8-18 下午9:35
 */

package com.xuesong.bspacthdemo;


import android.os.Environment;

import java.io.File;

public class Contants {

    public static final String PATCH_FILE = "apk.patch";
    public static final String URL_PATCH_DOWNLOAD = "http://119.23.236.161:8080/"+PATCH_FILE;
    //linux remote
    //public static final String URL_PATCH_DOWNLOAD = "http://www.dongnaoedu.com/"+PATCH_FILE;


    public static final String SD_CARD = Environment.getExternalStorageDirectory() + File.separator;

    //新版本apk的目录
    public static final String NEW_APK_PATH = SD_CARD+"dn_apk_new.apk";

    public static final String PATCH_FILE_PATH = SD_CARD+PATCH_FILE;
}
