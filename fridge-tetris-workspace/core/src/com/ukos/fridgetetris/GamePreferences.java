package com.ukos.fridgetetris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;

/**
 * Se encarga de cargar y de guardar la configuracion del juego.
 * @author Ukos
 */
public class GamePreferences {
	/** El nombre de las preferencias de esta aplicacion. */
	public static final String TAG = GamePreferences.class.getName();
	
	/**
	 * Unica instancia de {@code GamePreferences}. 
	 * Al ser declarada public y static puede ser accedida desde cualquier parte del codigo.
	 */
	public static final GamePreferences instance = new GamePreferences();
	
	/** Indica si la pieza fantasma esta activada. */
	public boolean ghost;
	/** Indica el numero de piezas mostradas por la vista previa. */
	public int previews;
	/** Indica si los sonidos estan activados. */
	public boolean sound;
	/** Indica si la musica esta activada. */
	public boolean music;
	/** Indica el volumen de los sonidos. */
	public float soundVolume;
	/** Indica el volumen de la musica. */
	public float musicVolume;
	/** Indica si las "High Scores" estan activados. */
	public boolean highscores;
	/** Indica si debe cifrarse el archivo de puntuaciones */
	public boolean encrypt;
	/** Indica el archivo de puntuaciones se encuentra cifrado */
	public boolean scoresEncrypted;
	
	/**
	 * La instancia de {@link Preferences} de esta aplicacion.
	 * Es utilizada para persistir la configuracion.
	 */
	private Preferences prefs;

	
	/** Las posibles cantidades de fichas a mostrar en la vista previa */ 
	public static Integer[] PreviewNumbers = 
			new Integer[]{ 0,1,2,3 };
	
	/**
	 * Recupera la instancia de {@link GamePreferences} de la aplicacion.
	 * El constructor es privado para evitar instanciacion por parte de otras clases y
	 * asegurar que solo exista una instancia de esta clase (Singleton).
	 */
	private GamePreferences(){
		prefs = Gdx.app.getPreferences(TAG);
	}
	//bloque de inicializacion estatico, asegura que las configuraciones hayan sido cargadas
	static{
		instance.load();		
	}
	
	/**
	 * Setea el estado de <code>this</code> a partir de los valores guardados en {@link #prefs}.
	 * <br>Se proveen valores por defecto para aquellos que no se encuentren.
	 */
	public void load(){
		ghost = prefs.getBoolean("ghost", true);
		previews = MathUtils.clamp(prefs.getInteger("previews", 1), 0, PreviewNumbers[PreviewNumbers.length - 1]);
		sound = prefs.getBoolean("sound", true);
		music = prefs.getBoolean("music", true);
		soundVolume = MathUtils.clamp(prefs.getFloat("soundVolume", 1), 0, 1);
		musicVolume = MathUtils.clamp(prefs.getFloat("musicVolume", 1), 0, 1);
		highscores = prefs.getBoolean("highscores", true);
		encrypt = prefs.getBoolean("encrypt", true);
		scoresEncrypted = prefs.getBoolean("scoresEncrypted", false);
	}
	
	/**
	 * Toma los valores actuales de {@code this} y los guarda en {@link #prefs}.
	 */
	public void save(){
		prefs.putBoolean("ghost", ghost);
		prefs.putInteger("previews", previews);
		prefs.putBoolean("sound", sound);
		prefs.putBoolean("music", music);
		prefs.putFloat("soundVolume", soundVolume);
		prefs.putFloat("musicVolume", musicVolume);
		prefs.putBoolean("highscores", highscores);
		prefs.putBoolean("encrypt", encrypt);
		prefs.putBoolean("scoresEncrypted", scoresEncrypted);
		prefs.flush();
	}
}
