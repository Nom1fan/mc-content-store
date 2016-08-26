/*******************************************************************************
 * Copyright 2014 Sergey Tarasevich
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
package com.nostra13.universalimageloader.sample.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.nostra13.universalimageloader.sample.Constants;
import com.nostra13.universalimageloader.sample.R;
import com.nostra13.universalimageloader.sample.fragment.ImageGIFGridFragment;
import com.nostra13.universalimageloader.sample.fragment.ImageGIFPagerFragment;
import com.nostra13.universalimageloader.sample.fragment.ImageGalleryFragment;
import com.nostra13.universalimageloader.sample.fragment.ImageGridFragment;
import com.nostra13.universalimageloader.sample.fragment.ImageListFragment;
import com.nostra13.universalimageloader.sample.fragment.ImageMusicListFragment;
import com.nostra13.universalimageloader.sample.fragment.ImageMusicPagerFragment_new;
import com.nostra13.universalimageloader.sample.fragment.ImagePagerFragment;
import com.nostra13.universalimageloader.sample.fragment.VideoGalleryFragment;
import com.nostra13.universalimageloader.sample.fragment.VideoPagerFragment;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class GalleryAndPagersLauncherActivity extends FragmentActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		int frIndex = getIntent().getIntExtra(Constants.Extra.FRAGMENT_INDEX, 0);
		Fragment fr;
		String tag;
		int titleRes;


		switch (frIndex) {
			default:
			case ImageListFragment.INDEX:
				tag = ImageListFragment.class.getSimpleName();
				fr = getSupportFragmentManager().findFragmentByTag(tag);
				if (fr == null) {
					fr = new ImageListFragment();
				}
				titleRes = R.string.ac_name_image_list;
				break;

			case ImageGridFragment.INDEX:
				tag = ImageGridFragment.class.getSimpleName();
				fr = getSupportFragmentManager().findFragmentByTag(tag);
				if (fr == null) {
					fr = new ImageGridFragment();
				}
				titleRes = R.string.ac_name_image_grid;
				break;
            case ImageGIFGridFragment.INDEX:
                tag = ImageGridFragment.class.getSimpleName();
                fr = getSupportFragmentManager().findFragmentByTag(tag);
                if (fr == null) {
                    fr = new ImageGIFGridFragment();
                }
                titleRes = R.string.ac_name_image_grid;
                break;

            case ImagePagerFragment.INDEX:
				tag = ImagePagerFragment.class.getSimpleName();
				fr = getSupportFragmentManager().findFragmentByTag(tag);
				if (fr == null) {
					fr = new ImagePagerFragment();
					fr.setArguments(getIntent().getExtras());
				}
				titleRes = R.string.ac_name_image_pager;
				break;


            case ImageGIFPagerFragment.INDEX:
                tag = ImagePagerFragment.class.getSimpleName();
                fr = getSupportFragmentManager().findFragmentByTag(tag);
                if (fr == null) {
                    fr = new ImageGIFPagerFragment();
                    fr.setArguments(getIntent().getExtras());
                }
                titleRes = R.string.ac_name_image_pager;
                break;

			case ImageGalleryFragment.INDEX:
				tag = ImageGalleryFragment.class.getSimpleName();
				fr = getSupportFragmentManager().findFragmentByTag(tag);
				if (fr == null) {
					fr = new ImageGalleryFragment();
				}
				titleRes = R.string.ac_name_image_gallery;
				break;

            case VideoPagerFragment.INDEX:
                tag = VideoPagerFragment.class.getSimpleName();
                fr = getSupportFragmentManager().findFragmentByTag(tag);
                //fr.setUserVisibleHint(true);
                if (fr == null) {
                    fr = new VideoPagerFragment();
                    fr.setArguments(getIntent().getExtras());
                    fr.isVisible();
//                    fr.onCreate(savedInstanceState);
                    //fr.setUserVisibleHint(false);
                }
                titleRes = R.string.button_video_gallery;
                setTitle(titleRes);
                getSupportFragmentManager().beginTransaction().replace(android.R.id.content, fr, tag).commit();
                break;
			case VideoGalleryFragment.INDEX:
				tag = VideoGalleryFragment.class.getSimpleName();
				fr = getSupportFragmentManager().findFragmentByTag(tag);
				if (fr == null) {
					fr = new VideoGalleryFragment();
                    //fr.setArguments(getIntent().getExtras());
				}
				titleRes = R.string.ac_name_image_gallery;
				break;
            case ImageMusicListFragment.INDEX:
                tag = ImageMusicListFragment.class.getSimpleName();
                fr = getSupportFragmentManager().findFragmentByTag(tag);
                if (fr == null) {
                    fr = new ImageMusicListFragment();
                    //fr.setArguments(getIntent().getExtras());
                }
                titleRes = R.string.ac_name_image_pager;
                break;
            case ImageMusicPagerFragment_new.INDEX:
                tag = ImageMusicPagerFragment_new.class.getSimpleName();
                fr = getSupportFragmentManager().findFragmentByTag(tag);
                if (fr == null) {
                    fr = new ImageMusicPagerFragment_new();
                    fr.setArguments(getIntent().getExtras());
                }
                titleRes = R.string.ac_name_image_pager;
                break;


        }

		setTitle(titleRes);
		getSupportFragmentManager().beginTransaction().replace(android.R.id.content, fr, tag).commit();
	}

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }
    }
}