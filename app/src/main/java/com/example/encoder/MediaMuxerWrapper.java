package com.example.encoder;/* * AudioVideoRecordingSample * Sample project to cature audio and video from internal mic/camera and save as MPEG4 file. * * Copyright (c) 2014-2015 saki t_saki@serenegiant.com * * File name: MediaMuxerWrapper.java * * Licensed under the Apache License, Version 2.0 (the "License"); * you may not use this file except in compliance with the License. *  You may obtain a copy of the License at * *     http://www.apache.org/licenses/LICENSE-2.0 * *  Unless required by applicable law or agreed to in writing, software *  distributed under the License is distributed on an "AS IS" BASIS, *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. *  See the License for the specific language governing permissions and *  limitations under the License. * * All files in the folder are under this Apache License, Version 2.0. */import java.io.File;import java.io.IOException;import java.nio.ByteBuffer;import java.text.SimpleDateFormat;import java.util.GregorianCalendar;import java.util.Locale;import android.media.MediaCodec;import android.media.MediaFormat;import android.media.MediaMuxer;import android.os.Environment;import android.text.TextUtils;import android.util.Log;import com.example.database.DBHelper;import com.example.database.Project;import com.example.database.Block;import com.example.database.Table;public class MediaMuxerWrapper {	private static final boolean DEBUG = false;	// TODO set false on release	private static final String TAG = "MediaMuxerWrapper";	// modify	// private static final String DIR_NAME = "AVRecSample";	public static final String DIR_NAME = "Rec";	private static final SimpleDateFormat mDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US);	private String mOutputPath;	private final MediaMuxer mMediaMuxer;	// API >= 18	private int mEncoderCount, mStatredCount;	private boolean mIsStarted;	private MediaEncoder mVideoEncoder, mAudioEncoder;	// add	private DBHelper dbHelper;	private int blockID;	/********** add **********/	/**	 * Constructor	 * @param ext extension of output file	 * @throws IOException	 */	public MediaMuxerWrapper(String ext, DBHelper dbHelper, int blockID) throws IOException {		this.dbHelper = dbHelper;		this.blockID = blockID;		if (TextUtils.isEmpty(ext)) ext = ".mp4";		try {			mOutputPath = getCaptureFile(createDirectory(), ext).toString();		} catch (final NullPointerException e) {			throw new RuntimeException("This app has no permission of writing external storage");		}		mMediaMuxer = new MediaMuxer(mOutputPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);		mEncoderCount = mStatredCount = 0;		mIsStarted = false;	}	/********************/	/**	 * Constructor	 * @param ext extension of output file	 * @throws IOException	 */	public MediaMuxerWrapper(String ext) throws IOException {		if (TextUtils.isEmpty(ext)) ext = ".mp4";		try {			mOutputPath = getCaptureFile(createDirectory(), ext).toString();		} catch (final NullPointerException e) {			throw new RuntimeException("This app has no permission of writing external storage");		}		mMediaMuxer = new MediaMuxer(mOutputPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);		mEncoderCount = mStatredCount = 0;		mIsStarted = false;	}	// add	public void setData(DBHelper dbHelper, int id){		this.dbHelper = dbHelper;		this.blockID = blockID;	}	public String getOutputPath() {		return mOutputPath;	}	public void prepare() throws IOException {		if (mVideoEncoder != null)			mVideoEncoder.prepare();		if (mAudioEncoder != null)			mAudioEncoder.prepare();	}	public void startRecording() {		if (mVideoEncoder != null)			mVideoEncoder.startRecording();		if (mAudioEncoder != null)			mAudioEncoder.startRecording();		dbHelper.setField(Table.BLOCK, String.valueOf(blockID), Block.FILE_URL.getName(), mOutputPath);	}	public void stopRecording() {		if (mVideoEncoder != null)			mVideoEncoder.stopRecording();		mVideoEncoder = null;		if (mAudioEncoder != null)			mAudioEncoder.stopRecording();		mAudioEncoder = null;	}	public synchronized boolean isStarted() {		return mIsStarted;	}//**********************************************************************//**********************************************************************	/**	 * assign encoder to this calss. this is called from encoder.	 * @param encoder instance of MediaVideoEncoder or MediaAudioEncoder	 */	/*package*/ void addEncoder(final MediaEncoder encoder) {		if (encoder instanceof MediaVideoEncoder) {			if (mVideoEncoder != null)				throw new IllegalArgumentException("Video encoder already added.");			mVideoEncoder = encoder;		} else if (encoder instanceof MediaAudioEncoder) {			if (mAudioEncoder != null)				throw new IllegalArgumentException("Video encoder already added.");			mAudioEncoder = encoder;		} else			throw new IllegalArgumentException("unsupported encoder");		mEncoderCount = (mVideoEncoder != null ? 1 : 0) + (mAudioEncoder != null ? 1 : 0);	}	/**	 * request start recording from encoder	 * @return true when muxer is ready to write	 */	/*package*/ synchronized boolean start() {		if (DEBUG) Log.v(TAG,  "start:");		mStatredCount++;		if ((mEncoderCount > 0) && (mStatredCount == mEncoderCount)) {			mMediaMuxer.start();			mIsStarted = true;			notifyAll();			if (DEBUG) Log.v(TAG,  "MediaMuxer started:");		}		return mIsStarted;	}	/**	 * request stop recording from encoder when encoder received EOS	 */	/*package*/ synchronized void stop() {		if (DEBUG) Log.v(TAG,  "stop:mStatredCount=" + mStatredCount);		mStatredCount--;		if ((mEncoderCount > 0) && (mStatredCount <= 0)) {			mMediaMuxer.stop();			mMediaMuxer.release();			mIsStarted = false;			if (DEBUG) Log.v(TAG,  "MediaMuxer stopped:");		}	}	/**	 * assign encoder to muxer	 * @param format	 * @return minus value indicate error	 */	/*package*/ synchronized int addTrack(final MediaFormat format) {		if (mIsStarted)			throw new IllegalStateException("muxer already started");		final int trackIx = mMediaMuxer.addTrack(format);		if (DEBUG) Log.i(TAG, "addTrack:trackNum=" + mEncoderCount + ",trackIx=" + trackIx + ",format=" + format);		return trackIx;	}	/**	 * write encoded data to muxer	 * @param trackIndex	 * @param byteBuf	 * @param bufferInfo	 */	/*package*/ synchronized void writeSampleData(final int trackIndex, final ByteBuffer byteBuf, final MediaCodec.BufferInfo bufferInfo) {		if (mStatredCount > 0)			mMediaMuxer.writeSampleData(trackIndex, byteBuf, bufferInfo);	}//**********************************************************************//**********************************************************************	/**	 * generate output file	 * @param type Environment.DIRECTORY_MOVIES / Environment.DIRECTORY_DCIM etc.	 * @param ext .mp4(.m4a for audio) or .png	 * @return return null when this app has no writing permission to external storage.	 */    /********** add **********/	public static final File getCaptureFile(final String dir, final String ext) {		String url = dir + "/" + DBHelper.getNowDateTime() + ext;		File file = new File(url);		return file;		/*		final File dir = new File(				String.valueOf(Environment.getExternalStorageDirectory())						+  "/" +DIR_NAME		);		Log.d(TAG, "path=" + dir.toString());		if(!dir.exists()) {			dir.mkdirs();		}		if (dir.canWrite()) {			url = String.valueOf(dir) + "/" + DBHelper.getNowDateTime() + ext;			File file = new File(url);			return file;		}		return null;		*/	}	/********** resource **********/	/*    public static final File getCaptureFile(final String type, final String ext) {		final File dir = new File(Environment.getExternalStoragePublicDirectory(type), DIR_NAME);		Log.d(TAG, "path=" + dir.toString());		dir.mkdirs();        if (dir.canWrite()) {        	return new File(dir, getDateTimeString() + ext);        }    	return null;    }	*/	private final String createDirectory(){		String storyID				= dbHelper.getColumn(Table.BLOCK, Block.PROJECT_ID.getName(),				Block.ID.getName(), String.valueOf(blockID))[0];		String storyName				= dbHelper.getColumn(Table.PROJECT, Project.NAME.getName(),				Project.ID.getName(), String.valueOf(storyID))[0];		File dir = new File(				String.valueOf(Environment.getExternalStorageDirectory())						+  "/" +DIR_NAME + "/" + storyName		);		if(!dir.exists()) {			dir.mkdirs();		}		if(!dir.canWrite()){			dir.setWritable(true);		}		return dir.toString();	}	/**	 * get current date and time as String	 * @return	 */	private static final String getDateTimeString() {		final GregorianCalendar now = new GregorianCalendar();		return mDateTimeFormat.format(now.getTime());	}}