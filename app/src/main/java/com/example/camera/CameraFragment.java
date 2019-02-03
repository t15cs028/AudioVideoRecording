package com.example.camera;/* * AudioVideoRecordingSample * Sample project to cature audio and video from internal mic/camera and save as MPEG4 file. * * Copyright (c) 2014-2015 saki t_saki@serenegiant.com * * File name: CameraFragment.java * * Licensed under the Apache License, Version 2.0 (the "License"); * you may not use this file except in compliance with the License. *  You may obtain a copy of the License at * *     http://www.apache.org/licenses/LICENSE-2.0 * *  Unless required by applicable law or agreed to in writing, software *  distributed under the License is distributed on an "AS IS" BASIS, *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. *  See the License for the specific language governing permissions and *  limitations under the License. * * All files in the folder are under this Apache License, Version 2.0. */import java.io.ByteArrayOutputStream;import java.io.IOException;import java.util.List;import android.annotation.SuppressLint;import android.content.Context;import android.content.res.Configuration;import android.content.res.Resources;import android.graphics.Bitmap;import android.graphics.BitmapFactory;import android.graphics.Color;import android.graphics.Matrix;import android.os.Bundle;import android.os.Handler;import android.support.v4.app.Fragment;import android.util.Log;import android.view.LayoutInflater;import android.view.View;import android.view.View.OnClickListener;import android.view.ViewGroup;import android.view.WindowManager;import android.view.animation.Animation;import android.view.animation.ScaleAnimation;import android.widget.Button;import android.widget.ImageButton;import android.widget.ImageView;import android.widget.SeekBar;import android.widget.TextView;import com.bumptech.glide.Glide;import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;import com.example.database.Composition;import com.example.database.DBHelper;import com.example.database.Table;import com.example.encoder.MediaAudioEncoder;import com.example.encoder.MediaEncoder;import com.example.encoder.MediaMuxerWrapper;import com.example.encoder.MediaVideoEncoder;import android.hardware.Sensor;import android.hardware.SensorEvent;import android.hardware.SensorEventListener;import android.hardware.SensorManager;import static android.content.Context.SENSOR_SERVICE;public class CameraFragment extends Fragment implements SensorEventListener{ //implements CameraBridgeViewBase.CvCameraViewListener2 {	private static final boolean DEBUG = false;	// TODO set false on release	private static final String TAG = "CameraFragment";	/********** add **********/	private SensorManager manager;	private TextView values;	protected final static double RAD2DEG = 180/Math.PI;	private View rootView;	private DBHelper dbHelper;	private int id;	private int layoutID;	private SeekBar zoom;	private float rate;	private int preBar;	// private Mat mOutputFrame;	// private CameraBridgeViewBase mCameraBridgeViewBase;	private ImageView mRecordLayout;	private ImageButton mInverted;	private int mLayoutFile;	private boolean mIsMirror;	private int reverseID;	private Button mZoomOperation;	private int zoomOperation;	private final static float slope = 4.0f;	private int count;	private final static int timeOfPause = 15;	private final static int timeOfZoom = 50;	private View mSlope;	private float[] fAccell = null;	private float[] fMagnetic = null;	/*********************/	/**	 * for camera preview display	 */	private CameraGLView mCameraView;	/**	 * for scale mode display	 */	private TextView mScaleModeView;	/**	 * button for start/stop recording	 */	private ImageButton mRecordButton;	/**	 * muxer for audio/video recording	 */	private MediaMuxerWrapper mMuxer;	public CameraFragment() {		// need default constructor	}	public static CameraFragment newInstance(DBHelper dbHelper, int id, int layoutID){		CameraFragment fragment = new CameraFragment();		fragment.dbHelper = dbHelper;		fragment.id = id;		fragment.layoutID = layoutID;		fragment.rate = 1;		fragment.preBar = 0;		fragment.mLayoutFile = 0;		fragment.mIsMirror = false;		fragment.reverseID = -1;		fragment.zoomOperation = 0;		fragment.count = 0;		return fragment;	}	@Override	public void onCreate(Bundle saveInstanceState){		super.onCreate(saveInstanceState);		getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);		manager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);		// to avoid renewing fragment		setRetainInstance(true);	}	@Override	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {		rootView = inflater.inflate(R.layout.fragment_main, container, false);		mCameraView = (CameraGLView)rootView.findViewById(R.id.cameraView);		mCameraView.setVideoSize(1920, 1080);		mCameraView.setOnClickListener(mOnClickListener);		// mCameraBridgeViewBase = (CameraBridgeViewBase) rootView.findViewById(R.id.openCV);		// mCameraBridgeViewBase.setCvCameraViewListener(this);		mScaleModeView = (TextView)rootView.findViewById(R.id.scalemode_textview);		updateScaleModeText();		mRecordButton = (ImageButton)rootView.findViewById(R.id.record_button);		mRecordButton.setOnClickListener(mOnClickListener);		/********** add **********/		mSlope = (View) rootView.findViewById(R.id.phoneSlope);		zoom = (SeekBar) rootView.findViewById(R.id.zoom);		zoom.setOnSeekBarChangeListener(				new SeekBar.OnSeekBarChangeListener() {					public void onProgressChanged(SeekBar seekBar,												  int progress, boolean fromUser) {						if(zoomOperation == 0) {							// ツマミをドラッグしたときに呼ばれる							float variable = (float) (zoom.getProgress() - preBar) * 0.01f;							rate += variable;							preBar = zoom.getProgress();							mCameraView.zooming(rate);							// mCameraBridgeViewBase.setZoom(rate);						}						else{							zoom.setProgress(preBar);						}					}					public void onStartTrackingTouch(SeekBar seekBar) {						// ツマミに触れたときに呼ばれる					}					public void onStopTrackingTouch(SeekBar seekBar) {						// ツマミを離したときに呼ばれる					}				}		);		String [] ids				= dbHelper.getColumn(				Table.COMPOSITION, Composition.FILE_ID.getName(),				Composition.ID.getName(), String.valueOf(layoutID)		);		String inverted = dbHelper.getColumn(				Table.COMPOSITION, Composition.REVERSE_ID.getName(),				Composition.ID.getName(), String.valueOf(layoutID)		)[0];		reverseID = Integer.parseInt(inverted);		mLayoutFile = Integer.parseInt(ids[0]);		mRecordLayout = (ImageView) rootView.findViewById(R.id.composition);		GlideDrawableImageViewTarget target = new GlideDrawableImageViewTarget(mRecordLayout);		Glide.with(getContext()).load(mLayoutFile).into(target);		// mRecordLayout.setImageResource(file_id);		mInverted = (ImageButton) rootView.findViewById(R.id.inverted);		mInverted.setOnClickListener(mOnClickListenerInverted);		mZoomOperation = (Button) rootView.findViewById(R.id.zooming_operation);		mZoomOperation.setOnClickListener(mOnClickListenerZooming);		/********************/		return rootView;	}	@Override	public void onResume() {		super.onResume();		if (DEBUG) Log.v(TAG, "onResume:");		mCameraView.onResume();		/*		if (!OpenCVLoader.initDebug()) {			Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");			OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, getContext(), mLoaderCallback);		} else {			Log.d(TAG, "OpenCV library found inside package. Using it!");			mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);		}		*/		// Listenerの登録		List<Sensor> sensors = manager.getSensorList(Sensor.TYPE_ORIENTATION);		if(sensors.size() > 0) {			Sensor s = sensors.get(0);			manager.registerListener(this, s, SensorManager.SENSOR_DELAY_UI);		}	}	/*	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(getContext()) {		@Override		public void onManagerConnected(int status) {			switch (status) {				case LoaderCallbackInterface.SUCCESS:				{					Log.i(TAG, "OpenCV loaded successfully");					mCameraBridgeViewBase.enableView();				} break;				default:				{					super.onManagerConnected(status);				} break;			}		}	};	*/	@Override	public void onPause() {		if (DEBUG) Log.v(TAG, "onPause:");		stopRecording();		mCameraView.onPause();		super.onPause();		/*		if (mCameraBridgeViewBase != null)			mCameraBridgeViewBase.disableView();			*/	}	/*	private static class OpenCVLoaderCallback extends BaseLoaderCallback {		private final CameraBridgeViewBase mCameraBase;		private OpenCVLoaderCallback(Context context, CameraBridgeViewBase cameraView) {			super(context);			mCameraBase = cameraView;		}		@Override		public void onManagerConnected(int status) {			switch (status) {				case LoaderCallbackInterface.SUCCESS:					mCameraBase.enableView();					break;				default:					super.onManagerConnected(status);					break;			}		}	}	*/	/**	 * method when touch record button	 */	private final OnClickListener mOnClickListener = new OnClickListener() {		@Override		public void onClick(final View view) {			switch (view.getId()) {			/*			case R.id.cameraView:				final int scale_mode = (mCameraView.getScaleMode() + 1) % 4;				mCameraView.setScaleMode(scale_mode);				updateScaleModeText();				break;			*/				case R.id.record_button:					if (mMuxer == null)						startRecording();					else						stopRecording();					break;			}		}	};	private void updateScaleModeText() {		final int scale_mode = mCameraView.getScaleMode();		mScaleModeView.setText(				scale_mode == 0 ? "scale to fit"						: (scale_mode == 1 ? "keep aspect(viewport)"						: (scale_mode == 2 ? "keep aspect(matrix)"						: (scale_mode == 3 ? "keep aspect(crop center)" : ""))));	}	/**	 * start resorcing	 * This is a sample project and call this on UI thread to avoid being complicated	 * but basically this should be called on private thread because prepareing	 * of encoder is heavy work	 */	private void startRecording() {		if (DEBUG) Log.v(TAG, "startRecording:");		try {			mRecordButton.setColorFilter(0xffff0000);	// turn red			mMuxer = new MediaMuxerWrapper(".mp4", dbHelper, id);	// if you record audio only, ".m4a" is also OK.			/********** add **********/			if(!mIsMirror) {				ImageView mRecordLayout = (ImageView) rootView.findViewById(R.id.composition);				GlideDrawableImageViewTarget target = new GlideDrawableImageViewTarget(mRecordLayout);				Glide.with(getContext()).load(mLayoutFile).into(target);			}			else {				invertComposition();			}			/********************/			if (true) {				// for video capturing				new MediaVideoEncoder(mMuxer, mMediaEncoderListener, mCameraView.getVideoWidth(), mCameraView.getVideoHeight());			}			if (true) {				// for audio capturing				new MediaAudioEncoder(mMuxer, mMediaEncoderListener);			}			mMuxer.prepare();			mMuxer.startRecording();			if(zoomOperation != 0) {				handler.post(runnable);			}		} catch (final IOException e) {			mRecordButton.setColorFilter(0);			Log.e(TAG, "startCapture:", e);		}	}	/**	 * request stop recording	 */	private void stopRecording() {		if (DEBUG) Log.v(TAG, "stopRecording:mMuxer=" + mMuxer);		mRecordButton.setColorFilter(0);	// return to default color		if (mMuxer != null) {			mMuxer.stopRecording();			mMuxer = null;			// you should not wait here		}	}	/**	 * callback methods from encoder	 */	private final MediaEncoder.MediaEncoderListener mMediaEncoderListener = new MediaEncoder.MediaEncoderListener() {		@Override		public void onPrepared(final MediaEncoder encoder) {			if (DEBUG) Log.v(TAG, "onPrepared:encoder=" + encoder);			if (encoder instanceof MediaVideoEncoder)				mCameraView.setVideoEncoder((MediaVideoEncoder)encoder);		}		@Override		public void onStopped(final MediaEncoder encoder) {			if (DEBUG) Log.v(TAG, "onStopped:encoder=" + encoder);			if (encoder instanceof MediaVideoEncoder)				mCameraView.setVideoEncoder(null);		}	};	/********** add **********/	/*	@Override	public void onCameraViewStarted(int width, int height) {		// Mat(int rows, int cols, int type)		// rows(行): height, cols(列): width		mOutputFrame = new Mat(height, width, CvType.CV_8UC1);	}	@Override	public void onCameraViewStopped() {	}	@Override	public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {		// Cannyフィルタをかける		Imgproc.Canny(inputFrame.gray(), mOutputFrame, 80, 100);		// ビット反転		Core.bitwise_not(mOutputFrame, mOutputFrame);		return mOutputFrame;	}	*/	// 録画時間	Handler handler = new Handler();	Runnable runnable = new Runnable() {		@Override		public void run() {			count++;			switch (zoomOperation) {				case 1:					if (count >= timeOfPause && rate < 3.0f) {						preBar = (int)(200 * (rate-1) / 2);						rate += 2 * slope / 200;						if(rate >= 3.0f){							rate = 3.0f;							preBar = 200;						}						zoom.setProgress(preBar);						mCameraView.zooming(rate);					}					break;				case 2:					if (count >= timeOfPause && rate > 1.0f) {						preBar = (int)Math.ceil(200 * (rate-1) / 2);						rate -= 2 * slope / 200;						if(rate <= 1.0f){							rate = 1.0f;							preBar = 0;						}						zoom.setProgress(preBar);						mCameraView.zooming(rate);					}					break;			}			handler.postDelayed(this, 100);			if(count == timeOfPause*2 + timeOfZoom){				count = 0;				handler.removeCallbacks(runnable);				stopRecording();			}		}	};	// ズームの設定	private final OnClickListener mOnClickListenerZooming = new OnClickListener() {		@Override		public void onClick(final View view) {			zoomOperation++;			int operation = zoomOperation % 3;			switch (operation){				case 0:					mZoomOperation.setText("manual");					zoomOperation = 0;					preBar = 0;					rate = 1;					zoom.setProgress(preBar);					mCameraView.zooming(rate);					break;				case 1:					mZoomOperation.setText("auto\nzoom in");					preBar = 0;					rate = 1;					zoom.setProgress(preBar);					mCameraView.zooming(rate);					break;				case 2:					mZoomOperation.setText("auto\nzoom out");					preBar = 200;					rate = 3;					zoom.setProgress(preBar);					mCameraView.zooming(rate);					break;			}		}	};	// 構図を反転させる	private final OnClickListener mOnClickListenerInverted = new OnClickListener() {		@Override		public void onClick(final View view) {			if(!mIsMirror) {				invertComposition();			}			else {				mRecordLayout = (ImageView) rootView.findViewById(R.id.composition);				GlideDrawableImageViewTarget target = new GlideDrawableImageViewTarget(mRecordLayout);				Glide.with(getContext()).load(mLayoutFile).into(target);				mIsMirror = false;			}		}	};	public void onStart() { // ⇔ onStop		super.onStart();		manager.registerListener(				this,				manager.getDefaultSensor( Sensor.TYPE_ACCELEROMETER ),				SensorManager.SENSOR_DELAY_UI );		manager.registerListener(				this,				manager.getDefaultSensor( Sensor.TYPE_MAGNETIC_FIELD ),				SensorManager.SENSOR_DELAY_UI );	}	@Override	public void onStop() {		// TODO Auto-generated method stub		super.onStop();		// Listenerの登録解除		manager.unregisterListener(this);	}	@Override	public void onAccuracyChanged(Sensor sensor, int accuracy) {		// TODO Auto-generated method stub	}	private float rad2deg( float rad ) {		return rad * (float) 180.0 / (float) Math.PI;	}	@Override	public void onSensorChanged(SensorEvent event) {		// TODO Auto-generated method stub		/*		// センサの取得値をそれぞれ保存しておく		switch( event.sensor.getType()) {			case Sensor.TYPE_ACCELEROMETER:				fAccell = event.values.clone();				break;			case Sensor.TYPE_MAGNETIC_FIELD:				fMagnetic = event.values.clone();				break;		}		// fAccell と fMagnetic から傾きと方位角を計算する		if( fAccell != null && fMagnetic != null ) {			// 回転行列を得る			float[] inR = new float[9];			SensorManager.getRotationMatrix(					inR,					null,					fAccell,					fMagnetic );			// ワールド座標とデバイス座標のマッピングを変換する			float[] outR = new float[9];			SensorManager.remapCoordinateSystem(					inR,					SensorManager.AXIS_X, SensorManager.AXIS_Y,					outR );			// 姿勢を得る			float[] fAttitude = new float[3];			SensorManager.getOrientation(					outR,					fAttitude );			String str =					"---------- Orientation --------\n" +							String.format( "方位角:\t%d\n", Math.round(rad2deg( fAttitude[0] ))) +							String.format( "前後の傾斜:\t%d\n", Math.round(rad2deg( fAttitude[1] ))) +							String.format( "左右の傾斜:\t%d\n", Math.round(rad2deg( fAttitude[2] )));			System.out.println(str);		}		*/		if(event.sensor.getType() == Sensor.TYPE_ORIENTATION) {			Resources resources = getResources();			Configuration config = resources.getConfiguration();			float degree;			String str;			switch(config.orientation) {				case Configuration.ORIENTATION_PORTRAIT:					str = "傾きセンサー値:"							+ "\n方位角:" + Math.round(event.values[0])							+ "\n傾斜角:" + Math.round(event.values[1])							+ "\n回転角:" + Math.round(event.values[2]);					degree = (float) Math.round(event.values[2]);					break;				case Configuration.ORIENTATION_LANDSCAPE:					str = "傾きセンサー値:"							+ "\n方位角:" + Math.round(event.values[0])							+ "\n傾斜角:" + Math.round(event.values[1])							+ "\n回転角:" + Math.round(event.values[2]);					degree = (float) Math.round(event.values[2])-90;					if(Math.round(event.values[0])/180 > 0){						degree *= -1;					}					break;				default :					str = "傾きセンサー値:"							+ "\n方位角:" + Math.round(event.values[0])							+ "\n傾斜角:" + Math.round(event.values[1])							+ "\n回転角:" + Math.round(event.values[2]);					degree = (float) Math.round(event.values[2]);			}			System.out.println(str);			mSlope.setRotation(degree);			if(degree > -2 && degree < 2){				mSlope.setBackgroundColor(Color.parseColor("#FF0000"));			}			else {				mSlope.setBackgroundColor(Color.parseColor("#FFFF00"));			}		}	}	private void invertComposition(){		/*		Matrix matrix = new Matrix();		Bitmap img = BitmapFactory.decodeResource(getResources(), mLayoutFile);		matrix.preScale(-1, 1);		Bitmap newLayout = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, false);		// ByteArrayOutputStream stream = new ByteArrayOutputStream();		// newLayout.compress(Bitmap.CompressFormat.PNG, 100, stream);		// Glide.with(getContext()).load(stream.toByteArray()).asBitmap().into(mRecordLayout);		mRecordLayout.setImageBitmap(newLayout);		*/		if(reverseID != -1) {			mRecordLayout.setImageResource(reverseID);		}		mIsMirror = true;	}	/**********************/}