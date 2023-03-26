package com.dungeonEscape.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.dungeonEscape.DungeonEscape;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new DungeonEscape(), config);
		config.width = DungeonEscape.SCR_WIDTH;
		config.height = DungeonEscape.SCR_HEIGHT;
		//config.width = 1920;
		//config.height = 1080;
	}
}
