package com.ukos.fridgetetris;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.ukos.logics.Board;

/**
 * Controla los movimientos de la pieza cayendo segun los controles ingresados por el usuario.
 * @author Ukos
 *
 */
public class TetrominoController {
	
	private Board tablero;
	private int horizIters = 0;
	private float horizCurPos = 0;
	
	public TetrominoController(Board tablero) {
		this.tablero = tablero;
	}	

	enum Keys {
		LEFT, RIGHT, DOWN, UP
	}
	
	/**
	 * En este mapa se guarda el estado de cada control (si es {@code true} significa que 
	 * esta siendo presionado)
	 */
	static Map<Keys, Boolean> keys = new HashMap<Keys, Boolean>();
	static {
		keys.put(Keys.LEFT, false);
		keys.put(Keys.RIGHT, false);
		keys.put(Keys.DOWN, false);
		keys.put(Keys.UP, false);
	}

	/** Cambia el estado de la tecla "izquierda" a {@code true} (presionado) */
	public void leftPressed() {
		keys.put(Keys.LEFT, true);
	}
	/** Cambia el estado de la tecla "derecha" a {@code true} (presionado) */
	public void rightPressed() {
		keys.put(Keys.RIGHT, true);
	}
	/** Cambia el estado de la tecla "abajo" a {@code true} (presionado) */
	public void downPressed() {
		keys.put(Keys.DOWN, true);
	}
	/** Cambia el estado de la tecla "arriba" a {@code true} (presionado) */
	public void upPressed() {
		keys.put(Keys.UP, true);
	}
	/** Cambia el estado de la tecla "izquierda" a {@code false} (no presionado) */
	public void leftReleased() {
		keys.put(Keys.LEFT, false);
	}
	/** Cambia el estado de la tecla "derecha" a {@code false} (no presionado) */
	public void rightReleased() {
		keys.put(Keys.RIGHT, false);
	}
	/** Cambia el estado de la tecla "abajo" a {@code false} (no presionado) */
	public void downReleased() {
		keys.put(Keys.DOWN, false);
	}
	/** Cambia el estado de la tecla "arriba" a {@code false} (no presionado) */
	public void upReleased() {
		keys.put(Keys.UP, false);
	}
	
	/**
	 * Equivale al metodo {@link #upPressed()}.
	 */
	public void tap(){
		upPressed();
	}
	
	/**
	 * Mueve la pieza cayendo hacia la izquierda o derecha dependiendo del valor de {@code deltaX}.
	 * <br>La cantidad de espacios que se mueva la pieza dependera de la relacion entre {@code deltaX} y {@code ppm}
	 * @param deltaX
	 * @param ppm
	 */
	public void pan(float deltaX, int ppm) {
		horizCurPos += deltaX;				
		horizIters = (int) Math.copySign(Math.floor(Math.abs(horizCurPos) / ppm), horizCurPos);
		if(horizIters != 0){
			for (int i = 0; i < horizIters; i++)			
				tablero.testMovePieceToRight();	
			for (int i = 0; i > horizIters; i--)
				tablero.testMovePieceToLeft();
			horizCurPos = (horizCurPos % horizIters);
			horizIters = 0;
		}
		
	}
	
	/** Recalcula las entradas actuales. **/
	public void update(float delta) {
		processInput();
	}

	/** Cambia el estado del tablero segun las teclas presionadas. **/
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
			upReleased();
		}
	}
	/**
	 * Equivale a {@link #touchDown(float, float, int, Vector2) touchDown()},
	 * siempre y cuando el parametro {@code y} este debajo de cierto limite. 
	 * @param x
	 * @param y
	 * @param ppm pixeles por metro
	 * @param offset
	 * @return
	 */
	public boolean touchDown(float x, float y, int ppm, Vector2 offset) {
		if((offset.y + y) / ppm <=  4){
			downPressed();
			return true;
		}
		return false;
		
	}
	

}
