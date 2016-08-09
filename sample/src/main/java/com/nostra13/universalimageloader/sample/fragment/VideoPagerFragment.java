package com.nostra13.universalimageloader.sample.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.sample.Constants;
import com.nostra13.universalimageloader.sample.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by maitray on 9/8/16.
 */
public class VideoPagerFragment extends BaseFragment {
    public static final int INDEX = 9;
    public String[] IMAGE_URLS;
    ViewPager pager;
    Button button;
    ImageView image;
    View rootView;
    public static Boolean videoPlaying = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fr_image_pager, container, false);

        MyTask myTask = new MyTask();

        pager = (ViewPager) rootView.findViewById(R.id.pager);
        pager.setOffscreenPageLimit(0);
        myTask.execute();
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                videoPlaying = false;
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return rootView;

    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible())
        {
            if (!isVisibleToUser) {
                VideoView mvideoView = (VideoView) pager.getFocusedChild();
                mvideoView.pause();
            }
            if (isVisibleToUser) {
            }
        }
    }

    private class ImageAdapter extends PagerAdapter {

        //	private static final String[] IMAGE_URLS = Constants.IMAGES;

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
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return IMAGE_URLS.length;
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            View imageLayout = inflater.inflate(R.layout.item_video_pager, view, false);
            assert imageLayout != null;

            final VideoView videoview = (VideoView) imageLayout.findViewById(R.id.videoView);
            final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);
            button = (Button) imageLayout.findViewById(R.id.button);
            final String simage=IMAGE_URLS[position];
            try {
                // Start the MediaController
                MediaController mediacontroller = new MediaController(getActivity());
                mediacontroller.setAnchorView(videoview);
                // Get the URL from String VideoURL
                Uri video = Uri.parse(simage);
                videoview.setMediaController(mediacontroller);
                videoview.setVideoURI(video);

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            videoview.requestFocus();
            videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                // Close the progress bar and play the video
                public void onPrepared(MediaPlayer mp) {
                    if(!videoPlaying){
                        videoview.start();
                        videoPlaying = true;
                    }

                }
            });
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

                    // Execute DownloadImage AsyncTask
                    new DownloadImage().execute(simage);
                    String path1= Environment.getExternalStorageDirectory().toString();
                    path1=path1+"/imageloader";

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


    class MyTask extends AsyncTask<Void, Void, ArrayList<String>> {

        ArrayList<String> arr_linkText=new ArrayList<String>();

        @Override
        protected ArrayList<String> doInBackground(Void... params) {

            Document doc;
            String linkText = "";
            String url1="http://server.mediacallz.com/ContentStore/files/videos/";
            try {
                doc = Jsoup.connect(url1).get();
                //Elements links = doc.select("td.right td a").get();
                for (Element el : doc.select("td a")) {
                    linkText = el.attr("href");
                    Log.d("filename----", url1 + linkText);
                    arr_linkText.add(url1+linkText); // add value to ArrayList
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return arr_linkText;     //<< retrun ArrayList from here
        }


        @Override
        protected void onPostExecute(ArrayList<String> result) {

            IMAGE_URLS=arr_linkText.toArray(new String[0]);
            pager.setAdapter(new ImageAdapter(getActivity()));
            pager.setCurrentItem(getArguments().getInt(Constants.Extra.IMAGE_POSITION, 0));


        }
    }

    // DownloadImage AsyncTask
    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getContext(),"test",Toast.LENGTH_LONG);
            Toast.makeText(getContext(),"test1111",Toast.LENGTH_LONG);
            // Create a progressdialog
            // mProgressDialog = new ProgressDialog(MainActivity.this);
            // Set progressdialog title
            // mProgressDialog.setTitle("Download Image Tutorial");
            // Set progressdialog message
            //mProgressDialog.setMessage("Loading...");
            //mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            //mProgressDialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = URL[0];
            String[] ext = imageURL.split("/Photos/");

            Bitmap bitmap = null;
            try {
                // Download Image from URL
                InputStream input = new java.net.URL(imageURL).openStream();
                //Toast.makeText(getContext(),imageURL,Toast.LENGTH_LONG);
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // Set the bitmap into ImageView
//            image.setImageBitmap(result);
            String path = Environment.getExternalStorageDirectory().toString();
            path = path+"/imageloader";
            OutputStream fOut = null;
            String s = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
            StringBuilder salt = new StringBuilder();
            Random rnd = new Random();
            while (salt.length() < 18) {
                int index = (int) (rnd.nextFloat() * s.length());
                salt.append(s.charAt(index));
            }
            String saltStr = salt.toString();

            File f = new File(path);
            if(!f.exists())
            {
                f.mkdir();
            }
            File file = new File(path, saltStr+".jpeg"); // the File to save to
            try {
                fOut = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            result.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            Toast.makeText(getActivity().getApplicationContext(),"the file "+saltStr+".jpeg"+" is stored at "+path,Toast.LENGTH_LONG);

            // Close progressdialog
            // mProgressDialog.dismiss();

        }


    }
}
