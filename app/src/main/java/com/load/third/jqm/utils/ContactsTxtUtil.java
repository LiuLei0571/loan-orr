package com.load.third.jqm.utils;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import static com.load.third.jqm.utils.TempUtils.tempDirectory;
import static com.load.third.jqm.utils.TempUtils.tempFile;

/**
 * Created by Administrator on 2017/4/17.
 */

public class ContactsTxtUtil {
    // 将字符串写入到文本文件中
    public static void writeTxtToFile(String fileName, String strcontent) {
        File file = makeFile(fileName);
        String strContent = strcontent + "\r\n";
        try {
            Writer outTxt = new OutputStreamWriter(new FileOutputStream(file, true), "gbk");
            outTxt.write(strContent);
            outTxt.close( );
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }

    // 生成文件
    private static File makeFile(String fileName) {
        File file = null;
        try {
            if (!tempFile.exists( )) {
                tempFile.mkdirs( );
            }
            file = new File(tempDirectory + fileName);
            if (!file.exists( )) {
                file.createNewFile( );
            }
        } catch (Exception e) {
            Log.i("error:", e + "");
        }
        return file;
    }

}
