package com.ukos.fridgetetris;

import java.util.HashMap;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;

/**
 * Contiene una lista de {@link HighScore} ordenada de mayor a menor segun el valor de {@link HighScore#score}.
 * 
 * @author Ukos
 */
public class HighScores implements Serializable{
	/** La lista de {@code HighScore} */
	private Array<HighScore> scores;
	/** La cantidad maxima de {@code HighScore} a almacenar */
	public int listSize;
	
	/** Crea un nuevo {@code HighScores} con capacidad maxima = 10 */
	public HighScores() {
		scores = new Array<HighScore>();
		listSize = 10;
	}
		
	/**
	 * Agrega, un nuevo {@code HighScore} a {@code scores}.
	 * <br>El lugar del nuevo {@code HighScore} en el Array sera determinado por el valor de {@link HighScore#score score}
	 * @param newScore
	 * @return la posicion del nuevo elemento en el arreglo, o -1 si este no pudo insertarse.
	 */
	public int add(HighScore newScore){
		boolean flag = false;
		int rank = -1;
		for (int i = 0, j = scores.size; i < j && !flag; i++){
			if(newScore.compareTo(scores.get(i)) > 0){
				flag = true;
				scores.insert(i, newScore);
				rank = i;
			}
		}
		if(flag == false && scores.size < listSize){
			scores.add(newScore);
			rank = scores.size - 1;
		}
		if(scores.size > listSize)
			scores.removeRange(listSize - 1, scores.size - 1);	
		return rank;
	}
	
	/**
	 * Devuelve la menor puntuacion obtenida, o cero si el tamaño de {@link #scores} es menor a {@link #listSize}.
	 * @return
	 */
	public int lowestScore(){
//		return ( scores.size == 0 || scores.size < listSize ? 0 : scores.get(scores.size - 1).score );
		return ( scores.size < listSize ? 0 : scores.get(scores.size - 1).score );
	}
	
	public Array<HighScore> getList(){
		return scores;
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.utils.Json.Serializable#read(com.badlogic.gdx.utils.Json, com.badlogic.gdx.utils.JsonValue)
	 */
	@Override
	public void read(Json json, JsonValue jsonData) {
		json.setElementType(HighScores.class, "scores", HighScore.class);
		 json.addClassTag("highScore", HighScore.class);
		this.scores.clear();
		this.scores.addAll(json.readValue( "highScores", Array.class, HighScore.class, jsonData ));		
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.utils.Json.Serializable#write(com.badlogic.gdx.utils.Json)
	 */
	@Override
	public void write(Json json) {
		 json.setElementType(HighScores.class, "scores", HighScore.class);
		 json.addClassTag("highScore", HighScore.class);
		 json.writeValue("highScores", scores);
		
	}
	/**
	 * Representa una puntuacion obtenida por un jugador.
	 * @author Ukos
	 */
	public static class HighScore implements Comparable<HighScore>{
		/** El nombre del jugador. */
		public String name;
		/** La puntuacion del jugador */
		public int score;		
		
		/** Crea un nuevo {@code HighScore} */
		public HighScore(){
			name = "";
			score = 0;
		}
		
		/**
		 * Crea un nuevo {@code HighScore} con el nombre y la puntuacion especificados.
		 * @param name el nombre
		 * @param score la puntuacion
		 */
		public HighScore(String name, int score){
			this.name = name;
			this.score = score;
		}
		
		/**
		 * Compara este {@code HighScore} con otro, basandose en el valor de la variable {@code #score}.
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(HighScore o) {
			if (score > o.score)
				return 1;
			if (score < o.score)
				return -1;
			return 0;		
		}		
	}
	
}
