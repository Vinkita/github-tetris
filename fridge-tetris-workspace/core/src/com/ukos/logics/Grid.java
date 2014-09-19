/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ukos.logics;

/**
 * Clase que contiene el comportamiento comun a los objetos de tipo "Grilla"
 * @author Ukos
 */
public interface Grid extends Cloneable{
	
	public static String EMPTY = ".";
    	
    /**
     * Devuelve el estilo de la celda que se encuentra en el Punto especificado
     * @param punto las coordenadas de la celda?
     * @return el estilo de la celda que se encuentra en el punto
     */
    String cellAt(Point punto);
}
