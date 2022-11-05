package com.dungeon_escape;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainMenuScreen extends ScreenAdapter {

    DungeonEscape game;

    public MainMenuScreen(final DungeonEscape game) {
        game.is_dialog_open = false;
        game.main_menu_batch = new SpriteBatch();
        game.is_correct_name = false;
        this.game = game;
        game.listener = new Input.TextInputListener() {
            @Override
            public void input(String s) {
                if (s.length()<=11&&s.length()>=1){
                    game.name = s;
                    game.is_correct_name = true;
                }
                else {
                    Gdx.input.getTextInput(game.listener, "Name shoud be 1-11 letters", game.name, "");
                }
            }

            @Override
            public void canceled() {
                game.is_correct_name = false;
                game.is_dialog_open = false;
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
                    if (!game.is_dialog_open) {
                        game.is_dialog_open = true;
                        Gdx.input.getTextInput(game.listener, "Enter your name:", game.name, "");
                    }
                    return true;
                }
                if (button == Input.Buttons.LEFT && touch_y == 1 && touch_x >= 0 && touch_x <= 9) {
                    game.setScreen(new RecordScreen(game));
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void render(float delta) {
        if (game.is_correct_name) game.setScreen(new GameScreen(game));
        Gdx.gl.glClearColor(0, 0.25f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.main_menu_batch.begin();
        game.main_menu_batch.draw(game.begin_button, game.horizontal_otstup, game.vertical_otstup+game.size*2, game.size*10, game.size);
        game.main_menu_batch.draw(game.record_button, game.horizontal_otstup, game.vertical_otstup+game.size, game.size*10, game.size);
        game.main_menu_batch.end();
    }

    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
    }
}