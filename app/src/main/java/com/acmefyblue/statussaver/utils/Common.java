package com.acmefyblue.statussaver.utils;

import android.os.Environment;

import java.io.File;

public class Common {


    public static final int GRID_COUNT = 2;

    public static final File STATUS_DIRECTORY = new File(Environment.getExternalStorageDirectory() +
            File.separator + "WhatsApp/Media/.Statuses");
    public static final File STATUS_DIRECTORY2 = new File(Environment.getExternalStorageDirectory() +
            File.separator + "Android/media/com.whatsapp/Whatsapp/Media/.Statuses");

    public static String APP_DIR="/Status Saver";


}

