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
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.sample.Constants;
import com.nostra13.universalimageloader.sample.R;
import com.nostra13.universalimageloader.sample.asynctasks.PopulateMultipleUrlsListsAsyncTask;
import com.nostra13.universalimageloader.sample.behaviors.validate.ValidateAudioFormatBehavior;
import com.nostra13.universalimageloader.sample.behaviors.validate.media.ValidateImageFormatBehavior;
import com.nostra13.universalimageloader.sample.behaviors.validate.media.ValidateMediaFormatBehavior;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class ImageMusicPagerFragment_new extends BaseFragment implements PopulateMultipleUrlsListsAsyncTask.PostMultiPopulateListener {

    public static final int INDEX = 1234;
    private static final String TAG = ImagePagerFragment.class.getSimpleName();
    private List<String> audioUrls;
    private ViewPager pager;
    private Button button;
    private MediaController mediacontroller;
    private VideoView videoView;
    private List<String> thumbsUrls;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fr_image_pager, container, false);
        mediacontroller = new MediaController(getActivity());
        mediacontroller.setAnchorView(videoView);
        pager = (ViewPager) rootView.findViewById(R.id.pager);


        List<ValidateMediaFormatBehavior> validateMediaFormatBehaviors = new LinkedList<>();
        validateMediaFormatBehaviors.add(new ValidateImageFormatBehavior());
        validateMediaFormatBehaviors.add(new ValidateAudioFormatBehavior());

        List<String> urlsToScan = new LinkedList<String>() {{
            add(Constants.AUDIO_THUMBS_URL);
            add(Constants.AUDIO_LIB_URL);
        }};
        new PopulateMultipleUrlsListsAsyncTask(validateMediaFormatBehaviors, urlsToScan, this).execute();


        return rootView;
    }

    @Override
    public void constructPostPopulate(List<List<String>> listList) {

        thumbsUrls = listList.get(0);
        audioUrls = listList.get(1);

        pager.setAdapter(new ImageAdapter(getActivity()));
        pager.setCurrentItem(getArguments().getInt(Constants.Extra.MEDIA_POSITION, 0));
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                playAudioInPage(position);
            }

            @Override
            public void onPageSelected(int position) {
                playAudioInPage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void playAudioInPage(int position) {
        try {
            Uri videoUri = Uri.parse(audioUrls.get(position));
            mediacontroller.setVisibility(View.VISIBLE);
            videoView.setMediaController(mediacontroller);
            videoView.setVideoURI(videoUri);
            videoView.setZOrderOnTop(false);
            videoView.setVisibility(View.VISIBLE);
            videoView.requestFocus();
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    videoView.setBackgroundColor(Color.TRANSPARENT);
                    videoView.start();
                }
            });

        } catch (Exception e) {
            Log.e("Error streaming video", e.getMessage());
            e.printStackTrace();
        }
    }

    private class ImageAdapter extends PagerAdapter {

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
            return thumbsUrls.size();
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            View audioLayout = inflater.inflate(R.layout.item_audio_pager_new, view, false);
            assert audioLayout != null;
            ImageView imageView = (ImageView) audioLayout.findViewById(R.id.audioimage);
            videoView = (VideoView) audioLayout.findViewById(R.id.videoView);

            final ProgressBar spinner = (ProgressBar) audioLayout.findViewById(R.id.loading);
            button = (Button) audioLayout.findViewById(R.id.button);

            ImageLoader.getInstance().displayImage(thumbsUrls.get(position), imageView, options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    spinner.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    String message = null;
                    switch (failReason.getType()) {
                        case IO_ERROR:
                            message = "Input/Output error";
                            break;
                        case DECODING_ERROR:
                            message = "Image can't be decoded";
                            break;
                        case NETWORK_DENIED:
                            message = "Downloads are denied";
                            break;
                        case OUT_OF_MEMORY:
                            message = "Out Of Memory error";
                            break;
                        case UNKNOWN:
                            message = "Unknown error";
                            break;
                    }
                    Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();

                    spinner.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    spinner.setVisibility(View.GONE);
                }
            });

            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {

                    // Execute DownloadImageAsyncTask AsyncTask
                    //new DownloadImageAsyncTask(getActivity()).execute(simage);
                    //String path1= Environment.getExternalStorageDirectory().toString();
                    String path1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                    //path1=path1+"/imageloader";

                    Toast.makeText(getActivity().getApplication().getBaseContext(), "The file is downloaded at " + path1, Toast.LENGTH_LONG).show();
                    //Toast.makeText(getActivity().getApplication().getApplicationContext(), "this is image", Toast.LENGTH_LONG);
                }
            });

            view.addView(audioLayout, 0);
            return audioLayout;
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