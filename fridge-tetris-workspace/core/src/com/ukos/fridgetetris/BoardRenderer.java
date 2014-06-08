package com.ukos.fridgetetris;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ArrayMap;
import com.ukos.logics.BlockDrawable;
import com.ukos.logics.Board;
import com.ukos.logics.Tetromino;

public class BoardRenderer {

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Rectangle[][] gridRectangles;
	private ArrayMap<String, Texture> textures;
	private Board tablero;
	
	public BoardRenderer(Board tablero) {
		camera = new OrthographicCamera();
		//The base unit for screen dimensions is equal to one tetromino unit
		//The playfield is 10 wide x 20 high.
		//The playfield is centered on the screen with 1 unit blank all around.
		//So, final camera perspective is is 12 wide x 20 high
		camera.setToOrtho(false, 12, 22);
		
		batch = new SpriteBatch();
		this.tablero = tablero;
		gridRectangles = createGridRectangles();
		
		textures = new ArrayMap<String, Texture>();
		for(Iterator<String[]> it = Tetromino.colors.values().iterator(); it.hasNext() ;) {
			String[] arr = it.next();
			for(int i = 0, j = arr.length; i < j; i++) {
				textures.put(arr[i], new Texture(Gdx.files.internal("data/tetrominos/" + arr[i] )));
			}
		}
	}

	public void render(float delta) {
		camera.update();
      
		//render by ortho camera coordinates:
		batch.setProjectionMatrix(camera.combined);
		
		//Draw current grid:
		batch.begin();
		for (BlockDrawable block : tablero.getBlocksToDraw()) {
			System.out.println(block.getStyle());
			Rectangle rect = gridRectangles[(int) block.getPoint().Y()][(int) block.getPoint().X()];
			batch.draw(textures.get(block.getStyle()), rect.x, rect.y, rect.width, rect.height);
		}
		batch.end();
		
	}
	
	private Rectangle[][] createGridRectangles() {
		Rectangle[][] rects = new Rectangle[20][10];
		//Create 20 rows of 10 rectangles:
		for (int row=0; row<20; row++) {
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

}
