package com.ukos.screens;

import java.util.Observable;
import java.util.Observer;

import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.ukos.fridgetetris.AudioManager;
import com.ukos.fridgetetris.BoardRenderer;
import com.ukos.fridgetetris.GamePreferences;
import com.ukos.fridgetetris.HighScores;
import com.ukos.fridgetetris.ScoreService;
import com.ukos.fridgetetris.TetrominoControladora;
import com.ukos.logics.Board;
import com.ukos.logics.IStopBlockListener;
import com.ukos.logics.ScoreCounter;

public class GameScreen implements Screen, InputProcessor, GestureListener, Observer, IStopBlockListener {
	
	private Board tablero;
	private BoardRenderer renderer;
	private TetrominoControladora controladora;
	private TransluscentMenuScreen pause;
	private TransluscentMenuScreen over;
	private HighScoreLayer highScores;
	private Music musica;
	
	private ScoreCounter puntos;
	
//	TEMPORARIO ENCONTRAR MEJOR FORMA POR DIOS
	private int PPM = 40;
	
	private enum State {
		RUNNING, PAUSED, OVER
	}
	
	static State state = State.RUNNING;
	
	TweenManager tweenManager = new TweenManager();
	private Stage stage;
	
	public GameScreen(){
		this(new HighScoreLayer(new Skin(Gdx.files.internal("ui/mainMenuSkin.json"), new TextureAtlas("ui/menu.pack"))));
	}
	public GameScreen(HighScoreLayer hs){
		this.highScores = hs;
//		highScores.setSkin(skin);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Table.drawDebug(stage);
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
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(new ExtendViewport(width, height));
		stage.getViewport().update(width, height, true);
		renderer.resize(width, height);
		PPM = renderer.getPPM();

	}

	@Override
	public void show() {
		tablero = new Board(10, 20);
		tablero.setGhostActivated(GamePreferences.instance.ghost);
		puntos = new ScoreCounter(tablero.getLevel());
		tablero.addRowListener(puntos);
		tablero.addBlockListener(this);
		
		renderer = new BoardRenderer(tablero, puntos);
		tablero.addBlockListener(renderer);
		controladora = new TetrominoControladora(tablero);
		
		Skin menuSkin = new Skin(Gdx.files.internal("ui/mainMenuSkin.json"), new TextureAtlas("ui/menu.pack"));
		
		stage = new Stage();
		pause = new PauseScreen(stage, menuSkin);
		pause.addObserver(this);
		pause.setTween(tweenManager);
		
		over = new GameOverScreen(stage, menuSkin);
		over.addObserver(this);
		over.setTween(tweenManager);
		
		stage.addActor(highScores);
		
		musica = Gdx.audio.newMusic(Gdx.files.internal("music/bicycle.mp3"));
		AudioManager.instance.play(musica);
		
		Gdx.input.setInputProcessor(new InputMultiplexer(this, new GestureDetector(this), pause.getStage()));
		state = State.RUNNING;
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
		 stage.dispose();
		 musica.dispose();
		 renderer.dispose();
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
		if (keycode == Keys.ESCAPE || keycode == Keys.BACK)
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
		if(state == State.RUNNING)
		{
			return controladora.touchDown(screenX, Gdx.graphics.getHeight() - screenY, PPM);
//			return true;
		}
		return false;
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(state == State.RUNNING) {
			controladora.downReleased();
			return false;
		}
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
		AudioManager.instance.pause(musica);
		pause.fadeIn();
	}
	
	public void doUnPause(){
		pause.fadeOut();
		AudioManager.instance.play(musica);
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
		renderer.reset();
		over.fadeOut();
	}

	@Override
	public void onStoppedBlock() {
		if(tablero.isGameOver()) {
			state = State.OVER;
			over.fadeIn();			
			HighScores auxScores = ScoreService.retrieveScores();
			if(puntos.getTotalScore() >= auxScores.lowestScore()){
				highScores.fadein(puntos.getTotalScore());				
			}
		}
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
//		controladora.touchDown(x, y, ppm);
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		if(state == State.RUNNING) {				
			controladora.tap();
			return true;
		}
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		if(state == State.RUNNING){
			controladora.pan(deltaX, PPM);
			return true;
		}
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}

	public void setHighScoreLayer(HighScoreLayer layerHighScore) {
		highScores = layerHighScore;		
	}


}
