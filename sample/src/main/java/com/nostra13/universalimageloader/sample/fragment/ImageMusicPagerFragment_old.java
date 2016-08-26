/*******************************************************************************
 * Copyright 2011-2014 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
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
import android.media.AudioManager;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.sample.Constants;
import com.nostra13.universalimageloader.sample.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class ImageMusicPagerFragment_old extends BaseFragment {

    public static final int INDEX = 11;
    public String[] IMAGE_URLS;
    ViewPager pager;
    Button button;
    Button play, pause,stop;
    ImageView image;
    MyTask myTask = new MyTask();


    //    MediaController mediaplayer=null;
    MediaPlayer mediaplayer;
    String simage;
    Uri audio;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fr_image_pager, container, false);
       mediaplayer=new MediaPlayer();
        myTask.execute();

        pager = (ViewPager) rootView.findViewById(R.id.pager);

//        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                simage=IMAGE_URLS[position];
//                try{
//
//                    //audio=Uri.parse(simage);
//
//                   // mediaplayer.setDataSource(getContext(),Uri.parse(simage));
//                    mediaplayer.setDataSource(getContext(),Uri.parse(IMAGE_URLS[position]));
//                    mediaplayer.prepare();
//                    mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                    mediaplayer.start();
//
//                }
//                catch (Exception e){
//                   // Log.d("this--","------"+e.getMessage());
//                    e.printStackTrace();
//                }
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


    private class ImageAdapter extends PagerAdapter {

        //	private static final String[] IMAGE_URLS = Constants.IMAGES;

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
            return IMAGE_URLS.length;
        }




        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            View imageLayout = inflater.inflate(R.layout.item_audio_pager, view, false);
            assert imageLayout != null;
            ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
            ImageView v1=(ImageView)imageLayout.findViewById(R.id.audioview);
            v1.setImageDrawable(getResources().getDrawable(R.drawable.mp3));
            play = (Button) imageLayout.findViewById(R.id.play);
            pause = (Button) imageLayout.findViewById(R.id.pause);
            stop = (Button) imageLayout.findViewById(R.id.stop);
           // mediaplayer=MediaPlayer.create(getContext(),Uri.parse(IMAGE_URLS[position]));
            try {

                mediaplayer.setDataSource(getContext(),Uri.parse(IMAGE_URLS[position]));
                mediaplayer.prepare();
                mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaplayer.start();



            }
        catch (IllegalStateException e) {
            Log.d("this---" , "IllegalStateException: " + e.getMessage());
        }
        catch (IOException e) {
            Log.d("this---", "IOException: " + e.getMessage());
        }
        catch (IllegalArgumentException e) {
            Log.d("this---", "IllegalArgumentException: " + e.getMessage());
        }
        catch (SecurityException e) {
            Log.d("this---", "SecurityException: " + e.getMessage());
        }
            catch(Exception e){


            }
            //final GifImageView imageView =(GifImageView) videoLayout.findViewById(R.id.image);
            //imageView.startAnimation();
            //final GifImageView imageView =(GifImageView) videoLayout.findViewById(R.id.image);
            //imageView.startAnimation();

//            new GifDataDownloader() {
//                @Override protected void onPostExecute(final byte[] bytes) {
//                    imageView.setBytes(bytes);
//                    imageView.startAnimation();
//                    Log.d("TAG----", "GIF width is " + imageView.getGifWidth());
//                    Log.d("TAG---", "GIF height is " + imageView.getGifHeight());
//                }
//            }.execute(IMAGE_URLS[position]);

            final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);
            button = (Button) imageLayout.findViewById(R.id.button);
            final String simage = IMAGE_URLS[position];


//            ImageLoader.getInstance().displayImage(IMAGE_URLS[position], imageView, options, new SimpleImageLoadingListener() {
//                @Override
//                public void onLoadingStarted(String imageUri, View view) {
//                    spinner.setVisibility(View.VISIBLE);
//                }
//
//                @Override
//                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                    String message = null;
//                    switch (failReason.getType()) {
//                        case IO_ERROR:
//                            message = "Input/Output error";
//                            break;
//                        case DECODING_ERROR:
//                            message = "Image can't be decoded";
//                            break;
//                        case NETWORK_DENIED:
//                            message = "Downloads are denied";
//                            break;
//                        case OUT_OF_MEMORY:
//                            message = "Out Of Memory error";
//                            break;
//                        case UNKNOWN:
//                            message = "Unknown error";
//                            break;
//                    }
//                    Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
//
//                    spinner.setVisibility(View.GONE);
//                }
//
//                @Override
//                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                    spinner.setVisibility(View.GONE);
//                }
//            });

            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {

                    // Execute DownloadImageAsyncTask AsyncTask
                    new DownloadImage().execute(simage);
                    //String path1= Environment.getExternalStorageDirectory().toString();
                    String path1=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                    //path1=path1+"/imageloader";

                    Toast.makeText(getActivity().getApplication().getBaseContext(), "The file is downloaded at " + path1, Toast.LENGTH_LONG).show();
                    //Toast.makeText(getActivity().getApplication().getApplicationContext(), "this is image", Toast.LENGTH_LONG);
                }
            });
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!mediaplayer.isPlaying())
                        mediaplayer.start();
                }
            });
            stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mediaplayer.isPlaying())
                        mediaplayer.stop();
                }
            });
            pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mediaplayer.isPlaying())
                        mediaplayer.pause();
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

        ArrayList<String> arr_linkText = new ArrayList<String>();

        @Override
        protected ArrayList<String> doInBackground(Void... params) {

            Document doc;
            String linkText = "";
            String url1 = "http://server.mediacallz.com/ContentStore/files/Audio/";
            try {
                doc = Jsoup.connect(url1).get();
                //Elements links = doc.select("td.right td a").get();
                for (Element el : doc.select("td a")) {
                    linkText = el.attr("href");
                    Log.d("filename----", url1 + linkText);
                    if(linkText.contains(".")){
                    arr_linkText.add(url1 + linkText); // add value to ArrayList
                    }}
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return arr_linkText;     //<< retrun ArrayList from here
        }


        @Override
        protected void onPostExecute(ArrayList<String> result) {

            IMAGE_URLS = arr_linkText.toArray(new String[0]);
            pager.setAdapter(new ImageAdapter(getActivity()));
            pager.setCurrentItem(getArguments().getInt(Constants.Extra.MEDIA_POSITION, 0));


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
            String filename = new BigInteger(130,new SecureRandom()).toString(32)+".mp3";
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
                    +File.separator +"ImageLoader"+ File.separator + "Photos";
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