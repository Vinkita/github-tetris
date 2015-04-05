package com.ukos.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ukos.screens.TransluscentMenuScreen.Event;
import com.ukos.tween.ActorAccessor;

/**
 * Pantalla de fin del juego
 * @author Ukos
 */
public class GameOverScreen extends TransluscentMenuScreen {
	
	public GameOverScreen(Stage stage, Skin skin) {
		super(stage, skin);
	}

	/**
	 * Define el texto del titulo del menu y del boton {@link TransluscentMenuScreen#buttonBack buttonBack}
	 * @see com.ukos.screens.TransluscentMenuScreen#setStrings()
	 */
	@Override
	protected void setStrings() {
		headerText = "GAME OVER";
		backButtonText = "Replay";
	}

	@Override
	protected Event getButtonBackEvent() {
		return Event.RESET_CLICK;
	}

	/**
	 * Esconde esta TransluscentMenuScreen, desvaneciendola gradualmente hacia negro y luego hacia transparencia.
	 * Al terminar esta accion, llama al metodo {@link #fireEvent(Event)}.
	 * @see com.ukos.screens.TransluscentMenuScreen#fadeOut()
	 */
	@Override
	public void fadeOut() {
		Timeline.createSequence().beginSequence()
		.push(Tween.set(black, ActorAccessor.VISIBILITY).target(1))
		.push(Tween.to(black, ActorAccessor.ALPHA, .5f).target(1))
		.push(Tween.set(table, ActorAccessor.ALPHA).target(0))
		.push(Tween.to(black, ActorAccessor.ALPHA, 1f).target(0))
		.push(Tween.set(black, ActorAccessor.VISIBILITY).target(0.0f))
		.push(Tween.set(table, ActorAccessor.VISIBILITY).target(0.0f))
		.end().start(tweenManager).setCallback(new TweenCallback() {
			
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				fireEvent(Event.FADE_OUT_OVER);
			}
		});
		
		tweenManager.update(Gdx.graphics.getDeltaTime());
	}
	

}
