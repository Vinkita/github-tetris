package com.ukos.screens;

import java.util.Observable;
import java.util.Observer;

import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ukos.fridgetetris.BoardRenderer;
import com.ukos.fridgetetris.TetrominoControladora;
import com.ukos.logics.Board;
import com.ukos.logics.IStopBlockListener;
import com.ukos.logics.ScoreCounter;

public class GameScreen implements Screen, InputProcessor, Observer, IStopBlockListener {
	
	private Board tablero;
	private BoardRenderer renderer;
	private TetrominoControladora controladora;
	private TransluscentMenuScreen pause;
	private TransluscentMenuScreen over;
	
	private ScoreCounter puntos;
	
	public static boolean pauseLock = false; 
	private boolean paused = false;
	private enum State {
		RUNNING, PAUSED, OVER
	}
	
	static State state = State.RUNNING;
	
	TweenManager tweenManager = new TweenManager();

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		switch(state){
			case PAUSED:
			case OVER:
				break;
			case RUNNING:
				tablero.update(delta);
				controladora.update(delta);
				break;
		}
		renderer.render(delta);
		tweenManager.update(delta);

	}

	@Override
	public void resize(int width, int height) {
		renderer.resize(width, height);

	}

	@Override
	public void show() {
		tablero = new Board(10, 20);
		tablero.setGhostActivated(true);
		puntos = new ScoreCounter();
		tablero.addRowListener(puntos);
		tablero.addBlockListener(this);
		
		renderer = new BoardRenderer(tablero, puntos);
		tablero.addBlockListener(renderer);
		controladora = new TetrominoControladora(tablero);
		
		Skin menuSkin = new Skin(Gdx.files.internal("ui/mainMenuSkin.json"), new TextureAtlas("ui/menu.pack"));
		
		pause = new PauseScreen(renderer.getStage(), menuSkin);
		pause.addObserver(this);
		pause.setTween(tweenManager);
		
		over = new GameOverScreen(renderer.getStage(), menuSkin);
		over.addObserver(this);
		over.setTween(tweenManager);
		
		Gdx.input.setInputProcessor(new InputMultiplexer(this, pause.getStage()));
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}
	
	
	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.LEFT)
			controladora.leftPressed();
		if (keycode == Keys.RIGHT)
			controladora.rightPressed();
		if (keycode == Keys.DOWN)
			controladora.downPressed();
		if (keycode == Keys.UP)
			controladora.upPressed();
		if (keycode == Keys.ESCAPE)
			switch(state){
				case RUNNING:
					doPause();
					break;
				case PAUSED:
					doUnPause();
					break;
				default:
					break;
			}
		return true;
	}
	
	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.LEFT)
			controladora.leftReleased();
		if (keycode == Keys.RIGHT)
			controladora.rightReleased();
		if (keycode == Keys.DOWN)
			controladora.downReleased();
		if (keycode == Keys.UP)
			controladora.upReleased();
		return true;
	}
	
	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void doPause(){
		state = State.PAUSED;
		pause.fadeIn();
	}
	
	public void doUnPause(){
		pause.fadeOut();
	}
	
	public static void startRunning(){
		state = State.RUNNING;		
	}

	@Override
	public void update(Observable obs, Object obj) {
		if(obs instanceof TransluscentMenuScreen)
			switch (((TransluscentMenuScreen)obs).lastEvent){
				case NONE:
					break;
				case BACK_CLICK:
					doUnPause();
					break;
				case FADE_OUT_PAUSE:
				case FADE_OUT_OVER:
					startRunning();
					break;
				case RESET_CLICK:
					resetLevel();
					break;
			}
	}

	private void resetLevel() {
		tablero.reset();
		puntos.reset();		
		over.fadeOut();
	}

	@Override
	public void onStoppedBlock() {
		if(tablero.isGameOver()) {
			state = State.OVER;
			over.fadeIn();			
		}
	}


}
