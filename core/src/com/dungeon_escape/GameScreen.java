package com.dungeon_escape;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class GameScreen extends ScreenAdapter {

    DungeonEscape game;
    Slime [] slimes;
    Cage [][] cages;
    Player player;
    Lever [] levers;
    OrthographicCamera camera;

    public GameScreen(DungeonEscape game) {

        camera = new OrthographicCamera(game.width, game.height);
        camera.setToOrtho(false, game.width, game.height);
        this.game = game;
        slimes = new Slime[10];
        cages = new Cage[game.cage_x][game.cage_y];
        levers = new Lever[3];
        for (int i = 0; i < game.cage_x; i++){
            for (int j = 0; j < game.cage_y; j++){
                if (game.map[i][j].contains("sf__")) cages[i][j] = new Cage(i, j, true, game.size, game.horisontal_otstup, game.vertical_otstup, game.stone_floor_texture_region, 1);
                else if (game.map[i][j].contains("nthi")) cages[i][j] = new Cage(i, j, true, game.size, game.horisontal_otstup, game.vertical_otstup, game.player_blast, 1);
                else if (game.map[i][j].contains("clmn")) cages[i][j] = new Cage(i, j, true, game.size, game.horisontal_otstup, game.vertical_otstup, game.clmn, 1);


                else if (game.map[i][j].contains("wd__")) cages[i][j] = new Cage(i, j, true, game.size, game.horisontal_otstup, game.vertical_otstup, game.wd__, 1);
                else if (game.map[i][j].contains("wu__")) cages[i][j] = new Cage(i, j, true, game.size, game.horisontal_otstup, game.vertical_otstup, game.wu__, 1);
                else if (game.map[i][j].contains("wl__")) cages[i][j] = new Cage(i, j, true, game.size, game.horisontal_otstup, game.vertical_otstup, game.wl__, 1);
                else if (game.map[i][j].contains("wr__")) cages[i][j] = new Cage(i, j, true, game.size, game.horisontal_otstup, game.vertical_otstup, game.wr__, 1);

                else if (game.map[i][j].contains("cul_")) cages[i][j] = new Cage(i, j, true, game.size, game.horisontal_otstup, game.vertical_otstup, game.cul_, 1);
                else if (game.map[i][j].contains("cur_")) cages[i][j] = new Cage(i, j, true, game.size, game.horisontal_otstup, game.vertical_otstup, game.cur_, 1);
                else if (game.map[i][j].contains("cdl_")) cages[i][j] = new Cage(i, j, true, game.size, game.horisontal_otstup, game.vertical_otstup, game.cdl_, 1);
                else if (game.map[i][j].contains("cdr_")) cages[i][j] = new Cage(i, j, true, game.size, game.horisontal_otstup, game.vertical_otstup, game.cdr_, 1);

                else if (game.map[i][j].contains("cwul")) cages[i][j] = new Cage(i, j, true, game.size, game.horisontal_otstup, game.vertical_otstup, game.cwul, 1);
                else if (game.map[i][j].contains("cwur")) cages[i][j] = new Cage(i, j, true, game.size, game.horisontal_otstup, game.vertical_otstup, game.cwur, 1);
                else if (game.map[i][j].contains("cwdl")) cages[i][j] = new Cage(i, j, true, game.size, game.horisontal_otstup, game.vertical_otstup, game.cwdl, 1);
                else if (game.map[i][j].contains("cwdr")) cages[i][j] = new Cage(i, j, true, game.size, game.horisontal_otstup, game.vertical_otstup, game.cwdr, 1);

                else cages[i][j] = new Cage(i, j, true, game.size, game.horisontal_otstup, game.vertical_otstup, game.begin_button, 1);
            }
        }
        for (int i = 0; i < slimes.length; i++){
            slimes[i] = new Slime(i, 3, game.size, game.horisontal_otstup, game.vertical_otstup, game.green_slime_texture_region, 6, game.speed, game.slime_blast, game.green_slime_attacking, game.green_slime_attacked, game.slime_attacking_sound, game.slime_attacked_sound, game.title_text_table);
        }
        for (int i=0; i< levers.length; i++){
            levers[i] = new Lever(i+3, 1, i+3, 5, game.size, game.horisontal_otstup, game.vertical_otstup, game.activ_lever, game.passiv_lever, game.speed, game.uchd, game.uohd, game.slime_attacked_sound, game.open_doors_sound, game.closed_doors_sound);
        }
        player = new Player(4, 2, game.size, game.horisontal_otstup, game.vertical_otstup, game.player_texture_region, 12, game.speed, game.player_blast, game.player_attacking, game.player_attacked, game.player_attacking_sound, game.player_attacked_sound);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            public boolean touchDown (int x, int y, int pointer, int button) {
                int touch_x;
                int touch_y;
                if ((Gdx.input.getX()-game.horisontal_otstup) / game.size>=0){
                    touch_x = (int) ((Gdx.input.getX()-game.horisontal_otstup*camera.zoom) / game.size);
                }
                else{
                    touch_x = (int) ((Gdx.input.getX()-game.horisontal_otstup*camera.zoom) / game.size - 1);
                }
                if ((game.height - (game.vertical_otstup+Gdx.input.getY())) / game.size >=0){
                    touch_y= (int) ((game.height - (game.vertical_otstup+Gdx.input.getY())) / game.size);
                }
                else {
                    touch_y = (int) ((game.height - (game.vertical_otstup+Gdx.input.getY())) / game.size - 1);
                }
                if (button == Input.Buttons.LEFT) {
                    for (Slime slime : slimes){
                        if (slime.getX() == touch_x && slime.getY() == touch_y) slime.attacking(9-slime.getX(), 0);
                    }
                    for (Lever lever : levers){
                        if (lever.getX() == touch_x && lever.getY() == touch_y) lever.click(cages);
                    }
                    if (player.getX() == touch_x && player.getY() == touch_y) player.attacking(4, 0);
                    return true;
                }
                if (button == Input.Buttons.RIGHT) {
                    for (Slime slime : slimes){
                        if (slime.getX() == touch_x && slime.getY() == touch_y) slime.attacked();
                        if (player.getX() == touch_x && player.getY() == touch_y) player.attacked();
                    }
                    return true;
                }
                return false;
            }

            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.UP){
                    camera.translate(0, game.size);
                    return true;
                }
                if (keycode == Input.Keys.DOWN){
                    camera.translate(0, -game.size);
                    return true;
                }
                if (keycode == Input.Keys.RIGHT){
                    camera.translate(game.size, 0);
                    return true;
                }
                if (keycode == Input.Keys.LEFT){
                    camera.translate(-game.size, 0);
                    return true;
                }
                if (keycode == Input.Keys.Q){
                    camera.zoom+=0.05f;
                    return true;
                }
                if (keycode == Input.Keys.A){
                    camera.zoom-=0.05f;
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0.4f, 0.4f, 0.4f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        //game.batch.setColor(0, 0, 0, 1);
        for (int i = 0; i < game.cage_x; i++){
            for (int j = 0; j < game.cage_y; j++){
                cages[i][j].draw(game.batch, game.size, Gdx.graphics.getDeltaTime());
            }
        }
        for (Slime slime: slimes){
            slime.draw(game.batch, game.size, Gdx.graphics.getDeltaTime());
        }
        for (Lever lever: levers){
            lever.draw(game.batch, game.size);
        }
        player.draw(game.batch, game.size, Gdx.graphics.getDeltaTime());
        camera.update();
        game.batch.end();

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }
}