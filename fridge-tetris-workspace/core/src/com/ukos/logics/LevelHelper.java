package com.ukos.logics;

public class LevelHelper {
//	private int level;
//	private long lv1autoFallRate = 1000000000;
//	private long lv1moveRate     = 200000000;

	public static void setLevelSpeed(int level, Board board){		
		long af = rateToNanoSeconds(afRateInFrames(level));
		board.setAutoFallRate(af);
		board.setMoveRate(af/5);
	}
	
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
	
	private static long rateToNanoSeconds(double frames){
		return (long) (frames / 30 * 1000000000);
	}
	
	public static void test(){
		for (int i = 0; i < 31; i++){
			double af = afRateInFrames(i);
			System.out.println("l " + i + ": " + af + " frames, " + rateToNanoSeconds(af) + " nanos");
		}
	}

}
