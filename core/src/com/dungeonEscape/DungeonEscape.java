package com.dungeonEscape;

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

	BitmapFont infoFont, recordFont, deathScreenFont, winScreenFont, infoNameFont, settingFont, slimeFont, tipsFont;

	Texture greenSlimeTextureRegion, slimeChargeTextureRegion, greenAttackedSlimeTextureRegion, greenAttackingSlimeTextureRegion,
			playerTextureRegionRight, playerTextureRegionLeft, playerCharge,
			playerAttackedRight, playerAttackingRight,
			playerAttackedLeft, playerAttackingLeft,
			playerTextureRegionMowingRight, playerTextureRegionMowingLeft,
			beginButton, recordButton, returnButton, rowHeading,
			returnButtonLarge, settingsScreenButton, deleteButton,
			beginButtonEng, recordButtonEng, returnButtonEng, rowHeadingEng,
			returnButtonLargeEng, settingsScreenButtonEng, deleteButtonEng,
			row, infoWindow, russianButton, englishButton, russianButtonActive, englishButtonActive,
			titleTextTable, border, activeAttackButton, passiveAttackButton, waitingButton,
			deathScreenImg, winScreenImg,
			arrowUp, arrowDown, arrowNo, arrowNext,
			wu__, wd__, wl__, wr__,
			cwul, cwur, cwdl, cwdr,
			cul_, cur_, cdl_, cdr_,
			activeLever, passiveLever,
			chd, ohd, cvd, ovd,
			screensaver, stoneFloorTextureRegion, exitImg, exitDoor, stoneFloorSc,
			wdwt, sfwm, clmn, mapImg, passivMapButton, activeMapButton, deathImg, emptyButton,
			yesButton, noButton, yesButtonEng, noButtonEng, passiveTipsButton, activeTipsButton,
			yesButtonActive, noButtonActive, yesButtonEngActive, noButtonEngActive, closeButton,
			tip0, tip1, tip2, tip3, tip4, tip5, tip6;

	Sound slimeAttackedSound, slimeAttackingSound,
			playerAttackingSound, sound,
			leverSound, openDoorsSound, closedDoorsSound;

	Music theme;

	float size, horizontalIndend, verticalIndent, leftBorderX, leftBorderY, rightBorderX, rightBorderY,
			upBorderX, upBorderY, downBorderX, downBorderY, width, height, speed;
	final String FONT_CHARS = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяabcdefghijklmnopqrstuvwxyzАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?№-+=()*&.;:,{}\"´`'<>";

	public static final int SCR_WIDTH = 960, SCR_HEIGHT = 540;
	//public static final int SCR_WIDTH = 800, SCR_HEIGHT = 600;

	String [][] map;
	int[][] slimes_mass;
	int[][] levers_mass;
	int cageX, cageY, slimeMassX, slimeMassY, leverMassX, leverMassY, moves;
	Input.TextInputListener listener, gameListener, settingsListener;
	String name;
	boolean isEnglish, attackButtonAutoReset;

	@Override
	public void create() {
		isEnglish = false;
		attackButtonAutoReset = false;
		gameBatch = new SpriteBatch();
		winScreenBatch = new SpriteBatch();
		name = "";

		//slime res
		greenSlimeTextureRegion = new Texture("slime/images/greenSlimeTextureRegion.png");
		greenAttackingSlimeTextureRegion = new Texture("slime/images/greenAttackingSlimeTextureRegion.png");
		greenAttackedSlimeTextureRegion = new Texture("slime/images/greenAttackedSlimeTextureRegion.png");
		slimeChargeTextureRegion = new Texture("slime/images/slimeChargeTextureRegion.png");
		slimeAttackedSound = Gdx.audio.newSound(Gdx.files.internal("slime/sounds/slime_attacked.mp3"));
		slimeAttackingSound = Gdx.audio.newSound(Gdx.files.internal("slime/sounds/slime_attacking.wav"));

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
		playerCharge = new Texture("interface/nothing.png");
		sound = Gdx.audio.newSound(Gdx.files.internal("player/attacked_player_sound.wav"));
		playerAttackingSound = Gdx.audio.newSound(Gdx.files.internal("player/attacking_player_sound.wav"));
		playerTextureRegionMowingRight = new Texture("player/player_right_moving_texture_region.png");
		playerTextureRegionMowingLeft = new Texture("player/player_left_moving_texture_region.png");

		//lever res
		activeLever = new Texture("levers/al__.png");
		passiveLever = new Texture("levers/dl__.png");

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
		yesButtonActive = new Texture("interface/buttons/yes_button_activ.png");
		yesButtonEng = new Texture("interface/buttons/yes_button_eng.png");
		yesButtonEngActive = new Texture("interface/buttons/yes_button_eng_activ.png");
		noButton = new Texture("interface/buttons/no_button.png");
		noButtonActive = new Texture("interface/buttons/no_button_activ.png");
		noButtonEng = new Texture("interface/buttons/no_button_eng.png");
		noButtonEngActive = new Texture("interface/buttons/no_button_eng_activ.png");

		passivMapButton = new Texture("interface/buttons/passiv_map_button.png");
		activeMapButton = new Texture("interface/buttons/activ_map_button.png");
		passiveAttackButton = new Texture("interface/buttons/passiv_attack_button.png");
		activeAttackButton = new Texture("interface/buttons/activ_attack_button.png");
		waitingButton = new Texture("interface/buttons/waiting_button.png");
		russianButton = new Texture("interface/buttons/russian_button.png");
		russianButtonActive = new Texture("interface/buttons/russian_button_activ.png");
		englishButton = new Texture("interface/buttons/english_button.png");
		englishButtonActive = new Texture("interface/buttons/english_button_activ.png");
		passiveTipsButton = new Texture("interface/buttons/passiv_tips_button.png");
		activeTipsButton = new Texture("interface/buttons/activ_tips_button.png");
		closeButton = new Texture("interface/buttons/close_button.png");

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

		tip0 = new Texture("interface/tips/tip0.png");
		tip1 = new Texture("interface/tips/tip1.png");
		tip2 = new Texture("interface/tips/tip2.png");
		tip3 = new Texture("interface/tips/tip3.png");
		tip4 = new Texture("interface/tips/tip4.png");
		tip5 = new Texture("interface/tips/tip5.png");
		tip6 = new Texture("interface/tips/tip6.png");

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
		horizontalIndend = (width - size * 10) / 2;
		verticalIndent = 0;
		if(horizontalIndend < 0){
			horizontalIndend = 0;
			size = width / 10;
			verticalIndent = (height - 7 * size) / 2;
		}
		leftBorderX = 0;
		leftBorderY = 0;
		rightBorderX = width - horizontalIndend;
		rightBorderY = 0;
		upBorderX = 0;
		upBorderY = height - verticalIndent;
		downBorderX = 0;
		downBorderY = 0;

		speed = size*3;

		map = new String[41][41];
		FileHandle file = Gdx.files.internal("text_resources/map.txt");
		String [] text_y = file.readString().split("\n");
		cageY = text_y.length;
		for (int i = 0; i < cageY; i++){
			String [] text_x = text_y[i].split(" ");
			cageX = text_x.length;
			for (int j = 0; j < cageX; j++){
				map [j][(cageY - 1) - i] = text_x[j];
			}
		}

		FileHandle slime_file = Gdx.files.internal("text_resources/slimes.txt");
		Scanner slimes_scan = new Scanner(slime_file.read());
		slimeMassX = 2;
		slimeMassY = 28;
		slimes_mass = new int[slimeMassY][slimeMassX];
		for (int i = 0; i < slimeMassY; i++){
			for (int j = 0; j < slimeMassX; j++){
				slimes_mass[i][j] = slimes_scan.nextInt();
			}
		}

		FileHandle lever_file = Gdx.files.internal("text_resources/levers.txt");
		Scanner levers_scan = new Scanner(lever_file.read());
		leverMassX = 5;
		leverMassY = 21;
		levers_mass = new int[leverMassY][leverMassX];
		for (int i = 0; i < leverMassY; i++){
			for (int j = 0; j < leverMassX; j++){
				levers_mass[i][j] = levers_scan.nextInt();
			}
		}

		//fonts
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("text_resources/Noah.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.characters = FONT_CHARS;

		parameter.size = (int)(size / 2.1f);
		parameter.color = Color.WHITE;
		deathScreenFont = generator.generateFont(parameter);

		parameter.size = (int)(size / 2.3f);
		parameter.color = Color.WHITE;
		winScreenFont = generator.generateFont(parameter);

		parameter.size = (int)(size / 5.5f);
		parameter.color = Color.BLACK;
		infoFont = generator.generateFont(parameter);

		parameter.size = (int)(size / 7.6f);
		parameter.color = Color.BLACK;
		infoNameFont = generator.generateFont(parameter);

		parameter.size = (int)(size / 2.1f);
		parameter.color = Color.DARK_GRAY;
		recordFont = generator.generateFont(parameter);

		parameter.size = (int)(size / 2.1f);
		parameter.color = Color.DARK_GRAY;
		settingFont = generator.generateFont(parameter);

		parameter.size = (int)(size / 5.5f);
		parameter.color = Color.RED;
		slimeFont = generator.generateFont(parameter);

		parameter.size = (int)(size / 4);
		parameter.color = Color.WHITE;
		tipsFont = generator.generateFont(parameter);

		generator.dispose();

		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void dispose () {
		greenSlimeTextureRegion.dispose();
		slimeChargeTextureRegion.dispose();
		greenAttackedSlimeTextureRegion.dispose();
		greenAttackingSlimeTextureRegion.dispose();
		playerTextureRegionRight.dispose();
		playerTextureRegionLeft.dispose();
		playerCharge.dispose();
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
		russianButtonActive.dispose();
		englishButtonActive.dispose();
		titleTextTable.dispose();
		border.dispose();
		activeAttackButton.dispose();
		passiveAttackButton.dispose();
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
		activeLever.dispose();
		passiveLever.dispose();
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
		activeMapButton.dispose();
		deathImg.dispose();
		emptyButton.dispose();
		yesButton.dispose();
		noButton.dispose();
		yesButtonEng.dispose();
		noButtonEng.dispose();
		yesButtonActive.dispose();
		noButtonActive.dispose();
		yesButtonEngActive.dispose();
		noButtonEngActive.dispose();

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