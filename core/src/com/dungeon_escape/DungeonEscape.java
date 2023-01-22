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
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.Scanner;

public class DungeonEscape extends Game {

	SpriteBatch gameBatch, recordsBatch, mainMenuBatch, deathScreenBatch, winScreenBatch, settingsBatch;

	BitmapFont infoFont, recordFont, deathScreenFont, winScreenFont, infoNameFont, settingFont, slimeFont;

	Texture greenSlimeTextureRegion, slimeBlast, greenSlimeAttacked, greenSlimeAttacking,
			playerTextureRegionRight, playerTextureRegionLeft, playerBlast,
			playerAttackedRight, playerAttackingRight,
			playerAttackedLeft, playerAttackingLeft,
			playerTextureRegionMowingRight, playerTextureRegionMowingLeft,
			beginButton, recordButton, returnButton, rowHeading,
			returnButtonLarge, settingsScreenButton, deleteButton,
			beginButtonEng, recordButtonEng, returnButtonEng, rowHeadingEng,
			returnButtonLargeEng, settingsScreenButtonEng, deleteButtonEng,
			row, infoWindow, russianButton, englishButton, russianButtonActiv, englishButtonActiv,
			titleTextTable, border, activAttackButton, passivAttackButton, waitingButton,
			deathScreenImg, winScreenImg,
			arrowUp, arrowDown, arrowNo, arrowNext,
			wu__, wd__, wl__, wr__,
			cwul, cwur, cwdl, cwdr,
			cul_, cur_, cdl_, cdr_,
			activLever, passivLever,
			chd, ohd, cvd, ovd,
			screensaver, stoneFloorTextureRegion, exitImg, exitDoor, stoneFloorSc,
			wdwt, sfwm, clmn, mapImg, passivMapButton, activMapButton, deathImg, emptyButton,
			yesButton, noButton, yesButtonEng, noButtonEng,
			yesButtonActiv, noButtonActiv, yesButtonEngActiv, noButtonEngActiv;

	Sound slimeAttackedSound, slimeAttackingSound,
			playerAttackingSound, sound,
			leverSound, openDoorsSound, closedDoorsSound;

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
		gameBatch = new SpriteBatch();
		name = "";

		//slime res
		greenSlimeTextureRegion = new Texture("slime/green_slime_texture_region.png");
		greenSlimeAttacking = new Texture("slime/green_attacking_slime.png");
		greenSlimeAttacked = new Texture("slime/green_attacked_slime.png");
		slimeBlast = new Texture("slime/slime_charge.png");
		slimeAttackedSound = Gdx.audio.newSound(Gdx.files.internal("slime/slime_attacked.mp3"));
		slimeAttackingSound = Gdx.audio.newSound(Gdx.files.internal("slime/slime_attacking.wav"));

		//floor res
		stoneFloorTextureRegion = new Texture("floor/stone_floor.png");
		clmn = new Texture("floor/clmn.png");
		stoneFloorSc = new Texture("floor/stone_floor_sc.png");
		sfwm = new Texture("floor/stone_floor_with_map.png");

		//player res
		playerTextureRegionRight = new Texture("player/player_texture_region_right.png");
		playerTextureRegionLeft = new Texture("player/player_texture_region_left.png");
		playerAttackedRight = new Texture("player/player_attacked_right.png");
		playerAttackingRight = new Texture("player/player_attacking_right.png");
		playerAttackedLeft = new Texture("player/player_attacked_left.png");
		playerAttackingLeft = new Texture("player/player_attacking_left.png");
		playerBlast = new Texture("interface/nothing.png");
		sound = Gdx.audio.newSound(Gdx.files.internal("player/attacked_player_sound.wav"));
		playerAttackingSound = Gdx.audio.newSound(Gdx.files.internal("player/attacking_player_sound.wav"));
		playerTextureRegionMowingRight = new Texture("player/player_right_moving_texture_region.png");
		playerTextureRegionMowingLeft = new Texture("player/player_left_moving_texture_region.png");

		//lever res
		activLever = new Texture("levers/al__.png");
		passivLever = new Texture("levers/dl__.png");

		//doors res
		ohd = new Texture("doors/ohd.png");
		chd = new Texture("doors/chd.png");
		ovd = new Texture("doors/ovd.png");
		cvd = new Texture("doors/cvd.png");
		openDoorsSound = Gdx.audio.newSound(Gdx.files.internal("doors/sounds/open_doors_sound.ogg"));
		closedDoorsSound = Gdx.audio.newSound(Gdx.files.internal("doors/sounds/closed_doors_sound.ogg"));
		leverSound = Gdx.audio.newSound(Gdx.files.internal("doors/sounds/lever_sound.wav"));
		exitImg = new Texture("doors/exit.png");
		exitDoor = new Texture("doors/exit_door.png");

		//interface res
		beginButton = new Texture("interface/buttons/begin_button.png");
		beginButtonEng = new Texture("interface/buttons/begin_button_eng.png");
		recordButton = new Texture("interface/buttons/record_button.png");
		recordButtonEng = new Texture("interface/buttons/record_button_eng.png");
		returnButton = new Texture("interface/buttons/return_button.png");
		returnButtonEng = new Texture("interface/buttons/return_button_eng.png");
		returnButtonLarge = new Texture("interface/buttons/return_button_large.png");
		returnButtonLargeEng = new Texture("interface/buttons/return_button_large_eng.png");
		deleteButton = new Texture("interface/buttons/delete_button.png");
		deleteButtonEng = new Texture("interface/buttons/delete_button_eng.png");
		settingsScreenButton = new Texture("interface/buttons/settings_button.png");
		settingsScreenButtonEng = new Texture("interface/buttons/settings_button_eng.png");
		rowHeading = new Texture("interface/row_heading.png");
		rowHeadingEng = new Texture("interface/row_heading_eng.png");
		emptyButton = new Texture("interface/buttons/empty_button.png");
		yesButton = new Texture("interface/buttons/yes_button.png");
		yesButtonActiv = new Texture("interface/buttons/yes_button_activ.png");
		yesButtonEng = new Texture("interface/buttons/yes_button_eng.png");
		yesButtonEngActiv = new Texture("interface/buttons/yes_button_eng_activ.png");
		noButton = new Texture("interface/buttons/no_button.png");
		noButtonActiv = new Texture("interface/buttons/no_button_activ.png");
		noButtonEng = new Texture("interface/buttons/no_button_eng.png");
		noButtonEngActiv = new Texture("interface/buttons/no_button_eng_activ.png");

		passivMapButton = new Texture("interface/buttons/passiv_map_button.png");
		activMapButton = new Texture("interface/buttons/activ_map_button.png");
		passivAttackButton = new Texture("interface/buttons/passiv_attack_button.png");
		activAttackButton = new Texture("interface/buttons/activ_attack_button.png");
		waitingButton = new Texture("interface/buttons/waiting_button.png");
		russianButton = new Texture("interface/buttons/russian_button.png");
		russianButtonActiv = new Texture("interface/buttons/russian_button_activ.png");
		englishButton = new Texture("interface/buttons/english_button.png");
		englishButtonActiv = new Texture("interface/buttons/english_button_activ.png");

		row = new Texture("interface/row.png");
		arrowNext = new Texture("interface/arrow_next.png");
		titleTextTable = new Texture("interface/title_text_table.png");
		border = new Texture("interface/border.png");
		infoWindow = new Texture("interface/info_window.png");
		screensaver = new Texture("interface/screensaver.png");
		deathScreenImg = new Texture("interface/death_screen_img.png");
		winScreenImg = new Texture("interface/win_screen.png");
		arrowUp = new Texture("interface/arrow_up.png");
		arrowDown = new Texture("interface/arrow_down.png");
		arrowNo = new Texture("interface/arrow_no.png");
		mapImg = new Texture("interface/map.png");
		deathImg = new Texture("interface/death_img.png");
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
		deathScreenFont = generator.generateFont(parameter);

		parameter.size = (int)(size/2.3f);
		parameter.color = Color.WHITE;
		winScreenFont = generator.generateFont(parameter);

		parameter.size = (int)(size/5.5f);
		parameter.color = Color.BLACK;
		infoFont = generator.generateFont(parameter);

		parameter.size = (int)(size/7.6f);
		parameter.color = Color.BLACK;
		infoNameFont = generator.generateFont(parameter);

		parameter.size = (int)(size/2.1f);
		parameter.color = Color.DARK_GRAY;
		recordFont = generator.generateFont(parameter);

		parameter.size = (int)(size/2.1f);
		parameter.color = Color.DARK_GRAY;
		settingFont = generator.generateFont(parameter);

		parameter.size = (int)(size/5.5f);
		parameter.color = Color.RED;
		slimeFont = generator.generateFont(parameter);

		generator.dispose();

		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void dispose () {
		greenSlimeTextureRegion.dispose();
		slimeBlast.dispose();
		greenSlimeAttacked.dispose();
		greenSlimeAttacking.dispose();
		playerTextureRegionRight.dispose();
		playerTextureRegionLeft.dispose();
		playerBlast.dispose();
		playerAttackedRight.dispose();
		playerAttackingRight.dispose();
		playerAttackedLeft.dispose();
		playerAttackingLeft.dispose();
		playerTextureRegionMowingRight.dispose();
		playerTextureRegionMowingLeft.dispose();
		beginButton.dispose();
		recordButton.dispose();
		returnButton.dispose();
		rowHeading.dispose();
		returnButtonLarge.dispose();
		settingsScreenButton.dispose();
		deleteButton.dispose();
		beginButtonEng.dispose();
		recordButtonEng.dispose();
		returnButtonEng.dispose();
		rowHeadingEng.dispose();
		returnButtonLargeEng.dispose();
		settingsScreenButtonEng.dispose();
		deleteButtonEng.dispose();
		row.dispose();
		infoWindow.dispose();
		russianButton.dispose();
		englishButton.dispose();
		russianButtonActiv.dispose();
		englishButtonActiv.dispose();
		titleTextTable.dispose();
		border.dispose();
		activAttackButton.dispose();
		passivAttackButton.dispose();
		waitingButton.dispose();
		deathScreenImg.dispose();
		winScreenImg.dispose();
		arrowUp.dispose();
		arrowDown.dispose();
		arrowNo.dispose();
		arrowNext.dispose();
		wu__.dispose();
		wd__.dispose();
		wl__.dispose();
		wr__.dispose();
		cwul.dispose();
		cwur.dispose();
		cwdl.dispose();
		cwdr.dispose();
		cul_.dispose();
		cur_.dispose();
		cdl_.dispose();
		cdr_.dispose();
		activLever.dispose();
		passivLever.dispose();
		chd.dispose();
		ohd.dispose();
		cvd.dispose();
		ovd.dispose();
		screensaver.dispose();
		stoneFloorTextureRegion.dispose();
		exitImg.dispose();
		exitDoor.dispose();
		stoneFloorSc.dispose();
		wdwt.dispose();
		sfwm.dispose();
		clmn.dispose();
		mapImg.dispose();
		passivMapButton.dispose();
		activMapButton.dispose();
		deathImg.dispose();
		emptyButton.dispose();
		yesButton.dispose();
		noButton.dispose();
		yesButtonEng.dispose();
		noButtonEng.dispose();
		yesButtonActiv.dispose();
		noButtonActiv.dispose();
		yesButtonEngActiv.dispose();
		noButtonEngActiv.dispose();

		infoFont.dispose();
		recordFont.dispose();
		deathScreenFont.dispose();
		winScreenFont.dispose();
		infoNameFont.dispose();
		settingFont.dispose();
		slimeFont.dispose();

		gameBatch.dispose();
		recordsBatch.dispose();
		mainMenuBatch.dispose();
		deathScreenBatch.dispose();
		winScreenBatch.dispose();
		settingsBatch.dispose();

		slimeAttackedSound.dispose();
		slimeAttackingSound.dispose();
		playerAttackingSound.dispose();
		sound.dispose();
		leverSound.dispose();
		openDoorsSound.dispose();
		closedDoorsSound.dispose();

		theme.dispose();
	}
}