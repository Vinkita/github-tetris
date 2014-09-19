package com.ukos.fridgetetris;

import java.util.HashMap;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;

public class HighScores implements Serializable{
	private Array<HighScore> scores;
	public int listSize;
	
	public HighScores() {
		scores = new Array<HighScore>();
		listSize = 10;
	}
		
	public void add(HighScore newScore){
		boolean flag = false;
		for (int i = 0, j = scores.size; i < j && !flag; i++){
			if(newScore.compareTo(scores.get(i)) > 0){
				flag = true;
				scores.insert(i, newScore);
			}
		}
		if(flag == false && scores.size < listSize)
			scores.add(newScore);
		if(scores.size > listSize)
			scores.removeRange(listSize - 1, scores.size - 1);		
	}
	
	public int lowestScore(){
//		return ( scores.size == 0 || scores.size < listSize ? 0 : scores.get(scores.size - 1).score );
		return ( scores.size < listSize ? 0 : scores.get(scores.size - 1).score );
	}
	
	public Array<HighScore> getList(){
		return scores;
	}

	public static class HighScore implements Comparable<HighScore>{
		public String name;
		public int score;		
		
		public HighScore(){
			name = "";
			score = 0;
		}
		
		public HighScore(String name, int score){
			this.name = name;
			this.score = score;
		}
		
		@Override
		public int compareTo(HighScore o) {
			if (score > o.score)
				return 1;
			if (score < o.score)
				return -1;
			return 0;		
		}		
	}


	@Override
	public void read(Json json, JsonValue jsonData) {
		this.scores.clear();
		this.scores.addAll(json.readValue( "highScores", Array.class, HighScore.class, jsonData ));		
	}

	@Override
	public void write(Json json) {
		 json.setElementType(HighScores.class, "scores", HighScore.class);
		 json.writeValue("highScores", scores);
		
	}
}
