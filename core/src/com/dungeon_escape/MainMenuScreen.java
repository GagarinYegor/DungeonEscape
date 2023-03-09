package com.dungeon_escape;
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
    float right_moving, left_moving, up_moving, down_moving,
            buttons_real_x, buttons_real_y, zamedlenie, start_timer;
    boolean is_correct_name, is_dialog_open;
    OrthographicCamera main_menu_camera;

    public MainMenuScreen(final DungeonEscape game) {
        this.game = game;
        zamedlenie = 0.5f;
        start_timer = 0.1f;
        game.mainMenuBatch = new SpriteBatch();

        right_moving = game.size;
        left_moving = 0;
        up_moving = 0;
        down_moving = 0;

        main_menu_camera = new OrthographicCamera(game.width, game.height);
        main_menu_camera.setToOrtho(false, game.width, game.height);
        main_menu_camera.translate(game.size, game.size);
        buttons_real_x= game.size;
        buttons_real_y=game.size;

        slime = new Slime(7, 5, game.size, game.horizontalOtstup, game.verticalOtstup,
                game.greenSlimeTextureRegion, 6, game.speed, game.slimeBlast,
                game.greenSlimeAttacking, game.greenSlimeAttacked,
                game.slimeAttackingSound, game.slimeAttackedSound, game.titleTextTable, game.slimeFont, 100, 100);

        player = new Player(2, 5, game.size, game.horizontalOtstup, game.verticalOtstup,
                game.playerTextureRegionRight, game.playerTextureRegionLeft,
                12,
                game.playerTextureRegionMowingRight, game.playerTextureRegionMowingLeft,
                14,
                game.speed, game.playerBlast,
                game.playerAttackingRight, game.playerAttackedRight,
                game.playerAttackingLeft, game.playerAttackedLeft,
                game.playerAttackingSound, game.sound, "", 100, 100);

        is_dialog_open = false;
        is_correct_name = false;

        game.listener = new Input.TextInputListener() {
            @Override
            public void input(String s) {
                if (s.length()<=10&&s.length()>=1&&s.indexOf(" ")==-1){
                    game.name = s;
                    is_correct_name = true;
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
                is_correct_name = false;
                is_dialog_open = false;
            }
        };
    }


    @Override
    public void show(){
        Gdx.input.setInputProcessor(new InputAdapter() {
            public boolean touchDown (int x, int y, int pointer, int button) {
                int touch_x;
                int touch_y;
                if ((Gdx.input.getX()-game.horizontalOtstup) / game.size>=0){
                    touch_x = (int) ((Gdx.input.getX()-game.horizontalOtstup) / game.size);
                }
                else{
                    touch_x = (int) ((Gdx.input.getX()-game.horizontalOtstup) / game.size - 1);
                }
                if ((game.height - (game.verticalOtstup +Gdx.input.getY())) / game.size >=0){
                    touch_y= (int) ((game.height - (game.verticalOtstup +Gdx.input.getY())) / game.size);
                }
                else {
                    touch_y = (int) ((game.height - (game.verticalOtstup +Gdx.input.getY())) / game.size - 1);
                }
                if (button == Input.Buttons.LEFT && touch_y == 2 && touch_x >= 0 && touch_x <= 9) {
                    FileHandle saved_file = Gdx.files.local("text_resources/saved_records.txt");
                    if (saved_file.exists()) {
                        is_correct_name = true;
                    }
                    else {
                        if (!is_dialog_open) {
                            is_dialog_open = true;
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
                    if (!is_dialog_open) {
                        game.setScreen(new RecordScreen(game));
                    }
                    return true;
                }
                if (button == Input.Buttons.LEFT && touch_y == 0 && touch_x >= 0 && touch_x <= 9) {
                    if (!is_dialog_open) {
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
        if (game.speed*delta*zamedlenie > up_moving && up_moving > 0){
            main_menu_camera.translate(0, up_moving);
            up_moving = 0;
            right_moving = game.size*4;
            //buttons_real_y=0;
            //is_hod = true;
        }
        if (up_moving > 0){
            main_menu_camera.translate(0, game.speed*delta*zamedlenie);
            up_moving -= game.speed*delta*zamedlenie;
            buttons_real_y+=game.speed*delta*zamedlenie;
            //if (camera_move_up == 0) is_hod = true;
        }
        if (game.speed*delta*zamedlenie > right_moving && right_moving > 0){
            main_menu_camera.translate(right_moving, 0);
            right_moving = 0;
            down_moving = game.size*4;
            //buttons_real_x=0;
            //is_hod = true;
        }
        if (right_moving > 0){
            main_menu_camera.translate(game.speed*delta*zamedlenie, 0);
            right_moving -= game.speed*delta*zamedlenie;
            buttons_real_x+=game.speed*delta*zamedlenie;
            //if (right_moving == 0) is_hod = true;
        }
        if (game.speed*delta*zamedlenie > down_moving && down_moving > 0){
            main_menu_camera.translate(0, -down_moving);
            down_moving = 0;
            left_moving = game.size*4;
            //buttons_real_y=0;
            //is_hod = true;
        }
        if (down_moving > 0){
            main_menu_camera.translate(0, -game.speed*delta*zamedlenie);
            down_moving -= game.speed*delta*zamedlenie;
            buttons_real_y-=game.speed*delta*zamedlenie;
            //if (camera_move_down == 0) is_hod = true;
        }
        if (game.speed*delta*zamedlenie > left_moving && left_moving > 0){
            main_menu_camera.translate(-left_moving, 0);
            left_moving = 0;
            up_moving = game.size*4;
            //buttons_real_x=0;
            //is_hod = true;
        }
        if (left_moving > 0){
            main_menu_camera.translate(-game.speed*delta*zamedlenie, 0);
            left_moving -= game.speed*delta*zamedlenie;
            buttons_real_x -=game.speed*delta*zamedlenie;
            //if (camera_move_left == 0) is_hod = true;
        }

        if (is_correct_name) game.setScreen(new GameScreen(game));
        Gdx.gl.glClearColor(0.4f, 0.4f, 0.4f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.mainMenuBatch.setProjectionMatrix(main_menu_camera.combined);
        game.mainMenuBatch.begin();
        game.mainMenuBatch.draw(game.screensaver, game.horizontalOtstup, game.verticalOtstup, game.size*10, game.size*7);
        slime.draw(game.mainMenuBatch, game.size, delta);
        player.draw(game.mainMenuBatch, game.size, delta);
        if(!game.isEnglish) {
            game.mainMenuBatch.draw(game.beginButton, game.horizontalOtstup + buttons_real_x, game.verticalOtstup + buttons_real_y + game.size * 2, game.size * 10, game.size);
            game.mainMenuBatch.draw(game.recordButton, game.horizontalOtstup + buttons_real_x, game.verticalOtstup + buttons_real_y + game.size, game.size * 10, game.size);
            game.mainMenuBatch.draw(game.settingsScreenButton, game.horizontalOtstup + buttons_real_x, game.verticalOtstup + buttons_real_y + game.size * 0, game.size * 10, game.size);
        }
        else {
            game.mainMenuBatch.draw(game.beginButtonEng, game.horizontalOtstup + buttons_real_x, game.verticalOtstup + buttons_real_y + game.size * 2, game.size * 10, game.size);
            game.mainMenuBatch.draw(game.recordButtonEng, game.horizontalOtstup + buttons_real_x, game.verticalOtstup + buttons_real_y + game.size, game.size * 10, game.size);
            game.mainMenuBatch.draw(game.settingsScreenButtonEng, game.horizontalOtstup + buttons_real_x, game.verticalOtstup + buttons_real_y + game.size * 0, game.size * 10, game.size);
        }
        if (start_timer>=0){
            start_timer-=delta;
            game.mainMenuBatch.draw(game.border, buttons_real_x-game.size*2, buttons_real_y-game.size*2, game.width+game.size*4, game.height+game.size*4);
        }
        main_menu_camera.update();
        game.mainMenuBatch.end();
    }

    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
    }
}