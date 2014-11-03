package com.ukos.fridgetetris;

import java.util.Iterator;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.TimeUtils;
import com.ukos.logics.BlockDrawable;
import com.ukos.logics.Board;
import com.ukos.logics.IStopBlockListener;
import com.ukos.logics.Point;
import com.ukos.logics.ScoreCounter;
import com.ukos.logics.Tetromino;
import com.ukos.logics.Tetromino.shape;
import com.ukos.tween.BmFontAccessor;

public class BoardRenderer implements IStopBlockListener{

	private OrthographicCamera camera;
	private OrthographicCamera cameraGUI;
	private SpriteBatch batch;
	private Board tablero;
	private ScoreCounter puntos;	
	TweenManager tweenManager;
	
	//TEXTURAS
	private TextureAtlas atlas;
	private ArrayMap<String, TextureRegion> texRegions;
	private ArrayMap<String, TextureRegion> prevRegions;	
	private Array<BlockDrawable> previewList;	
	private TextureRegion heladera;
	private Texture rayas;
	
	//FUENTES
	private BitmapFont labelFont; 
	private BitmapFont numberFont; 
	private BitmapFont scoreCenterFont; 
	private String levelLabel;
	private String levelString;
	private String scoreLabel;
	private String scoreString;
	private int score;	
	private int scoreVisual;	
	private int prevCant;
	
	//DIMENSIONES PANTALLA Y UBICACION ELEMENTOS
	private int boardWidth;
	private int boardHeight;
	private int PIXELS_PER_METER;
	private float viewportWidth;
	private float viewportHeight;
	private float scoreXPosition;
	private float scoreYPosition;
	private Vector2 boardOffset;
	private Vector2 previewOffset;
	
//ParticleEffect
	private ParticleEffect particle;
	private ParticleEffectPool pool;
	private Array<PooledEffect> effects;
	private ExplosionChecker explosionChecker = new ExplosionChecker();
	private Timeline boing;
	
	public BoardRenderer(Board tablero, ScoreCounter puntos) {
		this.tablero = tablero;
		this.puntos = puntos;
		init();
	}
	
	public void init(){
		boardWidth = (int) (tablero.getWidth() + 2);
		boardHeight = (int) (tablero.getHeight() + 2);
		boardOffset = new Vector2(1, 1);
		previewOffset = new Vector2(boardWidth, boardHeight / 2);
		prevCant = GamePreferences.instance.previews;		
		initParticles();		
		initCameras();
		
		batch = new SpriteBatch();			
		atlas = new TextureAtlas("ui/tetra.pack");		
		setupPieceTextures();
		setupPreviewTextures();
		setupPreviewList();
		initFonts();		
		initTween();
	}
	
	public void reset(){
		score = scoreVisual = 0;
		updateScore();
	}
	
	private void initParticles(){
		//ParticleEffect
		particle = new ParticleEffect();
		particle.load(Gdx.files.internal("particle/explosion.p"), Gdx.files.internal("particle"));
		pool = new ParticleEffectPool(particle, 0, 16);
		effects = new Array<PooledEffect>();
	}

	private void initCameras(){
		calculateViewPortDimensions();
		camera = new OrthographicCamera();
		cameraGUI = new OrthographicCamera();
		camera.setToOrtho(false, viewportWidth, viewportHeight);
		cameraGUI.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
	
	private void initFonts(){
		numberFont = new BitmapFont(Gdx.files.internal("fonts/Opificio_15.fnt"), true);
		numberFont.setColor(Color.BLACK);
		numberFont.setScale(1f);
		numberFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);				
		labelFont = new BitmapFont(Gdx.files.internal("fonts/Opificio_15.fnt"), true);
		labelFont.setColor(Color.OLIVE);
		labelFont.setScale(1.5f);
		labelFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);				
		scoreCenterFont = new BitmapFont(Gdx.files.internal("fonts/Opificio_64.fnt"), true);
		scoreCenterFont.setColor(Color.BLACK);
		scoreCenterFont.setColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, 0);
		scoreCenterFont.setScale(1f);
		scoreCenterFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);				
		scoreLabel = "SCORE";
		levelLabel = "LEVEL";
		scoreVisual = score = 0;
		levelString = "" + tablero.getLevel();
	}
	
	private void initTween(){
		tweenManager = new TweenManager();
		Tween.registerAccessor(BitmapFont.class, new BmFontAccessor());
		boing = Timeline.createParallel().beginParallel()
				.push(Tween.to(scoreCenterFont, BmFontAccessor.SCALE, .5f).target(2).repeatYoyo(1, 0))
				.push(Tween.to(scoreCenterFont, BmFontAccessor.COLOR, .5f).target(0, 0, 1).repeatYoyo(1, 0))
				.push(Tween.to(scoreCenterFont, BmFontAccessor.ALPHA, .5f).target(1).repeatYoyo(1, 0).setCallback(new TweenCallback() {					
					@Override
					public void onEvent(int type, BaseTween<?> source) {
						boing.pause();						
					}
				}))				
				.repeat(-1, 0)
				.end()
				.start(tweenManager);
		boing.pause();
//				tweenManager.update(Gdx.graphics.getDeltaTime());
	}
	
	private void calculateViewPortDimensions() {
		int screenWidth = Gdx.graphics.getWidth();
		int screenHeight = Gdx.graphics.getHeight();
		float targetRatio = boardHeight / (boardWidth + 1);
		float screenRatio = screenHeight / screenWidth;
		
		if(screenRatio > targetRatio){
			viewportWidth = (boardWidth + 1);
			PIXELS_PER_METER = (int) (screenWidth / viewportWidth);
			viewportHeight = screenHeight / PIXELS_PER_METER;
		} else {
			viewportHeight = boardHeight;
			PIXELS_PER_METER = (int) (screenHeight / viewportHeight);					
			viewportWidth = (screenWidth / PIXELS_PER_METER)+.5f;
		} 	
		
		scoreXPosition = (boardWidth)*PIXELS_PER_METER;
		scoreYPosition = (boardHeight/10)*PIXELS_PER_METER;
	}

	public void render(float delta) {
		camera.update();
      
		//render by ortho camera coordinates:
		batch.setProjectionMatrix(camera.combined);
		
		//Draw current grid:
		batch.begin();
		batch.draw(rayas, 0, 0, viewportWidth, viewportHeight);
		batch.draw(heladera, 0, 0, boardWidth, boardHeight);		
		for (BlockDrawable block : tablero.getAllBlocksToDraw())
			block.draw(batch, texRegions.get(block.getStyle()), boardOffset );

		
		explosionChecker.checkRowExplosion();
		
		for (Iterator<PooledEffect> it = effects.iterator(); it.hasNext(); ) {
			PooledEffect effect = it.next();
			if (effect.isComplete()) {
				it.remove();
				effect.free();
			}
//			if (!effect.isComplete()) {
				effect.draw(batch, delta);
//				}
		}

		renderPreview();
		renderScore(delta);
		tweenManager.update(delta);
		renderBoing();
		batch.end();
		
	}

	public void resize(int width, int height) {
		calculateViewPortDimensions();
		camera.setToOrtho(false, viewportWidth, viewportHeight);
		cameraGUI.setToOrtho(true, width, height);
	}
	
	private void setupPieceTextures(){
		texRegions = new ArrayMap<String, TextureRegion>();		
		for(Iterator<String[]> it = Tetromino.colors.values().iterator(); it.hasNext() ;) {
			String[] arr = it.next();
			for(int i = 0, j = arr.length; i < j; i++) {
				texRegions.put(arr[i], new TextureRegion(atlas.findRegion(arr[i])));
			}
		}						
		heladera = atlas.findRegion("Fridge");
		rayas = new Texture(Gdx.files.internal("ui/rayas.png")); 
	}
	
	private void setupPreviewTextures(){
		prevRegions = new ArrayMap<String, TextureRegion>();
		String type;
		for(shape forma : shape.values()) {
			type = forma.name();
			prevRegions.put(type, atlas.findRegion(type));			
		}
	}
	
	private void setupPreviewList(){
		previewList = new Array<BlockDrawable>();
		float pad = 0;
		for(int i = 0, j = prevCant; i < j; i++){
			previewList.add(new BlockDrawable(new Point(0, 0 - i - pad), ""));
			pad += .25f;
		}
	}
	
	private void updatePreview(){
		String key;
		float w , h;	
		for (int i = 0; i < prevCant; i++){
			key = tablero.getPreviewPieces(prevCant).get(i).getTextureKey();
			if(key != "I"){
				h = 1;
				if(key != "O"){
					w = 1.5f;
				} else {
					w = 1;
				}
			} else{
				h = .5f;
				w = 2;
			}
			BlockDrawable blaux = previewList.get(i);
			blaux.setSize(w, h);
			blaux.setStyle(key);
		}
	}
	
	private void renderPreview(){
		for (BlockDrawable blaux : previewList){
			blaux.draw(batch, prevRegions.get(blaux.getStyle()), previewOffset);
		}
	}
	
	private void updateScore(){
		scoreString = "";
		for(char aux : String.valueOf(scoreVisual).toCharArray()){
			scoreString += aux + "\n";
		}
		score = puntos.getTotalScore();
		levelString = "" + (tablero.getLevel()+1);
	}

	private void renderScore(float delta) {
		batch.setProjectionMatrix(cameraGUI.combined);
		if(scoreVisual < score){
			scoreVisual = (int) Math.min(score, scoreVisual + 250 * (tablero.getLevel()+1) * delta);
			updateScore();
		} 
//		else if (scoreVisual > score){
//			scoreVisual = score;
//		}
//		BitmapFontCache cache = numberFont.getCache();
//		cache.setWrappedText("" + levelLabel + "\r\n" + levelString 
//							+ "\r\n\r\n" + scoreLabel + "\r\n" + scoreString,
//							scoreXPosition, scoreYPosition, 
//							(viewportWidth-boardWidth)*PIXELS_PER_METER, HAlignment.LEFT);
//		cache.setColors(Color.OLIVE, 0, levelLabel.toCharArray().length);
//		cache.setColors(Color.OLIVE, levelLabel.toCharArray().length + 1 , levelLabel.toCharArray().length + 1 + scoreLabel.toCharArray().length);
//		cache.draw(batch);
		numberFont.drawWrapped(batch, "" + levelLabel + "\r\n" + levelString 
				+ "\r\n\r\n" + scoreLabel + "\r\n" + scoreString,
				scoreXPosition, scoreYPosition, (viewportWidth-boardWidth)*PIXELS_PER_METER);
	}
	
	@Override
	public void onStoppedBlock() {
		updateScore();	
		updatePreview();
		boing.resume();
	}
	
	void renderBoing(){
		if(puntos.getLastScore() != 0){
			float centerX = PIXELS_PER_METER * boardWidth / 2;
			float centerY = PIXELS_PER_METER * boardHeight / 2;
			scoreCenterFont.drawMultiLine(batch, String.valueOf(puntos.getLastScore()), 
					centerX, centerY - (scoreCenterFont.getXHeight()/2), 1, HAlignment.CENTER);			
		}		
	}
	
private class ExplosionChecker {
		
		private int x=0;
		private long explodeRate = 5000000;
		private long lastExplode=0;
		private String imagePath = "";
		
		/*
		 * OPTIMIZAR EL LLAMADO A getDeletedRows TANTAS VECES!!
		 */
		private void checkRowExplosion() {
			ArrayMap<Integer, String[]> deletedRows = tablero.getDeletedRows();
			
			if (x == 10) {
				x = 0;
				deletedRows.clear();
			}
			if (deletedRows.size == 4) {
				Gdx.app.log("Columna:", Integer.toString(x));
			}
			if (deletedRows.size > 0) {
				if (TimeUtils.nanoTime() - lastExplode > explodeRate) {
					for (int i = 0; i < deletedRows.size; i++) {
						int y = deletedRows.getKeyAt(i);
						PooledEffect effect = pool.obtain();
						effect.setPosition(x+1.5f, y+1.5f);
						imagePath = deletedRows.get(y)[x];
						TextureRegion aux = texRegions.get(imagePath);
						effect.getEmitters().first().setSprite(new Sprite(aux));
						effects.add(effect);
					}
				}
				x++;
				lastExplode=TimeUtils.nanoTime();
			}
		}
	}

	public void dispose() {
		
		batch.dispose();	
		
		atlas.dispose();
		rayas.dispose();;
		
		labelFont.dispose(); 
		numberFont.dispose(); 
		scoreCenterFont.dispose(); 
		
		particle.dispose();
		for(PooledEffect auxEffect : effects)
			auxEffect.dispose();
		
	}

	public int getPPM() {
		return PIXELS_PER_METER;
	} 
	
	public Vector2 getOffset(){
		return new Vector2(boardOffset);
	}
	

}
