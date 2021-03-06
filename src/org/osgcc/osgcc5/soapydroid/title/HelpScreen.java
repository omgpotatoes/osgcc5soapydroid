package org.osgcc.osgcc5.soapydroid.title;

import java.util.HashMap;
import java.util.Map;

import org.osgcc.osgcc5.soapydroid.EinsteinDefenseActivity;
import org.osgcc.osgcc5.soapydroid.EinsteinDefensePanel;
import org.osgcc.osgcc5.soapydroid.R;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.SoundPool;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class HelpScreen extends View{

	
	private static final String DEBUG_TAG = "Help Screen";
	private static Map<Integer, Integer> soundCache = EinsteinDefenseActivity.getSoundCache() ;
	
	private static SoundPool soundPool              = EinsteinDefenseActivity.getSoundPool() ;
	
	private static Map<Integer, Bitmap> imageCache = EinsteinDefenseActivity.getImageCache() ;
	GestureDetector listener ;
	Context         context  ;
	
	public HelpScreen(Context context) {
		super(context);
		this.context = context ;
		listener     = new GestureDetector(context, new ButtonPush()) ;
		// TODO Auto-generated constructor stub
	}
		
	@Override
	protected void onDraw(Canvas canvas) {
		Log.d(DEBUG_TAG, "drawing canvas...");
		// draw test
		//canvas.drawBitmap(imageCache.get(R.drawable.cow), 50, 500, null);
		
		// draw background
		canvas.drawBitmap(imageCache.get(R.drawable.help), 0, 0, null)      ;
		canvas.drawBitmap(imageCache.get(R.drawable.okbutton),  573F, 625F,  null);
		// draw collidable objects
		// NOTE: must figure out how to draw with rotation!
		
		
		// draw inactive projectiles
		
		}
		
		
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			return listener.onTouchEvent(event);
		}

	private class ButtonPush implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener{

		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			float x = e.getX();
			float y = e.getY();
					Log.v("onDown", "") ;
			if(x >= 573F && x <= 673F)
			{
				
				if(y >= 625F && y <= 725F)
				{
					soundPool.play(soundCache.get(2), 1F, 1F, 1, 0, 1F) ;
					((Activity)context).setContentView(new TitleScreen(context)) ;
				}
			}
			
			return true;
		}

		@Override
		public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
				float arg3) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onLongPress(MotionEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
				float arg3) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onShowPress(MotionEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onSingleTapUp(MotionEvent arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onDoubleTapEvent(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}
		
	}
}
