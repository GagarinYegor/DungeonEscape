package com.dungeon_escape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Scanner;
import java.util.Vector;

public class RecordScreen extends ScreenAdapter {

    DungeonEscape game;
    Vector <String> strings;

    public RecordScreen(DungeonEscape game) {
        game.records_batch = new SpriteBatch();
        this.game = game;
        FileHandle record_file = Gdx.files.internal("text_resources/records.txt");
        Scanner records_scan = new Scanner(record_file.read());
        strings = new Vector<>();
        while (records_scan.hasNextLine()){
            strings.add(records_scan.nextLine());
        }
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
        Gdx.gl.glClearColor(0, .25f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.records_batch.begin();
        game.records_batch.draw(game.return_button, game.horizontal_otstup, game.vertical_otstup, game.size*10, game.size);
        game.records_batch.draw(game.row_heading, game.horizontal_otstup, game.vertical_otstup+game.size*6, game.size*10, game.size);
        for (int i = 0; i<strings.size(); i++){
            game.records_batch.draw(game.row, game.horizontal_otstup, game.vertical_otstup+game.size*(5-i), game.size*10, game.size);
            game.record_font.draw(game.records_batch, strings.get(i).split(" ")[0], game.horizontal_otstup+game.size/10, game.vertical_otstup+game.size*(6-i)- game.size/4);
            game.record_font.draw(game.records_batch, strings.get(i).split(" ")[1], game.horizontal_otstup+game.size/10+ game.size*5, game.vertical_otstup+game.size*(6-i)- game.size/4);
            game.record_font.draw(game.records_batch, strings.get(i).split(" ")[2], game.horizontal_otstup+game.size/10+ game.size*7, game.vertical_otstup+game.size*(6-i)- game.size/4);
        }
        game.records_batch.end();
    }

    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
    }
}