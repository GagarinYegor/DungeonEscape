package com.dungeonEscape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SettingsScreen extends ScreenAdapter {

    DungeonEscape game;
    float startTimer;
    boolean isDialogOpen;

    public SettingsScreen(final DungeonEscape game) {
        startTimer = 0.1f;
        isDialogOpen = false;
        game.settingsBatch = new SpriteBatch();
        this.game = game;
        game.settingsListener = new Input.TextInputListener() {
            @Override
            public void input(String s) {
                if ((!game.isEnglish && s.equals("Да")) || (game.isEnglish && s.equals("Yes"))) {
                    startTimer = 0.1f;
                    FileHandle win_file = Gdx.files.local("text_resources/records.txt");
                    win_file.writeString("", false);
                    isDialogOpen = false;
                }
                else {
                    if (!game.isEnglish) {
                        isDialogOpen = true;
                        Gdx.input.getTextInput(game.settingsListener, "Вы уверены что хотите очистить попытки?", "", "Введите \"Да\" в это поле");
                    } else {
                        isDialogOpen = true;
                        Gdx.input.getTextInput(game.settingsListener, "Are you sure you want to erase the history?", "", "Enter \"Yes\" here");
                    }
                }
            }
            @Override
            public void canceled() {
                isDialogOpen = false;
            }
        };
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
                if (button == Input.Buttons.LEFT && touch_y == 0 && touch_x >= 0 && touch_x <= 9 && !isDialogOpen) {
                    game.setScreen(new MainMenuScreen(game));
                    return true;
                }
                if (button == Input.Buttons.LEFT && touch_y == 3 && touch_x >= 0 && touch_x <= 4 && !game.isEnglish && !isDialogOpen) {
                    startTimer = 0.1f;
                    game.isEnglish = true;
                    return true;
                }
                if (button == Input.Buttons.LEFT && touch_y == 3 && touch_x >= 5 && touch_x <= 9 && game.isEnglish && !isDialogOpen) {
                    startTimer = 0.1f;
                    game.isEnglish = false;
                    return true;
                }
                if (button == Input.Buttons.LEFT && touch_y == 1 && touch_x >= 0 && touch_x <= 9) {
                    if (!game.isEnglish) {
                        isDialogOpen = true;
                        Gdx.input.getTextInput(game.settingsListener, "Вы уверены что хотите очистить попытки?", "", "Введите \"Да\" в это поле");
                    } else {
                        isDialogOpen = true;
                        Gdx.input.getTextInput(game.settingsListener, "Are you sure you want to erase the history?", "", "Enter \"Yes\" here");
                    }
                }
                if (button == Input.Buttons.LEFT && touch_y == 5 && touch_x >= 0 && touch_x <= 1 && !game.attackButtonAutoReset && !isDialogOpen) {
                    startTimer = 0.1f;
                    game.attackButtonAutoReset = true;
                    return true;
                }
                if (button == Input.Buttons.LEFT && touch_y == 5 && touch_x >= 8 && touch_x <= 9 && game.attackButtonAutoReset && !isDialogOpen) {
                    startTimer = 0.1f;
                    game.attackButtonAutoReset = false;
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
        game.settingsBatch.begin();
        if (!game.isEnglish){
            game.settingsBatch.draw(game.englishButton, game.horizontalIndend, game.verticalIndent +game.size*3, game.size*5, game.size);
            game.settingsBatch.draw(game.russianButtonActive, game.horizontalIndend +game.size*5, game.verticalIndent +game.size*3, game.size*5, game.size);
        }
        else {
            game.settingsBatch.draw(game.englishButtonActive, game.horizontalIndend, game.verticalIndent +game.size*3, game.size*5, game.size);
            game.settingsBatch.draw(game.russianButton, game.horizontalIndend +game.size*5, game.verticalIndent +game.size*3, game.size*5, game.size);
        }
        game.settingsBatch.draw(game.emptyButton, game.horizontalIndend, game.verticalIndent +game.size*4, game.size*10, game.size);
        game.settingsBatch.draw(game.emptyButton, game.horizontalIndend, game.verticalIndent +game.size*6, game.size*10, game.size);
        if (!game.isEnglish) {
            game.settingFont.draw(game.settingsBatch, "Язык интерфейса:", game.horizontalIndend +game.size/10, game.verticalIndent +game.size*5-game.size/3);
            game.settingFont.draw(game.settingsBatch, "Автоотключение кнопки атаки:", game.horizontalIndend +game.size/10, game.verticalIndent +game.size*7-game.size/3);
            game.settingsBatch.draw(game.returnButtonLarge, game.horizontalIndend, game.verticalIndent, game.size * 10, game.size);
            game.settingsBatch.draw(game.deleteButton, game.horizontalIndend, game.verticalIndent + game.size * 1, game.size * 10, game.size);
            if (!game.attackButtonAutoReset) {
                game.settingsBatch.draw(game.yesButton, game.horizontalIndend, game.verticalIndent + game.size * 5, game.size * 2, game.size);
                game.settingsBatch.draw(game.noButtonActive, game.horizontalIndend + game.size * 8, game.verticalIndent + game.size * 5, game.size * 2, game.size);
            }
            else {
                game.settingsBatch.draw(game.yesButtonActive, game.horizontalIndend, game.verticalIndent + game.size * 5, game.size * 2, game.size);
                game.settingsBatch.draw(game.noButton, game.horizontalIndend + game.size * 8, game.verticalIndent + game.size * 5, game.size * 2, game.size);
            }
        }
        else {
            game.settingFont.draw(game.settingsBatch, "Interface language:", game.horizontalIndend +game.size/10, game.verticalIndent +game.size*5-game.size/3);
            game.settingFont.draw(game.settingsBatch, "Auto restart attack button:", game.horizontalIndend +game.size/10, game.verticalIndent +game.size*7-game.size/3);
            game.settingsBatch.draw(game.returnButtonLargeEng, game.horizontalIndend, game.verticalIndent, game.size * 10, game.size);
            game.settingsBatch.draw(game.deleteButtonEng, game.horizontalIndend, game.verticalIndent + game.size * 1, game.size * 10, game.size);
            if (!game.attackButtonAutoReset) {
                game.settingsBatch.draw(game.yesButtonEng, game.horizontalIndend, game.verticalIndent + game.size * 5, game.size * 2, game.size);
                game.settingsBatch.draw(game.noButtonEngActive, game.horizontalIndend + game.size * 8, game.verticalIndent + game.size * 5, game.size * 2, game.size);
            }
            else {
                game.settingsBatch.draw(game.yesButtonEngActive, game.horizontalIndend, game.verticalIndent + game.size * 5, game.size * 2, game.size);
                game.settingsBatch.draw(game.noButtonEng, game.horizontalIndend + game.size * 8, game.verticalIndent + game.size * 5, game.size * 2, game.size);
            }
        }
        if (startTimer >=0){
            startTimer -=delta;
            game.settingsBatch.draw(game.border, -game.size, -game.size, game.width+game.size*2, game.height+game.size*2);
        }
        game.settingsBatch.end();
    }

    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
    }
}
