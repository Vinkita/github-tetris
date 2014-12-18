/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ukos.logics;

import java.util.LinkedHashMap;

import com.badlogic.gdx.utils.Array;

/**
 * Se encarga de instanciar las RotatablePiece correspondientes a las 7 formas posibles de un Tetromino.
 * @author Ukos
 */
public class Tetromino {
	
	// Nuevas configuraciones para Tetris Libgdx
	
	/**
	 * Contiene los nombres de los 7 Tetrominos
	 */
	public static enum shape {T,I,O,S,Z,L,J};
	
	/**
	 * Contiene una serie de arreglos que a su vez contienen los 
	 * codigos de textura de cada rotacion de los Tetrominos
	 */
	public static LinkedHashMap<String, String[]> colors;
	
	// Bloque de inicializacion estática que se encarga de generar loc codigos de textura e insertarlos en colors
	static{
		
		colors = new LinkedHashMap<String, String[]>();
		
		for(shape forma : shape.values()) {
			String type = forma.name();
			for (int i = 0; i <4; i++) {
				String key = type + i;
				String[] values = new String[4]; 
				for(int j = 1; j <= 4; j++) {
					values[j-1] = key + j;
				}
				colors.put(key, values);
			}
			
		}
			
	}	
	
	
	// Fin nuevas configuraciones
    
    /**
     * Tetromino "T"
     */
    public static final RotatablePiece T_SHAPE = new RotatablePiece(4, 0, new Array<BlockDrawable>(
    																		new BlockDrawable[] {
					    														new BlockDrawable(new Point(0,1), "T01"),
					    														new BlockDrawable(new Point(-1,0), "T02"),
					    														new BlockDrawable(new Point(0,0), "T03"),
					    														new BlockDrawable(new Point(1,0), "T04")
    
    																		}), "T");
    /**
     * Tetromino "I"
     */
    public static final RotatablePiece I_SHAPE = new RotatablePiece(4, 0, new Array<BlockDrawable>(
																			new BlockDrawable[] {
																					new BlockDrawable(new Point(-2,0), "I01"),
																					new BlockDrawable(new Point(-1,0), "I02"),
																					new BlockDrawable(new Point(0,0), "I03"),
																					new BlockDrawable(new Point(1,0), "I04"),
																				}), "I");
    /**
     * Tetromino "O"
     */
    public static final RotatablePiece O_SHAPE = new RotatablePiece(1, 0, new Array<BlockDrawable>(
																			new BlockDrawable[] {
																					new BlockDrawable(new Point(0,1), "O01"),
																					new BlockDrawable(new Point(1,1), "O02"),
																					new BlockDrawable(new Point(0,0), "O03"),
																					new BlockDrawable(new Point(1,0), "O04"),
																				}), "O");
    /**
     * Tetromino "S"
     */
    public static final RotatablePiece S_SHAPE = new RotatablePiece(4, 0, new Array<BlockDrawable>(
																			new BlockDrawable[] {
																					new BlockDrawable(new Point(0,0), "S01"),
																					new BlockDrawable(new Point(1,0), "S02"),
																					new BlockDrawable(new Point(-1,-1), "S03"),
																					new BlockDrawable(new Point(0,-1), "S04"),
																				}), "S");
    /**
     * Tetromino "Z"
     */
    public static final RotatablePiece Z_SHAPE = new RotatablePiece(4, 0, new Array<BlockDrawable>(
																			new BlockDrawable[] {
																					new BlockDrawable(new Point(-1,0), "Z01"),
																					new BlockDrawable(new Point(0,0), "Z02"),
																					new BlockDrawable(new Point(0,-1), "Z03"),
																					new BlockDrawable(new Point(1,-1), "Z04"),
																				}), "Z");
    /**
     * Tetromino "L"
     */
    public static final RotatablePiece L_SHAPE = new RotatablePiece(4, 0, new Array<BlockDrawable>(
																			new BlockDrawable[] {
																					new BlockDrawable(new Point(1,1), "L01"),
																					new BlockDrawable(new Point(-1,0), "L02"),
																					new BlockDrawable(new Point(0,0), "L03"),
																					new BlockDrawable(new Point(1,0), "L04"),
																				}), "L");
    /**
     * Tetromino "J"
     */
    public static final RotatablePiece J_SHAPE = new RotatablePiece(4, 0, new Array<BlockDrawable>(
																			new BlockDrawable[] {
																					new BlockDrawable(new Point(-1,1), "J01"),
																					new BlockDrawable(new Point(-1,0), "J02"),
																					new BlockDrawable(new Point(0,0), "J03"),
																					new BlockDrawable(new Point(1,0), "J04"),
																				}), "J");
    private Tetromino(){
    }
    
    public static Array<RotatablePiece> allPieces(){
    	Array<RotatablePiece> arraux = new Array<RotatablePiece>();
    	arraux.add(I_SHAPE);    	
    	arraux.add(J_SHAPE);    	
    	arraux.add(L_SHAPE);  	
    	arraux.add(O_SHAPE);  	
    	arraux.add(S_SHAPE);  	
    	arraux.add(T_SHAPE);  	
    	arraux.add(Z_SHAPE);
    	return arraux;
    }
       
}
