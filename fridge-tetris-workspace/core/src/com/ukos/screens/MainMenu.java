package com.ukos.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.ukos.fridgetetris.AudioManager;
import com.ukos.fridgetetris.GamePreferences;
import com.ukos.tween.ActorAccessor;

/**
 * El Menu Principal. 
 * <br>Esta compuesto de diversas capas, cada una agrupa distintas secciones del menu.
 * @author Ukos
 */
public class MainMenu implements Screen {
	
	private Stage stage;
	private Skin skin;
	private Stack stack;
	private Image heading; 
	private Table layerBackground;
	private Table layerMenu;
	private Table layerSettings;
	private HighScoreLayer layerHighScore;
	private TweenManager tweenManager;
	
	//Main menu
	TextButton buttonPlay;
	TextButton buttonSettings;
	TextButton buttonScores;
	TextButton buttonExit;
	
	//Settings
	private CheckBox chkGhost;
	private SelectBox<Integer> selNumPrevs;
	private CheckBox chkMusic;
	private Slider sldMusic;
	TextButton buttonSave;
	TextButton buttonCancel;

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
//		TextButton.drawDebug(stage);
		
		stage.act(delta);		
		stage.draw();		
		
		tweenManager.update(delta);
		Table.drawDebug(stage);
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(new ExtendViewport(width, height));
		stage.getViewport().update(width, height, true);
		stack.invalidateHierarchy();
//		layerBackground.invalidateHierarchy();
//		layerMenu.invalidateHierarchy();
//		layerSettings.invalidateHierarchy();
	}

	@Override
	public void show() {
		stage = new Stage();
		
		Gdx.input.setInputProcessor(stage);

		skin = new Skin(Gdx.files.internal("ui/mainMenuSkin.json"), new TextureAtlas("ui/menu.pack"));
		
		
//		stage.addActor(imgBackground);
		
		layerBackground = buildBackgroundLayer();
		layerMenu = buildMenuLayer();
		layerSettings = buildSettingsLayer();
//		layerHighScore.debug();
//		layerHighScore.background(skin.getDrawable("black"));//borrar estas dos lineas
//		layerHighScore.setColor(layerHighScore.getColor().r, layerHighScore.getColor().g, layerHighScore.getColor().b, .5f);
//		layerHighScore.setVisible(true);
		
		stack = new Stack();
		stack.setFillParent(true);
		stage.addActor(stack);
		stack.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		stack.add(layerBackground);
		stack.add(layerMenu);
		stack.add(layerSettings);
		if(GamePreferences.instance.highscores){			
			layerHighScore = new HighScoreLayer(skin);
			layerHighScore.setPreviousLayer(layerMenu);
			stack.add(layerHighScore);
		}
//		layerSettings.setX(Gdx.graphics.getWidth());
		showMenu();
		

	}
	

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
	}
	
	/**
	 * Se encarga de la creacion de la capa "background".
	 * @return la capa "background".
	 */
	private Table buildBackgroundLayer(){
		Table auxBG = new Table(skin);
		auxBG.setFillParent(true);
//		imgBackground = new Image(skin.getDrawable("mainIce"));
//		imgBackground.setFillParent(true);
		auxBG.setBackground(skin.getDrawable("mainIce"));
		return auxBG;
	}
	
	/**
	 * Se encarga de la creacion de la capa "Menu".
	 * <br>
	 * <li>Instancia la imagen de cabecera.
	 * <li>Instancia los botones y setea sus propiedades graficas. 
	 * <li>Crea los listeners de cada boton.  
	 * <li>Ubica los componentes del menu en sus lugares.
	 * @return  la capa "Menu".
	 */
	private Table buildMenuLayer(){
		Table auxMenu = new Table(skin);
		auxMenu.setFillParent(true);		
//		auxMenu.debug();
		int buttonPad = 10;
				
		heading = new Image(skin.getDrawable("title"));
		
//		creating buttons
		buttonPlay = new TextButton("PLAY", skin, "pink");
		buttonPlay.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
//				((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(layerHighScore));
				((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen());
			}
		});
		buttonPlay.pad(buttonPad);
		
		buttonSettings = new TextButton("SETTINGS", skin, "green");
		buttonSettings.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showSettings();
			}
		});
		buttonSettings.pad(buttonPad);
		
		if(GamePreferences.instance.highscores){
			buttonScores = new TextButton("HIGH SCORES", skin, "orange");
			buttonScores.addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y) {
					showScores();
	//				layerHighScore.fadein(10000);
				}
			});
			buttonScores.pad(buttonPad);
		}
				
		buttonExit = new TextButton("EXIT", skin, "blue");
		buttonExit.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Timeline.createParallel().beginParallel()
				.push(Tween.to(layerMenu, ActorAccessor.ALPHA, .5f).target(0))
				.push(Tween.to(layerMenu, ActorAccessor.Y, .5f).target(layerMenu.getY() - 200))
				.setCallback(new TweenCallback() {					
					@Override
					public void onEvent(int type, BaseTween<?> source) {
//						((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenu());
						Gdx.app.exit();
					}
				})
				.end().start(tweenManager);
			}
		});
		buttonExit.pad(buttonPad);
		
		float buttonWidth;
		if(GamePreferences.instance.highscores)
			buttonWidth = buttonScores.getWidth();
		else
			buttonWidth = buttonSettings.getWidth();
		
//		arming table
		auxMenu.add(heading).spaceBottom(100).row();
		auxMenu.add(buttonPlay).width(buttonWidth).spaceBottom(15).row();
		auxMenu.add(buttonSettings).width(buttonWidth).spaceBottom(15).row();
		if(GamePreferences.instance.highscores){
			auxMenu.add(buttonScores).width(buttonWidth).spaceBottom(15).row();
		}
		auxMenu.add(buttonExit).width(buttonWidth);
//		stage.addActor(auxMenu);
		
		
		return auxMenu;
	}
	
	/**
	 * Se encarga de la creacion de la capa "Preferencias".
	 * <br>
	 * <li>Instancia los diferentes elementos que componen este menu.
	 * <li>Instancia los botones y setea sus propiedades graficas. 
	 * <li>Crea los listeners de cada boton.  
	 * <li>Ubica cada elemento en su correspondiente lugar.
	 * @return  la capa "Preferencias".
	 */
	private Table buildSettingsLayer(){
		Table auxSett = new Table(skin);
		Window win = new Window("", skin);
		win.setMovable(false);
		auxSett.setFillParent(true);		
		
		chkGhost = new CheckBox("Ghost Piece", skin, "tetris");
		selNumPrevs = new SelectBox<Integer>(skin);
		selNumPrevs.setItems(GamePreferences.PreviewNumbers);	
		
		buttonSave = new TextButton("Save", skin, "greenSmall");
		buttonSave.pad(15);
		buttonCancel = new TextButton("Cancel", skin, "blueSmall");
		buttonCancel.pad(15);
		
		//listeners
		buttonSave.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				saveSettings();
				hideSettings();
			}
		});
		buttonCancel.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {				
				hideSettings();
			}
		});
		
//		auxSett.add(heading).spaceBottom(100).row();
		win.add(new Label("SETTINGS", skin, "tetris")).colspan(2).spaceBottom(100).row();
		win.add(new Label("Game", skin, "blueMedium")).colspan(2).row();
		win.add(chkGhost).left().padLeft(5).padBottom(10).row();
		win.add(new Label("Number of Preview Pieces:", skin, "tetrisMedium")).left().colspan(2).padLeft(5).row();
		win.add(selNumPrevs).fillX().colspan(2).pad(0, 5, 0, 5).row();
		win.add(buildAudioMenu()).colspan(2).fillX().padTop(15).row();
		win.add().padBottom(50).row();
		win.add(buttonSave).width(buttonCancel.getWidth()).uniformX();//.width(buttonWidth);
		win.add(buttonCancel).uniformX();
		
		auxSett.add(win);
		auxSett.setVisible(false);
		
		return auxSett;	
	}
	
	/**
	 * @return el submenu "audio" de la capa settings
	 */
	private Table buildAudioMenu(){
		Table audio = new Table(skin);
		chkMusic = new CheckBox("Music", skin, "tetris");
		sldMusic = new Slider(0, 1, .1f, false, skin);
		
		audio.add(new Label("Audio", skin, "blueMedium")).colspan(2).row();
		audio.add(chkMusic).left().padLeft(5).padRight(15);
		audio.add(sldMusic).expandX().fillX().padRight(10);
		return audio;
	}
	
	/**
	 * Setea los valores correspondientes a cada indicador de la capa Preferencias, 
	 * según los valores guardados en la instancia de <code>GamePreferences.
	 * @see GamePreferences
	 */
	private void loadSettings(){
		GamePreferences prefs = GamePreferences.instance;
		chkGhost.setChecked(prefs.ghost);
		selNumPrevs.setSelected(prefs.previews);	
		chkMusic.setChecked(prefs.music);
		sldMusic.setValue(prefs.musicVolume);
	}
	
	/**
	 * Setea y guarda los valores de la instancia de <code>GamePreferences, 
	 * segun los valores de los indicadores de la capa Preferencias. 
	 */
	private void saveSettings(){
		GamePreferences prefs = GamePreferences.instance;
		prefs.ghost = chkGhost.isChecked();
		prefs.previews = selNumPrevs.getSelectedIndex();
		prefs.music = chkMusic.isChecked();
		prefs.musicVolume = sldMusic.getValue();
		prefs.save();
	}
	
	/**
	 * Se encarga de hacer una transicion entre la pantalla vacia 
	 * (solo se muestra la capa background) y el menu principal.
	 */
	private void showMenu(){
//		creating animations
		tweenManager = new TweenManager();
		Tween.registerAccessor(Actor.class, new ActorAccessor());
		
//		heading and buttons fade-in
		if(GamePreferences.instance.highscores){			
		Timeline.createSequence().beginSequence()
			.push(Tween.set(buttonPlay, ActorAccessor.ALPHA).target(0))
			.push(Tween.set(buttonSettings, ActorAccessor.ALPHA).target(0))
			.push(Tween.set(buttonScores, ActorAccessor.ALPHA).target(0))
			.push(Tween.set(buttonExit, ActorAccessor.ALPHA).target(0))
			.push(Tween.from(heading, ActorAccessor.ALPHA, .25f).target(0))
			.push(Tween.to(buttonPlay, ActorAccessor.ALPHA, .25f).target(1))
			.push(Tween.to(buttonSettings, ActorAccessor.ALPHA, .25f).target(1))
			.push(Tween.to(buttonScores, ActorAccessor.ALPHA, .25f).target(1))
			.push(Tween.to(buttonExit, ActorAccessor.ALPHA, .25f).target(1))
			.end().start(tweenManager);
		} else {			
			Timeline.createSequence().beginSequence()
			.push(Tween.set(buttonPlay, ActorAccessor.ALPHA).target(0))
			.push(Tween.set(buttonSettings, ActorAccessor.ALPHA).target(0))
			.push(Tween.set(buttonScores, ActorAccessor.ALPHA).target(0))
			.push(Tween.set(buttonExit, ActorAccessor.ALPHA).target(0))
			.push(Tween.from(heading, ActorAccessor.ALPHA, .25f).target(0))
			.push(Tween.to(buttonPlay, ActorAccessor.ALPHA, .25f).target(1))
			.push(Tween.to(buttonSettings, ActorAccessor.ALPHA, .25f).target(1))
			.push(Tween.to(buttonExit, ActorAccessor.ALPHA, .25f).target(1))
			.end().start(tweenManager);
		}

		//	table fade-in
		Tween.from(layerMenu, ActorAccessor.ALPHA, .5f).target(0).start(tweenManager);
		Tween.from(layerMenu, ActorAccessor.Y, .5f).target(Gdx.graphics.getHeight() / 4).start(tweenManager);
		
		tweenManager.update(Gdx.graphics.getDeltaTime());
	}
	
	
	/**
	 * Se encarga de hacer una transicion entre el menu principal y el menu de Preferencias.
	 * <br>Este metodo es llamado cuando el boton "Settings" del menu principal es pulsado. 
	 */
	private void showSettings(){
		loadSettings();
		Timeline.createSequence().beginSequence()
		.push(Tween.set(layerSettings, ActorAccessor.X).target(layerSettings.getWidth()))
		.push(Tween.set(layerSettings, ActorAccessor.VISIBILITY).target(1))
		.push(
			Timeline.createParallel().beginParallel()
			.push(Tween.to(layerMenu, ActorAccessor.X, .8f).target(-layerMenu.getWidth()))
			.push(Tween.to(layerSettings, ActorAccessor.X, .8f).target(0))
			.end()
		)		
		.push(Tween.set(layerMenu, ActorAccessor.VISIBILITY).target(0))
		.end().start(tweenManager);
		tweenManager.update(Gdx.graphics.getDeltaTime());
	}
	
	/**
	 * Se encarga de hacer una transicion entre el menu de preferencias y el menu de principal.
	 * <br>Este metodo es llamado cuando los botones "Save" o "Cancel" del menu de preferencias 
	 * son pulsados. 
	 */
	private void hideSettings(){
		Timeline.createSequence().beginSequence()
//		.push(Tween.set(layerMenu, ActorAccessor.X).target(layerMenu.getWidth()))
		.push(Tween.set(layerMenu, ActorAccessor.X).target(-layerMenu.getWidth()))
		.push(Tween.set(layerMenu, ActorAccessor.VISIBILITY).target(1))
		.push(
			Timeline.createParallel().beginParallel()
			.push(Tween.to(layerSettings, ActorAccessor.X, .8f).target(layerSettings.getWidth()))
			.push(Tween.to(layerMenu, ActorAccessor.X, .8f).target(0))
			.end()
		)		
		.push(Tween.set(layerSettings, ActorAccessor.VISIBILITY).target(0))
		.end().start(tweenManager);
		tweenManager.update(Gdx.graphics.getDeltaTime());
		AudioManager.instance.onSettingsUpdated();
	}
	
	/**
	 * Se encarga de hacer una transicion entre el menu principal y la pantalla de puntuaciones.
	 * <br>Este metodo es llamado cuando el boton "High Scores" del menu principal es pulsado. 
	 */
	private void showScores(){
		Timeline.createSequence().beginSequence()
		.push(Tween.set(layerHighScore, ActorAccessor.X).target(-layerHighScore.getWidth()))
		.push(Tween.set(layerHighScore, ActorAccessor.VISIBILITY).target(1))
		.push(
			Timeline.createParallel().beginParallel()
			.push(Tween.to(layerMenu, ActorAccessor.X, .8f).target(layerMenu.getWidth()))
			.push(Tween.to(layerHighScore, ActorAccessor.X, .8f).target(0))
			.end()
		)		
		.push(Tween.set(layerMenu, ActorAccessor.VISIBILITY).target(0))
		.end().start(tweenManager);
		tweenManager.update(Gdx.graphics.getDeltaTime());
	}
	
//	private void hideScores(){
//		Timeline.createSequence().beginSequence()
////		.push(Tween.set(layerMenu, ActorAccessor.X).target(layerMenu.getWidth()))
//		.push(Tween.set(layerMenu, ActorAccessor.X).target(layerMenu.getWidth()))
//		.push(Tween.set(layerMenu, ActorAccessor.VISIBILITY).target(1))
//		.push(
//			Timeline.createParallel().beginParallel()
//			.push(Tween.to(layerHighScore, ActorAccessor.X, .8f).target(-layerHighScore.getWidth()))
//			.push(Tween.to(layerMenu, ActorAccessor.X, .8f).target(0))
//			.end()
//		)		
//		.push(Tween.set(layerHighScore, ActorAccessor.VISIBILITY).target(0))
//		.end().start(tweenManager);
//		tweenManager.update(Gdx.graphics.getDeltaTime());
//	}

}
