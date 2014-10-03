package com.ukos.logics;

/**
 * Una clase puede implementar la interfaz IStopBlockListener para ser notificada cuando 
 * se detiene una pieza en un objeto Board.
 * @author Ukos 
 */
public interface IStopBlockListener {
	/**
	 * Este metodo es llamado cuando se remueven se detiene una pieza en un objeto <tt>Board</tt>.
	 */
	void onStoppedBlock();
}
