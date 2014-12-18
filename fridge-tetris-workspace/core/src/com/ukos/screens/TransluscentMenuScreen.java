package com.ukos.screens;

import java.util.Observable;
import java.util.Observer;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.ukos.tween.ActorAccessor;

/**
 * Menu generico, contiene los elementos y comportamientos 
 * comunes a las pantallas de pausa y de fin de juego.
 * @author Ukos
 */
public abstract class TransluscentMenuScreen extends Observable{
	private Stage stage;
	private Skin skin;
	protected Table table;
	private Drawable background;
	protected TweenManager tweenManager;
	
	enum Event { BACK_CLICK, RESET_CLICK, FADE_OUT_PAUSE, FADE_OUT_OVER, NONE }
	protected Event lastEvent = Event.NONE;
	
	protected String backButtonText = "";
	protected String headerText = "";
	protected Image black;
	
	TextButton buttonBack;
	TextButton buttonMenu;
	TextButton buttonExit;
	
	/**
	 * Crea una nueva pantalla.
	 * @param stage  el {@link Stage} que contendra los elementos de la pantalla
	 * @param skin
	 */
	public TransluscentMenuScreen(Stage stage, Skin skin){
		this.stage = stage;
		this.skin = skin;
		setUpTable();
	}
	
	public Stage getStage(){
		return stage;
	}
	
	/**
	 * Define las "Etiquetas" de los botones y el titulo de la pantalla.
	 */
	protected abstract void setStrings();
	/**
	 * @return el {@link Event} correspondiente a {@link #buttonBack}
	 */
	protected abstract Event getButtonBackEvent();
	
	/**
	 * Crea y configura a {@link #table} y a sus elementos.
	 */
	private void setUpTable(){
		black = new Image(skin, "black");
		black.setColor(0, 0, 0, 0);
		black.setFillParent(true);
		black.setVisible(false);
		
		setupButtons();
		background = skin.getDrawable("black50");

		table = new Table(skin);
		table.setFillParent(true);
		table.setVisible(false);
		table.setColor(table.getColor().r, table.getColor().g, table.getColor().b, 0);
		table.setBackground(background);
		table.add(new Label(headerText, skin, "tetris")).spaceBottom(100).row();
		table.add(buttonBack).spaceBottom(15).row();
		table.add(buttonMenu).spaceBottom(15).uniformX().row();
		table.add(buttonExit);
		stage.addActor(table);
		stage.addActor(black);
		
		table.debug();
		
		Tween.registerAccessor(Actor.class, new ActorAccessor());
	}
	
	/**
	 * Instancia y configura los botones del menu. 
	 */
	private void setupButtons(){
		setStrings();
		buttonBack = new TextButton(backButtonText, skin, "pink");
		buttonMenu = new TextButton("Main Menu", skin, "green");
		buttonExit = new TextButton("Quit", skin, "blue");
		
		buttonBack.addListener(new ClickListener(){			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				fireEvent(getButtonBackEvent());
			}
		});
		
		buttonMenu.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
			}
		});
		
		buttonExit.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});
	}
	
	/**
	 * Muestra esta {@code TransluscentMenuScreen}, haciendola visible gradualmente.
	 */
	public void fadeIn(){
		Timeline.createSequence().beginSequence()
		.push(Tween.set(table, ActorAccessor.VISIBILITY).target(1))
		.push(Tween.to(table, ActorAccessor.ALPHA, .25f).target(1))
		.end().start(tweenManager);
		
		tweenManager.update(Gdx.graphics.getDeltaTime());
	}
	
	/**
	 * Esconde esta {@code TransluscentMenuScreen}, desvaneciendola gradualmente.  
	 */
	public abstract void fadeOut();
	
	/**
	 * Setea {@link #lastEvent} y notifica a los observadores.
	 * @param newEvent
	 */
	protected void fireEvent(Event newEvent){
		lastEvent = newEvent;
		setChanged();
		notifyObservers();
	}

	/**
	 * Agrega un observador a la lista de  observadores de este objeto.
	 * @see java.util.Observable#addObserver(java.util.Observer)
	 */
	@Override
	public synchronized void addObserver(Observer arg0) {
		super.addObserver(arg0);
	}

	/**
	 * Si este objeto ha cambiado notifica a todos sus observadores.
	 * @see java.util.Observable#notifyObservers()
	 */
	@Override
	public void notifyObservers() {
		super.notifyObservers();
	}

	/**
	 * @param tweenManager
	 */
	public void setTween(TweenManager tweenManager) {
		this.tweenManager =	tweenManager;		
	}
	
	

}
