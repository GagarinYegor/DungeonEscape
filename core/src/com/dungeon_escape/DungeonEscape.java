package com.dungeon_escape;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class DungeonEscape extends Game {

	SpriteBatch batch;
	BitmapFont font, record_font;
	Texture green_slime_texture_region, slime_blast, green_slime_attacked, green_slime_attacking,
			stone_floor_texture_region,
			player_texture_region, player_blast, player_attacked, player_attacking,
			begin_button, record_button, return_button, row, row_heading, arrow_next, title_text_table,
			wu__, wd__, wl__, wr__,
			cwul, cwur, cwdl, cwdr,
			cul_, cur_, cdl_, cdr_,
			activ_lever, passiv_lever,
			uchd, uohd, dchd, dohd;
	Sound slime_attacked_sound, slime_attacking_sound,
			player_attacking_sound, player_attacked_sound,
			lever_sound, open_doors_sound, closed_doors_sound;
	TextureRegion font_region;

	float size, horisontal_otstup, vertical_otstup, left_border_x, left_border_y, right_border_x, right_border_y,
			up_border_x, up_border_y, down_border_x, down_border_y, width, height, speed;
	public static final int SCR_WIDTH = 960, SCR_HEIGHT = 540;
	String [][] map;
	int cage_x, cage_y;
	HashMap<String, TextureRegion> font_map;


	@Override
	public void create() {
		speed = 150f;
		batch = new SpriteBatch();

		//slime res
		green_slime_texture_region = new Texture("slime/green_slime_texture_region.png");
		green_slime_attacking = new Texture("slime/green_attacking_slime.png");
		green_slime_attacked = new Texture("slime/green_attacked_slime.png");
		slime_blast = new Texture("slime/slime_charge.png");
		slime_attacked_sound = Gdx.audio.newSound(Gdx.files.internal("slime/slime_attacked.mp3"));
		slime_attacking_sound = Gdx.audio.newSound(Gdx.files.internal("slime/slime_attacking.wav"));

		//floor res
		stone_floor_texture_region = new Texture("floor/stone_floor.png");

		//player res
		player_texture_region = new Texture("player/player_texture_region.png");
		player_attacked = new Texture("player/player_attacked.png");
		player_attacking = new Texture("player/player_attacking.png");
		player_blast = new Texture("interface/nothing.png");
		player_attacked_sound = Gdx.audio.newSound(Gdx.files.internal("player/attacked_player_sound.wav"));
		player_attacking_sound = Gdx.audio.newSound(Gdx.files.internal("player/attacking_player_sound.wav"));

		//lever res
		activ_lever = new Texture("levers/al__.png");
		passiv_lever = new Texture("levers/dl__.png");

		//doors res
		uchd = new Texture("doors/uchd.png");
		uohd = new Texture("doors/uohd.png");
		dchd = new Texture("doors/dchd.png");
		dohd = new Texture("doors/dohd.png");
		open_doors_sound = Gdx.audio.newSound(Gdx.files.internal("doors/open_doors_sound.ogg"));
		closed_doors_sound = Gdx.audio.newSound(Gdx.files.internal("doors/closed_doors_sound.ogg"));

		//interface res
		begin_button = new Texture("interface/begin_button.png");
		record_button = new Texture("interface/record_button.png");
		return_button = new Texture("interface/return_button.png");
		row = new Texture("interface/row.png");
		row_heading = new Texture("interface/row_heading.png");
		arrow_next = new Texture("interface/arrow_next.png");
		font_region = new TextureRegion(new Texture("interface/font.png"));
		title_text_table = new Texture("interface/title_text_table.png");

		//walls res
		wu__ = new Texture("walls/wu__.png");
		wd__ = new Texture("walls/wd__.png");
		wl__ = new Texture("walls/wl__.png");
		wr__ = new Texture("walls/wr__.png");

		cul_ = new Texture("corners/cul_.png");
		cur_ = new Texture("corners/cur_.png");
		cdl_ = new Texture("corners/cdl_.png");
		cdr_ = new Texture("corners/cdr_.png");

		cwul = new Texture("corners_and_walls/cwul.png");
		cwur = new Texture("corners_and_walls/cwur.png");
		cwdl = new Texture("corners_and_walls/cwdl.png");
		cwdr = new Texture("corners_and_walls/cwdr.png");

		//fonts
		font = new BitmapFont();
		record_font = new BitmapFont();


		width = Gdx.app.getGraphics().getWidth();
		height = Gdx.app.getGraphics().getHeight();
		size = height / 7;
		horisontal_otstup = (width-size*10)/2;
		vertical_otstup = 0;
		if(horisontal_otstup < 0){
			horisontal_otstup = 0;
			size = width/10;
			vertical_otstup = (height - 7*size)/2;
		}
		left_border_x = 0;
		left_border_y = 0;
		right_border_x = width-horisontal_otstup;
		right_border_y = 0;
		up_border_x = 0;
		up_border_y = height-vertical_otstup;
		down_border_x = 0;
		down_border_y = 0;

		map = new String[10][7];
		FileHandle file = Gdx.files.internal("text_resources/map.txt");
		String [] text_y = file.readString().split("\n");
		cage_y = text_y.length;
		for (int i=0; i<cage_y; i++){
			String [] text_x = text_y[i].split(" ");
			cage_x = text_x.length;
			for (int j=0; j<cage_x; j++){
				map [j][(cage_y-1)-i] = text_x[j];
				System.out.println(map [j][i]);
			}
		}

		font_map = new HashMap<>();
		int fontFrameCount = 32;
		int frameWidth = font_region.getRegionWidth() / fontFrameCount;
		font_map.put("А", new TextureRegion(font_region, 0 * frameWidth, 0, frameWidth, font_region.getRegionHeight()));
		font_map.put("Б", new TextureRegion(font_region, 1 * frameWidth, 0, frameWidth, font_region.getRegionHeight()));
		font_map.put("В", new TextureRegion(font_region, 2 * frameWidth, 0, frameWidth, font_region.getRegionHeight()));
		font_map.put("Г", new TextureRegion(font_region, 3 * frameWidth, 0, frameWidth, font_region.getRegionHeight()));
		font_map.put("Д", new TextureRegion(font_region, 4 * frameWidth, 0, frameWidth, font_region.getRegionHeight()));
		font_map.put("Е", new TextureRegion(font_region, 5 * frameWidth, 0, frameWidth, font_region.getRegionHeight()));
		font_map.put("Ж", new TextureRegion(font_region, 6 * frameWidth, 0, frameWidth, font_region.getRegionHeight()));
		font_map.put("З", new TextureRegion(font_region, 7 * frameWidth, 0, frameWidth, font_region.getRegionHeight()));
		font_map.put("И", new TextureRegion(font_region, 8 * frameWidth, 0, frameWidth, font_region.getRegionHeight()));
		font_map.put("Й", new TextureRegion(font_region, 9 * frameWidth, 0, frameWidth, font_region.getRegionHeight()));
		font_map.put("К", new TextureRegion(font_region, 10 * frameWidth, 0, frameWidth, font_region.getRegionHeight()));
		font_map.put("Л", new TextureRegion(font_region, 11 * frameWidth, 0, frameWidth, font_region.getRegionHeight()));
		font_map.put("М", new TextureRegion(font_region, 12 * frameWidth, 0, frameWidth, font_region.getRegionHeight()));
		font_map.put("Н", new TextureRegion(font_region, 13 * frameWidth, 0, frameWidth, font_region.getRegionHeight()));
		font_map.put("О", new TextureRegion(font_region, 14 * frameWidth, 0, frameWidth, font_region.getRegionHeight()));
		font_map.put("П", new TextureRegion(font_region, 15 * frameWidth, 0, frameWidth, font_region.getRegionHeight()));
		font_map.put("Р", new TextureRegion(font_region, 16 * frameWidth, 0, frameWidth, font_region.getRegionHeight()));
		font_map.put("С", new TextureRegion(font_region, 17 * frameWidth, 0, frameWidth, font_region.getRegionHeight()));
		font_map.put("Т", new TextureRegion(font_region, 18 * frameWidth, 0, frameWidth, font_region.getRegionHeight()));
		font_map.put("У", new TextureRegion(font_region, 19 * frameWidth, 0, frameWidth, font_region.getRegionHeight()));
		font_map.put("Ф", new TextureRegion(font_region, 20 * frameWidth, 0, frameWidth, font_region.getRegionHeight()));
		font_map.put("Х", new TextureRegion(font_region, 21 * frameWidth, 0, frameWidth, font_region.getRegionHeight()));
		font_map.put("Ц", new TextureRegion(font_region, 22 * frameWidth, 0, frameWidth, font_region.getRegionHeight()));
		font_map.put("Ч", new TextureRegion(font_region, 23 * frameWidth, 0, frameWidth, font_region.getRegionHeight()));
		font_map.put("Ш", new TextureRegion(font_region, 24 * frameWidth, 0, frameWidth, font_region.getRegionHeight()));
		font_map.put("Щ", new TextureRegion(font_region, 25 * frameWidth, 0, frameWidth, font_region.getRegionHeight()));
		font_map.put("Ъ", new TextureRegion(font_region, 26 * frameWidth, 0, frameWidth, font_region.getRegionHeight()));
		font_map.put("Ы", new TextureRegion(font_region, 27 * frameWidth, 0, frameWidth, font_region.getRegionHeight()));
		font_map.put("Ь", new TextureRegion(font_region, 28 * frameWidth, 0, frameWidth, font_region.getRegionHeight()));
		font_map.put("Э", new TextureRegion(font_region, 29 * frameWidth, 0, frameWidth, font_region.getRegionHeight()));
		font_map.put("Ю", new TextureRegion(font_region, 30 * frameWidth, 0, frameWidth, font_region.getRegionHeight()));
		font_map.put("Я", new TextureRegion(font_region, 31 * frameWidth, 0, frameWidth, font_region.getRegionHeight()));

		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void dispose () {
		batch.dispose();
		green_slime_texture_region.dispose();
		begin_button.dispose();
		record_button.dispose();
		stone_floor_texture_region.dispose();
		font.dispose();
	}
}