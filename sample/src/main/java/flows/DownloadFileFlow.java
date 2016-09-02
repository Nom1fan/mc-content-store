package flows;

import android.content.Context;

import com.nostra13.universalimageloader.sample.asynctasks.DownloadFileAsyncTask;
import com.nostra13.universalimageloader.sample.asynctasks.GetFileSizeFromUrlAsyncTask;

import java.io.IOException;

/**
 * Created by Mor on 02/09/2016.
 */
public class DownloadFileFlow implements DownloadFileFlowListener {

    private Context context;
    private String url;

    public void startDownloadFileFlow(Context context, String url) {
        this.context = context;
        this.url = url;
        new GetFileSizeFromUrlAsyncTask(this).execute(url);
    }

    @Override
    public void continueDownloadFileFlow(int fileSize) {
        try {
            new DownloadFileAsyncTask(context, url, fileSize).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
