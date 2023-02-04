package com.dungeon_escape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SettingsScreen extends ScreenAdapter {

    DungeonEscape game;
    float start_timer;
    boolean is_dialog_open;

    public SettingsScreen(final DungeonEscape game) {
        start_timer = 0.1f;
        is_dialog_open = false;
        game.settingsBatch = new SpriteBatch();
        this.game = game;
        game.settingsListener = new Input.TextInputListener() {
            @Override
            public void input(String s) {
                if ((!game.is_english && s.equals("Да")) || (game.is_english && s.equals("Yes"))) {
                    start_timer = 0.1f;
                    FileHandle win_file = Gdx.files.local("text_resources/records.txt");
                    win_file.writeString("", false);
                    is_dialog_open = false;
                }
                else {
                    if (!game.is_english) {
                        is_dialog_open = true;
                        Gdx.input.getTextInput(game.settingsListener, "Вы уверены что хотите очистить попытки?", "", "Введите \"Да\" в это поле");
                    } else {
                        is_dialog_open = true;
                        Gdx.input.getTextInput(game.settingsListener, "Are you sure you want to erase the history?", "", "Enter \"Yes\" here");
                    }
                }
            }
            @Override
            public void canceled() {
                is_dialog_open = false;
            }
        };
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
                if (button == Input.Buttons.LEFT && touch_y == 0 && touch_x >= 0 && touch_x <= 9 && !is_dialog_open) {
                    game.setScreen(new MainMenuScreen(game));
                    return true;
                }
                if (button == Input.Buttons.LEFT && touch_y == 3 && touch_x >= 0 && touch_x <= 4 && !game.is_english && !is_dialog_open) {
                    start_timer = 0.1f;
                    game.is_english = true;
                    return true;
                }
                if (button == Input.Buttons.LEFT && touch_y == 3 && touch_x >= 5 && touch_x <= 9 && game.is_english && !is_dialog_open) {
                    start_timer = 0.1f;
                    game.is_english = false;
                    return true;
                }
                if (button == Input.Buttons.LEFT && touch_y == 1 && touch_x >= 0 && touch_x <= 9) {
                    if (!game.is_english) {
                        is_dialog_open = true;
                        Gdx.input.getTextInput(game.settingsListener, "Вы уверены что хотите очистить попытки?", "", "Введите \"Да\" в это поле");
                    } else {
                        is_dialog_open = true;
                        Gdx.input.getTextInput(game.settingsListener, "Are you sure you want to erase the history?", "", "Enter \"Yes\" here");
                    }
                }
                if (button == Input.Buttons.LEFT && touch_y == 5 && touch_x >= 0 && touch_x <= 1 && !game.attack_button_auto_reset && !is_dialog_open) {
                    start_timer = 0.1f;
                    game.attack_button_auto_reset = true;
                    return true;
                }
                if (button == Input.Buttons.LEFT && touch_y == 5 && touch_x >= 8 && touch_x <= 9 && game.attack_button_auto_reset && !is_dialog_open) {
                    start_timer = 0.1f;
                    game.attack_button_auto_reset = false;
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
        if (!game.is_english){
            game.settingsBatch.draw(game.englishButton, game.horizontalOtstup, game.verticalOtstup +game.size*3, game.size*5, game.size);
            game.settingsBatch.draw(game.russianButtonActiv, game.horizontalOtstup +game.size*5, game.verticalOtstup +game.size*3, game.size*5, game.size);
        }
        else {
            game.settingsBatch.draw(game.englishButtonActiv, game.horizontalOtstup, game.verticalOtstup +game.size*3, game.size*5, game.size);
            game.settingsBatch.draw(game.russianButton, game.horizontalOtstup +game.size*5, game.verticalOtstup +game.size*3, game.size*5, game.size);
        }
        game.settingsBatch.draw(game.emptyButton, game.horizontalOtstup, game.verticalOtstup +game.size*4, game.size*10, game.size);
        game.settingsBatch.draw(game.emptyButton, game.horizontalOtstup, game.verticalOtstup +game.size*6, game.size*10, game.size);
        if (!game.is_english) {
            game.settingFont.draw(game.settingsBatch, "Язык интерфейса:", game.horizontalOtstup +game.size/10, game.verticalOtstup +game.size*5-game.size/3);
            game.settingFont.draw(game.settingsBatch, "Автоотключение кнопки атаки:", game.horizontalOtstup +game.size/10, game.verticalOtstup +game.size*7-game.size/3);
            game.settingsBatch.draw(game.returnButtonLarge, game.horizontalOtstup, game.verticalOtstup, game.size * 10, game.size);
            game.settingsBatch.draw(game.deleteButton, game.horizontalOtstup, game.verticalOtstup + game.size * 1, game.size * 10, game.size);
            if (!game.attack_button_auto_reset) {
                game.settingsBatch.draw(game.yesButton, game.horizontalOtstup, game.verticalOtstup + game.size * 5, game.size * 2, game.size);
                game.settingsBatch.draw(game.noButtonActiv, game.horizontalOtstup + game.size * 8, game.verticalOtstup + game.size * 5, game.size * 2, game.size);
            }
            else {
                game.settingsBatch.draw(game.yesButtonActiv, game.horizontalOtstup, game.verticalOtstup + game.size * 5, game.size * 2, game.size);
                game.settingsBatch.draw(game.noButton, game.horizontalOtstup + game.size * 8, game.verticalOtstup + game.size * 5, game.size * 2, game.size);
            }
        }
        else {
            game.settingFont.draw(game.settingsBatch, "Interface language:", game.horizontalOtstup +game.size/10, game.verticalOtstup +game.size*5-game.size/3);
            game.settingFont.draw(game.settingsBatch, "Auto restart attack button:", game.horizontalOtstup +game.size/10, game.verticalOtstup +game.size*7-game.size/3);
            game.settingsBatch.draw(game.returnButtonLargeEng, game.horizontalOtstup, game.verticalOtstup, game.size * 10, game.size);
            game.settingsBatch.draw(game.deleteButtonEng, game.horizontalOtstup, game.verticalOtstup + game.size * 1, game.size * 10, game.size);
            if (!game.attack_button_auto_reset) {
                game.settingsBatch.draw(game.yesButtonEng, game.horizontalOtstup, game.verticalOtstup + game.size * 5, game.size * 2, game.size);
                game.settingsBatch.draw(game.noButtonEngActiv, game.horizontalOtstup + game.size * 8, game.verticalOtstup + game.size * 5, game.size * 2, game.size);
            }
            else {
                game.settingsBatch.draw(game.yesButtonEngActiv, game.horizontalOtstup, game.verticalOtstup + game.size * 5, game.size * 2, game.size);
                game.settingsBatch.draw(game.noButtonEng, game.horizontalOtstup + game.size * 8, game.verticalOtstup + game.size * 5, game.size * 2, game.size);
            }
        }
        if (start_timer>=0){
            start_timer-=delta;
            game.settingsBatch.draw(game.border, -game.size, -game.size, game.width+game.size*2, game.height+game.size*2);
        }
        game.settingsBatch.end();
    }

    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
    }
}
