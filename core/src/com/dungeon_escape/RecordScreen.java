package com.dungeon_escape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Scanner;
import java.util.Vector;

public class RecordScreen extends ScreenAdapter {

    DungeonEscape game;
    Vector <String> strings;
    OrthographicCamera record_screen_camera;
    int sdvig;
    float start_timer;

    public RecordScreen(DungeonEscape game) {
        start_timer = 0.1f;
        sdvig = 0;
        game.records_batch = new SpriteBatch();
        this.game = game;
        FileHandle record_file = Gdx.files.local("text_resources/records.txt");
        Scanner records_scan = new Scanner(record_file.read());
        strings = new Vector<>();
        while (records_scan.hasNextLine()){
            strings.add(records_scan.nextLine());
        }

        record_screen_camera = new OrthographicCamera(game.width, game.height);
        record_screen_camera.setToOrtho(false, game.width, game.height);
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
                if (strings.size()>5){
                    if (button == Input.Buttons.LEFT && touch_y == 0 && touch_x == 9 && sdvig != strings.size()-6) {
                        record_screen_camera.translate(0, -game.size);
                        sdvig+=1;
                        start_timer = 0.05f;
                        return true;
                    }
                    if (button == Input.Buttons.LEFT && touch_y == 0 && touch_x == 0 && sdvig !=0) {
                        record_screen_camera.translate(0, game.size);
                        sdvig-=1;
                        start_timer = 0.05f;
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.25f, .25f, 0.25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.records_batch.setProjectionMatrix(record_screen_camera.combined);
        game.records_batch.begin();
        if (strings.size()>6) {
            for (int i = sdvig; i < sdvig + 5; i++) {
                game.records_batch.draw(game.row, game.horizontal_otstup, game.vertical_otstup + game.size * (5 - i), game.size * 10, game.size);
                game.record_font.draw(game.records_batch, strings.get(i + 1).split(" ")[0], game.horizontal_otstup + game.size / 10, game.vertical_otstup + game.size * (6 - i) - game.size / 3);
                game.record_font.draw(game.records_batch, strings.get(i + 1).split(" ")[1], game.horizontal_otstup + game.size / 10 + game.size * 5, game.vertical_otstup + game.size * (6 - i) - game.size / 3);
                game.record_font.draw(game.records_batch, strings.get(i + 1).split(" ")[2], game.horizontal_otstup + game.size / 10 + game.size * 7, game.vertical_otstup + game.size * (6 - i) - game.size / 3);
            }
        }
        else {
            for (int i = 0; i < strings.size() - 1; i++) {
                game.records_batch.draw(game.row, game.horizontal_otstup, game.vertical_otstup + game.size * (5 - i), game.size * 10, game.size);
                game.record_font.draw(game.records_batch, strings.get(i + 1).split(" ")[0], game.horizontal_otstup + game.size / 10, game.vertical_otstup + game.size * (6 - i) - game.size / 3);
                game.record_font.draw(game.records_batch, strings.get(i + 1).split(" ")[1], game.horizontal_otstup + game.size / 10 + game.size * 5, game.vertical_otstup + game.size * (6 - i) - game.size / 3);
                game.record_font.draw(game.records_batch, strings.get(i + 1).split(" ")[2], game.horizontal_otstup + game.size / 10 + game.size * 7, game.vertical_otstup + game.size * (6 - i) - game.size / 3);
            }
        }
        if (sdvig != strings.size()-6 && strings.size()>6) {
            game.records_batch.draw(game.arrow_down, game.horizontal_otstup+game.size*9, game.vertical_otstup-game.size*sdvig, game.size, game.size);
        }
        else game.records_batch.draw(game.arrow_no, game.horizontal_otstup+game.size*9, game.vertical_otstup-game.size*sdvig, game.size, game.size);
        if (sdvig !=0) {
            game.records_batch.draw(game.arrow_up, game.horizontal_otstup, game.vertical_otstup-game.size*sdvig, game.size, game.size);
        }
        else game.records_batch.draw(game.arrow_no, game.horizontal_otstup, game.vertical_otstup-game.size*sdvig, game.size, game.size);
        if (!game.is_english) {
            game.records_batch.draw(game.return_button, game.horizontal_otstup + game.size, game.vertical_otstup - game.size * sdvig, game.size * 8, game.size);
        }
        else {
            game.records_batch.draw(game.return_button_eng, game.horizontal_otstup + game.size, game.vertical_otstup - game.size * sdvig, game.size * 8, game.size);
        }
        game.records_batch.draw(game.row_heading, game.horizontal_otstup, game.vertical_otstup+game.size*6-game.size*sdvig, game.size*10, game.size);
        if (start_timer>=0){
            start_timer-=delta;
            game.records_batch.draw(game.border, -game.size, -game.size-game.size*sdvig, game.width+game.size*2, game.height+game.size*2);
        }
        record_screen_camera.update();
        game.records_batch.end();
    }

    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
    }
}