package com.areoplane.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.areoplane.game.AreoPlaneGame;


public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "AreoPlane Chess";
		config.width = 640;
		config.height = 480;
		config.resizable = false;
		new LwjglApplication(new AreoPlaneGame(), config);
	}
}
