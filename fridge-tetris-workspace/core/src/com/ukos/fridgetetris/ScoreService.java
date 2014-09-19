package com.ukos.fridgetetris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;

public class ScoreService {
	private static final String PROFILE_DATA_FILE = "scores-v1.json";
	private static HighScores scores;

	public static HighScores retrieveScores() {
		if (scores != null)
			return scores;

		FileHandle profileDataFile = Gdx.files.external(PROFILE_DATA_FILE);
		Json json = new Json();
		if (profileDataFile.exists()) {
			try {
				String scoresAsText = profileDataFile.readString();
//				scoresAsText = Base64Coder.decodeString(scoresAsText);
				scores = json.fromJson(HighScores.class, scoresAsText);
			} catch (Exception e) {
				Gdx.app.error("ERROR",
						"Unable to parse existing profile data file", e);

				scores = new HighScores();
				persist(scores);
			}
		} else {
			scores = new HighScores();
			persist(scores);
		}
		return scores;
	}

	protected static void persist(HighScores scores) {
		Json json = new Json();
		FileHandle profileDataFile = Gdx.files.external(PROFILE_DATA_FILE);
		String scoresAsText = json.prettyPrint(scores);
//		scoresAsText = Base64Coder.encodeString(scoresAsText);

		profileDataFile.writeString(scoresAsText, false);
	}
	
	public static void persist() {
		if (scores != null) {
			persist(scores);
		}
	}

}
