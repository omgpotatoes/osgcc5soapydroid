package org.osgcc.osgcc5.soapydroid;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


import org.osgcc.osgcc5.soapydroid.R ;
import org.osgcc.osgcc5.soapydroid.title.TitleScreen;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;

public class EinsteinDefenseActivity extends Activity {

	/**
	 * tag for debugging data in output logfile
	 */
	public static final String DEBUG_TAG = "EinsteinDefenseActivity";

	/**
	 * Cache for image files.
	 */
	private static Map<Integer, Bitmap> imageCache = new HashMap<Integer, Bitmap>();
	
	/**
	 * Cache for all sound files.
	 */
	private static SoundPool soundCache;
	//private static SoundPool soundCache = new SoundPool(maxStreams, streamType, srcQuality);
	
	private static Map<Integer, InputStream> textCache = new HashMap<Integer, InputStream>();
	
	/**
	 * Reference to title screen.
	 */
	private static TitleScreen titleScreen;
	
	/**
	 * Reference to activity object.
	 */
	private static EinsteinDefenseActivity activity;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//setContentView(R.layout.main);
		Log.d(DEBUG_TAG, "loading images...");
		loadBitmaps();
		Log.d(DEBUG_TAG, "loading sounds...");
		loadSounds();
		Log.d(DEBUG_TAG, "loading text...");
		loadText();
		
		Log.d(DEBUG_TAG, "starting view initialization...");
		
		 TitleScreen titleScreen = new TitleScreen(this) ;
		 setContentView(titleScreen) ;
		  
		 
	}

	
	private void loadBitmaps() {
		imageCache.put(R.drawable.okbutton, BitmapFactory.decodeResource(getResources(), R.drawable.okbutton));
		imageCache.put(R.drawable.helpbutton, BitmapFactory.decodeResource(getResources(), R.drawable.helpbutton));
		imageCache.put(R.drawable.startbutton, BitmapFactory.decodeResource(getResources(), R.drawable.startbutton));
		imageCache.put(R.drawable.cow, BitmapFactory.decodeResource(getResources(), R.drawable.cow));
		imageCache.put(R.drawable.tree, BitmapFactory.decodeResource(getResources(), R.drawable.tree));
		imageCache.put(R.drawable.rock, BitmapFactory.decodeResource(getResources(), R.drawable.rock));
		imageCache.put(R.drawable.iceberg, BitmapFactory.decodeResource(getResources(), R.drawable.iceberg));
		imageCache.put(R.drawable.background, BitmapFactory.decodeResource(getResources(), R.drawable.background));
		imageCache.put(R.drawable.logo, BitmapFactory.decodeResource(getResources(), R.drawable.logo));
		imageCache.put(R.drawable.einstein, BitmapFactory.decodeResource(getResources(), R.drawable.einstein));
	}
	
	private void loadSounds() {
		
		
		
	}
	
	private void loadText() {
		textCache.put(R.raw.leveldata, getResources().openRawResource(R.raw.leveldata));
	}
	
	public static Map<Integer, Bitmap> getImageCache() {
		return imageCache;
	}
	
	public static SoundPool getSoundCache() {
		return soundCache;
	}
	
	public static Map<Integer, InputStream> getTextCache() {
		return textCache;
	}
	
	public static void loadTitleScreen() {
		activity.setContentView(titleScreen);
	}


}