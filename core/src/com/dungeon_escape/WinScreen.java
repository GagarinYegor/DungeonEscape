package com.dungeon_escape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class WinScreen extends ScreenAdapter {

    DungeonEscape game;

    public WinScreen(DungeonEscape game) {
        game.winScreenBatch = new SpriteBatch();
        this.game = game;
        FileHandle win_file = Gdx.files.local("text_resources/records.txt");
        win_file.writeString("\n"+game.name+" "+game.moves+" true", true);
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
        game.winScreenBatch.begin();
        game.winScreenBatch.draw(game.win_screen_img, game.horizontal_otstup, game.vertical_otstup, game.size*10, game.size*7);
        if (!game.is_english) {
            game.winScreenFont.draw(game.winScreenBatch, "Игрок " + game.name + " успешно покинул подземелье!", game.horizontal_otstup + game.size / 10, game.vertical_otstup + game.size * 6 + game.size / 2);
            game.winScreenBatch.draw(game.return_button_large, game.horizontal_otstup, game.vertical_otstup, game.size*10, game.size);
        }
        else {
            game.winScreenFont.draw(game.winScreenBatch, "Player  " + game.name + " is finally escaped the dungeon!", game.horizontal_otstup + game.size / 10, game.vertical_otstup + game.size * 6 + game.size / 2);
            game.winScreenBatch.draw(game.return_button_large_eng, game.horizontal_otstup, game.vertical_otstup, game.size*10, game.size);
        }
        game.winScreenBatch.end();
    }

    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
    }
}