package com.ukos.screens;


import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.esotericsoftware.tablelayout.Cell;
import com.sun.xml.internal.messaging.saaj.packaging.mime.util.ASCIIUtility;
import com.ukos.fridgetetris.HighScores;
import com.ukos.fridgetetris.HighScores.HighScore;
import com.ukos.fridgetetris.ScoreService;
import com.ukos.tween.ActorAccessor;

public class HighScoreLayer extends Table {
	TweenManager tweenManager;
	HighScores scores;
	Skin skin;
	int newScore;
	
	Table scoresLayer;
	
	Table buttonsLayer;
	Cell<TextButton> buttonCell = null;
	TextButton backButton;
	TextButton fadeOutButton;
	
	Table dialogLayer;
	Label lblName;
	TextField txtName;
	TextButton okButton;
	protected Table previousLayer;
	
	public HighScoreLayer(Skin skin) {
		scores = ScoreService.retrieveScores();
		this.skin = skin;
		this.setFillParent(true);	
		this.bottom().left();
//		this.setBackground(skin.getDrawable("black50"));
		tweenManager = new TweenManager();
		setupButtons();
		
		scoresLayer = new Table();
		buttonsLayer = new Table();
//		tabla.setBackground(skin.getDrawable("black50"));
		scoresLayer = rebuildScores(scoresLayer);
		buttonsLayer = buildButtonsLayer();
		this.add(scoresLayer);
		this.add(buttonsLayer);
		dialogLayer = setupBox();
		this.add(dialogLayer);
		this.setVisible(false);
	}
	
	private void setupButtons(){
		// botones pantalla
		fadeOutButton = new TextButton("Ok", skin, "orange");
		backButton = new TextButton("Ok", skin, "pink");
		fadeOutButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				fadeOut();
			}
		});
		backButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
					slideLayer(previousLayer);
			}
		});
		//boton dialogo
		okButton = new TextButton("Ok", skin, "green");
		okButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				scores.add(new HighScore(txtName.getText(), newScore));
				ScoreService.persist();
				rebuildScores(scoresLayer);
				Timeline.createSequence().beginSequence()
				.push(Tween.to(dialogLayer, ActorAccessor.ALPHA, .5f).target(0))
				.push(Tween.set(dialogLayer, ActorAccessor.VISIBILITY).target(0))
				.end().start(tweenManager);
			}
		});
	}

	private Table buildButtonsLayer(){
		Table btable = new Table();
		btable.setFillParent(true);
		btable.bottom().center();
		buttonCell = btable.add(backButton).bottom().center();
		return btable;
	}
	
	private Table rebuildScores(Table taux) {
		taux.debug();
		taux.clear();
		taux.setFillParent(true);
		int i = 1;
		for (HighScore hs : scores.getList()){
			taux.add(new Label(i + ". " + hs.name, skin));
			taux.add(new Label("" + hs.score, skin));
			taux.row();
			i++;
		}		
		
//		taux.add(backButton).bottom().center();
//		taux.add(fadeOutButton).bottom().center();
		return taux;
	}
	
	private Table setupBox() {
		Table taux = new Table();
		lblName = new Label("Name: ", skin);
		txtName = new TextField("", skin);

		taux.add(lblName);
		taux.add(txtName);
		taux.row();
		taux.add(okButton);
		taux.setVisible(false);
		return taux;
	}

	public void fadein(int score) {
		buttonCell.setWidget(fadeOutButton);
//		fadeOutButton.setVisible(true);
//		okButton.setVisible(false);
		newScore = score;
		txtName.setText("");
		this.setBackground(skin.getDrawable("mainIce"));
		Timeline.createSequence().beginSequence()
		.push(Tween.set(this, ActorAccessor.ALPHA).target(0))		
		.push(Tween.set(this, ActorAccessor.VISIBILITY).target(1))
		.push(Tween.set(dialogLayer, ActorAccessor.VISIBILITY).target(1))
		.push(Tween.set(dialogLayer, ActorAccessor.ALPHA).target(1))
		.push(Tween.to(this, ActorAccessor.ALPHA, .5f).target(1))
		.end().start(tweenManager);
	}
	
	public void fadeOut() {
//		okButton.setVisible(true);
//		fadeOutButton.setVisible(false);
		final HighScoreLayer aux = this;
		Timeline.createSequence().beginSequence()
		.push(Tween.to(this, ActorAccessor.ALPHA, .5f).target(0))
		.push(Tween.set(dialogLayer, ActorAccessor.VISIBILITY).target(0))
		.push(Tween.set(this, ActorAccessor.VISIBILITY).target(0))		
		.end().setCallback(new TweenCallback() {
			
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				aux.setBackground((Drawable)null);				
				buttonCell.setWidget(backButton);
			}
		}).start(tweenManager);		
	}
	
	public void slideLayer(Table prevLayer){
		Timeline.createSequence().beginSequence()
//		.push(Tween.set(layerMenu, ActorAccessor.X).target(layerMenu.getWidth()))
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

	public void setPreviousLayer(Table prevLayer) {
		 this.previousLayer = prevLayer;
		
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		tweenManager.update(delta);
	}
}
