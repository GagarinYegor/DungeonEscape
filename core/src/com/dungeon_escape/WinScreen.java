package com.dungeon_escape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.io.IOException;
import java.io.Writer;

public class WinScreen extends ScreenAdapter {

    DungeonEscape game;

    public WinScreen(DungeonEscape game) {
        game.win_screen_batch = new SpriteBatch();
        this.game = game;
        FileHandle win_file = Gdx.files.local("text_resources/records.txt");
        win_file.writeString("\n"+game.name+" "+game.player_lvl+" "+game.moves, true);
        //win_file.writeString(game.name+" "+game.player_lvl+" "+game.moves, false);
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
                return false;
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.win_screen_batch.begin();
        game.win_screen_batch.draw(game.win_screen_img, game.horizontal_otstup, game.vertical_otstup, game.size*10, game.size*7);
        game.win_screen_batch.draw(game.return_button, game.horizontal_otstup+game.size, game.vertical_otstup, game.size*8, game.size);
        game.win_screen_batch.end();
    }

    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
    }
}