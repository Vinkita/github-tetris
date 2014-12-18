package com.ukos.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ukos.screens.TransluscentMenuScreen.Event;
import com.ukos.tween.ActorAccessor;

/**
 * Pantalla de pausa
 * @author Ukos
 */
public class PauseScreen extends TransluscentMenuScreen {

	/**
	 * @see TransluscentMenuScreen#TransluscentMenuScreen(Stage, Skin)
	 */
	public PauseScreen(Stage stage, Skin skin) {
		super(stage, skin);
	}
	@Override
	protected void setStrings() {
		headerText = "Game Paused";
		backButtonText = "Back";
	}
	@Override
	protected Event getButtonBackEvent() {
		return Event.BACK_CLICK;
	}
	/**
	 * Esconde esta TransluscentMenuScreen, desvaneciendola gradualmente. Al terminar
	 * esta accion, llama al metodo {@link #fireEvent(Event)}.
	 * @see com.ukos.screens.TransluscentMenuScreen#fadeOut()
	 */
	@Override
	public void fadeOut() {
		Timeline.createSequence().beginSequence()
		.push(Tween.to(table, ActorAccessor.ALPHA, .25f).target(0))
		.push(Tween.set(table, ActorAccessor.VISIBILITY).target(0.0f))
		.end().start(tweenManager).setCallback(new TweenCallback() {
			
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				fireEvent(Event.FADE_OUT_PAUSE);
			}
		});
		
		tweenManager.update(Gdx.graphics.getDeltaTime());
	}
	
	

}
