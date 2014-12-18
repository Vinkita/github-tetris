package com.ukos.fridgetetris;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.ukos.screens.MainMenu;

/**
 * Provee un punto de control central sobre la reproduccion de sonido del juego.
 * @author Ukos
 */
public class AudioManager {
	
	/**
	 * La unica instancia de esta clase. 
	 * Al ser declarada public y static puede ser accedida desde cualquier parte del codigo.
	 *   
	 */
	public static final AudioManager instance = new AudioManager();
	private Music playingMusic;
	
	/**
	 * El constructor es privado para evitar instanciacion por parte de otras clases y
	 * asegurar que solo exista una instancia de esta clase (Singleton).
	 */
	private AudioManager(){}
	
	/**
	 * Reproduce un sonido ({@link Sound}).
	 * @param sound el sonido a reproducir.
	 * @see Sound#play(float, float, float)
	 */
	public void play(Sound sound){
		play(sound, 1);
	}	
	
	/**
	 * Reproduce un sonido ({@link Sound}).
	 * @param sound el sonido a reproducir.
	 * @param volume el volumen (rango [0,1])
	 * @see Sound#play(float, float, float)
	 */
	public void play(Sound sound, float volume){
		play(sound, volume, 1);
	}	
	
	/**
	 * Reproduce un sonido ({@link Sound}).
	 * @param sound el sonido a reproducir.
	 * @param volume el volumen (rango [0,1])
	 * @param pitch el multiplicador de tono(rango [0.5,2.0])
	 * @see Sound#play(float, float, float)
	 */
	public void play(Sound sound, float volume, float pitch){
		play(sound, volume, pitch, 0);
	}
	
	/**
	 * Reproduce un sonido ({@link Sound}).
	 * @param sound el sonido a reproducir.
	 * @param volume el volumen (rango [0,1])
	 * @param pitch el multiplicador de tono(rango [0.5,2.0])
	 * @param pan el balance de audio
	 * @see Sound#play(float, float, float)
	 */
	public void play(Sound sound, float volume, float pitch, float pan){
		if (!GamePreferences.instance.sound) 
			return;
		sound.play(GamePreferences.instance.soundVolume * volume, pitch, pan);
	}
	
	/**
	 * Si {@link GamePreferences#music music} es {@code true}, reproduce musica.
	 * <li>Si ya se esta reproduciendo una instancia diferente de {@code Music}, esta es detenida 
	 * primero.
	 * @param music la musica a ser reproducida.
	 */
	public void play(Music music){
		if (music != playingMusic){
			stopMusic();
			playingMusic = music;
		}
		if (GamePreferences.instance.music){
			music.setLooping(true);
			music.setVolume(GamePreferences.instance.musicVolume);
			music.play();
		}
	}
	
	/**
	 * Pausa una instancia de {@link Music}
	 * @param music la musica a ser pausada
	 */
	public void pause(){
		if (playingMusic != null)
			playingMusic.pause();
	}

	/**
	 * Si {@link #playingMusic} es distinto a <code>null</code>, su reproduccion es detenida.
	 */
	public void stopMusic() {
		 if (playingMusic != null)
			 playingMusic.stop();		
	}
	
	/**
	 * Setea el nuevo volumen de {@code #playingMusic}. Es llamado por {@code MainMenu} cuando cambia la 
	 * configuracion del juego.
	 */
	public void onSettingsUpdated(){
		if (playingMusic == null)
			return;
		playingMusic.setVolume(GamePreferences.instance.musicVolume);
		if(GamePreferences.instance.music){
			if(!playingMusic.isPlaying())
				playingMusic.play();
			else {
				playingMusic.pause();
				playingMusic.play();
			}
		}
		else
			playingMusic.stop();;
	}

}
