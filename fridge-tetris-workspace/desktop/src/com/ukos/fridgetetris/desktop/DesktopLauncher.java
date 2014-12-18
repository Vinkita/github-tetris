package com.ukos.fridgetetris.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ukos.fridgetetris.FridgeTetris;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Tetridge";
		config.useGL30 = false;
		config.width = 480;
		config.height = 800;
		
		new LwjglApplication(new FridgeTetris(), config);
//		new LwjglApplication(new InputDemo3(), config);
	}
}
