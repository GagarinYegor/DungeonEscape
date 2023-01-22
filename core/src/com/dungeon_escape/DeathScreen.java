package com.dungeon_escape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DeathScreen extends ScreenAdapter {

    DungeonEscape game;
    Slime [] slimes;

    public DeathScreen(DungeonEscape game) {
        game.deathScreenBatch = new SpriteBatch();
        this.game = game;
        FileHandle win_file = Gdx.files.local("text_resources/records.txt");
        win_file.writeString("\n"+game.name+" "+game.moves+" false", true);
        slimes = new Slime[10];
        for (int i = 0; i< 10; i++){
            slimes[i] =  new Slime(i, 1, game.size, game.horizontal_otstup, game.vertical_otstup, game.green_slime_texture_region, 6, game.speed, game.slime_blast, game.green_slime_attacking, game.green_slime_attacked, game.slime_attacking_sound, game.slime_attacked_sound, game.title_text_table, game.slimeFont);
        }
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
                if (button == Input.Buttons.LEFT && touch_y == 0 && touch_x >= 0 && touch_x <= 9) {
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
        game.deathScreenBatch.begin();
        game.deathScreenBatch.draw(game.death_screen_img, game.horizontal_otstup, game.vertical_otstup, game.size*10, game.size*7);
        for (Slime slime: slimes){
            slime.draw(game.deathScreenBatch, game.size, delta);
        }
        game.deathScreenBatch.draw(game.death_img, game.horizontal_otstup+game.size*4, game.vertical_otstup+game.size*3, game.size*2, game.size*2);
        if (!game.is_english) {
            game.deathScreenFont.draw(game.deathScreenBatch, "Игрок " + game.name + " был расплавлен слаймами", game.horizontal_otstup + game.size / 10, game.vertical_otstup + game.size * 6 + game.size / 2);
            game.deathScreenBatch.draw(game.return_button_large, game.horizontal_otstup, game.vertical_otstup, game.size*10, game.size);
        }
        else {
            game.deathScreenFont.draw(game.deathScreenBatch, "Player  " + game.name + " was melted by slimes", game.horizontal_otstup + game.size / 10, game.vertical_otstup + game.size * 6 + game.size / 2);
            game.deathScreenBatch.draw(game.return_button_large_eng, game.horizontal_otstup, game.vertical_otstup, game.size*10, game.size);
        }
        game.deathScreenBatch.end();
    }

    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
    }
}