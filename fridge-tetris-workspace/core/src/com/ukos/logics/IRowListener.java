package com.ukos.logics;

/**
 * Una clase puede implementar la interfaz IRowListener para ser notificada cuando 
 * se remueven filas en un objeto Board.
 * @author Ukos
 */
public interface IRowListener {
	/**
	 * Este metodo es llamado cuando se remueven filas en un objeto <tt>Board</tt>.
	 * @param rows  el numero de filas removidas
	 * @param boardLevel  el nivel en que se encuentra el juego
	 */
	void onRowsRemoved(int rows, int boardLevel);
}
