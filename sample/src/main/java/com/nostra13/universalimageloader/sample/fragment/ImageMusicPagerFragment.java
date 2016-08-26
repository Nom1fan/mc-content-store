package com.nostra13.universalimageloader.sample.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.sample.Constants;
import com.nostra13.universalimageloader.sample.R;
import com.nostra13.universalimageloader.sample.asynctasks.PopulateUrlsAsyncTask;
import com.nostra13.universalimageloader.sample.behaviors.validate.ValidateAudioFormatBehavior;

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
public class ImageMusicPagerFragment extends BaseFragment implements PopulateUrlsAsyncTask.PostPopulateListener {
    public static final int INDEX = 14;
    ViewPager pager;
    View audioLayout;
    Button button;
    VideoView videoview=null;
    TextView textview=null;
    View rootView;
    String simage;
    Uri video;
    MediaController mediacontroller;
    String[] filenames;

    private List<String> audioUrls;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fr_image_pager, container, false);

        mediacontroller = new MediaController(getActivity()){

            @Override
            public void hide() {
                mediacontroller.show();
            }
            public boolean dispatchKeyEvent(KeyEvent event)
            {
                if (event.getKeyCode() == KeyEvent.KEYCODE_BACK)
                    ((Activity) getContext()).finish();

                return super.dispatchKeyEvent(event);
            }
        };
  //      mediacontroller.setAnchorView(videoView);


        pager = (ViewPager) rootView.findViewById(R.id.pager);

        new PopulateUrlsAsyncTask(new ValidateAudioFormatBehavior(), Constants.AUDIO_LIB_URL, this).execute();


//        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//               // videoPlaying = false;
//            }
//
//
//            @Override
//            public void onPageSelected(int position) {
//                simage=IMAGE_URLS[position];
//                try {
//                    // Start the MediaController
//                    // Get the URL from String VideoURL
//                    video= Uri.parse(simage);
//
//                    videoview.setMediaController(mediacontroller);
//                    if(!mediacontroller.isShowing())
//                    {mediacontroller.show();}
//                    videoview.setVideoURI(video);
//
//                   textview.setText(filenames[position]);
//                    videoview.start();
//
//                } catch (Exception e) {
//                    Log.e("Error", e.getMessage());
//                    e.printStackTrace();
//                }
//
//                //videoView.requestFocus();
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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible())
        {
            if (!isVisibleToUser) {
               videoview = (VideoView) pager.getFocusedChild();
               videoview.pause();
            }
            if (isVisibleToUser) {
            }
        }
    }

    @Override
    public void constructPostPopulate(List<String> urls) {
        audioUrls = urls;
        pager.setAdapter(new ImageAdapter(getActivity()));
        pager.setCurrentItem(getArguments().getInt(Constants.Extra.MEDIA_POSITION, 0));
    }

    private class ImageAdapter extends PagerAdapter {

        //	private static final String[] IMAGE_URLS = Constants.IMAGES;
        //final RecyclerView.ViewHolder holder;
        private LayoutInflater inflater;
        private DisplayImageOptions options;

        ImageAdapter(Context context) {
            inflater = LayoutInflater.from(context);

//			options = new DisplayImageOptions.Builder()
//					.showImageForEmptyUri(R.drawable.ic_empty)
//					.showImageOnFail(R.drawable.ic_error)
//					.resetViewBeforeLoading(true)
//					.cacheOnDisk(true)
//					.imageScaleType(ImageScaleType.EXACTLY)
//					.bitmapConfig(Bitmap.Config.RGB_565)
//					.considerExifParams(true)
//					.displayer(new FadeInBitmapDisplayer(300))
//					.build();
//            holder = (RecyclerView.ViewHolder) view.getTag();;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return audioUrls.size();
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
             audioLayout = inflater.inflate(R.layout.item_audio_pager_new, view, false);
            assert audioLayout != null;

            videoview = (VideoView) audioLayout.findViewById(R.id.videoView);
            final ProgressBar spinner = (ProgressBar) audioLayout.findViewById(R.id.loading);
            button = (Button) audioLayout.findViewById(R.id.button);
            textview= (TextView) audioLayout.findViewById(R.id.textview);
            textview.setText(filenames[position]);
//

//            final String simage=IMAGE_URLS[position];
//            try {
//                // Start the MediaController
//                MediaController mediacontroller = new MediaController(getActivity());
//                mediacontroller.setAnchorView(videoView);
//                // Get the URL from String VideoURL
//                Uri videoUri = Uri.parse(simage);
//                videoView.setMediaController(mediacontroller);
//                videoView.setVideoURI(videoUri);
//
//            } catch (Exception e) {
//                Log.e("Error", e.getMessage());
//                e.printStackTrace();
//            }
//
//            videoView.requestFocus();
//            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                // Close the progress bar and play the videoUri
//                public void onPrepared(MediaPlayer mp) {
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
//                }
//            });
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
                    new DownloadImage().execute(simage);
                    String path1= Environment.getExternalStorageDirectory().toString();
                    path1=path1+"/imageloader";

                    Toast.makeText(getActivity().getApplication().getBaseContext(), "The file is downloaded at " + path1, Toast.LENGTH_LONG).show();
                }
            });

            view.addView(audioLayout, 0);
            return audioLayout;


        }

//        @Override
//        public void setPrimaryItem(ViewGroup container, int position, Object object) {
//            super.setPrimaryItem(container, position, object);
//            videoView.stopPlayback();
//
//        }

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
            PD= ProgressDialog.show(getActivity(),null, "Downloading ...", true);
            PD.setCancelable(true);
        }

        @Override
        protected String doInBackground(String... URL) {
            String filename = new BigInteger(130,new SecureRandom()).toString(32)+".mp4";
            DownloadFile(URL[0],filename);
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
                    + File.separator +"ImageLoader"+File.separator +"Video";
            File RootFile = new File(RootDir);
            RootFile.mkdir();
            File RootFile1=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

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
