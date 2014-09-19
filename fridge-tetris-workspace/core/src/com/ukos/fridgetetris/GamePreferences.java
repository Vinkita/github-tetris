package com.ukos.fridgetetris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;

public class GamePreferences {
	public static final String TAG = GamePreferences.class.getName();
	
	public static final GamePreferences instance = new GamePreferences();
	
	public boolean ghost;
	public int previews;
	public boolean sound;
	public boolean music;
	public float soundVolume;
	public float musicVolume;
	
	private Preferences prefs;

	
	public static Integer[] PreviewNumbers = 
			new Integer[]{ 0,1,2,3 };
	
	private GamePreferences(){
		prefs = Gdx.app.getPreferences(TAG);
	}
	
	public void load(){
		ghost = prefs.getBoolean("ghost", true);
		previews = MathUtils.clamp(prefs.getInteger("previews", 1), 0, 3);
		sound = prefs.getBoolean("sound", true);
		music = prefs.getBoolean("music", true);
		soundVolume = MathUtils.clamp(prefs.getFloat("soundVolume", 1), 0, 1);
		musicVolume = MathUtils.clamp(prefs.getFloat("musicVolume", 1), 0, 1);
	}
	
	public void save(){
		prefs.putBoolean("ghost", ghost);
		prefs.putInteger("previews", previews);
		prefs.putBoolean("sound", sound);
		prefs.putBoolean("music", music);
		prefs.putFloat("soundVolume", soundVolume);
		prefs.putFloat("musicVolume", musicVolume);
		prefs.flush();
	}
}
