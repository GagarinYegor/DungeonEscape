package com.dungeonEscape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;

public class WinScreen extends ScreenAdapter {

    DungeonEscape game;

    public WinScreen(DungeonEscape game) {
        this.game = game;
        FileHandle winFile = Gdx.files.local("text_resources/records.txt");
        winFile.writeString("\n"+game.name+" "+game.moves+" true", true);
        FileHandle savedFile = Gdx.files.local("text_resources/saved_records.txt");
        savedFile.delete();
    }

    @Override
    public void show(){
        Gdx.input.setInputProcessor(new InputAdapter() {
            public boolean touchDown (int x, int y, int pointer, int button) {
                int touchX;
                int touchY;
                if ((Gdx.input.getX()-game.horizontalIndend) / game.size>=0){
                    touchX = (int) ((Gdx.input.getX()-game.horizontalIndend) / game.size);
                }
                else{
                    touchX = (int) ((Gdx.input.getX()-game.horizontalIndend) / game.size - 1);
                }
                if ((game.height - (game.verticalIndent +Gdx.input.getY())) / game.size >=0){
                    touchY= (int) ((game.height - (game.verticalIndent +Gdx.input.getY())) / game.size);
                }
                else {
                    touchY = (int) ((game.height - (game.verticalIndent +Gdx.input.getY())) / game.size - 1);
                }
                if (button == Input.Buttons.LEFT && touchY == 0 && touchX >= 1 && touchX <= 8) {
                    game.setScreen(new MainMenuScreen(game));
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.winScreenBatch.begin();
        game.winScreenBatch.draw(game.winScreenImg, game.horizontalIndend, game.verticalIndent, game.size*10, game.size*7);
        if (!game.isEnglish) {
            game.winScreenFont.draw(game.winScreenBatch, "Игрок " + game.name + " успешно покинул подземелье!", game.horizontalIndend + game.size / 10, game.verticalIndent + game.size * 6 + game.size / 2);
            game.winScreenBatch.draw(game.returnButtonLarge, game.horizontalIndend, game.verticalIndent, game.size*10, game.size);
        }
        else {
            game.winScreenFont.draw(game.winScreenBatch, "Player  " + game.name + " is finally escaped the dungeon!", game.horizontalIndend + game.size / 10, game.verticalIndent + game.size * 6 + game.size / 2);
            game.winScreenBatch.draw(game.returnButtonLargeEng, game.horizontalIndend, game.verticalIndent, game.size*10, game.size);
        }
        game.winScreenBatch.end();
    }

    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
    }
}