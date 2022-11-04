package com.dungeon_escape;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

public class MainMenuScreen extends ScreenAdapter {

    DungeonEscape game;

    public MainMenuScreen(final DungeonEscape game) {
        game.is_correct_name = false;
        this.game = game;
        game.listener = new Input.TextInputListener() {
            @Override
            public void input(String s) {
                if (s.length()<=11){
                    game.name = s;
                    game.is_correct_name = true;
                }
                else Gdx.input.getTextInput(game.listener, "Name shoud be 1-11 letters", game.name, "");
            }

            @Override
            public void canceled() {
                game.is_correct_name = false;
            }
        };
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
                if (button == Input.Buttons.LEFT && touch_y == 2 && touch_x >= 0 && touch_x <= 9) {
                    Gdx.input.getTextInput(game.listener, "Enter your name:", game.name, "");
                    return true;
                }
                if (button == Input.Buttons.LEFT && touch_y == 1 && touch_x >= 0 && touch_x <= 9) {
                    game.setScreen(new RecordScreen(game));
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void render(float delta) {
        if (game.is_correct_name) game.setScreen(new GameScreen(game));
        Gdx.gl.glClearColor(0, 0.25f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(game.begin_button, game.right_border_x-10*game.size, game.up_border_y-game.size*5, game.size*10, game.size);
        game.batch.draw(game.record_button, game.right_border_x-10*game.size, game.up_border_y-game.size*6, game.size*10, game.size);
        game.batch.end();
    }

    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
    }
}