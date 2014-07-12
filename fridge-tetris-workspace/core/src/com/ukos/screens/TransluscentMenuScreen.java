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

public abstract class TransluscentMenuScreen extends Observable{
	private Stage stage;
	private Skin skin;
	protected Table table;
	private Drawable background;
	protected TweenManager tweenManager;
	
	enum Event { BACK_CLICK, RESET_CLICK, FADE_OUT_PAUSE, FADE_OUT_OVER, NONE }
	protected Event lastEvent = Event.NONE;
	
	protected String backButtonText = "";
	protected String headerText;
	protected Image black;
	

//	public PauseScreen(){
//		this(new Stage(), new Skin(new TextureAtlas("ui/menu.pack")));
//	}
	
	public TransluscentMenuScreen(Stage stage, Skin skin){
		this.stage = stage;
		this.skin = skin;
		setUpTable();
	}
	
	public Stage getStage(){
		return stage;
	}
	
	protected abstract void setStrings();
	protected abstract Event getBackEvent();
	
	private void setUpTable(){
		black = new Image(skin, "black");
		black.setColor(0, 0, 0, 0);
		black.setFillParent(true);
		stage.addActor(black);
		setStrings();
		
		TextButton buttonBack = new TextButton(backButtonText, skin, "pink");
		TextButton buttonMenu = new TextButton("Main Menu", skin, "green");
		TextButton buttonExit = new TextButton("Quit", skin, "blue");
		
		buttonBack.addListener(new ClickListener(){			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				setEvent(getBackEvent());
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
		
		background = skin.getDrawable("black50");

		table = new Table(skin);
		table.setFillParent(true);
		table.setBackground(background);
		table.add(new Label(headerText, skin)).spaceBottom(100).row();
		table.add(buttonBack).spaceBottom(15).row();
		table.add(buttonMenu).spaceBottom(15).uniformX().row();
		table.add(buttonExit);
		stage.addActor(table);
		
		table.setVisible(false);
		table.setColor(table.getColor().r, table.getColor().g, table.getColor().b, 0);
		table.debug();
		
//		tweenManager = new TweenManager();
		Tween.registerAccessor(Actor.class, new ActorAccessor());
	}
	
	public void fadeIn(){
		Timeline.createSequence().beginSequence()
		.push(Tween.set(table, ActorAccessor.VISIBILITY).target(1))
		.push(Tween.to(table, ActorAccessor.ALPHA, .25f).target(1))
		.end().start(tweenManager);
		
		tweenManager.update(Gdx.graphics.getDeltaTime());
	}
	
	public abstract void fadeOut();
	
	protected void setEvent(Event newEvent){
		lastEvent = newEvent;
		setChanged();
		notifyObservers();
	}

	@Override
	public synchronized void addObserver(Observer arg0) {
		super.addObserver(arg0);
	}

	@Override
	public void notifyObservers() {
		super.notifyObservers();
	}

	public void setTween(TweenManager tweenManager) {
		this.tweenManager =	tweenManager;		
	}
	
	

}
