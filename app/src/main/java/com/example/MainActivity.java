package com.example;
/*
 * AudioVideoRecordingSample
 * Sample project to cature audio and video from internal mic/camera and save as MPEG4 file.
 *
 * Copyright (c) 2014-2015 saki t_saki@serenegiant.com
 *
 * File name: MainActivity.java
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 * All files in the folder are under this Apache License, Version 2.0.
 */

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.camera.R;
import com.example.database.DBHelper;
import com.example.database.Table;

import static com.example.database.DBHelper.TAG;

public class MainActivity extends AppCompatActivity {

	private final int REQUEST_PERMISSION = 1000;
	private final int REQUEST_MULTI_PERMISSIONS = 101;
	private int auth = 0;

	static private DBHelper dBHelper = null;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {



		// アクションバーの非表示
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);


		// ナビゲーションバーの非表示
		View decor = this.getWindow().getDecorView();
		decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);



		// 権限が許可されているか確認
		if (ContextCompat.checkSelfPermission(
				this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
				&& ContextCompat.checkSelfPermission(
				this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
				&& ContextCompat.checkSelfPermission(
				this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){

			super.onCreate(savedInstanceState);


			setContentView(R.layout.activity_main);

			if (savedInstanceState == null) {
				FragmentManager fragmentManager = getSupportFragmentManager();
				FragmentTransaction fragmentTransaction =
						fragmentManager.beginTransaction();
				TitleFragment titleFragment = new TitleFragment();
				fragmentTransaction.replace(R.id.container, titleFragment);
				fragmentTransaction.commit();

				/*

				getFragmentManager().beginTransaction()
						.add(R.id.container, new CameraFragment()).commit();

				*/
			}
			else {
				System.out.println("savedInstanceState = " + savedInstanceState);
				dBHelper = (DBHelper) savedInstanceState.getSerializable("dbHelper");
			}


		}
		else {
			requestLocationPermission();
		}
		/*
		// キーボード入力でナビゲーションバーが表示されてしまったときに再設定
		setKeyboardVisibilityListener(new OnKeyboardVisibilityListener() {
			@Override
			public void onVisibilityChanged(boolean visible) {
				Log.i("Keyboard state", visible ? "Keyboard is active" : "Keyboard is Inactive");
				// ナビゲーションバーの非表示
				View decor = getWindow().getDecorView();
				decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
			}
		});
		*/
		// 動画再生でナビゲーションバーが表示されたときに再設定
		decor.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener(){
			@Override
			public void onSystemUiVisibilityChange(int visibility) {
				View decor = getWindow().getDecorView();
				decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		final int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// 許可を求める
	private void requestLocationPermission() {
		if (ActivityCompat.shouldShowRequestPermissionRationale(this,
				Manifest.permission.CAMERA)
				|| ActivityCompat.shouldShowRequestPermissionRationale(this,
				Manifest.permission.RECORD_AUDIO)
				|| ActivityCompat.shouldShowRequestPermissionRationale(this,
				Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO,
							Manifest.permission.WRITE_EXTERNAL_STORAGE,},REQUEST_PERMISSION);

		} else {
			Toast toast = Toast.makeText(this,
					"許可されないとアプリが実行できません", Toast.LENGTH_SHORT);
			toast.show();

			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO,
							Manifest.permission.WRITE_EXTERNAL_STORAGE,}, REQUEST_PERMISSION);

		}
	}

	// 許可を受け取り
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

		if (requestCode == REQUEST_PERMISSION || requestCode == REQUEST_MULTI_PERMISSIONS) {
			if (grantResults.length > 0) {
				for (int i = 0; i < permissions.length; i++) {
					if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
						if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
							// 許可された
						} else {
							auth++;
						}
					} else if (permissions[i].equals(Manifest.permission.CAMERA)) {
						if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
							// 許可された
						} else {
							auth++;
						}
					} else if (permissions[i].equals(Manifest.permission.RECORD_AUDIO)) {
						if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
							// 許可された
						} else {
							auth++;
						}
					} else if (permissions[i].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
						if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
							// 許可された
						} else {
							auth++;
						}
					}
				}
			}
			// 使用が許可された
			if (auth == 0) {
				setContentView(R.layout.activity_main);

				TitleFragment titleFragment = new TitleFragment();
				Bundle args = new Bundle();
				args.putSerializable("DBHelper", dBHelper);
				titleFragment.setArguments(args);
				FragmentTransaction fragmentTransaction =
						getSupportFragmentManager().beginTransaction();

				fragmentTransaction.replace(R.id.container, titleFragment);
				fragmentTransaction.commit();
				/*
				getFragmentManager().beginTransaction()
						.add(R.id.container, new CameraFragment()).commit();
				*/
			} else {
				// それでも拒否された時の対応
				Toast toast = Toast.makeText(this,
						"許可がないとアプリが実行できません", Toast.LENGTH_SHORT);
				toast.show();
			}

		}

	}

	/*
	private void setKeyboardVisibilityListener(final OnKeyboardVisibilityListener onKeyboardVisibilityListener) {
		final View parentView = (ViewGroup)findViewById(R.id.container);
		parentView.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					private boolean alreadyOpen;
					private final int defaultKeyboardHeightDP = 100;
					private final int EstimatedKeyboardDP = defaultKeyboardHeightDP + (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? 48 : 0);
					private final Rect rect = new Rect();
					@Override
					public void onGlobalLayout() {
						int estimatedKeyboardHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, EstimatedKeyboardDP, parentView.getResources().getDisplayMetrics());
						parentView.getWindowVisibleDisplayFrame(rect);
						int heightDiff = parentView.getRootView().getHeight() - (rect.bottom - rect.top);
						boolean isShown = heightDiff >= estimatedKeyboardHeight;
						if (isShown == alreadyOpen) {
							Log.i("Keyboard state", "Ignoring global layout change...");
							return;
						}
						alreadyOpen = isShown;
						onKeyboardVisibilityListener.onVisibilityChanged(isShown);
					}
				}
		);
	}
	*/

}
