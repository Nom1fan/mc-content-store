/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.sample.Constants;
import com.nostra13.universalimageloader.sample.R;
import com.nostra13.universalimageloader.sample.fragment.ImageGIFGridFragment;
import com.nostra13.universalimageloader.sample.fragment.ImageGridFragment;
import com.nostra13.universalimageloader.sample.fragment.ImageMusicListFragment;
import com.nostra13.universalimageloader.sample.fragment.VideoGalleryFragment;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class HomeActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_home);
	}

//	public void onImageListClick(View view) {
//		Intent intent = new Intent(this, GalleryLauncherFragmentActivity.class);
//		intent.putExtra(Constants.Extra.FRAGMENT_INDEX, ImageListFragment.INDEX);
//		startActivity(intent);
//	}

	public void onImageGridClick(View view) {
		Intent intent = new Intent(this, GalleryLauncherFragmentActivity.class);
		intent.putExtra(Constants.Extra.FRAGMENT_INDEX, ImageGridFragment.INDEX);
		startActivity(intent);
	}

    public void onImageGIFGridClick(View view) {
        Intent intent = new Intent(this, GalleryLauncherFragmentActivity.class);
        intent.putExtra(Constants.Extra.FRAGMENT_INDEX, ImageGIFGridFragment.INDEX);
        startActivity(intent);
    }

//	public void onImagePagerClick(View view) {
//		Intent intent = new Intent(this, GalleryLauncherFragmentActivity.class);
//		intent.putExtra(Constants.Extra.FRAGMENT_INDEX, ImagePagerFragment.INDEX);
//		startActivity(intent);
//	}
//
//	public void onImageGalleryClick(View view) {
//		Intent intent = new Intent(this, GalleryLauncherFragmentActivity.class);
//		intent.putExtra(Constants.Extra.FRAGMENT_INDEX, ImageGalleryFragment.INDEX);
//		startActivity(intent);
//	}
//
//	public void onFragmentsClick(View view) {
//		Intent intent = new Intent(this, ComplexImageActivity.class);
//		startActivity(intent);
//	}

	public void onVideoGalleryClick(View view){
		Intent intent = new Intent(this,GalleryLauncherFragmentActivity.class);
		intent.putExtra(Constants.Extra.FRAGMENT_INDEX, VideoGalleryFragment.INDEX);
		startActivity(intent);
	}

    public void onMusicListClick(View view){
        Intent intent = new Intent(this,GalleryLauncherFragmentActivity.class);
        intent.putExtra(Constants.Extra.FRAGMENT_INDEX, ImageMusicListFragment.INDEX);
        startActivity(intent);
    }

	@Override
	public void onBackPressed() {
		ImageLoader.getInstance().stop();
		super.onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.item_clear_memory_cache:
				ImageLoader.getInstance().clearMemoryCache();
				return true;
			case R.id.item_clear_disc_cache:
				ImageLoader.getInstance().clearDiskCache();
				return true;
			default:
				return false;
		}
	}

}