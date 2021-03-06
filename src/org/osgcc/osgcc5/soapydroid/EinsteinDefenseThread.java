package org.osgcc.osgcc5.soapydroid;

import java.util.List;
import java.util.Map;

import org.osgcc.osgcc5.soapydroid.levels.LevelInitializer;
import org.osgcc.osgcc5.soapydroid.physics.PhysicsEngine;
import org.osgcc.osgcc5.soapydroid.score.ScoreManager;
import org.osgcc.osgcc5.soapydroid.things.CollidableThing;
import org.osgcc.osgcc5.soapydroid.title.TitleScreen;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Looper;
import android.util.Log;

public class EinsteinDefenseThread extends Thread {

	/**
	 * tag for debugging data in output logfile
	 */
	public static final String DEBUG_TAG = "EinsteinDefenseActivity";

	/**
	 * Reference to EinsteinDefenseActivity.
	 */
	private Context context;
	
	/**
	 * Reference to main panel.
	 */
	EinsteinDefensePanel mainView;

	/**
	 * Indicates whether the thread should currently be running.
	 */
	private boolean running;

	
	
	

	/**
	 * List of active invaders.
	 * NOTE: this will be used by multiple threads. Make sure to synchronize!
	 */
	private List<CollidableThing> invaders;
	
	/**
	 * List of active projectiles.
	 * NOTE: this will be used by multiple threads. Make sure to synchronize!
	 */
	private List<CollidableThing> projectilesActive;
	
	/**
	 * List of inactive projectiles (waiting on the ground to be flung).
	 * NOTE: this will be used by multiple threads. Make sure to synchronize!
	 */
	private List<CollidableThing> projectilesInactive;
	/**
	 * Sounds
	 */
	private Map<Integer, Integer> soundCache ;
	private SoundPool             soundPool  ;
	/**
	 * Physics calculator.
	 */
	private PhysicsEngine physicsEngine;
	
	/**
	 * Manages current score, high scores.
	 */
	private ScoreManager scoreManager;

	/**
	 * Controls loading of data for each level.
	 */
	private LevelInitializer levelInitializer;

	/**
	 * Floor at which invaders esplode!? 
	 */
	private float earthFloor;
	
	public EinsteinDefenseThread(EinsteinDefensePanel mainView, 
			List<CollidableThing> invaders, 
			List<CollidableThing> projectilesActive,
			List<CollidableThing> projectilesInactive,
			PhysicsEngine physicsEngine,
			ScoreManager scoreManager,
			float earthFloor,
			Context context, 
			LevelInitializer levelInitializer) {
		this.mainView = mainView;
		
		this.invaders = invaders;
		this.projectilesActive = projectilesActive;
		this.projectilesInactive = projectilesInactive;
		this.physicsEngine = physicsEngine;
		this.scoreManager = scoreManager;
		this.earthFloor = earthFloor;
		this.context = context;
		this.levelInitializer = levelInitializer;
		
		this.soundCache = EinsteinDefenseActivity.getSoundCache() ;
		
		this.soundPool  = EinsteinDefenseActivity.getSoundPool() ;
		
	}

	public void setRunning(boolean running) {
		this.running = running;
		// TODO: if running == false, call onDestroy() on activity? BUT! call onPause() and onStop() first?! (wait, these should be called automatically by onDestroy()? ) NEVERMIND! just use "finish()", this will call onDestroy() etc. 
	}

	public boolean isRunning() {
		return running;
	}

	@Override
	public void run() {
		Looper.prepare();
		Canvas canvas;
		boolean loadNextLevel = false;
		while (running) {
			canvas = null;
			try {
				// don't let anything else interfere while we do canvas-y stuff
				canvas = mainView.getHolder().lockCanvas();
				synchronized (mainView.getHolder()) {
					// detect and handle collisions
					// this is currently very inefficient, fix later
					synchronized (projectilesActive) {
						for (int i=0; i<invaders.size(); i++) {
							CollidableThing invader = invaders.get(i);
							for (int j=0; j<projectilesActive.size(); j++) {
								CollidableThing projectile = projectilesActive.get(j);
								if (physicsEngine.haveCollided(projectile, invader)) {
									physicsEngine.collision(projectile, invader);
									if (projectile.getType().equals("iceberg")) {
										float mass = projectile.getMass();
										float newMass = mass - 1;
										if (newMass == 0) {
											projectilesActive.remove(projectile);
											j--;
										} else {
											projectile.setMass(newMass);
											Bitmap bitmap = projectile.getBitmap();
											Matrix matrix = new Matrix();
											matrix.postScale(newMass/mass, newMass/mass);
											Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, (int)projectile.getWidth(), (int)projectile.getHeight(), matrix, false);
											projectile.setBitmap(newBitmap);
										}
									}
								}
							}
						}
					}
					
					// update positions, velocities given gravity
					for (CollidableThing invader : invaders) {
						physicsEngine.updatePosition(invader);
					}
					
					synchronized (projectilesActive) {
						for (CollidableThing projectile : projectilesActive) {
							physicsEngine.gravity(projectile);
							physicsEngine.updatePosition(projectile);
						}
					}


					// check invaders for out-of-bounds-ness, hit the earth
					synchronized (invaders) {
						for (int i=0; i<invaders.size(); i++) {

							
							CollidableThing invader = invaders.get(i);
							// if more than 1.5x it's height or width out-of-bounds...
							float oobModifier = 1.5f;
							float x = invader.getX();
							float y = invader.getY();
							float width = invader.getWidth();
							float height = invader.getHeight();
							if (y < -1000 ||
									x < -width*oobModifier ||
									x > 1280 + width*oobModifier) {
								scoreManager.incrementScore(invader.getPoints());
								invaders.remove(invader);
								i--;
							} else if (y + height >= earthFloor) {
								scoreManager.decrementLife();
								// create an explosion here
								soundPool.play(soundCache.get(0), 1F, 1F, 1, 0, 1F) ;
								invaders.remove(invader);
								mainView.addExplosion(invader);
								i--;
							}

						}

					}
					
					// special rules
					// icebergs = reuse if they land on the ground
					synchronized (projectilesActive) {
						for (int i=0; i<projectilesActive.size(); i++) {
							CollidableThing projectile = projectilesActive.get(i);
							if (projectile.getType().equals("iceberg") && 
									(projectile.getY()+projectile.getHeight()) >= earthFloor) {
								
								projectile.setDx(0);
								projectile.setDy(0);
								projectilesActive.remove(projectile);
								projectilesInactive.add(projectile);

							}

						}
					}


					// update image positions
					// note: lint warning here: wrong call, should be draw()?
					mainView.onDraw(canvas);
					
					if (scoreManager.getLife() == 0) {
						
						running = false;
						
					} else if (invaders.size() == 0) {
						
						// set new level flag
						loadNextLevel = true;
						
					}
					
				}
				
			}  finally {

				if (canvas != null) {
					mainView.getHolder().unlockCanvasAndPost(canvas);
				}

			}
			
			// check whether we should load new level
			if (loadNextLevel) {
				// give user a pause
				try {
					this.sleep(4000);
				} catch (InterruptedException e) {
					// whatevs
				}
				
				levelInitializer.resetLists();
				levelInitializer.incrementLevel();
				levelInitializer.initializeLists(levelInitializer.getLevel());
				
				loadNextLevel = false;
			}

		}
		
		
		if (scoreManager.getLife() == 0) {
			
			Log.v(DEBUG_TAG, "trying to return to main screen...");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// whatevs
			}
			((EinsteinDefenseActivity)context).closeMedia();
			//mainView.onDraw(mainView.getHolder().lockCanvas());
			//((Activity)context).setContentView(new TitleScreen(context));
			//EinsteinDefenseActivity.loadTitleScreen();
			EinsteinDefenseActivity.activity.finish();
		} 
		
		
		
	}


}