package com.nostra13.universalimageloader.sample.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.nostra13.universalimageloader.sample.Constants;
import com.nostra13.universalimageloader.sample.R;
import com.nostra13.universalimageloader.sample.asynctasks.PopulateUrlsAsyncTask;
import com.nostra13.universalimageloader.sample.behaviors.validate.media.ValidateVideoFormatBehavior;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.util.List;

/**
 * Created by maitray on 9/8/16.
 */
public class VideoPagerFragment extends Fragment implements PopulateUrlsAsyncTask.PostPopulateListener {
    private static final String TAG = VideoPagerFragment.class.getSimpleName();
    public static final int INDEX = 9;
    ViewPager pager;
    FrameLayout videoLayout;
    Button button;
    VideoView videoView = null;
    View rootView;
    Uri videoUri;
    MediaController mediacontroller;
    private List<String> videoUrls;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fr_image_pager, container, false);
        mediacontroller = new MediaController(getActivity());
        mediacontroller.setAnchorView(videoView);

        pager = (ViewPager) rootView.findViewById(R.id.pager);
        pager.setVisibility(View.GONE);

        new PopulateUrlsAsyncTask(new ValidateVideoFormatBehavior(), Constants.VIDEO_LIB_URL, this).execute();

//        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                // videoPlaying = false;
//
//            }
//
//
//            @Override
//            public void onPageSelected(int position) {
//
//                simage = IMAGE_URLS[position];
//                try {
//                    // Start the MediaController
//                    // Get the URL from String VideoURL
//                    videoUri = Uri.parse(simage);
//                    videoView.setMediaController(mediacontroller);
//                    videoView.setVideoURI(videoUri);
//
//                } catch (Exception e) {
//                    Log.e("Error", e.getMessage());
//                    e.printStackTrace();
//                }
//
//                videoView.requestFocus();
//                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                    // Close the progress bar and play the videoUri
//                    public void onPrepared(MediaPlayer mp) {
////                    if(!videoPlaying){
////                        videoView.start();
////                        videoPlaying = true;
////                    }
////                    if(videoPlaying)
////                    {
////
////                        videoView.stopPlayback();
////                        videoPlaying=false;
////                    }
//                        videoView.start();
//                    }
//                });
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
        return rootView;
    }

    @Override
    public void constructPostPopulate(List<String> urls) {
        videoUrls = urls;
        //pager.setOffscreenPageLimit(0);
        pager.setAdapter(new ViewAdapter(getActivity()));
        pager.setVisibility(View.VISIBLE);
        pager.setCurrentItem(getArguments().getInt(Constants.Extra.MEDIA_POSITION));
//            pager.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    pager.setCurrentItem(getArguments().getInt(Constants.Extra.MEDIA_POSITION));
//                }
//            },200);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                playVideoInPage(position);
            }

            @Override
            public void onPageSelected(int position) {
                playVideoInPage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //videoView.stopPlayback();

            }
        });
    }

    private void playVideoInPage(int position) {
        try {
            videoUri = Uri.parse(videoUrls.get(position));
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        Toast.makeText(getContext(), "position " + isVisibleToUser, Toast.LENGTH_LONG).show();
//        if (isVisibleToUser) {
//            videoView.start();
//        } else {
//            videoView.stopPlayback();
//        }
    }

    private class ViewAdapter extends PagerAdapter {

        private LayoutInflater inflater;

        ViewAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return videoUrls.size();
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {

            videoLayout = (FrameLayout) inflater.inflate(R.layout.item_video_pager, view, false);
            if(videoLayout==null) {
                String errMsg = "VideoLayout returned as null after inflate()";
                Log.e(TAG, errMsg);
                throw new NullPointerException(errMsg);
            }

            videoView = (VideoView) videoLayout.findViewById(R.id.videoView);
            videoLayout.bringChildToFront(videoView);
            final ProgressBar spinner = (ProgressBar) videoLayout.findViewById(R.id.loading);
            button = (Button) videoLayout.findViewById(R.id.button);

            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
//                    new DownloadImage().execute(simage);
                }
            });

            view.addView(videoLayout, 0);
            return videoLayout;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
//            videoView.stopPlayback();
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

    // DownloadImageAsyncTask AsyncTask
    private class DownloadImage extends AsyncTask<String, Void, String> {

        ProgressDialog PD;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            PD = ProgressDialog.show(getActivity(), null, "Downloading ...", true);
            PD.setCancelable(true);
        }

        @Override
        protected String doInBackground(String... URL) {
            String filename = new BigInteger(130, new SecureRandom()).toString(32) + ".mp4";
            DownloadFile(URL[0], filename);
            return URL[0];
        }

        @Override
        protected void onPostExecute(String result) {
            PD.dismiss();
        }


    }

    public void DownloadFile(String fileURL, String fileName) {
        try {
            String RootDir = Environment.getExternalStorageDirectory()
                    + File.separator + "ImageLoader" + File.separator + "Video";
            File RootFile = new File(RootDir);
            RootFile.mkdir();
            File RootFile1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

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
