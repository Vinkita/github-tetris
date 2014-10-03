package com.ukos.fridgetetris;

import aurelienribon.tweenengine.Tween;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.ukos.logics.LevelHelper;
import com.ukos.screens.MainMenu;

public class FridgeTetris extends Game {
	
	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		Tween.setCombinedAttributesLimit(5);
		GamePreferences.instance.load();
		Gdx.input.setCatchBackKey(true);
		setScreen(new MainMenu());
	}

//	@Override
//	public void render () {
//		Gdx.gl.glClearColor(1, 0, 0, 1);
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//	}
}
