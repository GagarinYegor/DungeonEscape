package com.dungeon_escape;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.HashMap;
import java.util.Scanner;

public class DungeonEscape extends Game {

	SpriteBatch batch, records_batch, main_menu_batch,
			death_screen_batch, win_screen_batch, settings_batch;

	BitmapFont info_font, record_font, death_screen_font, win_screen_font, info_name_font, setting_font;

	Texture green_slime_texture_region, slime_blast, green_slime_attacked, green_slime_attacking,
			player_texture_region_right, player_texture_region_left, player_blast,
			player_attacked_right, player_attacking_right,
			player_attacked_left, player_attacking_left,
			player_texture_region_mowing_right, player_texture_region_mowing_left,
			begin_button, record_button, return_button, row_heading,
			return_button_large, settings_screen_button, delete_button,
			begin_button_eng, record_button_eng, return_button_eng,  row_heading_eng,
			return_button_large_eng, settings_screen_button_eng, delete_button_eng,
			row, info_window, russian_button, english_button, russian_button_activ, english_button_activ,
			title_text_table, border, activ_attack_button, passiv_attack_button, waiting_button,
			death_screen_img, win_screen_img,
			arrow_up, arrow_down, arrow_no, arrow_next,
			wu__, wd__, wl__, wr__,
			cwul, cwur, cwdl, cwdr,
			cul_, cur_, cdl_, cdr_,
			activ_lever, passiv_lever,
			chd, ohd, cvd, ovd,
			screensaver, stone_floor_texture_region, exit_img, exit_door, stone_floor_sc,
			wdwt, sfwm, clmn, map_img, passiv_map_button, activ_map_button, death_img, empty_button,
			yes_button, no_button, yes_button_eng, no_button_eng,
			yes_button_activ, no_button_activ, yes_button_eng_activ, no_button_eng_activ;

	Sound slime_attacked_sound, slime_attacking_sound,
			player_attacking_sound, player_attacked_sound,
			lever_sound, open_doors_sound, closed_doors_sound;

	Music theme;

	float size, horizontal_otstup, vertical_otstup, left_border_x, left_border_y, right_border_x, right_border_y,
			up_border_x, up_border_y, down_border_x, down_border_y, width, height, speed;
	final String FONT_CHARS = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяabcdefghijklmnopqrstuvwxyzАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?№-+=()*&.;:,{}\"´`'<>";

	public static final int SCR_WIDTH = 960, SCR_HEIGHT = 540;

	String [][] map;
	int[][] slimes_mass;
	int[][] levers_mass;
	int cage_x, cage_y, slime_mass_x, slime_mass_y, lever_mass_x, lever_mass_y, moves;
	Input.TextInputListener listener;
	String name;
	boolean is_english, attack_button_auto_reset;

	@Override
	public void create() {
		is_english = false;
		attack_button_auto_reset = false;
		batch = new SpriteBatch();
		name = "";

		//slime res
		green_slime_texture_region = new Texture("slime/green_slime_texture_region.png");
		green_slime_attacking = new Texture("slime/green_attacking_slime.png");
		green_slime_attacked = new Texture("slime/green_attacked_slime.png");
		slime_blast = new Texture("slime/slime_charge.png");
		slime_attacked_sound = Gdx.audio.newSound(Gdx.files.internal("slime/slime_attacked.mp3"));
		slime_attacking_sound = Gdx.audio.newSound(Gdx.files.internal("slime/slime_attacking.wav"));

		//floor res
		stone_floor_texture_region = new Texture("floor/stone_floor.png");
		clmn = new Texture("floor/clmn.png");
		stone_floor_sc = new Texture("floor/stone_floor_sc.png");
		sfwm = new Texture("floor/stone_floor_with_map.png");

		//player res
		player_texture_region_right = new Texture("player/player_texture_region_right.png");
		player_texture_region_left = new Texture("player/player_texture_region_left.png");
		player_attacked_right = new Texture("player/player_attacked_right.png");
		player_attacking_right = new Texture("player/player_attacking_right.png");
		player_attacked_left = new Texture("player/player_attacked_left.png");
		player_attacking_left = new Texture("player/player_attacking_left.png");
		player_blast = new Texture("interface/nothing.png");
		player_attacked_sound = Gdx.audio.newSound(Gdx.files.internal("player/attacked_player_sound.wav"));
		player_attacking_sound = Gdx.audio.newSound(Gdx.files.internal("player/attacking_player_sound.wav"));
		player_texture_region_mowing_right = new Texture("player/player_right_moving_texture_region.png");
		player_texture_region_mowing_left = new Texture("player/player_left_moving_texture_region.png");

		//lever res
		activ_lever = new Texture("levers/al__.png");
		passiv_lever = new Texture("levers/dl__.png");

		//doors res
		ohd = new Texture("doors/ohd.png");
		chd = new Texture("doors/chd.png");
		ovd = new Texture("doors/ovd.png");
		cvd = new Texture("doors/cvd.png");
		open_doors_sound = Gdx.audio.newSound(Gdx.files.internal("doors/open_doors_sound.ogg"));
		closed_doors_sound = Gdx.audio.newSound(Gdx.files.internal("doors/closed_doors_sound.ogg"));
		exit_img = new Texture("doors/exit.png");
		exit_door = new Texture("doors/exit_door.png");

		//interface res
		begin_button = new Texture("interface/buttons/begin_button.png");
		begin_button_eng = new Texture("interface/buttons/begin_button_eng.png");
		record_button = new Texture("interface/buttons/record_button.png");
		record_button_eng = new Texture("interface/buttons/record_button_eng.png");
		return_button = new Texture("interface/buttons/return_button.png");
		return_button_eng = new Texture("interface/buttons/return_button_eng.png");
		return_button_large = new Texture("interface/buttons/return_button_large.png");
		return_button_large_eng = new Texture("interface/buttons/return_button_large_eng.png");
		delete_button = new Texture("interface/buttons/delete_button.png");
		delete_button_eng = new Texture("interface/buttons/delete_button_eng.png");
		settings_screen_button = new Texture("interface/buttons/settings_button.png");
		settings_screen_button_eng = new Texture("interface/buttons/settings_button_eng.png");
		row_heading = new Texture("interface/row_heading.png");
		empty_button = new Texture("interface/buttons/empty_button.png");
		yes_button = new Texture("interface/buttons/yes_button.png");
		yes_button_activ = new Texture("interface/buttons/yes_button_activ.png");
		yes_button_eng = new Texture("interface/buttons/yes_button_eng.png");
		yes_button_eng_activ = new Texture("interface/buttons/yes_button_eng_activ.png");
		no_button = new Texture("interface/buttons/no_button.png");
		no_button_activ = new Texture("interface/buttons/no_button_activ.png");
		no_button_eng = new Texture("interface/buttons/no_button_eng.png");
		no_button_eng_activ = new Texture("interface/buttons/no_button_eng_activ.png");

		passiv_map_button = new Texture("interface/buttons/passiv_map_button.png");
		activ_map_button = new Texture("interface/buttons/activ_map_button.png");
		passiv_attack_button = new Texture("interface/buttons/passiv_attack_button.png");
		activ_attack_button = new Texture("interface/buttons/activ_attack_button.png");
		waiting_button = new Texture("interface/buttons/waiting_button.png");
		russian_button = new Texture("interface/buttons/russian_button.png");
		russian_button_activ = new Texture("interface/buttons/russian_button_activ.png");
		english_button = new Texture("interface/buttons/english_button.png");
		english_button_activ = new Texture("interface/buttons/english_button_activ.png");

		row = new Texture("interface/row.png");
		arrow_next = new Texture("interface/arrow_next.png");
		title_text_table = new Texture("interface/title_text_table.png");
		border = new Texture("interface/border.png");
		info_window = new Texture("interface/info_window.png");
		screensaver = new Texture("interface/screensaver.png");
		death_screen_img = new Texture("interface/death_screen_img.png");
		win_screen_img = new Texture("interface/win_screen.png");
		arrow_up = new Texture("interface/arrow_up.png");
		arrow_down = new Texture("interface/arrow_down.png");
		arrow_no = new Texture("interface/arrow_no.png");
		map_img = new Texture("interface/map.png");
		death_img = new Texture("interface/death_img.png");
		theme = Gdx.audio.newMusic(Gdx.files.internal("interface/theme.mp3"));

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

		wdwt = new Texture("walls/wdwt_texture_region.png");

		width = Gdx.app.getGraphics().getWidth();
		height = Gdx.app.getGraphics().getHeight();
		size = height / 7;
		horizontal_otstup = (width-size*10)/2;
		vertical_otstup = 0;
		if(horizontal_otstup < 0){
			horizontal_otstup = 0;
			size = width/10;
			vertical_otstup = (height - 7*size)/2;
		}
		left_border_x = 0;
		left_border_y = 0;
		right_border_x = width-horizontal_otstup;
		right_border_y = 0;
		up_border_x = 0;
		up_border_y = height-vertical_otstup;
		down_border_x = 0;
		down_border_y = 0;

		speed = size*3;

		map = new String[41][41];
		FileHandle file = Gdx.files.internal("text_resources/map.txt");
		String [] text_y = file.readString().split("\n");
		cage_y = text_y.length;
		for (int i=0; i<cage_y; i++){
			String [] text_x = text_y[i].split(" ");
			cage_x = text_x.length;
			for (int j=0; j<cage_x; j++){
				map [j][(cage_y-1)-i] = text_x[j];
			}
		}

		FileHandle slime_file = Gdx.files.internal("text_resources/slimes.txt");
		Scanner slimes_scan = new Scanner(slime_file.read());
		slime_mass_x = 2;
		slime_mass_y = 28;
		slimes_mass = new int[slime_mass_y][slime_mass_x];
		for (int i=0; i<slime_mass_y; i++){
			for (int j=0; j<slime_mass_x; j++){
				slimes_mass[i][j] = slimes_scan.nextInt();
			}
		}

		FileHandle lever_file = Gdx.files.internal("text_resources/levers.txt");
		Scanner levers_scan = new Scanner(lever_file.read());
		lever_mass_x = 5;
		lever_mass_y = 21;
		levers_mass = new int[lever_mass_y][lever_mass_x];
		for (int i=0; i<lever_mass_y; i++){
			for (int j=0; j<lever_mass_x; j++){
				levers_mass[i][j] = levers_scan.nextInt();
			}
		}

		//fonts
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("text_resources/Noah.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.characters = FONT_CHARS;

		parameter.size = (int)(size/2.1f);
		parameter.color = Color.WHITE;
		death_screen_font = generator.generateFont(parameter);

		parameter.size = (int)(size/2.3f);
		parameter.color = Color.WHITE;
		win_screen_font = generator.generateFont(parameter);

		parameter.size = (int)(size/5.5f);
		parameter.color = Color.BLACK;
		info_font = generator.generateFont(parameter);

		parameter.size = (int)(size/7.6f);
		parameter.color = Color.BLACK;
		info_name_font = generator.generateFont(parameter);

		parameter.size = (int)(size/2.1f);
		parameter.color = Color.DARK_GRAY;
		record_font = generator.generateFont(parameter);

		parameter.size = (int)(size/2.1f);
		parameter.color = Color.DARK_GRAY;
		setting_font = generator.generateFont(parameter);

		generator.dispose();

		//FileHandle rec_file = Gdx.files.local("text_resources/records.txt");
		//if (rec_file.length()==0) rec_file.writeString("Test 0 0", false);
		//rec_file.writeString("Test 0 0", false);

		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void dispose () {
		batch.dispose();
		green_slime_texture_region.dispose();
		begin_button.dispose();
		record_button.dispose();
		stone_floor_texture_region.dispose();
	}
}