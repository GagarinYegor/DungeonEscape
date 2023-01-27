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
        FileHandle saved_file = Gdx.files.local("text_resources/saved_records.txt");
        saved_file.delete();
        slimes = new Slime[10];
        for (int i = 0; i< 10; i++){
            slimes[i] =  new Slime(i, 1, game.size, game.horizontalOtstup, game.verticalOtstup, game.greenSlimeTextureRegion, 6, game.speed, game.slimeBlast, game.greenSlimeAttacking, game.greenSlimeAttacked, game.slimeAttackingSound, game.slimeAttackedSound, game.titleTextTable, game.slimeFont, 100, 100);
        }
        this.game = game;
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
        game.deathScreenBatch.draw(game.deathScreenImg, game.horizontalOtstup, game.verticalOtstup, game.size*10, game.size*7);
        for (Slime slime: slimes){
            slime.draw(game.deathScreenBatch, game.size, delta);
        }
        game.deathScreenBatch.draw(game.deathImg, game.horizontalOtstup +game.size*4, game.verticalOtstup +game.size*3, game.size*2, game.size*2);
        if (!game.is_english) {
            game.deathScreenFont.draw(game.deathScreenBatch, "Игрок " + game.name + " был расплавлен слаймами", game.horizontalOtstup + game.size / 10, game.verticalOtstup + game.size * 6 + game.size / 2);
            game.deathScreenBatch.draw(game.returnButtonLarge, game.horizontalOtstup, game.verticalOtstup, game.size*10, game.size);
        }
        else {
            game.deathScreenFont.draw(game.deathScreenBatch, "Player  " + game.name + " was melted by slimes", game.horizontalOtstup + game.size / 10, game.verticalOtstup + game.size * 6 + game.size / 2);
            game.deathScreenBatch.draw(game.returnButtonLargeEng, game.horizontalOtstup, game.verticalOtstup, game.size*10, game.size);
        }
        game.deathScreenBatch.end();
    }

    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
    }
}