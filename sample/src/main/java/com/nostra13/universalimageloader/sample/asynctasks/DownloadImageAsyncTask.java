package com.nostra13.universalimageloader.sample.asynctasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;

/**
 * Created by Mor on 23/08/2016.
 */
public class DownloadImageAsyncTask extends AsyncTask<String, Void, String> {

    private Context context;

    public DownloadImageAsyncTask(Context context) {
        this.context=context;
    }

    private ProgressDialog progressDialog;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(context ,null, "Downloading ...", true);
        progressDialog.setCancelable(true);
    }

    @Override
    protected String doInBackground(String... URL) {
        String filename = new BigInteger(130,new SecureRandom()).toString(32)+".jpg";
        downloadFile(URL[0],filename);
        return URL[0];
    }

    @Override
    protected void onPostExecute(String result) {
        progressDialog.dismiss();
    }

    public void downloadFile(String fileURL, String fileName) {
        try {
            String RootDir = Environment.getExternalStorageDirectory()
                    + File.separator +"ImageLoader"+ File.separator + "Photos";
            File RootFile = new File(RootDir);
            RootFile.mkdir();

            File downloadsDir=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            URL u = new URL(fileURL);
            HttpURLConnection c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();
            FileOutputStream f = new FileOutputStream(new File(downloadsDir,
                    fileName));
            InputStream in = c.getInputStream();
            byte[] buffer = new byte[1024];
            int len1 = 0;

            while ((len1 = in.read(buffer)) > 0) {
                f.write(buffer, 0, len1);
            }
            f.close();


        } catch (Exception e) {

            Log.d("Error....", e.toString());
        }

    }
}
