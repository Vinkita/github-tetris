package com.ukos.logics;

/**
 * Se encarga de cambiar la velocidad del juego segun el nivel actual.
 * @author Ukos
 *
 */
public class LevelHelper {

	/**
	 * Setea la velocidad de caida de las piezas según el nivel actual
	 * @param level  el nivel actual del juego
	 * @param board  el Board donde se desarrolla el juego
	 */
	public static void setLevelSpeed(int level, Board board){		
		long af = rateToNanoSeconds(afRateInFrames(level));
		board.setAutoFallRate(af);
		board.setMoveRate(af/5);
	}
	
	/**
	 * Calcula la velocidad de caida, en frames,  de las piezas segun el nivel 
	 * @param level  el nivel actual
	 * @return  la velocidad en frames (1 metro cada "X" frames)
	 */
	private static double afRateInFrames(int level){
		double m;
		double b;
		if(level <= 8){
			m = -(double)11/4;
			b = 30;
		} else if (level <= 10){
			m = -(double)3/2;
			b = 20;
			
		} else if (level <= 19){
			m = -(double)1/3;
			b = (double)25/3;
		} else if (level <= 28){
			m = -(double)1/10;
			b = (double)39/10;
		} else {
			m = 0;
			b = 1;
		}
		return (m * level) + b;
	}
	
	/**
	 * Convierte la velocidad en frames a la tasa de caida en nanosegundos
	 * @param frames  velocidad en frames
	 * @return  nanosegundos que debe tardar la pieza en caer un metro
	 */
	private static long rateToNanoSeconds(double frames){
		return (long) (frames / 30 * 1000000000);
	}
	
	/**
	 * Utilizada para verificar que las velocidades de cada nivel sean correctas
	 */
	public static void test(){
		for (int i = 0; i < 31; i++){
			double af = afRateInFrames(i);
			System.out.println("l " + i + ": " + af + " frames, " + rateToNanoSeconds(af) + " nanos");
		}
	}

}
