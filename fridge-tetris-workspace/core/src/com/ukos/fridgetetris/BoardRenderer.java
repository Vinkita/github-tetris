package com.ukos.fridgetetris;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.esotericsoftware.tablelayout.Cell;
import com.ukos.logics.BlockDrawable;
import com.ukos.logics.Board;
import com.ukos.logics.IStopBlockListener;
import com.ukos.logics.RotatablePiece;
import com.ukos.logics.ScoreCounter;
import com.ukos.logics.Tetromino;
import com.ukos.logics.Tetromino.shape;
import com.ukos.screens.PauseScreen;

public class BoardRenderer implements IStopBlockListener{

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Rectangle[][] gridRectangles;
//	private ArrayMap<String, Texture> textures;
	private Board tablero;
	
	
	
	private TextureAtlas atlas;
	private ArrayMap<String, TextureRegion> texRegions;
	private ArrayMap<String, Image> prevRegions;
	private Array<Cell<Actor>> cellPrevs;
	private int ppm = 32;
	
	private ScoreCounter puntos;
	private String scoreText;
	private Label PointsLabel;
	private Table table;
	private Table tableSide;
	private Stage stage;
	private Skin skin;
	private int prevCant = 1;
	
//	private Array<Sprite> prev = new Array<Sprite>();
	
	private int screenWidth = 12;
	private int screenHeight = 22;
	
	public BoardRenderer(Board tablero, ScoreCounter puntos) {
		camera = new OrthographicCamera();
		//The base unit for screen dimensions is equal to one tetromino unit
		//The playfield is 10 wide x 20 high.
		//The playfield is centered on the screen with 1 unit blank all around.
		//So, final camera perspective is is 12 wide x 20 high
		camera.setToOrtho(false, screenWidth, screenHeight);
		
		batch = new SpriteBatch();
		this.tablero = tablero;
		this.puntos = puntos;
		gridRectangles = createGridRectangles();
		
		setupPieceTextures();
		
		cellPrevs = new Array<Cell<Actor>>();
		
		stage = new Stage();
		skin = new Skin(Gdx.files.internal("ui/menuSkin.json"), new TextureAtlas("ui/atlas.pack"));
		PointsLabel = new Label("0", skin);	
		scoreText = "SCORE";
		PointsLabel.setFontScale(.5f);
		
//		TAble Setup
		tableSide = new Table(skin);
		tableSide.add(PointsLabel);
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
		for (BlockDrawable block : tablero.getBoardBlocksToDraw()) {
			drawBlock(block);
		}
		if(tablero.isGhostActivated()){			
			batch.setColor(1.0f,1.0f,1.0f,0.5f);
			for (BlockDrawable block : tablero.getGhostBlocksToDraw()) {
				drawBlock(block);				
//				ghostSprite.setTexture(textures.get(block.getStyle()));
//				ghostSprite.setSize(rect.width, rect.height);
//				ghostSprite.setPosition(rect.x, rect.y);
//				ghostSprite.setColor(1.0f, 1.0f, 1.0f, 0.5f);
//				ghostSprite.draw(batch);
			}
			batch.setColor(Color.WHITE);
		}
		for(BlockDrawable block : tablero.getFallingBlocksToDraw()){
			drawBlock(block);
		}

		batch.end();
		
		stage.act(delta);
		stage.draw();
	}
	
	private void drawBlock(BlockDrawable block){
		Rectangle rect = gridRectangles[(int) block.getPoint().Y()][(int) block.getPoint().X()];
//		batch.draw(textures.get(block.getStyle()), rect.x, rect.y, rect.width, rect.height);
		batch.draw(texRegions.get(block.getStyle()), rect.x, rect.y, rect.width, rect.height);
	}	
	
	private Rectangle[][] createGridRectangles() {
		Rectangle[][] rects = new Rectangle[22][10];
		//Create 20 rows of 10 rectangles:
		for (int row=0; row<22; row++) {
			for (int col=0;col<10;col++) {
				Rectangle rect = new Rectangle();
				rect.width = 1;
				rect.height = 1;
				rect.x = col+1;
				rect.y = row+1;
				rects[row][col] = rect;
			}
		}
		return rects;
	}

	public void resize(int width, int height) {
		stage.setViewport(new ExtendViewport(width, height));
		stage.getViewport().update(width, height, true);
		table.invalidateHierarchy();
	}
	
	private void setupPieceTextures(){
		atlas = new TextureAtlas("ui/tetra.pack");		
		texRegions = new ArrayMap<String, TextureRegion>();
		prevRegions = new ArrayMap<String, Image>();
		
		for(Iterator<String[]> it = Tetromino.colors.values().iterator(); it.hasNext() ;) {
			String[] arr = it.next();
			for(int i = 0, j = arr.length; i < j; i++) {
				texRegions.put(arr[i], new TextureRegion(atlas.findRegion(arr[i])));
			}
		}		
		
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
		PointsLabel.setText(scoreText + "\n" +String.valueOf(puntos.getTotalScore()));
	}

	@Override
	public void onStoppedBlock() {
		if(tablero.isGameOver())
			doEndGame();
		updateScore();
		preview();		
	}

	private void doEndGame() {
//		 Label gameOver = new Label("GAME OVER", skin);		 
//		 gameOver.setPosition(240 - gameOver.getWidth()/2, 400);
//		 stage.addActor(gameOver);
		
	}
	
	public Stage getStage(){
		return stage;
	}
	
//	private void setupSprite(Sprite sprite, BlockDrawable block){
//		sprite.setTexture(textures.get(block.getStyle()));
//		sprite.setPosition(block.getPoint().X(), block.getPoint().Y());
//	}

}
