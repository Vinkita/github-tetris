/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ukos.logics;

/**
 * Se encarga de asignar punos segun la cantidad de filas removidas 
 * @author Ukos
 */
public class ScoreCounter implements IRowListener{
    
    /**
     * El puntaje total desde el inicio del juego
     */
    private int totalScore = 0;
    /**
     * La cantidad total de filas removidas desde el inicio del juego
     */
    private int removedRows = 0;
    /**
     * El último puntaje por filas removidas
     */
    private int lastScore = 0;
    /**
     * El nivel acual del juego
     */
    private int level;
    
    /**
     * La cantidad de puntos por remover 1 fila
     */
    public static final int POINTS_1_ROW = 40;
    /**
     * La cantidad de puntos por remover 2 filas
     */
    public static final int POINTS_2_ROWS = 100;
    /**
     * La cantidad de puntos por remover 3 filas
     */
    public static final int POINTS_3_ROWS = 300;
    /**
     * La cantidad de puntos por remover 4 filas
     */
    public static final int POINTS_4_ROWS = 1200;
    
    /**
     * Arreglo que representa el sistema de puntuación
     */
    private final int[] scoreSys = new int[]{        
                                            0,
                                            POINTS_1_ROW,
                                            POINTS_2_ROWS,
                                            POINTS_3_ROWS,
                                            POINTS_4_ROWS };
    
    /**
     * Crea un objeto ScoreCounter 
     * @param initialLevel  el nivel con el que se inicia el juego
     */
    public ScoreCounter(int initialLevel){
    	level = initialLevel;
    }
    
    /**
     * @return  la ultima puntuacion por filas obtenida
     */
    public int getLastScore(){
    	return lastScore;
    }    

    /**
     * Asigna puntos según nivel y cantidad de filas removidas.
     * Suma los puntos a la puntuacion total.
     * @see com.ukos.logics.IRowListener#onRowsRemoved(int, int)
     */
    @Override
    public void onRowsRemoved(int rows, int boardLevel) {
    	if(level < boardLevel)
    		level = boardLevel;
        removedRows += rows;
        lastScore = scoreSys[rows] * (level + 1);
        totalScore += lastScore;
    }

    /**
     * @return  la puntuacion total hasta el momento 
     */
    public int getTotalScore() {
        return totalScore;
    }

    /**
     * @return  las filas removidas hasta el momento
     */
    public int getRemovedRows() {
        return removedRows;
    }
    
    /**
     * Devuelve todos los indicadores a 0
     */
    public void reset(){
    	totalScore = 0;
        removedRows = 0;
        lastScore = 0;
        level = 0;
    }
}
