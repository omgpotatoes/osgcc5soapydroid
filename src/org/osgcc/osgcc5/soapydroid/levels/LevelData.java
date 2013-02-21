package org.osgcc.osgcc5.soapydroid.levels;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.osgcc.osgcc5.soapydroid.things.CollidableCow;
import org.osgcc.osgcc5.soapydroid.things.CollidableEinstein;
import org.osgcc.osgcc5.soapydroid.things.CollidableIceberg;
import org.osgcc.osgcc5.soapydroid.things.CollidableRock;
import org.osgcc.osgcc5.soapydroid.things.CollidableThing;
import org.osgcc.osgcc5.soapydroid.things.CollidableTree;

//import android.content.res.Resources;
import android.util.Log;

import org.osgcc.osgcc5.soapydroid.EinsteinDefenseActivity;
import org.osgcc.osgcc5.soapydroid.EinsteinDefensePanel;
import org.osgcc.osgcc5.soapydroid.R;

//Loads the data for each new level
public class LevelData extends LevelInitializer{
	
	public static final String DEBUG_TAG = "EinsteinDefenseActivity";
	

	Scanner scanner;
	public LevelData(List<CollidableThing> invaders,
			List<CollidableThing> projectilesActive,
			List<CollidableThing> projectilesInactive,
			EinsteinDefensePanel panel) throws FileNotFoundException {
		super(invaders, projectilesActive, projectilesInactive, panel);
		
		InputStream reader = EinsteinDefenseActivity.getTextCache().get(R.raw.leveldata);
		scanner = new Scanner(reader);
	}

	@Override
	public void initializeLists(int level) {

		try {
			int mapNum = scanner.nextInt();
			if (mapNum == 1) {
				panel.setBackground(R.drawable.background_orig);
			} else if (mapNum == 2) {
				panel.setBackground(R.drawable.background_orig_night);
			} else if (mapNum == 3) {
				panel.setBackground(R.drawable.background_lvl2day);
			} else if (mapNum == 4) {
				panel.setBackground(R.drawable.background_lvl2day);
			} else if (mapNum == 5) {
				panel.setBackground(R.drawable.background_lvl3);
			} else if (mapNum == 6) {
				panel.setBackground(R.drawable.background_lvl3_night);
			} else if (mapNum == 7) {
				panel.setBackground(R.drawable.background_lvl4_day);
			} else if (mapNum == 8) {
				panel.setBackground(R.drawable.background_lvl4);
			} else if (mapNum == 9) {
				panel.setBackground(R.drawable.background_lvl4);
			} else {
				panel.setBackground(R.drawable.background_orig);
			}
			int numberOfThings = scanner.nextInt();
			CollidableThing[] thingsArray = new CollidableThing[numberOfThings];
			String thingType = "";
			String size = "";
			int score = 0;
			for (int k = 0; k < thingsArray.length; k++) {
				// loops through the text file one line at a time to get info
				// for lvls
				// 6 9 Cow small 906 700 3 Cow small 330 690 3 Iceberg small 30
				// 700 3 Einstein 57 180 -185 2 1 Einstein 57 260 -185 2 1
				// Einstein 57 210 -285 2 1 Einstein 57 800 -385 2 1 Einstein
				// 142 640 -385 1 3 Einstein 142 800 -500 1 3 Einstein 142 900
				// -400 2 3
				//
				thingType = scanner.next();
				if (!thingType.equals("Einstein")) {
					size = scanner.next();
					if (thingType.equals("Tree"))
						thingsArray[k] = new CollidableTree(size);
					else if (thingType.equals("Rock"))
						thingsArray[k] = new CollidableRock(size);
					else if (thingType.equals("Cow"))
						thingsArray[k] = new CollidableCow(size);
					else
						thingsArray[k] = new CollidableIceberg(size);
				} else {
					score = scanner.nextInt();
					thingsArray[k] = new CollidableEinstein(score);
				}

				thingsArray[k].setX(scanner.nextInt());
				thingsArray[k].setY(scanner.nextInt());
				thingsArray[k].setMass(scanner.nextInt());
				Log.v(DEBUG_TAG, "thingsArrayk: " + thingsArray[k].getType());
				if (thingType.equals("Einstein")) {
					thingsArray[k].setDy(scanner.nextInt());
					thingsArray[k].setPoints(score);
					invaders.add(thingsArray[k]);
				} else
					projectilesInactive.add(thingsArray[k]);
			}
		} catch (NoSuchElementException e) {
			// no more levels, so simply exit?
			EinsteinDefenseActivity.activity.finish();
		}
		
	}
	
	

}
