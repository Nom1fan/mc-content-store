package com.nostra13.universalimageloader.sample.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Mor on 01/07/2016.
 */
public abstract class MediaFilesUtils {

    private static final String TAG = MediaFilesUtils.class.getSimpleName();

    private static final String[] imageFormats = { "jpg", "png", "jpeg", "bmp", "gif" , "webp" };
    private static final List<String> imageFormatsList = Arrays.asList(imageFormats);
    private static final String[] audioFormats = { "mp3", "ogg" , "flac" , "mid" , "xmf" , "mxmf" , "rtx" , "ota" , "imy" , "wav" ,"m4a" , "aac"};
    private static final List<String> audioFormatsList = Arrays.asList(audioFormats);
    private static final String[] videoFormats = { "avi", "mpeg", "mp4", "3gp", "wmv" , "webm" , "mkv"  };
    private static final List<String> videoFormatsList = Arrays.asList(videoFormats);

    public static boolean isValidImageFormat(String pathOrUrl) {
        String extension = pathOrUrl.substring(pathOrUrl.lastIndexOf(".") + 1);
        return imageFormatsList.contains(extension.toLowerCase());
    }

    public static boolean isValidAudioFormat(String pathOrUrl) {
        String extension = pathOrUrl.substring(pathOrUrl.lastIndexOf(".") + 1);
        return audioFormatsList.contains(extension.toLowerCase());
    }

    public static boolean isValidVideoFormat(String pathOrUrl) {
        String extension = pathOrUrl.substring(pathOrUrl.lastIndexOf(".") + 1);
        return videoFormatsList.contains(extension.toLowerCase());
    }


    public static File getFileByMD5(String md5, String folderPath) {
        File result = null;
        File yourDir = new File(folderPath);
        for (File f : yourDir.listFiles()) {

            if (f.isFile()) {
                String fileName = f.getName();
                if (fileName.contains(md5)) {
                    result = f;
                    break;
                }
            }
        }
        return result;
    }

    public static void triggerMediaScanOnFile(Context context, File file) {
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
    }
}
