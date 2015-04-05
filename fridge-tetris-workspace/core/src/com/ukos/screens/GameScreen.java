package com.ukos.screens;

import java.util.Observable;
import java.util.Observer;

import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Game;
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
import com.ukos.fridgetetris.GamePreferences;
import com.ukos.fridgetetris.HighScores;
import com.ukos.fridgetetris.ScoreService;
import com.ukos.fridgetetris.TetrominoController;
import com.ukos.logics.Board;
import com.ukos.logics.IStopBlockListener;
import com.ukos.logics.Point;
import com.ukos.logics.ScoreCounter;

/**
 * Contiene y coordina los elementos necesarios para ejecutar el juego:
 * <li>El tablero
 * <li>La renderizadora del tablero
 * <li>La controladora de movimiento de piezas
 * <li>El contador de puntos
 * <li>Las pantallas de "pausa" y "game over"
 * <li>La pantalla de puntuaciones
 * 
 * @author Ukos
 *
 */
public class GameScreen implements Screen, InputProcessor, GestureListener,
		Observer, IStopBlockListener {

	/**
	 * Instancia de {@code Board} en que se desarrolla el juego.
	 */
	private Board tablero;
	/**
	 * Se encarga de renderizar la pantalla. 
	 */
	private BoardRenderer renderer;	
	/**
	 * Controla el movimento de las piezas
	 */
	private TetrominoController controladora;
	/**
	 * Pantalla de pausa
	 */
	private TransluscentMenuScreen pause;
	/**
	 * Pantalla de fin del juego
	 */
	private TransluscentMenuScreen over;
	/**
	 * Pantalla de puntuaciones
	 */
	private HighScoreLayer highScores;
	/**
	 * Contiene a las pantallas {@link #pause} y {@link #over}.
	 */
	private Stage stage;
	/**
	 * Asigna puntos cuando se forman lineas
	 */
	private ScoreCounter puntos;
	/**
	 * La musica del juego
	 */
	private Music musica;	
	/**
	 * Musica despues del nivel 10
	 */
	private Music sabre;
	
	TweenManager tweenManager = new TweenManager();
	private int PPM;
	private Vector2 offset;
	private Vector2 lastTouched;
	private Vector2 newTouched;

	private enum State {
		READY, RUNNING, PAUSED, OVER
	}

	static State state = State.READY;

	/**
	 * Crea una nueva {@code GameScreen}.
	 */
	public GameScreen() {
		if(GamePreferences.instance.highscores)
			this.highScores = new HighScoreLayer(new Skin(Gdx.files.internal("ui/mainMenuSkin.json"), 
												 new TextureAtlas("ui/menu.pack")));
	}

	/**
	 * Renderiza la pantalla. Además si el estado del juego es RUNNING, actualiza<br>
	 * el estado del tablero y la controladora llamando a sus metodos update().
	 * 
	 * @see com.badlogic.gdx.Screen#render(float)
	 * @see BoardRenderer#render(float)
	 * @see Board#update(float)
	 * @see TetrominoController#update(float)
	 */
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Table.drawDebug(stage);
		switch (state) {
		case RUNNING:
			tablero.update(delta);
			controladora.update(delta);
			break;
		default:
			break;
		}
		renderer.render(delta);
		tweenManager.update(delta);
		stage.act(delta);
		stage.draw();
	}

	/**
	 * Llamado cuando la pantalla cambia de tamaño.
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
	 *<li>la controladora de movimiento?. 
	 *<li>el renderizador del tablero. 
	 *<li>las pantallas de pausa y de fin del juego. 
	 *<li>la musica.
	 * 
	 * @see com.badlogic.gdx.Screen#show()
	 */
	@Override
	public void show() {
		Gdx.input.setCatchBackKey(true);
		tablero = new Board(10, 20);
		tablero.setGhostActivated(GamePreferences.instance.ghost);
		puntos = new ScoreCounter(tablero.getLevel());
		tablero.addRowListener(puntos);
		tablero.addBlockListener(this);

		renderer = new BoardRenderer(tablero, puntos);
		tablero.addBlockListener(renderer);
		controladora = new TetrominoController(tablero);

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

		musica = Gdx.audio.newMusic(Gdx.files.internal("music/Bradinsky.mp3"));
		
		lastTouched = new Vector2();
		newTouched = new Vector2();

		Gdx.input.setInputProcessor(new InputMultiplexer(this,
				new GestureDetector(this), pause.getStage()));
		run();
	}

	@Override
	public void hide() {
		Gdx.input.setCatchBackKey(false);
		dispose();
	}

	/**
	 * Pausa el juego y la musica. También muestra la pantalla de pausa.
	 * @see com.badlogic.gdx.Screen#pause()
	 */
	@Override
	public void pause() {
		if(isRunning()){
			state = State.PAUSED;
			AudioManager.instance.pause();
			pause.fadeIn();			
		}
	}
	
	/** 
	 * Reanuda la musica y cambia el estado del juego a RUNNING.
	 * @see com.badlogic.gdx.Screen#resume()
	 */
	public void run(){
		state = State.RUNNING;
		AudioManager.instance.play(musica);
	}

	@Override
	public void resume() {		
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
	 * @see TetrominoController#leftPressed()
	 * @see TetrominoController#rightPressed()
	 * @see TetrominoController#downPressed()
	 * @see TetrominoController#upPressed()
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
	 * @see TetrominoController#leftReleased()
	 * @see TetrominoController#rightReleased()
	 * @see TetrominoController#downReleased()
	 * @see TetrominoController#upReleased()
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
	 *{@link TetrominoController#touchDown(float, float, int)}. 
	 * @see com.badlogic.gdx.InputProcessor#touchDown(int, int, int, int)
	 */
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (isRunning()) {
			return controladora.touchDown(screenX, Gdx.graphics.getHeight()
					- screenY, PPM, offset);
			// return true;
		}
		return false;
	}

	/** 
	 * Cuando se suelta una tecla o un boton del mouse, llama al metodo 
	 * {@link TetrominoController#downReleased()}.
	 * @see com.badlogic.gdx.InputProcessor#touchUp(int, int, int, int)
	 */
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (isRunning()) {
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
	 * Llamado cuando se produce un cambio en {@link #pause} o en {@link #over}.
	 * <br>La accion ejecutada por este metodo dependera 
	 * del cambio ocurrido (Representado por {@code TransluscentMenuScreen#lastEvent}).
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
				run();
				break;
			case RESET_CLICK:
				resetLevel();
				break;
			}
	}

	/**
	 * Resetea al tablero, al contador de puntos y a la renderizadora, reiniciando asi al juego en curso.
	 */
	private void resetLevel() {
		tablero.reset();
		puntos.reset();
		renderer.reset();
		state = State.READY;
		over.fadeOut();
	}

	/**
	 * Chequea el estado del tablero para saber si ha terminado el juego.
	 * Si el juego ha terminado entonces:
	 * <li>Finaliza el juego mediante el metodo {@link #endGame()}.
	 * <li>Si la puntuacion del jugador se encuentra entre las mejores muestra la pantalla "HighScores"
	 * @see HighScoreLayer#fadein(int) 
	 * @see com.ukos.logics.IStopBlockListener#onStoppedBlock()
	 */
	@Override
	public void onStoppedBlock() {
		if (tablero.isGameOver()) {
			endGame();
			if(GamePreferences.instance.highscores){				
				HighScores auxScores = ScoreService.retrieveScores();
				if (puntos.getTotalScore() > auxScores.lowestScore()) {
					highScores.fadein(puntos.getTotalScore());
				}
			}
		} else {
			if(tablero.getLevel() >= 9 && sabre == null){
				musica = sabre = Gdx.audio.newMusic(Gdx.files.internal("music/SabreDance.mp3"));;
				AudioManager.instance.play(musica);
			}
		}
	}
	
	/**
	 * Finaliza el juego, realizando las siguientes acciones:
	 * <li>Cambia el estado del juego a OVER.
	 * <li>Detiene la musica.
	 * <li>Muestra la pantalla "GameOver" llamando a su metodo {@code fadeIn()}
	 * @see GameOverScreen#fadeIn()
	 */
	public void endGame(){
		state = State.OVER;
		AudioManager.instance.stopMusic();
		over.fadeIn();
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		return false;
	}

	/**
	 * Al tapear la pantalla se llama al metodo {@link TetrominoController#tap() tap()} de la controladora
	 * @see com.badlogic.gdx.input.GestureDetector.GestureListener#tap(float, float, int, int)
	 */
	@Override
	public boolean tap(float x, float y, int count, int button) {
		if (isRunning()) {
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
		if (velocityY > 0)
			controladora.swipeDown();
		return false;
	}

	/**
	 * Al deslizar el cursor sobre la pantalla se llama al metodo {@link TetrominoController#pan(float, int) pan()} de la controladora
	 * @see com.badlogic.gdx.input.GestureDetector.GestureListener#pan(float, float, float, float)
	 */
	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		if (isRunning()) {
			controladora.pan(deltaX, PPM);
			return true;
		}
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		if(isRunning()){
			controladora.rightReleased();
			controladora.leftReleased();
			return true;
		}
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

	private boolean isRunning(){
		if(state == State.RUNNING) return true;
		return false;
	}

}
