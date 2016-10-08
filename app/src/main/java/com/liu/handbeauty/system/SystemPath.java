package com.liu.handbeauty.system;

import android.os.Environment;

/**
 * Created by Liu on 2016-05-25.
 */
public class SystemPath {
    public static String baseUrl="http://www.tngou.net/tnfs/api/";
    public static String imgUrl="http://tnfs.tngou.net/image";

    public static String SdPath= Environment.getExternalStorageDirectory().getAbsolutePath();
    public static String crash=SdPath+"/Beauty/crash";
    public static String download=SdPath+"/Beauty/download";
}
