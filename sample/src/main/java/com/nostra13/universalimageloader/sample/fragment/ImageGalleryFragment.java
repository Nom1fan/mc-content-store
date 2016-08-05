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

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.sample.Constants;
import com.nostra13.universalimageloader.sample.R;
import com.nostra13.universalimageloader.sample.activity.SimpleImageActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import static com.nostra13.universalimageloader.sample.Constants.grabWordList;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class ImageGalleryFragment extends BaseFragment {
    public String[] IMAGE_URLS;
	public static final int INDEX = 3;
    public Gallery gallery;
	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fr_image_gallery, container, false);
        MyTask myTask = new MyTask();

         gallery = (Gallery) rootView.findViewById(R.id.gallery);
        myTask.execute();
		return rootView;
	}


	private void startImagePagerActivity(int position) {
		Intent intent = new Intent(getActivity(), SimpleImageActivity.class);
		intent.putExtra(Constants.Extra.FRAGMENT_INDEX, ImagePagerFragment.INDEX);
		intent.putExtra(Constants.Extra.IMAGE_POSITION, position);
		startActivity(intent);
	}

	private class ImageAdapter extends BaseAdapter {

	//private static final String[] IMAGE_URLS = Constants.IMAGES;

//        private static String[] IMAGE_URLS;

//        static {
//
//                IMAGE_URLS = Constants.IMAGES;
//
//        }



        private LayoutInflater inflater;


		private DisplayImageOptions options;

		ImageAdapter(Context context) {
			inflater = LayoutInflater.from(context);

			options = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.ic_stub)
					.showImageForEmptyUri(R.drawable.ic_empty)
					.showImageOnFail(R.drawable.ic_error)
					.cacheInMemory(true)
					.cacheOnDisk(true)
					.considerExifParams(true)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.displayer(new RoundedBitmapDisplayer(20))
					.build();
//            myTask.execute();
		}

		@Override
		public int getCount() {
			return IMAGE_URLS.length;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView = (ImageView) convertView;
			if (imageView == null) {
				imageView = (ImageView) inflater.inflate(R.layout.item_gallery_image, parent, false);
			}
			ImageLoader.getInstance().displayImage(IMAGE_URLS[position], imageView, options);
			return imageView;
		}


	}

class MyTask extends AsyncTask<Void, Void, ArrayList<String>> {

    ArrayList<String> arr_linkText=new ArrayList<String>();

    @Override
    protected ArrayList<String> doInBackground(Void... params) {

        Document doc;
        String linkText = "";
        String url1="http://server.mediacallz.com/ContentStore/files/Photos/";
        try {
            doc = Jsoup.connect(url1).get();
            //Elements links = doc.select("td.right td a").get();
            for (Element el : doc.select("td a")) {
                linkText = el.attr("href");
                Log.d("filename----",url1+linkText);
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
        gallery.setAdapter(new ImageAdapter(getActivity()));
        gallery.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startImagePagerActivity(position);
            }
        });
    }
    }
}