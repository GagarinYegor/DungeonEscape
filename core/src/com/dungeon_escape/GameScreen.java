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
    float camera_real_x, camera_real_y, displacement;
    int camera_x, camera_y;
    boolean is_hod, is_attack;

    public void check_hod(){
        boolean flag = true;
        for (Slime slime:slimes){
            if (slime.getMoving()) flag = false;
        }
        if (player.getMoving()) flag = false;
        if (flag) is_hod = true;
    }

    public void Go(int x, int y){
        is_hod = false;
        camera_x+=x;
        camera_y+=y;
        player.move(x, y);
        System.out.println(camera_x +" "+ camera_y);
    }

    public GameScreen(DungeonEscape game) {
        camera = new OrthographicCamera(game.width, game.height);
        camera.setToOrtho(false, game.width, game.height);
        camera_x = 4;
        camera_y = 3;
        camera_real_x = (camera_x+1)* game.size+ game.horizontal_otstup;
        camera_real_y = camera_y* game.size+ game.vertical_otstup;
        is_hod = true;
        is_attack = true;
        this.game = game;
        slimes = new Slime[10];
        cages = new Cage[game.cage_x][game.cage_y];
        levers = new Lever[3];
        for (int i = 0; i < game.cage_x; i++){
            for (int j = 0; j < game.cage_y; j++){
                if (game.map[i][j].contains("sf__")) cages[i][j] = new Cage(i, j, true, game.size, game.horizontal_otstup, game.vertical_otstup, game.stone_floor_texture_region, 1);
                else if (game.map[i][j].contains("nthi")) cages[i][j] = new Cage(i, j, false, game.size, game.horizontal_otstup, game.vertical_otstup, game.player_blast, 1);
                else if (game.map[i][j].contains("clmn")) cages[i][j] = new Cage(i, j, false, game.size, game.horizontal_otstup, game.vertical_otstup, game.clmn, 1);


                else if (game.map[i][j].contains("wd__")) cages[i][j] = new Cage(i, j, false, game.size, game.horizontal_otstup, game.vertical_otstup, game.wd__, 1);
                else if (game.map[i][j].contains("wu__")) cages[i][j] = new Cage(i, j, false, game.size, game.horizontal_otstup, game.vertical_otstup, game.wu__, 1);
                else if (game.map[i][j].contains("wl__")) cages[i][j] = new Cage(i, j, false, game.size, game.horizontal_otstup, game.vertical_otstup, game.wl__, 1);
                else if (game.map[i][j].contains("wr__")) cages[i][j] = new Cage(i, j, false, game.size, game.horizontal_otstup, game.vertical_otstup, game.wr__, 1);

                else if (game.map[i][j].contains("cul_")) cages[i][j] = new Cage(i, j, false, game.size, game.horizontal_otstup, game.vertical_otstup, game.cul_, 1);
                else if (game.map[i][j].contains("cur_")) cages[i][j] = new Cage(i, j, false, game.size, game.horizontal_otstup, game.vertical_otstup, game.cur_, 1);
                else if (game.map[i][j].contains("cdl_")) cages[i][j] = new Cage(i, j, false, game.size, game.horizontal_otstup, game.vertical_otstup, game.cdl_, 1);
                else if (game.map[i][j].contains("cdr_")) cages[i][j] = new Cage(i, j, false, game.size, game.horizontal_otstup, game.vertical_otstup, game.cdr_, 1);

                else if (game.map[i][j].contains("cwul")) cages[i][j] = new Cage(i, j, false, game.size, game.horizontal_otstup, game.vertical_otstup, game.cwul, 1);
                else if (game.map[i][j].contains("cwur")) cages[i][j] = new Cage(i, j, false, game.size, game.horizontal_otstup, game.vertical_otstup, game.cwur, 1);
                else if (game.map[i][j].contains("cwdl")) cages[i][j] = new Cage(i, j, false, game.size, game.horizontal_otstup, game.vertical_otstup, game.cwdl, 1);
                else if (game.map[i][j].contains("cwdr")) cages[i][j] = new Cage(i, j, false, game.size, game.horizontal_otstup, game.vertical_otstup, game.cwdr, 1);

                else cages[i][j] = new Cage(i, j, true, game.size, game.horizontal_otstup, game.vertical_otstup, game.begin_button, 1);
            }
        }
        for (int i = 0; i < slimes.length; i++){
            slimes[i] = new Slime(i, 3, game.size, game.horizontal_otstup, game.vertical_otstup, game.green_slime_texture_region, 6, game.speed, game.slime_blast, game.green_slime_attacking, game.green_slime_attacked, game.slime_attacking_sound, game.slime_attacked_sound, game.title_text_table);
        }
        for (int i=0; i< levers.length; i++){
            levers[i] = new Lever(i+3, 1, i+3, 5, game.size, game.horizontal_otstup, game.vertical_otstup, game.activ_lever, game.passiv_lever, game.speed, game.uchd, game.uohd, game.slime_attacked_sound, game.open_doors_sound, game.closed_doors_sound);
        }
        player = new Player(3, 3, game.size, game.horizontal_otstup, game.vertical_otstup, game.player_texture_region, 12, game.speed, game.player_blast, game.player_attacking, game.player_attacked, game.player_attacking_sound, game.player_attacked_sound);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            public boolean touchDown (int x, int y, int pointer, int button) {
                System.out.println(camera.zoom);
                int touch_x;
                int touch_y;
                if ((Gdx.input.getX()-game.horizontal_otstup) / game.size / camera.zoom>=0){
                    touch_x = (int) ((Gdx.input.getX()-game.horizontal_otstup) / (game.size + game.size * (camera.zoom-1)));
                    System.out.println("cccccccccccc");
                }
                else{
                    touch_x = (int) ((Gdx.input.getX()-game.horizontal_otstup) / game.size / camera.zoom - 1);
                }
                if ((game.height - (game.vertical_otstup+Gdx.input.getY())) / game.size >=0){
                    touch_y= (int) ((game.height - (game.vertical_otstup+Gdx.input.getY())) / game.size);
                }
                else {
                    touch_y = (int) ((game.height - (game.vertical_otstup+Gdx.input.getY())) / game.size - 1);
                }
                if (button == Input.Buttons.LEFT) {
                    if (is_hod) {
                        if (is_attack) {
                            if (touch_x == 3 && touch_y == 4) {
                                for (Slime slime : slimes){
                                    if (slime.getX() == player.getX() && slime.getY() == player.getY()+1) {
                                        player.attacking(player.getX(), player.getY()+1);
                                        slime.attacked();
                                        is_attack = false;
                                    }
                                }
                            }
                            if (touch_x == 3 && touch_y == 2) {
                                for (Slime slime : slimes){
                                    if (slime.getX() == player.getX() && slime.getY() == player.getY()-1) {
                                        player.attacking(player.getX(), player.getY()-1);
                                        slime.attacked();
                                        is_attack = false;
                                    }
                                }
                            }
                            if (touch_x == 4 && touch_y == 3) {
                                for (Slime slime : slimes){
                                    if (slime.getX() == player.getX()+1 && slime.getY() == player.getY()) {
                                        player.attacking(player.getX()+1, player.getY());
                                        slime.attacked();
                                        is_attack = false;
                                    }
                                }
                            }
                            if (touch_x == 2 && touch_y == 3) {
                                for (Slime slime : slimes){
                                    if (slime.getX() == player.getX()-1 && slime.getY() == player.getY()) {
                                        player.attacking(player.getX()-1, player.getY());
                                        slime.attacked();
                                        is_attack = false;
                                    }
                                }
                            }
                        }
                        else{
                            if (touch_x == 4 && touch_y == 4) {
                                if (cages[player.getX()][player.getY()+1].get_movable()) {
                                    Go(0, 1);
                                }
                            }
                            if (touch_x == 4 && touch_y == 2) {
                                if (cages[player.getX()][player.getY()-1].get_movable()) {
                                    Go(0, -1);
                                }
                            }
                            if (touch_x == 5 && touch_y == 3) {
                                if (cages[player.getX()+1][player.getY()].get_movable()) {
                                    Go(1, 0);
                                }
                            }
                            if (touch_x == 3 && touch_y == 3) {
                                if (cages[player.getX()-1][player.getY()].get_movable()) {
                                    Go(-1, 0);
                                }
                            }
                        }
                    }
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
                    player.move(0, 1);
                    camera.translate(0, game.size);
                    return true;
                }
                if (keycode == Input.Keys.DOWN){
                    player.move(0, -1);
                    camera.translate(0, -game.size);
                    return true;
                }
                if (keycode == Input.Keys.RIGHT){
                    player.move(1, 0);
                    camera.translate(game.size, 0);
                    return true;
                }
                if (keycode == Input.Keys.LEFT){
                    player.move(-1, 0);
                    camera.translate(-game.size, 0);
                    return true;
                }
                if (keycode == Input.Keys.Q){
                    camera.zoom+=game.size/10;
                    return true;
                }
                if (keycode == Input.Keys.A){
                    camera.zoom-=game.size/10;
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void render(float delta) {
        displacement = game.speed*delta;
        if (camera_real_x<camera_x*game.size+game.horizontal_otstup){
            if (camera_real_x+displacement<camera_x*game.size+game.horizontal_otstup) {
                camera_real_x += displacement;
                camera.translate(displacement, 0);
            }
            else{
                camera_real_x+=displacement-(camera_real_x+displacement-(camera_x*game.size+game.horizontal_otstup));
                camera.translate(displacement-(camera_real_x+displacement-(camera_x*game.size+game.horizontal_otstup)), 0);
            }
        }
        if (camera_real_x>camera_x*game.size+game.horizontal_otstup){
            if (camera_real_x+displacement>camera_x*game.size+game.horizontal_otstup) {
                camera_real_x -= displacement;
                camera.translate(-displacement, 0);
            }
            else{
                camera_real_x-=displacement-(camera_real_x+displacement-(camera_x*game.size+game.horizontal_otstup));
                camera.translate(-displacement-(camera_real_x+displacement-(camera_x*game.size+game.horizontal_otstup)), 0);
            }
        }


        Gdx.gl.glClearColor(0.4f, 0.4f, 0.4f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        for (int i = 0; i < game.cage_x; i++){
            for (int j = 0; j < game.cage_y; j++){
                cages[i][j].draw(game.batch, game.size, delta);
            }
        }
        for (Slime slime: slimes){
            slime.draw(game.batch, game.size, delta);
        }
        for (Lever lever: levers){
            lever.draw(game.batch, game.size);
        }
        game.batch.draw(game.border, game.left_border_x, game.left_border_y, game.horizontal_otstup, game.height);
        game.batch.draw(game.border, game.right_border_x, game.right_border_y, game.horizontal_otstup, game.height);
        game.batch.draw(game.border, game.up_border_x, game.up_border_y, game.width, game.vertical_otstup);
        game.batch.draw(game.border, game.down_border_x, game.down_border_y, game.width, game.vertical_otstup);
        player.draw(game.batch, game.size, delta);
        camera.update();
        game.batch.end();
        check_hod();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }
}