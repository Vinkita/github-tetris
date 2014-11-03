package com.ukos.fridgetetris;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.ukos.logics.Board;

public class TetrominoControladora {
	
	private Board tablero;
	private int horizIters = 0;
	private float horizPrevPos = 0;
	private float horizCurPos = 0;
	
	public TetrominoControladora(Board tablero) {
		this.tablero = tablero;
	}	

	enum Keys {
		LEFT, RIGHT, DOWN, UP
	}
	
	static Map<Keys, Boolean> keys = new HashMap<Keys, Boolean>();
	static {
		keys.put(Keys.LEFT, false);
		keys.put(Keys.RIGHT, false);
		keys.put(Keys.DOWN, false);
		keys.put(Keys.UP, false);
	}

	public void leftPressed() {
		keys.put(Keys.LEFT, true);
	}
	public void rightPressed() {
		keys.put(Keys.RIGHT, true);
	}
	public void downPressed() {
		keys.put(Keys.DOWN, true);
	}
	public void upPressed() {
		keys.put(Keys.UP, true);
	}
	public void leftReleased() {
		keys.put(Keys.LEFT, false);
	}
	public void rightReleased() {
		keys.put(Keys.RIGHT, false);
	}
	public void downReleased() {
		keys.put(Keys.DOWN, false);
	}
	public void upReleased() {
		keys.put(Keys.UP, false);
	}
	
	public void tap(){
		upPressed();
	}
	
	public void pan(float deltaX, int ppm) {
		horizPrevPos = horizCurPos;
		horizCurPos += deltaX;				
//		horizIters = (int) Math.floor(horizCurPos / ppm);
		horizIters = (int) Math.copySign(Math.floor(Math.abs(horizCurPos) / ppm), horizCurPos);
		if(horizIters != 0){
			for (int i = 0; i < horizIters; i++)
				tablero.movePieceToRight();
			for (int i = 0; i > horizIters; i--)
				tablero.movePieceToLeft();
			horizCurPos = (horizCurPos % horizIters);
			horizIters = 0;
		}
		
	}
	
//	public void pan2(float x, Vector2 offset, int ppm) {
//		horizPrevPos = horizCurPos;
//		horizCurPos = (int) (x/ppm) + offset.x;
//		if(horizCurPos < 0)
//			horizCurPos = 0;
//		else if(horizCurPos > tablero.getWidth())
//			horizCurPos = tablero.getWidth();
//		if(horizCurPos > tablero.getFallingPiece().getX()){			
//			while(horizCurPos > tablero.getFallingPiece().getX()){
//				tablero.movePieceToRight();
////				horizPrevPos++;
//			}
//		} else if (horizCurPos < tablero.getFallingPiece().getX()){
//			while(horizCurPos < tablero.getFallingPiece().getX()){
//				tablero.movePieceToLeft();
////				horizPrevPos--;
//			}			
//		}		
//	}
//
	
	public void pan3(float x, Vector2 offset, int ppm){
		int pos = (int) (x/ppm + offset.x);
		tablero.slideToPoint(pos);
	}
	
	/** The main update method. It recalculates the actual inputs. **/
	public void update(float delta) {
		processInput();
	}

	/** Change Board's state and parameters based on input controls **/
	private void processInput() {
		if (keys.get(Keys.LEFT)) {
			tablero.movePieceToLeft();
		}
		if (keys.get(Keys.RIGHT)) {
			tablero.movePieceToRight();
		}
		if (keys.get(Keys.DOWN)) {
			tablero.movePieceDown();
		}
		if (keys.get(Keys.UP)) {
			tablero.rotatePieceRight();
			//Only do one rotation per press:
			upReleased();
		}
	}
	public boolean touchDown(float x, float y, int ppm) {
		if(y / ppm <=  2){
			downPressed();
			return true;
		}
		return false;
		
	}
	

}
