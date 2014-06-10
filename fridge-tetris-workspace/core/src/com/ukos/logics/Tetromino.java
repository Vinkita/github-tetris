/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ukos.logics;

import java.util.LinkedHashMap;

import com.badlogic.gdx.utils.Array;

/**
 *
 * @author Ukita
 */
public class Tetromino {
	
	// Nuevas configuraciones para Tetris Libgdx
	
	public static enum shape {T,I,O,S,Z,L,J};
	
	public static LinkedHashMap<String, String[]> colors;
	
	static{
		
		colors = new LinkedHashMap<String, String[]>();
		
		for(shape forma : shape.values()) {
			String type = forma.name();
			for (int i = 0; i <4; i++) {
				String key = type + i;
				String[] values = new String[4]; 
				for(int j = 1; j <= 4; j++) {
					values[j-1] = key + j + ".png";
				}
				colors.put(key, values);
			}
			
		}
		
//		Usamos este for para ver que se carguen bien los nombres de las texturas
//		for (Iterator<String[]> aux = colors.values().iterator(); aux.hasNext();) {
//			for (String aux2 : aux.next())
//				System.out.println(aux2);
//		}
			
	}	
	
	
	// Fin
    
    public static final RotatablePiece T_SHAPE = new RotatablePiece(4, 0, new Array<BlockDrawable>(
    																		new BlockDrawable[] {
					    														new BlockDrawable(new Point(0,1), "T01.png"),
					    														new BlockDrawable(new Point(-1,0), "T02.png"),
					    														new BlockDrawable(new Point(0,0), "T03.png"),
					    														new BlockDrawable(new Point(1,0), "T04.png")
    																		}), "T");
    public static final RotatablePiece I_SHAPE = new RotatablePiece(4, 0, new Array<BlockDrawable>(
																			new BlockDrawable[] {
																					new BlockDrawable(new Point(-2,0), "I01.png"),
																					new BlockDrawable(new Point(-1,0), "I02.png"),
																					new BlockDrawable(new Point(0,0), "I03.png"),
																					new BlockDrawable(new Point(1,0), "I04.png"),
																				}), "I");
    public static final RotatablePiece O_SHAPE = new RotatablePiece(1, 0, new Array<BlockDrawable>(
																			new BlockDrawable[] {
																					new BlockDrawable(new Point(0,1), "O01.png"),
																					new BlockDrawable(new Point(1,1), "O02.png"),
																					new BlockDrawable(new Point(0,0), "O03.png"),
																					new BlockDrawable(new Point(1,0), "O04.png"),
																				}), "O");
    public static final RotatablePiece S_SHAPE = new RotatablePiece(4, 0, new Array<BlockDrawable>(
																			new BlockDrawable[] {
																					new BlockDrawable(new Point(0,0), "S01.png"),
																					new BlockDrawable(new Point(1,0), "S02.png"),
																					new BlockDrawable(new Point(-1,-1), "S03.png"),
																					new BlockDrawable(new Point(0,-1), "S04.png"),
																				}), "S");
    public static final RotatablePiece Z_SHAPE = new RotatablePiece(4, 0, new Array<BlockDrawable>(
																			new BlockDrawable[] {
																					new BlockDrawable(new Point(-1,0), "Z01.png"),
																					new BlockDrawable(new Point(0,0), "Z02.png"),
																					new BlockDrawable(new Point(0,-1), "Z03.png"),
																					new BlockDrawable(new Point(1,-1), "Z04.png"),
																				}), "Z");
    public static final RotatablePiece L_SHAPE = new RotatablePiece(4, 0, new Array<BlockDrawable>(
																			new BlockDrawable[] {
																					new BlockDrawable(new Point(1,1), "L01.png"),
																					new BlockDrawable(new Point(-1,0), "L02.png"),
																					new BlockDrawable(new Point(0,0), "L03.png"),
																					new BlockDrawable(new Point(1,0), "L04.png"),
																				}), "L");
    public static final RotatablePiece J_SHAPE = new RotatablePiece(4, 0, new Array<BlockDrawable>(
																			new BlockDrawable[] {
																					new BlockDrawable(new Point(-1,1), "J01.png"),
																					new BlockDrawable(new Point(-1,0), "J02.png"),
																					new BlockDrawable(new Point(0,0), "J03.png"),
																					new BlockDrawable(new Point(1,0), "J04.png"),
																				}), "J");
    private Tetromino(){
    }
    
//    public Tetromino(int maxRots, int curRotation, String shape) {
//        this(maxRots, curRotation);
////        this.shape = this.initialRotation(curRotation, shape);
//        Piece piece = firstRotation(new Piece(shape), curRotation);
//        this.rotations = fillRotations(piece, maxRots);
//    }
//    
//    private Piece firstRotation(Piece piece, int curRotation){
//        for(int i = 0; i < curRotation; i++){
//            piece = piece.rotateRight();
//        }
//        return piece;
//    }
//    
//    private Piece[] fillRotations(Piece piece, int maxRots){
//        Piece[] arr = new Piece[maxRots];
//        arr[0] = piece;
//        for(int i = 1; i < maxRots; i++){
//            arr[i] = arr[i - 1].rotateRight();
//        }
//        return arr;
//    }
//    
//    private Tetromino(int curRotation, Grid[] rotations) {
//        this(rotations.length, curRotation);
//        this.rotations = rotations;
//    }
//    
//    private Tetromino(int maxRots, int curRotation) {
//        if(curRotation >= maxRots)
//            curRotation = 0;
//        else if (curRotation < 0)
//            curRotation = maxRots - 1;
//        this.curRotation = curRotation;
//    }
//    
//    @Override
//    public Tetromino rotateRight(){
//        return new Tetromino(curRotation + 1, rotations);
//    }
//    
//    @Override
//    public Tetromino rotateLeft(){
//        return new Tetromino(curRotation - 1, rotations);
//    }
//    
//    private Grid myGrid(){
//        return rotations[curRotation];
//    }
//
//    @Override
//    public int rows() {
//        return myGrid().rows();
//    }
//
//    @Override
//    public int cols() {
//        return myGrid().cols();
//    }
//
//    @Override
//    public char cellAt(Point punto) {
//        return myGrid().cellAt(punto);
//    }
//
//    @Override
//    public String toString() {
//        return GridStatics.toString(this);
//    }
//    
//    @Override
//    public boolean equals(Object o){
//        return this.toString().equals(o.toString());
//    }
//       
}
