package com.dungeon_escape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

public class RecordScreen extends ScreenAdapter {

    DungeonEscape game;
    String [] rows;
    int list_num;

    public RecordScreen(DungeonEscape game) {
        this.game = game;
        FileHandle file = Gdx.files.internal("text_resources/records.txt");
        rows = file.readString().split("\n");
        game.record_font.getData().setScale(game.size/30, game.size/30);
        game.record_font.setColor(Color.BLACK);
        list_num = (rows.length-1)/5;
        System.out.println(list_num);
    }

    @Override
    public void show(){
        FileHandle file = Gdx.files.internal("text_resources/records.txt");
        rows = file.readString().split("\n");
        game.record_font.getData().setScale(game.size/30, game.size/30);
        game.record_font.setColor(Color.BLACK);
        list_num = (rows.length-1)/5;
        System.out.println(list_num);
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
        game.batch.begin();
        game.batch.draw(game.return_button, game.horizontal_otstup, game.vertical_otstup+game.size*0, game.size*10, game.size);
        game.batch.draw(game.row_heading, game.horizontal_otstup, game.vertical_otstup+game.size*6, game.size*10, game.size);
        for (int i = 0; i<(list_num)*5; i++){
            String [] string = rows[i+(list_num-1)*5].split(" ");
            game.batch.draw(game.row, game.horizontal_otstup, game.vertical_otstup+game.size*(5-i), game.size*10, game.size);
            game.record_font.draw(game.batch, string[0], game.horizontal_otstup+ game.size*0+game.size/10, game.vertical_otstup+game.size*(6-i)- game.size/4);
        }
        game.batch.end();
    }

    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
    }
}