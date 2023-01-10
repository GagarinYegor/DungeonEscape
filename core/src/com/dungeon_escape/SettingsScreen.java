package com.dungeon_escape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SettingsScreen extends ScreenAdapter {

    DungeonEscape game;
    float start_timer;

    public SettingsScreen(DungeonEscape game) {
        start_timer = 0.1f;
        game.settings_batch = new SpriteBatch();
        this.game = game;
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
                if (button == Input.Buttons.LEFT && touch_y == 0 && touch_x >= 1 && touch_x <= 8) {
                    game.setScreen(new MainMenuScreen(game));
                    return true;
                }
                if (button == Input.Buttons.LEFT && touch_y == 1 && touch_x >= 0 && touch_x <= 9) {
                    FileHandle win_file = Gdx.files.local("text_resources/records.txt");
                    win_file.writeString("", false);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.25f, 0.25f, 0.25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.settings_batch.begin();
        if (!game.is_english) {
            game.settings_batch.draw(game.return_button_large, game.horizontal_otstup, game.vertical_otstup, game.size * 10, game.size);
            game.settings_batch.draw(game.delete_button, game.horizontal_otstup, game.vertical_otstup + game.size * 1, game.size * 10, game.size);
        }
        else {
            game.settings_batch.draw(game.return_button_large_eng, game.horizontal_otstup, game.vertical_otstup, game.size * 10, game.size);
            game.settings_batch.draw(game.delete_button_eng, game.horizontal_otstup, game.vertical_otstup + game.size * 1, game.size * 10, game.size);
        }
        if (start_timer>=0){
            start_timer-=delta;
            game.settings_batch.draw(game.border, -game.size, -game.size, game.width+game.size*2, game.height+game.size*2);
        }
        game.settings_batch.end();
    }

    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
    }
}
