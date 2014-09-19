/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ukos.logics;

import com.badlogic.gdx.utils.Array;

/**
 * Contiene el comportamiento comun a los objetos de tipo "Rotatable"	
 * @author Ukos
 */
public interface RotatableGrid extends Grid {
    
    /**
     * Devuelve un objeto de tipo RotatableGrid rotado a la derecha
     * @return el objeto rotado 
     */
    RotatableGrid rotateRight();
    
    /**
     * Devuelve un objeto de tipo RotatableGrid rotado a la izquierda
     * @return el objeto rotado 
     */
    RotatableGrid rotateLeft();
    
    /**
     * Crea un arreglo que contiene los puntos pertenecientes a la pieza 
     * @return un arreglo de Punto
     */
    Array<Point> getPoints();
    
    /**
     * Crea un arreglo que contiene los bloques que componen la pieza
     * @return un arreglo de BlockDrawable
     */
    Array<BlockDrawable> allBlocks();

	Object clone() throws CloneNotSupportedException;

}
