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

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.felipecsl.gifimageview.library.GifImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.sample.Constants;
import com.nostra13.universalimageloader.sample.R;
import com.nostra13.universalimageloader.sample.asynctasks.DownloadImageAsyncTask;
import com.nostra13.universalimageloader.sample.asynctasks.PopulateUrlsAsyncTask;
import com.nostra13.universalimageloader.sample.behaviors.validate.media.ValidateImageFormatBehavior;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class ImageGIFPagerFragment extends BaseFragment implements PopulateUrlsAsyncTask.PostPopulateListener {


    public static final int INDEX = 7;
    private ViewPager pager;
    private Button button;
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
        private DisplayImageOptions options;

        ImageAdapter(Context context) {
            inflater = LayoutInflater.from(context);

            options = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.drawable.ic_empty)
                    .showImageOnFail(R.drawable.ic_error)
                    .resetViewBeforeLoading(true)
                    .cacheOnDisk(true)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .considerExifParams(true)
                    .displayer(new FadeInBitmapDisplayer(300))
                    .build();
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
            //ImageView imageView = (ImageView) videoLayout.findViewById(R.id.image);
            final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);
            final GifImageView imageView = (GifImageView) imageLayout.findViewById(R.id.image);
            //imageView.startAnimation();


            new GifDataDownloader() {
                protected void onPreExecute() {
                    super.onPreExecute();
                    spinner.setVisibility(View.VISIBLE);
                    //progressDialog = ProgressDialog.show(getContext(),"wait","");
                    // progressDialog.setMessage("Loading...");
                    //progressDialog.show();
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
                    //progressDialog.dismiss();
                }
            }.execute(gifUrls.get(position));

            //spinner.setVisibility(View.GONE);
            //final ProgressBar spinner = (ProgressBar) videoLayout.findViewById(R.id.loading);
            button = (Button) imageLayout.findViewById(R.id.button);


//            ImageLoader.getInstance().displayImage(IMAGE_URLS[position], imageView, options, new SimpleImageLoadingListener() {
//				@Override
//				public void onLoadingStarted(String imageUri, View view) {
//					spinner.setVisibility(View.VISIBLE);
//				}
//
//				@Override
//				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//					String message = null;
//					switch (failReason.getType()) {
//						case IO_ERROR:
//							message = "Input/Output error";
//							break;
//						case DECODING_ERROR:
//							message = "Image can't be decoded";
//							break;
//						case NETWORK_DENIED:
//							message = "Downloads are denied";
//							break;
//						case OUT_OF_MEMORY:
//							message = "Out Of Memory error";
//							break;
//						case UNKNOWN:
//							message = "Unknown error";
//							break;
//					}
//					Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
//
//					spinner.setVisibility(View.GONE);
//				}
//
//				@Override
//				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//					spinner.setVisibility(View.GONE);
//				}
//			});

            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {

                    // Execute DownloadImageAsyncTask AsyncTask
                    //new DownloadImageAsyncTask().execute(simage);
                    new DownloadImageAsyncTask(getActivity()).execute(gifUrls.get(position));
                    String path1 = Environment.getExternalStorageDirectory().toString();
                    path1 = path1 + "/imageloader";

                    Toast.makeText(getActivity().getApplication().getBaseContext(), "The file is downloaded at " + path1, Toast.LENGTH_LONG).show();

                }
            });

            view.addView(imageLayout, 0);
            return imageLayout;


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

// DownloadImageAsyncTask AsyncTask


 class GifDataDownloader extends AsyncTask<String, Void, byte[]> {
    private static final String TAG = "GifDataDownloader";
    ProgressDialog pdia = null;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // spinner.setVisibility(View.VISIBLE);
        //progressDialog = ProgressDialog.show(getContext(),"wait","");
        // progressDialog.setMessage("Loading...");
        //progressDialog.show();
    }

    @Override
    protected byte[] doInBackground(final String... params) {
        final String gifUrl = params[0];

        if (gifUrl == null)
            return null;

        try {
            byte[] b = ByteArrayHttpClient.get(gifUrl);
            //spinner.setVisibility(View.GONE);
            return b;
        } catch (OutOfMemoryError e) {
            Log.e(TAG, "GifDecode OOM: " + gifUrl, e);
            return null;
        }

    }


    @Override
    protected void onPostExecute(byte[] bytes) {
        super.onPostExecute(bytes);
        //          progressDialog.dismiss();

    }

    public void DownloadFile(String fileURL, String fileName) {
        try {
            String RootDir = Environment.getExternalStorageDirectory()
                    + File.separator + "ImageLoader" + File.separator + "GIF";
            File RootFile = new File(RootDir);
            File RootFile1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            RootFile.mkdir();
            // File root = Environment.getExternalStorageDirectory();
            URL u = new URL(fileURL);
            HttpURLConnection c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();
            FileOutputStream f = new FileOutputStream(new File(RootFile1,
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

