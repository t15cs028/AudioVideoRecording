package com.example.camera;
/*
 * AudioVideoRecordingSample
 * Sample project to cature audio and video from internal mic/camera and save as MPEG4 file.
 *
 * Copyright (c) 2014-2015 saki t_saki@serenegiant.com
 *
 * File name: CameraFragment.java
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

import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.database.Composition;
import com.example.database.DBHelper;
import com.example.database.Table;
import com.example.encoder.MediaAudioEncoder;
import com.example.encoder.MediaEncoder;
import com.example.encoder.MediaMuxerWrapper;
import com.example.encoder.MediaVideoEncoder;

public class CameraFragment extends Fragment {
	private static final boolean DEBUG = false;	// TODO set false on release
	private static final String TAG = "CameraFragment";

	private DBHelper dbHelper;
	private int id;
	private int layoutID;

	private String fileURL;

	/**
	 * for camera preview display
	 */
	private CameraGLView mCameraView;
	/**
	 * for scale mode display
	 */
	private TextView mScaleModeView;
	/**
	 * button for start/stop recording
	 */
	private ImageButton mRecordButton;
	/**
	 * muxer for audio/video recording
	 */
	private MediaMuxerWrapper mMuxer;

	public CameraFragment() {
		// need default constructor
	}


	public static CameraFragment newInstance(DBHelper dbHelper, int id, int layoutID){
		CameraFragment fragment = new CameraFragment();
		fragment.dbHelper = dbHelper;
		fragment.id = id;
		fragment.layoutID = layoutID;
		return fragment;
	}

	@Override
	public void onCreate(Bundle saveInstanceState){
		super.onCreate(saveInstanceState);
		// to avoid renewing fragment
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
		mCameraView = (CameraGLView)rootView.findViewById(R.id.cameraView);
		mCameraView.setVideoSize(1920, 1080);
		mCameraView.setOnClickListener(mOnClickListener);
		mScaleModeView = (TextView)rootView.findViewById(R.id.scalemode_textview);
		updateScaleModeText();
		mRecordButton = (ImageButton)rootView.findViewById(R.id.record_button);
		mRecordButton.setOnClickListener(mOnClickListener);

		/********** add **********/
		String [] ids
				= dbHelper.getColumn(
				Table.COMPOSITION, Composition.FILE_ID.getName(),
				Composition.ID.getName(), String.valueOf(layoutID)
		);

		int file_id = Integer.parseInt(ids[0]);
		ImageView mRecordLayout = (ImageView) rootView.findViewById(R.id.composition);
		mRecordLayout.setImageResource(file_id);
		/********************/

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (DEBUG) Log.v(TAG, "onResume:");
		mCameraView.onResume();
	}

	@Override
	public void onPause() {
		if (DEBUG) Log.v(TAG, "onPause:");
		stopRecording();
		mCameraView.onPause();
		super.onPause();
	}

	/**
	 * method when touch record button
	 */
	private final OnClickListener mOnClickListener = new OnClickListener() {
		@Override
		public void onClick(final View view) {
			switch (view.getId()) {
			/*
			case R.id.cameraView:
				final int scale_mode = (mCameraView.getScaleMode() + 1) % 4;
				mCameraView.setScaleMode(scale_mode);
				updateScaleModeText();
				break;
			*/
			case R.id.record_button:
				if (mMuxer == null)
					startRecording();
				else
					stopRecording();
				break;
			}
		}
	};

	private void updateScaleModeText() {
		final int scale_mode = mCameraView.getScaleMode();
		mScaleModeView.setText(
			scale_mode == 0 ? "scale to fit"
			: (scale_mode == 1 ? "keep aspect(viewport)"
			: (scale_mode == 2 ? "keep aspect(matrix)"
			: (scale_mode == 3 ? "keep aspect(crop center)" : ""))));
	}

	/**
	 * start resorcing
	 * This is a sample project and call this on UI thread to avoid being complicated
	 * but basically this should be called on private thread because prepareing
	 * of encoder is heavy work
	 */
	private void startRecording() {
		if (DEBUG) Log.v(TAG, "startRecording:");
		try {
			mRecordButton.setColorFilter(0xffff0000);	// turn red
			mMuxer = new MediaMuxerWrapper(".mp4", dbHelper, id);	// if you record audio only, ".m4a" is also OK.
			// mMuxer.setData(dbHelper, id);
			if (true) {
				// for video capturing
				new MediaVideoEncoder(mMuxer, mMediaEncoderListener, mCameraView.getVideoWidth(), mCameraView.getVideoHeight());
			}
			if (true) {
				// for audio capturing
				new MediaAudioEncoder(mMuxer, mMediaEncoderListener);
			}
			mMuxer.prepare();
			mMuxer.startRecording();
		} catch (final IOException e) {
			mRecordButton.setColorFilter(0);
			Log.e(TAG, "startCapture:", e);
		}
	}

	/**
	 * request stop recording
	 */
	private void stopRecording() {
		if (DEBUG) Log.v(TAG, "stopRecording:mMuxer=" + mMuxer);
		mRecordButton.setColorFilter(0);	// return to default color
		if (mMuxer != null) {
			mMuxer.stopRecording();
			mMuxer = null;
			// you should not wait here
		}
	}

	/**
	 * callback methods from encoder
	 */
	private final MediaEncoder.MediaEncoderListener mMediaEncoderListener = new MediaEncoder.MediaEncoderListener() {
		@Override
		public void onPrepared(final MediaEncoder encoder) {
			if (DEBUG) Log.v(TAG, "onPrepared:encoder=" + encoder);
			if (encoder instanceof MediaVideoEncoder)
				mCameraView.setVideoEncoder((MediaVideoEncoder)encoder);
		}

		@Override
		public void onStopped(final MediaEncoder encoder) {
			if (DEBUG) Log.v(TAG, "onStopped:encoder=" + encoder);
			if (encoder instanceof MediaVideoEncoder)
				mCameraView.setVideoEncoder(null);
		}
	};
}
