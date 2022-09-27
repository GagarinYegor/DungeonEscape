package com.dungeon_escape.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.dungeon_escape.DungeonEscape;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new DungeonEscape(), config);
		config.width = DungeonEscape.SCR_WIDTH;
		config.height = DungeonEscape.SCR_HEIGHT;
	}
}
