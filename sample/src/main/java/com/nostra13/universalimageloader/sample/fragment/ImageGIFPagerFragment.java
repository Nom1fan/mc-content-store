/*******************************************************************************
 * Copyright 2011-2014 Sergey Tarasevich
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.nostra13.universalimageloader.sample.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.felipecsl.gifimageview.library.GifImageView;
import com.nostra13.universalimageloader.sample.Constants;
import com.nostra13.universalimageloader.sample.R;
import com.nostra13.universalimageloader.sample.asynctasks.PopulateUrlsAsyncTask;
import com.nostra13.universalimageloader.sample.behaviors.validate.media.ValidateImageFormatBehavior;

import java.util.List;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class ImageGIFPagerFragment extends BaseFragment implements PopulateUrlsAsyncTask.PostPopulateListener {


    public static final int INDEX = 7;
    private ViewPager pager;
    private Button downloadButton;
    private List<String> gifUrls;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fr_image_pager, container, false);
        pager = (ViewPager) rootView.findViewById(R.id.pager);

        new PopulateUrlsAsyncTask(new ValidateImageFormatBehavior(), Constants.GIF_LIB_URL, this).execute();

        return rootView;
    }

    @Override
    public void constructPostPopulate(List<String> urls) {
        gifUrls = urls;
        pager.setAdapter(new ImageAdapter(getActivity()));
        pager.setCurrentItem(getArguments().getInt(Constants.Extra.MEDIA_POSITION, 0));
    }

    public class ImageAdapter extends PagerAdapter {

        private LayoutInflater inflater;

        ImageAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return gifUrls.size();
        }

        @Override
        public Object instantiateItem(ViewGroup view, final int position) {
            View imageLayout = inflater.inflate(R.layout.item_pager_image, view, false);
            assert imageLayout != null;
            final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);
            final GifImageView imageView = (GifImageView) imageLayout.findViewById(R.id.image);

            executeGifDownloader(position, spinner, imageView);

            prepareDownloadButton(position, imageLayout);

            view.addView(imageLayout, 0);
            return imageLayout;


        }

        private void executeGifDownloader(int position, final ProgressBar spinner, final GifImageView imageView) {
            new GifDataDownloader() {
                protected void onPreExecute() {
                    super.onPreExecute();
                    spinner.setVisibility(View.VISIBLE);
                }

                @Override
                protected void onPostExecute(byte[] bytes) {
                    super.onPostExecute(bytes);
                    spinner.setVisibility(View.GONE);
                    imageView.setBytes(bytes);
                    imageView.startAnimation();
                    Log.d("TAG----", "GIF width is " + imageView.getGifWidth());
                    Log.d("TAG---", "GIF height is " + imageView.getGifHeight());
                    spinner.setVisibility(View.GONE);
                }
            }.execute(gifUrls.get(position));
        }

        private void prepareDownloadButton(final int position, View imageLayout) {
            downloadButton = (Button) imageLayout.findViewById(R.id.downloadButton);
            downloadButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    downloadFile(gifUrls.get(position));
                }
            });
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }
    }

}

 class GifDataDownloader extends AsyncTask<String, Void, byte[]> {
    private static final String TAG = "GifDataDownloader";

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected byte[] doInBackground(final String... params) {
        final String gifUrl = params[0];

        if (gifUrl == null)
            return null;

        try {
            return ByteArrayHttpClient.get(gifUrl);
        } catch (OutOfMemoryError e) {
            Log.e(TAG, "GifDecode OOM: " + gifUrl, e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(byte[] bytes) {
        super.onPostExecute(bytes);
    }
}

