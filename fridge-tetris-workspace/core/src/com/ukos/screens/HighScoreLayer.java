package com.ukos.screens;


import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.esotericsoftware.tablelayout.Cell;
import com.ukos.fridgetetris.HighScores;
import com.ukos.fridgetetris.HighScores.HighScore;
import com.ukos.fridgetetris.ScoreService;
import com.ukos.tween.ActorAccessor;

/**
 * Menu de HighScores (Puntuaciones mas altas).
 * <br> Este menu esta pensado para ser agregado como elemento a otro menu o pantalla.
 * @author Ukos
 */
public class HighScoreLayer extends Stack {
	TweenManager tweenManager;
	/** Contiene las puntuaciones mas altas y los nombres de los jugadores que las obtuvieron. */
	HighScores scores;
	/** Determina la apariencia de la {@code HighScoreLayer}. */
	Skin skin;
	/** La puntuacion obtenida por el jugador al final del juego. */
	int newScore;
	
	/** La capa de fondo, contiene la imagen de fondo de {@code this}. */
	Table backgroundLayer;
	/** La capa de puntuaciones, utilizada para mostrar las puntuaciones mas altas en forma de lista. */
	Table scoresLayer;
	/** La capa de botones. Contiene los botones utilizados para ocultar {@code this}. */
	Table buttonsLayer;
	/** La capa "Dialog". Permite al jugador introducir su nombre para luego guardar su puntuacion. */
	Table dialogLayer;
	
	/**
	 * Contendrá, segun corresponda, al boton <code>backButton</code> o al boton <code>fadeOutButton</code>
	 * @see #backButton 
	 * @see #fadeOutButton
	 */
	Cell<TextButton> buttonCell = null;
	/**
	 * Al ser clickeado, llamará al metodo <code>fadeOut()</code>
	 * @see # fadeOut()
	 */
	TextButton backButton;
	/**
	 * Al ser clickeado, llamará al metodo <code>slideLayer()</code>
	 * @see # slideLayer()
	 */
	TextButton fadeOutButton;
	
	/**
	 * Etiqueta "nombre".
	 */
	Label lblName;
	/**
	 * Aqui se introduce el nombre del jugador al terminar la partida.
	 */
	TextField txtName;
	/**
	 * Al ser clickeado, agregara la nueva puntuacion al objeto <code>scores</code>.
	 * <br>Tambien lamara al metodo <code>rebuildScores()</code> para actializar la capa "Scores"
	 * @see #rebuildScores(Table) 
	 */
	TextButton okButton;
	/**
	 * El {@code Actor} que ocupaba la pantalla antes que esta {@code HighScoreLayer}.
	 */
	protected Actor previousLayer;
	
	/**
	 * Crea una nueva {@code HighScoreLayer} la cual inicialmente no es visible.
	 */
	public HighScoreLayer(Skin skin) {
		scores = ScoreService.retrieveScores();
		this.skin = skin;
		this.setFillParent(true);
		tweenManager = new TweenManager();
		setupButtons();
		
		backgroundLayer = buildBackgroundLayer();
		scoresLayer = rebuildScores(scoresLayer);
		buttonsLayer = buildButtonsLayer();
		dialogLayer = setupDialog();
		
		Table tab = new Table(skin);
		tab.setFillParent(true);
		tab.add(new Label("HighScores", skin, "tetris")).padTop(50).row();
		tab.add(scoresLayer).expandY().fillY().row();
		tab.add(buttonsLayer).fillX().bottom().padBottom(20);
		
		this.add(backgroundLayer);
		this.add(tab);
		this.add(dialogLayer);
		this.setVisible(false);
	}
	
	/**
	 * Instancia los botones y agrega los listeners correspondientes a cada uno.
	 */
	private void setupButtons(){
		// botones pantalla
		fadeOutButton = new TextButton("Ok", skin, "orange");
		backButton = new TextButton("Ok", skin, "pink");
		fadeOutButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				fadeOut();
				rebuildScores(scoresLayer);
			}
		});
		backButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
					slideLayer(previousLayer);
			}
		});
		//boton dialogo
		okButton = new TextButton("Ok", skin, "greenSmall");
		okButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				persistScore();
				Timeline.createSequence().beginSequence()
				.push(Tween.to(dialogLayer, ActorAccessor.ALPHA, .5f).target(0))
				.push(Tween.set(dialogLayer, ActorAccessor.VISIBILITY).target(0))
				.end().start(tweenManager);
			}
		});
	}
	
	/**
	 * Construye la capa "Background".
	 * @return la capa "Background".
	 */	
	private Table buildBackgroundLayer(){
		Table taux = new Table();
		taux.setFillParent(true);
		taux.setBackground(skin.getDrawable("mainIce"));
		taux.setVisible(false);
		return taux;
	}
	
	/**
	 * Construye la capa "Buttons", en la cual se ubican los botones 
	 * @return la capa "Buttons".
	 */
	private Table buildButtonsLayer(){
		Table btable = new Table();
		buttonCell = btable.add(backButton).width(backButton.getHeight() * 1.5f);
		return btable;
	}
	
	/**
	 * Construye (o reconstruye, segun el caso) la capa "Scores".
	 * <br> La última puntuacion obtenida es resaltada. 
	 * @param taux
	 * @param latestScore la ultima puntuacion en ser agregada
	 * @return La capa "Scores".
	 */
	private Table rebuildScores(Table taux, int latestScore) {
		if(taux != null)
			taux.clear();			
		else 
			taux = new Table();
		if(latestScore > -1) latestScore++;
		
		taux.add().prefWidth(8*30);
		taux.add().prefWidth(8*10).row();
		int i = 1;
		String style = "default";
		for (HighScore hs : scores.getList()){
			if(latestScore == i)
				style = "default-red";
			else 
				style = "default";
			taux.add(new Label(i + ". " + hs.name, skin, style)).left();
			taux.add(new Label("" + hs.score, skin, style)).right();
			taux.row();
			i++;
		}		
		
		return taux;
	}
	/**
	 * Construye (o reconstruye, segun el caso) la capa "Scores".
	 * @param taux
	 * @return La capa "Scores".
	 */
	private Table rebuildScores(Table taux) {
		return rebuildScores(taux, -1);
	}
	
	/**
	 * Crea la capa "Dialog".
	 * @return la capa "Dialog".
	 */
	private Table setupDialog() {
		Table tbaux = new Table();
		Window taux = new Window("", skin);
		taux.setModal(true);
		lblName = new Label("Name: ", skin, "tetrisSmall");
		txtName = new TextField("", skin);
		txtName.setMaxLength(20);

		taux.row().padTop(20);
		taux.add(lblName);
		taux.add(txtName);
		taux.row();
		taux.add(okButton).width(okButton.getHeight() * 1.5f).colspan(2).center().padTop(40);
		
		tbaux.add(taux);
		tbaux.setVisible(false);
		return tbaux;
	}

	/**
	 * Inserta una nueva instancia de HighScores en {@code scores} y persiste los cambios.
	 */
	private void persistScore(){
		int rank = scores.add(new HighScore(txtName.getText(), newScore));
		ScoreService.persist();
		rebuildScores(scoresLayer, rank);
	}
	
	/**
	 * Muestra esta {@code HighScoreLayer}, haciendola visible gradualmente.
	 */
	public void fadein() {
		buttonCell.setWidget(fadeOutButton);
//		fadeOutButton.setVisible(true);
//		okButton.setVisible(false);
		backgroundLayer.setVisible(true);
		Timeline.createSequence().beginSequence()
		.push(Tween.set(this, ActorAccessor.ALPHA).target(0))		
		.push(Tween.set(this, ActorAccessor.VISIBILITY).target(1))
		.push(Tween.to(this, ActorAccessor.ALPHA, .5f).target(1))
		.end().start(tweenManager);
	}
	
	/**
	 * Muestra esta {@code HighScoreLayer}, haciendola visible gradualmente.
	 * <br>Adicionalmente, muestra la capa "Dialog" para que el jugador 
	 * introduzca su nombre y su puntuacion sea guardada.
	 * @param score  la puntuacion del jugador.
	 */
	public void fadein(int score) {
		newScore = score;
		txtName.setText("");
		dialogLayer.setVisible(true);
		dialogLayer.getColor().a = 1;
		fadein();
	}
	
	/**
	 * Esconde esta {@code HighScoreLayer}, desvaneciendola gradualmente.  
	 */
	public void fadeOut() {
		Timeline.createSequence().beginSequence()
		.push(Tween.to(this, ActorAccessor.ALPHA, .5f).target(0))
		.push(Tween.set(dialogLayer, ActorAccessor.VISIBILITY).target(0))
		.push(Tween.set(this, ActorAccessor.VISIBILITY).target(0))		
		.end().setCallback(new TweenCallback() {
			
			@Override
			public void onEvent(int type, BaseTween<?> source) {	
				backgroundLayer.setVisible(false);
				buttonCell.setWidget(backButton);
			}
		}).start(tweenManager);		
	}
	
	/**
	 * Desliza esta {@code HighScoreLayer} hacia la izquierda, hacia fuera de la pantalla, 
	 * mientras que al mismo tiempo mueve a <code>prevLayer</code> desde la derecha de la pantalla para ocupar su lugar. 
	 * @param prevLayer ocupara el lugar de esta {@code HighScoreLayer} en la pantalla.
	 */
	public void slideLayer(Actor prevLayer){
		Timeline.createSequence().beginSequence()
		.push(Tween.set(prevLayer, ActorAccessor.X).target(prevLayer.getWidth()))
		.push(Tween.set(prevLayer, ActorAccessor.VISIBILITY).target(1))
		.push(
			Timeline.createParallel().beginParallel()
			.push(Tween.to(this, ActorAccessor.X, .8f).target(-this.getWidth()))
			.push(Tween.to(prevLayer, ActorAccessor.X, .8f).target(0))
			.end()
		)		
		.push(Tween.set(this, ActorAccessor.VISIBILITY).target(0))
		.end().start(tweenManager);
		tweenManager.update(Gdx.graphics.getDeltaTime());
	}

	/**
	 * Setea la capa previa a esta, es decir, 
	 * la capa que sera mostrada al invocar al metodo <code>slideLayer()</code>.
	 * @param prevLayer
	 * @see slideLayer()
	 */
	public void setPreviousLayer(Table prevLayer) {
		 this.previousLayer = prevLayer;
		
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		tweenManager.update(delta);
	}
}
