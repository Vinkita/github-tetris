/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ukos.logics;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.badlogic.gdx.utils.Array;

/**
 * CLase que se encarga de la aleatorizacion de las piezas
 * @author Ukos
 */
public class ShuffleBag {
	/**
	 * Contiene las piezas sin aleatorizar. Se utiliza como base 
	 */
	private ArrayList<RotatablePiece> UnShuffledBag = new ArrayList<RotatablePiece>();
	/**
	 * Contiene piezas aleatorizadas
	 */
	private ArrayList<RotatablePiece> ShuffledBag = new ArrayList<RotatablePiece>();

	/**
	 * Crea una nueva ShuffleBag
	 */
	public ShuffleBag() {
		fillUnshuffled();
		refillShuffled();
	}

	/**
	 * Inicializa "UnshuffledBag"
	 */
	private void fillUnshuffled() {
		UnShuffledBag = new ArrayList<RotatablePiece>();
		UnShuffledBag.add(0, Tetromino.I_SHAPE);
		UnShuffledBag.add(1, Tetromino.O_SHAPE);
		UnShuffledBag.add(2, Tetromino.T_SHAPE);
		UnShuffledBag.add(3, Tetromino.S_SHAPE);
		UnShuffledBag.add(4, Tetromino.Z_SHAPE);
		UnShuffledBag.add(5, Tetromino.J_SHAPE);
		UnShuffledBag.add(6, Tetromino.L_SHAPE);
	}

	/**
	 * Llena "ShuffledBag" con piezas aleatorizadas
	 */
	private void refillShuffled() {
		@SuppressWarnings("unchecked")
		ArrayList<RotatablePiece> auxMap = (ArrayList<RotatablePiece>) UnShuffledBag
				.clone();

		Random rand = new Random();
		while (!auxMap.isEmpty())
			ShuffledBag.add(auxMap.remove(rand.nextInt(auxMap.size())));
	}

	/**
	 * Quita una pieza de la bolsa
	 * @return La primer pieza de la bolsa
	 */
	public RotatablePiece pullOut() {

		RotatablePiece pulledTetro = ShuffledBag.remove(0);
		if (ShuffledBag.isEmpty()) {
			refillShuffled();
		}
		return pulledTetro;
	}

	/** 
	 *  Devuelve, sin quitarla de la bolsa, la primer pieza
	 * @return La primer pieza de la bolsa
	 */
	public RotatablePiece preview() {
		return ShuffledBag.get(0);
	}

	/**
	 * Devuelve, en orden y sin quitarlas de la bolsa, las siguientes piezas de la bolsa
	 * @param cant  La cantidad requerida de piezas
	 * @return Un arreglo que contiene las proximas piezas
	 */
	public Array<RotatablePiece> preview(int cant) {
		Array<RotatablePiece> arr = new Array<RotatablePiece>();
		if (ShuffledBag.size() < cant)
			refillShuffled();
		for (int i = 0; i < cant; i++)
			arr.add(ShuffledBag.get(i));
		return arr;
	}
}
