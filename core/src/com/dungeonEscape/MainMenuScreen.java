package com.dungeonEscape;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainMenuScreen extends ScreenAdapter {

    DungeonEscape game;
    Slime slime;
    Player player;
    float rightMoving, leftMoving, upMoving, downMoving,
            buttonsRealX, buttonsRealY, slowing, startTimer;
    boolean isCorrectName, isDialogOpen;
    OrthographicCamera mainMenuCamera;

    public MainMenuScreen(final DungeonEscape game) {
        this.game = game;
        slowing = 0.5f;
        startTimer = 0.1f;
        game.mainMenuBatch = new SpriteBatch();

        rightMoving = game.size;
        leftMoving = 0;
        upMoving = 0;
        downMoving = 0;

        mainMenuCamera = new OrthographicCamera(game.width, game.height);
        mainMenuCamera.setToOrtho(false, game.width, game.height);
        mainMenuCamera.translate(game.size, game.size);
        buttonsRealX = game.size;
        buttonsRealY = game.size;

        slime = new Slime(7, 5, game.size, game.horizontalIndend, game.verticalIndent,
                game.greenSlimeTextureRegion, 6, game.speed, game.slimeChargeTextureRegion,
                game.greenAttackingSlimeTextureRegion, game.greenAttackedSlimeTextureRegion,
                game.slimeAttackingSound, game.slimeAttackedSound, game.titleTextTable, game.slimeFont, 100, 100);

        player = new Player(2, 5, game.size, game.horizontalIndend, game.verticalIndent,
                game.playerTextureRegionRight, game.playerTextureRegionLeft,
                12,
                game.playerTextureRegionMowingRight, game.playerTextureRegionMowingLeft,
                14,
                game.speed, game.playerCharge,
                game.playerAttackingRight, game.playerAttackedRight,
                game.playerAttackingLeft, game.playerAttackedLeft,
                game.playerAttackingSound, game.sound, "", 100, 100);

        isDialogOpen = false;
        isCorrectName = false;

        game.listener = new Input.TextInputListener() {
            @Override
            public void input(String s) {
                if (s.length() <= 10 && s.length() >= 1 && s.indexOf(" ") == -1){
                    game.name = s;
                    isCorrectName = true;
                }
                else {
                    if (!game.isEnglish){
                        Gdx.input.getTextInput(game.listener, "Имя должно состоять из 1-10 букв без пробела", game.name, "");
                    }
                    else {
                        Gdx.input.getTextInput(game.listener, "Name shoud be 1-10 letters without spaces", game.name, "");
                    }
                }
            }

            @Override
            public void canceled() {
                isCorrectName = false;
                isDialogOpen = false;
            }
        };
    }


    @Override
    public void show(){
        Gdx.input.setInputProcessor(new InputAdapter() {
            public boolean touchDown (int x, int y, int pointer, int button) {
                int touch_x;
                int touch_y;
                if ((Gdx.input.getX()-game.horizontalIndend) / game.size >= 0){
                    touch_x = (int) ((Gdx.input.getX() - game.horizontalIndend) / game.size);
                }
                else{
                    touch_x = (int) ((Gdx.input.getX() - game.horizontalIndend) / game.size - 1);
                }
                if ((game.height - (game.verticalIndent + Gdx.input.getY())) / game.size >= 0){
                    touch_y = (int) ((game.height - (game.verticalIndent + Gdx.input.getY())) / game.size);
                }
                else {
                    touch_y = (int) ((game.height - (game.verticalIndent + Gdx.input.getY())) / game.size - 1);
                }
                if (button == Input.Buttons.LEFT && touch_y == 2 && touch_x >= 0 && touch_x <= 9) {
                    FileHandle saved_file = Gdx.files.local("text_resources/saved_records.txt");
                    if (saved_file.exists()) {
                        isCorrectName = true;
                    }
                    else {
                        if (!isDialogOpen) {
                            isDialogOpen = true;
                            if (!game.isEnglish) {
                                Gdx.input.getTextInput(game.listener, "Введите имя:", game.name, "");
                            } else {
                                Gdx.input.getTextInput(game.listener, "Enter your name:", game.name, "");
                            }
                        }
                    }
                    return true;
                }
                if (button == Input.Buttons.LEFT && touch_y == 1 && touch_x >= 0 && touch_x <= 9) {
                    if (!isDialogOpen) {
                        game.setScreen(new RecordScreen(game));
                    }
                    return true;
                }
                if (button == Input.Buttons.LEFT && touch_y == 0 && touch_x >= 0 && touch_x <= 9) {
                    if (!isDialogOpen) {
                        game.setScreen(new SettingsScreen(game));
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void render(float delta) {
        if (game.speed * delta * slowing > upMoving && upMoving > 0){
            mainMenuCamera.translate(0, upMoving);
            upMoving = 0;
            rightMoving = game.size*4;
            //buttons_real_y=0;
            //is_hod = true;
        }
        if (upMoving > 0){
            mainMenuCamera.translate(0, game.speed * delta * slowing);
            upMoving -= game.speed * delta * slowing;
            buttonsRealY +=game.speed * delta * slowing;
            //if (camera_move_up == 0) is_hod = true;
        }
        if (game.speed * delta * slowing > rightMoving && rightMoving > 0){
            mainMenuCamera.translate(rightMoving, 0);
            rightMoving = 0;
            downMoving = game.size * 4;
            //buttons_real_x=0;
            //is_hod = true;
        }
        if (rightMoving > 0){
            mainMenuCamera.translate(game.speed * delta * slowing, 0);
            rightMoving -= game.speed * delta * slowing;
            buttonsRealX +=game.speed * delta * slowing;
            //if (right_moving == 0) is_hod = true;
        }
        if (game.speed*delta* slowing > downMoving && downMoving > 0){
            mainMenuCamera.translate(0, -downMoving);
            downMoving = 0;
            leftMoving = game.size * 4;
            //buttons_real_y=0;
            //is_hod = true;
        }
        if (downMoving > 0){
            mainMenuCamera.translate(0, -game.speed * delta * slowing);
            downMoving -= game.speed * delta * slowing;
            buttonsRealY -= game.speed * delta * slowing;
            //if (camera_move_down == 0) is_hod = true;
        }
        if (game.speed*delta* slowing > leftMoving && leftMoving > 0){
            mainMenuCamera.translate(-leftMoving, 0);
            leftMoving = 0;
            upMoving = game.size * 4;
            //buttons_real_x=0;
            //is_hod = true;
        }
        if (leftMoving > 0){
            mainMenuCamera.translate(-game.speed*delta* slowing, 0);
            leftMoving -= game.speed*delta* slowing;
            buttonsRealX -=game.speed*delta* slowing;
            //if (camera_move_left == 0) is_hod = true;
        }

        if (isCorrectName) game.setScreen(new GameScreen(game));
        Gdx.gl.glClearColor(0.4f, 0.4f, 0.4f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.mainMenuBatch.setProjectionMatrix(mainMenuCamera.combined);
        game.mainMenuBatch.begin();
        game.mainMenuBatch.draw(game.screensaver, game.horizontalIndend, game.verticalIndent, game.size*10, game.size*7);
        slime.draw(game.mainMenuBatch, game.size, delta);
        player.draw(game.mainMenuBatch, game.size, delta);
        if(!game.isEnglish) {
            game.mainMenuBatch.draw(game.beginButton, game.horizontalIndend + buttonsRealX, game.verticalIndent + buttonsRealY + game.size * 2, game.size * 10, game.size);
            game.mainMenuBatch.draw(game.recordButton, game.horizontalIndend + buttonsRealX, game.verticalIndent + buttonsRealY + game.size, game.size * 10, game.size);
            game.mainMenuBatch.draw(game.settingsScreenButton, game.horizontalIndend + buttonsRealX, game.verticalIndent + buttonsRealY + game.size * 0, game.size * 10, game.size);
        }
        else {
            game.mainMenuBatch.draw(game.beginButtonEng, game.horizontalIndend + buttonsRealX, game.verticalIndent + buttonsRealY + game.size * 2, game.size * 10, game.size);
            game.mainMenuBatch.draw(game.recordButtonEng, game.horizontalIndend + buttonsRealX, game.verticalIndent + buttonsRealY + game.size, game.size * 10, game.size);
            game.mainMenuBatch.draw(game.settingsScreenButtonEng, game.horizontalIndend + buttonsRealX, game.verticalIndent + buttonsRealY + game.size * 0, game.size * 10, game.size);
        }
        if (startTimer >=0){
            startTimer -=delta;
            game.mainMenuBatch.draw(game.border, buttonsRealX -game.size*2, buttonsRealY -game.size*2, game.width+game.size*4, game.height+game.size*4);
        }
        mainMenuCamera.update();
        game.mainMenuBatch.end();
    }

    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
    }
}