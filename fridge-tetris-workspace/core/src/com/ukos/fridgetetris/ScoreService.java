package com.ukos.fridgetetris;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.ukos.fridgetetris.HighScores.HighScore;

/**
 * Se encarga de cargar y de guardar los puntajes. 
 * @author Ukos
 */
public class ScoreService {
	/** Nombre del archivo donde se persisten los HighScore */
	private static final String PROFILE_DATA_FILE = "scores-v1.json";
	/** Contiene una serie de {@link HighScore} */
	private static HighScores scores;
	/** Indica si los datos a persistir deben ser cifrados */
	static boolean encrypt = GamePreferences.instance.encrypt;
	/** Indica si los datos en archivo estan cifrados */
	static boolean encrypted = GamePreferences.instance.scoresEncrypted;

	/**
	 * Recupera y lee el archivo donde se guardan las puntuaciones. 
	 * La informacion obtenida es luego convertida de formato JSON a un objeto {@code HighScores}.
	 * @return 
	 */
	public static HighScores retrieveScores() {
		if (scores != null)
			return scores;

		FileHandle profileDataFile = getFileHandle();
		Json json = new Json();
		if (profileDataFile.exists()) {
			try {
				String scoresAsText = profileDataFile.readString();
				if(encrypted){
					scoresAsText = Base64Coder.decodeString(scoresAsText);					
				}
				scores = json.fromJson(HighScores.class, scoresAsText);
			} catch (Exception e) {
				Gdx.app.error("ERROR",
						"Unable to parse existing data file", e);

				scores = new HighScores();
				persist(scores);
			}
		} else {
			scores = new HighScores();
			persist(scores);
		}
		return scores;
	}

	/**
	 * Convierte {@code scores} a un String en formato JSON, lo codifica en formato Base64, y lo guarda en un archivo.
	 * @param scores la instancia de {@code HighScores} a ser persistida.
	 */
	protected static void persist(HighScores scores) {
		Json json = new Json();
		FileHandle profileDataFile = getFileHandle();
		String scoresAsText = json.prettyPrint(scores);
		if(encrypt){
			scoresAsText = Base64Coder.encodeString(scoresAsText);
			if(!encrypted){
				GamePreferences.instance.scoresEncrypted = encrypted = true;
				GamePreferences.instance.save();
			}
		} else {
			if(encrypted){
				GamePreferences.instance.scoresEncrypted = encrypted = false;
				GamePreferences.instance.save();				
			}
		}
		try {
			profileDataFile.writeString(scoresAsText, false);			
		} catch (Exception e) {
			Gdx.app.error("ERROR",
					"Unable to persist data file", e);
		}
	}
	
	/** @see #persist(HighScores) */
	public static void persist() {
		if (scores != null) {
			persist(scores);
		}
	}
	
	/**
	 * @return la instancia de {@code FileHandle} apropiada segun el tipo de aplicacion. 
	 */
	private static FileHandle getFileHandle(){
		FileHandle auxHandle;
		if(Gdx.app.getType() == ApplicationType.Android)			
			auxHandle = Gdx.files.local(PROFILE_DATA_FILE);
		else
			auxHandle = Gdx.files.external(PROFILE_DATA_FILE);
		return auxHandle;
	}

}
