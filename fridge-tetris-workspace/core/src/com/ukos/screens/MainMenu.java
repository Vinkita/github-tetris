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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.ukos.tween.ActorAccessor;

public class MainMenu implements Screen {
	
	private Stage stage;
	private Skin skin;
	private Image heading; 
	private Image background; 
	private Table table;
	private TweenManager tweenManager;

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
//		Table.drawDebug(stage);
//		TextButton.drawDebug(stage);
		
		stage.act(delta);		
		stage.draw();		
		
		tweenManager.update(delta);
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(new ExtendViewport(width, height));
		stage.getViewport().update(width, height, true);
		table.invalidateHierarchy();
	}

	@Override
	public void show() {
		stage = new Stage();
		
		Gdx.input.setInputProcessor(stage);

		skin = new Skin(Gdx.files.internal("ui/mainMenuSkin.json"), new TextureAtlas("ui/menu.pack"));
		
		background = new Image(skin.getDrawable("mainIce"));
		background.setFillParent(true);
		stage.addActor(background);
		
		table = new Table(skin);
		table.setFillParent(true);		
		table.debug();
				
		heading = new Image(skin.getDrawable("title"));
		
//		Label heading = new Label("FRIDGE \n"
//								+ "TETRIS!", skin, "big");
//		heading.setFontScale(2);
		
//		creating buttons
		TextButton buttonPlay = new TextButton("PLAY", skin, "pink");
		buttonPlay.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
//				((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
				((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen());
			}
		});
		buttonPlay.pad(15);
		
		TextButton buttonSettings = new TextButton("SETTINGS", skin, "green");
		buttonSettings.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
//				((Game) Gdx.app.getApplicationListener()).setScreen(new Settings());
			}
		});
		buttonSettings.pad(15);
		
		TextButton buttonExit = new TextButton("EXIT", skin, "blue");
		buttonExit.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Timeline.createParallel().beginParallel()
				.push(Tween.to(table, ActorAccessor.ALPHA, .5f).target(0))
				.push(Tween.to(table, ActorAccessor.Y, .5f).target(table.getY() - 200))
				.setCallback(new TweenCallback() {					
					@Override
					public void onEvent(int type, BaseTween<?> source) {
//						Gdx.app.exit();
						((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
					}
				})
				.end().start(tweenManager);
			}
		});
		buttonExit.pad(15);
		
		buttonPlay.debug();
		float buttonWidth = buttonSettings.getWidth();
		
//		arming table
		table.add(heading).spaceBottom(100).row();
		table.add(buttonPlay).width(buttonWidth).spaceBottom(15).row();
		table.add(buttonSettings).width(buttonWidth).spaceBottom(15).row();
		table.add(buttonExit).width(buttonWidth);
		stage.addActor(table);
		
//		creating animations
		tweenManager = new TweenManager();
		Tween.registerAccessor(Actor.class, new ActorAccessor());

//		heading shitting rainbows
//		Timeline.createSequence().beginSequence()
//			.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(0, 0, 1))
//			.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(0, 1, 0))
//			.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(1, 0, 0))
//			.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(1, 1, 0))
//			.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(1, 0, 1))
//			.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(0, 1, 1))
//			.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(1, 1, 1))
//			.end().repeat(Tween.INFINITY, 0).start(tweenManager);
		
//		heading and buttons fade-in
		Timeline.createSequence().beginSequence()
			.push(Tween.set(buttonPlay, ActorAccessor.ALPHA).target(0))
			.push(Tween.set(buttonSettings, ActorAccessor.ALPHA).target(0))
			.push(Tween.set(buttonExit, ActorAccessor.ALPHA).target(0))
			.push(Tween.from(heading, ActorAccessor.ALPHA, .25f).target(0))
			.push(Tween.to(buttonPlay, ActorAccessor.ALPHA, .25f).target(1))
			.push(Tween.to(buttonSettings, ActorAccessor.ALPHA, .25f).target(1))
			.push(Tween.to(buttonExit, ActorAccessor.ALPHA, .25f).target(1))
			.end().start(tweenManager);

		//	table fade-in
		Tween.from(table, ActorAccessor.ALPHA, .5f).target(0).start(tweenManager);
		Tween.from(table, ActorAccessor.Y, .5f).target(Gdx.graphics.getHeight() / 4).start(tweenManager);
		
		tweenManager.update(Gdx.graphics.getDeltaTime());
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

}
