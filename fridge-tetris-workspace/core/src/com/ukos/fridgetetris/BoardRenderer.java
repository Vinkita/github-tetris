package com.ukos.fridgetetris;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.esotericsoftware.tablelayout.Cell;
import com.ukos.logics.BlockDrawable;
import com.ukos.logics.Board;
import com.ukos.logics.IStopBlockListener;
import com.ukos.logics.RotatablePiece;
import com.ukos.logics.ScoreCounter;
import com.ukos.logics.Tetromino;
import com.ukos.logics.Tetromino.shape;

public class BoardRenderer implements IStopBlockListener{

	private OrthographicCamera camera;
	private SpriteBatch batch;
//	private ArrayMap<String, Texture> textures;
	private Board tablero;
	
	
	//TEXTURAS
	private TextureAtlas atlas;
	private Skin skin;
	private ArrayMap<String, TextureRegion> texRegions;
	private ArrayMap<String, Image> prevRegions;
	private Array<Cell<Actor>> cellPrevs;
	private TextureRegion heladera;
	private int ppm = 32;
	
	private ScoreCounter puntos;
	private String scoreText;
	private String scoreString;
	private Label PointsLabel;
	private Label ScoreLabel;
	private Table table;
	private Table tableSide;
	private Stage stage;
	private int prevCant = 1;
	private Vector2 offset = new Vector2(1, 1);
	
//	private Array<Sprite> prev = new Array<Sprite>();
	
	private int screenWidth = 12;
	private int screenHeight = 22;
	
//ParticleEffect
	private ParticleEffect particle;
	private ParticleEffectPool pool;
	private Array<PooledEffect> effects;
	private ExplosionChecker explosionChecker = new ExplosionChecker();
	
	public BoardRenderer(Board tablero, ScoreCounter puntos) {
	//ParticleEffect
			particle = new ParticleEffect();
			particle.load(Gdx.files.internal("particle/explosion.p"), Gdx.files.internal("data/tetrominos"));
			pool = new ParticleEffectPool(particle, 0, 16);
			effects = new Array<PooledEffect>();
		
		camera = new OrthographicCamera();
		//The base unit for screen dimensions is equal to one tetromino unit
		//The playfield is 10 wide x 20 high.
		//The playfield is centered on the screen with 1 unit blank all around.
		//So, final camera perspective is is 12 wide x 20 high
		camera.setToOrtho(false, screenWidth, screenHeight);
		
		batch = new SpriteBatch();
		this.tablero = tablero;
		this.puntos = puntos;
		
		atlas = new TextureAtlas("ui/tetra.pack");		
		setupPieceTextures();
		setupPreviewTextures();
		
		cellPrevs = new Array<Cell<Actor>>();
		
		stage = new Stage();
		skin = new Skin(Gdx.files.internal("ui/menuSkin.json"), new TextureAtlas("ui/atlas.pack"));
		scoreText = "SCORE";
		ScoreLabel = new Label(scoreText, skin);	
		PointsLabel = new Label("0", skin);	
		ScoreLabel.setFontScale(.2f);
		PointsLabel.setFontScale(.5f);
		ScoreLabel.setColor(Color.BLACK);
		PointsLabel.setColor(Color.BLACK);
		
//		TAble Setup
		tableSide = new Table(skin);
		tableSide.add(ScoreLabel).row();
		tableSide.add(PointsLabel).right();
		setupPreview(tableSide);
		
		table = new Table(skin);
		table.setFillParent(true);
		table.debug();
		
		table.setBounds(0, 0, stage.getWidth(), stage.getHeight());
		table.add().expand(12, 22).maxSize(12f, 22f);
		table.add(tableSide);
		stage.addActor(table);
		
//		skinMenu = new Skin(Gdx.files.internal("ui/mainMenuSkin.json"), new TextureAtlas("ui/menu.pack"));
//		pausa  = new PauseScreen(stage, skinMenu);
	}

	public void render(float delta) {
		Table.drawDebug(stage);
		camera.update();
      
		//render by ortho camera coordinates:
		batch.setProjectionMatrix(camera.combined);
		
		//Draw current grid:
		batch.begin();
		batch.draw(heladera, 0, 0, 12, 22);
//		for (BlockDrawable block : tablero.getBoardBlocksToDraw()) {
//			drawBlock(block);
//		}
//		if(tablero.isGhostActivated()){			
//			batch.setColor(1.0f,1.0f,1.0f,0.5f);
//			for (BlockDrawable block : tablero.getGhostBlocksToDraw()) {
//				drawBlock(block);				
////				ghostSprite.setTexture(textures.get(block.getStyle()));
////				ghostSprite.setSize(rect.width, rect.height);
////				ghostSprite.setPosition(rect.x, rect.y);
////				ghostSprite.setColor(1.0f, 1.0f, 1.0f, 0.5f);
////				ghostSprite.draw(batch);
//			}
//			batch.setColor(Color.WHITE);
//		}
//		for(BlockDrawable block : tablero.getFallingBlocksToDraw()){
//			drawBlock(block);
//		}
		for (BlockDrawable block : tablero.getAllBlocksToDraw())
			block.draw(batch, texRegions.get(block.getStyle()), offset );
		
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

		batch.end();
		
		stage.act(delta);
		stage.draw();
	}
	
//	private void drawBlock(BlockDrawable block){
//		Rectangle rect = gridRectangles[(int) block.getPoint().Y()][(int) block.getPoint().X()];
//		batch.draw(texRegions.get(block.getStyle()), rect.x, rect.y, rect.width, rect.height);
//	}	

	public void resize(int width, int height) {
		stage.setViewport(new ExtendViewport(width, height));
		stage.getViewport().update(width, height, true);
		table.invalidateHierarchy();
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
	}
	
	private void setupPreviewTextures(){
		prevRegions = new ArrayMap<String, Image>();
		for(shape forma : shape.values()) {
			String type = forma.name();
			Image aux = new Image(atlas.findRegion(type));
//			if(aux.getImageWidth() % 3 == 0){
//				aux.setWidth(.5f * 3);;
//			} else if (aux.getImageWidth() % 4 == 0) {
//				aux.setWidth(.5f * 4);
//			} else {
//				aux.setWidth(.5f * 2);
//			}
//			aux.setScaling(Scaling.fillX);
			prevRegions.put(type, aux);
		}
	}
	
	private void setupPreview(Table table){
		for(int i = 0; i < prevCant; i ++){
			table.row();
			Cell<Actor> aux = table.add();
			aux.size(4 * ppm * .5f, 2 * ppm * .5f);
			cellPrevs.add(aux);		
		}
	}
	
	private void preview(){
		RotatablePiece paux = tablero.getPreviewPieces(1).get(0);
		String key = paux.getTextureKey();
		(cellPrevs.first()).setWidget(prevRegions.get(key));
	}
	
	private void updateScore(){
		scoreString = "";
		for(char aux : String.valueOf(puntos.getTotalScore()).toCharArray()){
			scoreString += aux + "\n";
		}
		ScoreLabel.setText(scoreText);
		PointsLabel.setText(scoreString);
	}

	@Override
	public void onStoppedBlock() {
		updateScore();
		preview();		
	}

	public Stage getStage(){
		return stage;
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
	

}
