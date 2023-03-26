package com.dungeonEscape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Scanner;
import java.util.Vector;

public class RecordScreen extends ScreenAdapter {

    DungeonEscape game;
    Vector <String> strings;
    OrthographicCamera recordScreenCamera;
    int slip;
    float startTimer;

    public RecordScreen(DungeonEscape game) {
        startTimer = 0.1f;
        slip = 0;
        game.recordsBatch = new SpriteBatch();
        this.game = game;
        FileHandle record_file = Gdx.files.local("text_resources/records.txt");
        if (!record_file.exists()){
            FileHandle file = Gdx.files.local("text_resources/records.txt");
            file.writeString("", false);
        }
        Scanner records_scan = new Scanner(record_file.read());
        strings = new Vector<>();
        while (records_scan.hasNextLine()){
            strings.add(records_scan.nextLine());
        }

        recordScreenCamera = new OrthographicCamera(game.width, game.height);
        recordScreenCamera.setToOrtho(false, game.width, game.height);
    }

    @Override
    public void show(){
        Gdx.input.setInputProcessor(new InputAdapter() {
            public boolean touchDown (int x, int y, int pointer, int button) {
                int touch_x;
                int touch_y;
                if ((Gdx.input.getX()-game.horizontalIndend) / game.size>=0){
                    touch_x = (int) ((Gdx.input.getX()-game.horizontalIndend) / game.size);
                }
                else{
                    touch_x = (int) ((Gdx.input.getX()-game.horizontalIndend) / game.size - 1);
                }
                if ((game.height - (game.verticalIndent +Gdx.input.getY())) / game.size >=0){
                    touch_y= (int) ((game.height - (game.verticalIndent +Gdx.input.getY())) / game.size);
                }
                else {
                    touch_y = (int) ((game.height - (game.verticalIndent +Gdx.input.getY())) / game.size - 1);
                }
                if (button == Input.Buttons.LEFT && touch_y == 0 && touch_x >= 1 && touch_x <= 8) {
                    game.setScreen(new MainMenuScreen(game));
                    return true;
                }
                if (strings.size()>5){
                    if (button == Input.Buttons.LEFT && touch_y == 0 && touch_x == 9 && slip != strings.size()-6) {
                        recordScreenCamera.translate(0, -game.size);
                        slip +=1;
                        startTimer = 0.05f;
                        return true;
                    }
                    if (button == Input.Buttons.LEFT && touch_y == 0 && touch_x == 0 && slip !=0) {
                        recordScreenCamera.translate(0, game.size);
                        slip -=1;
                        startTimer = 0.05f;
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
        game.recordsBatch.setProjectionMatrix(recordScreenCamera.combined);
        game.recordsBatch.begin();
        if (strings.size()>6) {
            for (int i = slip; i < slip + 5; i++) {
                game.recordsBatch.draw(game.row, game.horizontalIndend, game.verticalIndent + game.size * (5 - i), game.size * 10, game.size);
                game.recordFont.draw(game.recordsBatch, strings.get(i + 1).split(" ")[0], game.horizontalIndend + game.size / 10, game.verticalIndent + game.size * (6 - i) - game.size / 3);
                game.recordFont.draw(game.recordsBatch, strings.get(i + 1).split(" ")[1], game.horizontalIndend + game.size / 10 + game.size * 3.3f, game.verticalIndent + game.size * (6 - i) - game.size / 3);
                if (!game.isEnglish) {
                    if (strings.get(i + 1).split(" ")[2].contains("true")){
                        game.recordFont.draw(game.recordsBatch, "Пройдено", game.horizontalIndend + game.size / 10 + game.size * 6.6f, game.verticalIndent + game.size * (6 - i) - game.size / 3);
                    }
                    else {
                        game.recordFont.draw(game.recordsBatch, "Не пройдено", game.horizontalIndend + game.size / 10 + game.size * 6.6f, game.verticalIndent + game.size * (6 - i) - game.size / 3);
                    }
                }
                else {
                    if (strings.get(i + 1).split(" ")[2].contains("true")){
                        game.recordFont.draw(game.recordsBatch, "Passed", game.horizontalIndend + game.size / 10 + game.size * 6.6f, game.verticalIndent + game.size * (6 - i) - game.size / 3);
                    }
                    else {
                        game.recordFont.draw(game.recordsBatch, "Not passed", game.horizontalIndend + game.size / 10 + game.size * 6.6f, game.verticalIndent + game.size * (6 - i) - game.size / 3);
                    }
                }
            }
        }
        else {
            for (int i = 0; i < strings.size() - 1; i++) {
                game.recordsBatch.draw(game.row, game.horizontalIndend, game.verticalIndent + game.size * (5 - i), game.size * 10, game.size);
                game.recordFont.draw(game.recordsBatch, strings.get(i + 1).split(" ")[0], game.horizontalIndend + game.size / 10, game.verticalIndent + game.size * (6 - i) - game.size / 3);
                game.recordFont.draw(game.recordsBatch, strings.get(i + 1).split(" ")[1], game.horizontalIndend + game.size / 10 + game.size * 3.3f, game.verticalIndent + game.size * (6 - i) - game.size / 3);
                if (!game.isEnglish) {
                    if (strings.get(i + 1).split(" ")[2].contains("true")){
                        game.recordFont.draw(game.recordsBatch, "Пройдено", game.horizontalIndend + game.size / 10 + game.size * 6.6f, game.verticalIndent + game.size * (6 - i) - game.size / 3);
                    }
                    else {
                        game.recordFont.draw(game.recordsBatch, "Не пройдено", game.horizontalIndend + game.size / 10 + game.size * 6.6f, game.verticalIndent + game.size * (6 - i) - game.size / 3);
                    }
                }
                else {
                    if (strings.get(i + 1).split(" ")[2].contains("true")){
                        game.recordFont.draw(game.recordsBatch, "Passed", game.horizontalIndend + game.size / 10 + game.size * 6.6f, game.verticalIndent + game.size * (6 - i) - game.size / 3);
                    }
                    else {
                        game.recordFont.draw(game.recordsBatch, "Not passed", game.horizontalIndend + game.size / 10 + game.size * 6.6f, game.verticalIndent + game.size * (6 - i) - game.size / 3);
                    }
                }
           }
        }
        if (slip != strings.size()-6 && strings.size()>6) {
            game.recordsBatch.draw(game.arrowDown, game.horizontalIndend +game.size*9, game.verticalIndent -game.size* slip, game.size, game.size);
        }
        else game.recordsBatch.draw(game.arrowNo, game.horizontalIndend +game.size*9, game.verticalIndent -game.size* slip, game.size, game.size);
        if (slip !=0) {
            game.recordsBatch.draw(game.arrowUp, game.horizontalIndend, game.verticalIndent -game.size* slip, game.size, game.size);
        }
        else game.recordsBatch.draw(game.arrowNo, game.horizontalIndend, game.verticalIndent -game.size* slip, game.size, game.size);
        if (!game.isEnglish) {
            game.recordsBatch.draw(game.returnButton, game.horizontalIndend + game.size, game.verticalIndent - game.size * slip, game.size * 8, game.size);
        }
        else {
            game.recordsBatch.draw(game.returnButtonEng, game.horizontalIndend + game.size, game.verticalIndent - game.size * slip, game.size * 8, game.size);
        }
        if (!game.isEnglish) {
            game.recordsBatch.draw(game.rowHeading, game.horizontalIndend, game.verticalIndent + game.size * 6 - game.size * slip, game.size * 10, game.size);
        }
        else {
            game.recordsBatch.draw(game.rowHeadingEng, game.horizontalIndend, game.verticalIndent + game.size * 6 - game.size * slip, game.size * 10, game.size);
        }
        if (startTimer >=0){
            startTimer -=delta;
            game.recordsBatch.draw(game.border, -game.size, -game.size-game.size* slip, game.width+game.size*2, game.height+game.size*2);
        }
        recordScreenCamera.update();
        game.recordsBatch.end();
    }

    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
    }
}