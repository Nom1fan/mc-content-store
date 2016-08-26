package com.nostra13.universalimageloader.sample.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.nostra13.universalimageloader.sample.behaviors.validate.media.ValidateMediaFormatBehavior;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mor on 23/08/2016.
 */
public class PopulateUrlsAsyncTask extends AsyncTask<Void, Void, List<String>> {

    private static final String TAG = PopulateUrlsAsyncTask.class.getSimpleName();
    private ValidateMediaFormatBehavior validateMediaFormatBehavior;
    private ArrayList<String> resultUrls = new ArrayList<>();
    private PostPopulateListener listener;
    private String urlToScan;

    public PopulateUrlsAsyncTask(ValidateMediaFormatBehavior validateMediaFormatBehavior, String urlToScan, PostPopulateListener listener) {
        this.listener = listener;
        this.urlToScan = urlToScan;
        this.validateMediaFormatBehavior = validateMediaFormatBehavior;
    }

    @Override
    protected List<String> doInBackground(Void... params) {

        Document doc;
        String link;
        try {
            doc = Jsoup.connect(urlToScan).get();
            for (Element el : doc.select("td a")) {
                link = el.attr("href");
                Log.d(TAG, urlToScan + link);
                if(validateMediaFormatBehavior.isValidFormatByLink(link))
                    resultUrls.add(urlToScan + link);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultUrls;
    }

    @Override
    protected void onPostExecute(List<String> resultUrls) {
        listener.constructPostPopulate(resultUrls);
    }

    public interface PostPopulateListener {
        void constructPostPopulate(List<String> urls);
    }
}
