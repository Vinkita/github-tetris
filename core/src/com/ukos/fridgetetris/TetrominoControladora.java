package com.ukos.fridgetetris;

import java.util.HashMap;
import java.util.Map;

import com.ukos.logics.Board;

public class TetrominoControladora {
	
	private Board tablero;
	
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

}
