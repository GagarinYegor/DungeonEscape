package com.dungeon_escape;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
        game.main_menu_batch = new SpriteBatch();

        right_moving = game.size;
        left_moving = 0;
        up_moving = 0;
        down_moving = 0;

        main_menu_camera = new OrthographicCamera(game.width, game.height);
        main_menu_camera.setToOrtho(false, game.width, game.height);
        main_menu_camera.translate(game.size, game.size);
        buttons_real_x= game.size;
        buttons_real_y=game.size;

        slime = new Slime(7, 5, game.size, game.horizontal_otstup, game.vertical_otstup,
                game.green_slime_texture_region, 6, game.speed, game.slime_blast,
                game.green_slime_attacking, game.green_slime_attacked,
                game.slime_attacking_sound, game.slime_attacked_sound, game.title_text_table);

        player = new Player(2, 5, game.size, game.horizontal_otstup, game.vertical_otstup,
                game.player_texture_region_right, game.player_texture_region_left,
                12,
                game.player_texture_region_mowing_right, game.player_texture_region_mowing_left,
                14,
                game.speed, game.player_blast,
                game.player_attacking_right, game.player_attacked_right,
                game.player_attacking_left, game.player_attacked_left,
                game.player_attacking_sound, game.player_attacked_sound, "");

        is_dialog_open = false;
        is_correct_name = false;

        game.listener = new Input.TextInputListener() {
            @Override
            public void input(String s) {
                if (s.length()<=10&&s.length()>=1){
                    game.name = s;
                    is_correct_name = true;
                }
                else {
                    if (!game.is_english){
                        Gdx.input.getTextInput(game.listener, "?????? ???????????? ???????????????? ???? 1-10 ????????", game.name, "");
                    }
                    else {
                        Gdx.input.getTextInput(game.listener, "Name shoud be 1-10 letters", game.name, "");
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
                if ((Gdx.input.getX()-game.horizontal_otstup) / game.size>=0){
                    touch_x = (int) ((Gdx.input.getX()-game.horizontal_otstup) / game.size);
                }
                else{
                    touch_x = (int) ((Gdx.input.getX()-game.horizontal_otstup) / game.size - 1);
                }
                if ((game.height - (game.vertical_otstup+Gdx.input.getY())) / game.size >=0){
                    touch_y= (int) ((game.height - (game.vertical_otstup+Gdx.input.getY())) / game.size);
                }
                else {
                    touch_y = (int) ((game.height - (game.vertical_otstup+Gdx.input.getY())) / game.size - 1);
                }
                if (button == Input.Buttons.LEFT && touch_y == 2 && touch_x >= 0 && touch_x <= 9) {
                    if (!is_dialog_open) {
                        is_dialog_open = true;
                        if (!game.is_english) {
                            Gdx.input.getTextInput(game.listener, "?????????????? ??????:", game.name, "");
                        }
                        else {
                            Gdx.input.getTextInput(game.listener, "Enter your name:", game.name, "");
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
        game.main_menu_batch.setProjectionMatrix(main_menu_camera.combined);
        game.main_menu_batch.begin();
        game.main_menu_batch.draw(game.screensaver, game.horizontal_otstup, game.vertical_otstup, game.size*10, game.size*7);
        slime.draw(game.main_menu_batch, game.size, delta);
        player.draw(game.main_menu_batch, game.size, delta);
        if(!game.is_english) {
            game.main_menu_batch.draw(game.begin_button, game.horizontal_otstup + buttons_real_x, game.vertical_otstup + buttons_real_y + game.size * 2, game.size * 10, game.size);
            game.main_menu_batch.draw(game.record_button, game.horizontal_otstup + buttons_real_x, game.vertical_otstup + buttons_real_y + game.size, game.size * 10, game.size);
            game.main_menu_batch.draw(game.settings_screen_button, game.horizontal_otstup + buttons_real_x, game.vertical_otstup + buttons_real_y + game.size * 0, game.size * 10, game.size);
        }
        else {
            game.main_menu_batch.draw(game.begin_button_eng, game.horizontal_otstup + buttons_real_x, game.vertical_otstup + buttons_real_y + game.size * 2, game.size * 10, game.size);
            game.main_menu_batch.draw(game.record_button_eng, game.horizontal_otstup + buttons_real_x, game.vertical_otstup + buttons_real_y + game.size, game.size * 10, game.size);
            game.main_menu_batch.draw(game.settings_screen_button_eng, game.horizontal_otstup + buttons_real_x, game.vertical_otstup + buttons_real_y + game.size * 0, game.size * 10, game.size);
        }
        if (start_timer>=0){
            start_timer-=delta;
            game.main_menu_batch.draw(game.border, buttons_real_x-game.size*2, buttons_real_y-game.size*2, game.width+game.size*4, game.height+game.size*4);
        }
        main_menu_camera.update();
        game.main_menu_batch.end();
    }

    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
    }
}