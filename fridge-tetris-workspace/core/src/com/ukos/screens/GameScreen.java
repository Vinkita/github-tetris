package com.ukos.screens;

import java.util.Observable;
import java.util.Observer;

import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Game;
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

public class GameScreen implements Screen, InputProcessor, GestureListener,
		Observer, IStopBlockListener {

	/**
	 * En el tablero se desarrola el juego.
	 * TODO feo
	 */
	private Board tablero;
	/**
	 * Se encarga de renderizar la pantalla. 
	 */
	private BoardRenderer renderer;	
	private TetrominoControladora controladora;
	private TransluscentMenuScreen pause;
	private TransluscentMenuScreen over;
	private HighScoreLayer highScores;
	private Stage stage;
	private ScoreCounter puntos;
	private Music musica;
	
	TweenManager tweenManager = new TweenManager();
	private int PPM;
	private Vector2 offset;

	private enum State {
		RUNNING, PAUSED, OVER
	}

	static State state = State.RUNNING;

	/**
	 * Crea una nueva {@code GameScreen}.
	 */
	public GameScreen() {
		if(GamePreferences.instance.highscores)
			this.highScores = new HighScoreLayer(new Skin(Gdx.files.internal("ui/mainMenuSkin.json"), 
												 new TextureAtlas("ui/menu.pack")));
	}

//	/**
//	 * Crea una nueva {@code GameScreen}.
//	 * 
//	 * @param hs la capa "HighScores" de esta pantalla.
//	 */
//	public GameScreen(HighScoreLayer hs) {
//		this.highScores = hs;
//		// highScores.setSkin(skin);
//	}

	/**
	 * Renderiza la pantalla. Además si el estado del juego es RUNNING, actualiza<br>
	 * el estado del tablero y la controladora llamando a sus metodos update().
	 * 
	 * @see com.badlogic.gdx.Screen#render(float)
	 * @see BoardRenderer#render(float)
	 * @see Board#update(float)
	 * @see TetrominoControladora#update(float)
	 */
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Table.drawDebug(stage);
		switch (state) {
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

	/**
	 * Llamado cuando la pantalla cambia de tamaño. TODO
	 * 
	 * @see com.badlogic.gdx.Screen#resize(int, int)
	 */
	@Override
	public void resize(int width, int height) {
		stage.setViewport(new ExtendViewport(width, height));
		stage.getViewport().update(width, height, true);
		renderer.resize(width, height);
		PPM = renderer.getPPM();

	}

	/**
	 * Llamado cuando esta {@code GameScreen} se vuelve la pantalla del juego. <br>
	 * Aqui se instancian: 
	 *<li>el tablero. 
	 *<li>el contador de puntuacion. 
	 *<li>la controladora. 
	 *<li>el renderizador del tablero. 
	 *<li>las pantallas de pausa y de fin del juego. 
	 *<li>TODO fix javadoc
	 * 
	 * @see com.badlogic.gdx.Screen#show()
	 */
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

		Skin menuSkin = new Skin(Gdx.files.internal("ui/mainMenuSkin.json"),
				new TextureAtlas("ui/menu.pack"));
		
		offset = renderer.getOffset();

		stage = new Stage();
		pause = new PauseScreen(stage, menuSkin);
		pause.addObserver(this);
		pause.setTween(tweenManager);

		over = new GameOverScreen(stage, menuSkin);
		over.addObserver(this);
		over.setTween(tweenManager);

		if(GamePreferences.instance.highscores)
			stage.addActor(highScores);

		musica = Gdx.audio.newMusic(Gdx.files.internal("music/bicycle.mp3"));

		Gdx.input.setInputProcessor(new InputMultiplexer(this,
				new GestureDetector(this), pause.getStage()));
		resume();
	}

	@Override
	public void hide() {
		dispose();
	}

	/**
	 * Pausa el juego y la musica. También muestra la pantalla de pausa.
	 * @see com.badlogic.gdx.Screen#pause()
	 */
	@Override
	public void pause() {
		state = State.PAUSED;
		AudioManager.instance.pause(musica);
		pause.fadeIn();
	}

	/** 
	 * Reanuda el juego y la musica.
	 * @see com.badlogic.gdx.Screen#resume()
	 */
	@Override
	public void resume() {
		state = State.RUNNING;
		AudioManager.instance.play(musica);
	}

	/**
	 * Libera todos los recursos de esta {@code GameScreen}.
	 * @see com.badlogic.gdx.Screen#dispose()
	 */
	@Override
	public void dispose() {
		stage.dispose();
		musica.dispose();
		renderer.dispose();
	}

	/**
	 * Cuando se presiona una tecla, realiza una de las siguientes acciones:
	 * <li>Llama al metodo correspondiente de la controladora.
	 * <li>Pausa el juego llamando al metodo {@link #pause()}.
	 * <li>Esconde la pantalla de pausa llamando a su metodo {@link PauseScreen#fadeOut() fadeout()}.
	 * <li>Cambia esta pantalla por una nueva pantalla {@link MainMenu}.
	 * @see TetrominoControladora#leftPressed()
	 * @see TetrominoControladora#rightPressed()
	 * @see TetrominoControladora#downPressed()
	 * @see TetrominoControladora#upPressed()
	 * @see com.badlogic.gdx.InputProcessor#keyDown(int)
	 */
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
			switch (state) {
			case RUNNING:
				pause();
				break;
			case PAUSED:
				pause.fadeOut();
				break;
			case OVER:
				((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
				break;
			default:
				break;
			}
		return true;
	}

	/**
	 * Cuando se suelta una tecla, llama al metodo correspondiente de la controladora.
	 * @see TetrominoControladora#leftReleased()
	 * @see TetrominoControladora#rightReleased()
	 * @see TetrominoControladora#downReleased()
	 * @see TetrominoControladora#upReleased()
	 * @see com.badlogic.gdx.InputProcessor#keyUp(int)
	 */
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
		return false;
	}

	/**
	 * Cuando la pantalla es tocada o un boton del mouse es oprimido, llama al metodo 
	 *{@link TetrominoControladora#touchDown(float, float, int)}. 
	 * @see com.badlogic.gdx.InputProcessor#touchDown(int, int, int, int)
	 */
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (state == State.RUNNING) {
			return controladora.touchDown(screenX, Gdx.graphics.getHeight()
					- screenY, PPM);
			// return true;
		}
		return false;
	}

	/** 
	 * Cuando se suelta una tecla o un boton del mouse, llama al metodo 
	 * {@link TetrominoControladora#downReleased()}.
	 * @see com.badlogic.gdx.InputProcessor#touchUp(int, int, int, int)
	 */
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (state == State.RUNNING) {
			controladora.downReleased();
			return false;
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	/**
	 * TODO falta
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable obs, Object obj) {
		if (obs instanceof TransluscentMenuScreen)
			switch (((TransluscentMenuScreen) obs).lastEvent) {
			case NONE:
				break;
			case BACK_CLICK:
				pause.fadeOut();
				break;
			case FADE_OUT_PAUSE:
			case FADE_OUT_OVER:
				resume();
				break;
			case RESET_CLICK:
				resetLevel();
				break;
			}
	}

	/**
	 * Resetea al tablero, al contador de puntos y a la renderizadora, reiniciando asi al juego en curso.
	 * TODO esta feo.
	 */
	private void resetLevel() {
		tablero.reset();
		puntos.reset();
		renderer.reset();
		over.fadeOut();
	}

	/**
	 * Chequea el estado del tablero para saber si ha terminado el juego.
	 * Si el juego ha terminado entonces:
	 * <li>Cambia el estado del juego a OVER.
	 * <li>Muestra la pantalla "GameOver"
	 * <li>Si la puntuacion del jugador se encuentra entre las mejores muestra la pantalla "HighScores"
	 * @see GameOverScreen#fadeIn()
	 * @see HighScoreLayer#fadein(int) 
	 * @see com.ukos.logics.IStopBlockListener#onStoppedBlock()
	 */
	@Override
	public void onStoppedBlock() {
		if (tablero.isGameOver()) {
			state = State.OVER;
			over.fadeIn();
			if(GamePreferences.instance.highscores){				
				HighScores auxScores = ScoreService.retrieveScores();
				if (puntos.getTotalScore() >= auxScores.lowestScore()) {
					highScores.fadein(puntos.getTotalScore());
				}
			}
		}
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		return false;
	}

	/**
	 * Al tapear la pantalla se llama al metodo {@link TetrominoControladora#tap() tap()} de la controladora
	 * @see com.badlogic.gdx.input.GestureDetector.GestureListener#tap(float, float, int, int)
	 */
	@Override
	public boolean tap(float x, float y, int count, int button) {
		if (state == State.RUNNING) {
			controladora.tap();
			return true;
		}
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		return false;
	}

	/**
	 * Al deslizar el cursor sobre la pantalla se llama al metodo {@link TetrominoControladora#pan(float, int) pan()} de la controladora
	 * @see com.badlogic.gdx.input.GestureDetector.GestureListener#pan(float, float, float, float)
	 */
	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		if (state == State.RUNNING) {
			controladora.pan(deltaX, PPM);
			return true;
		}
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		return false;
	}

//	public void setHighScoreLayer(HighScoreLayer layerHighScore) {
//		highScores = layerHighScore;
//	}

}
