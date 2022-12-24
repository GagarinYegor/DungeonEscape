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
    float start_timer, camera_move_up, camera_move_down, camera_move_left, camera_move_right;
    boolean is_hod, is_attack, check_flag, slime_hod, flag_stop;

    public void check_hod(){
        if (player.getHealth()>0) {
            check_flag = true;
            for (Slime slime : slimes) {
                if (slime.getHealth()>0) {
                    if (slime.getMoving()) check_flag = false;
                    if (slime.getAttack()) check_flag = false;
                    if (slime.getAttacked()) check_flag = false;
                }
            }
            if (player.getMoving()) check_flag = false;
            if (player.getAttack()) check_flag = false;
            if (player.getAttacked()) check_flag = false;
            if (check_flag) is_hod = true;
        }
        else{
            game.theme.stop();
            game.setScreen(new DeathScreen(game));
        }

    }

    public void Go(int x, int y){
        is_hod = false;
        if (x==0&&y==1){
            camera_move_up+= game.size;
        }
        if (x==0&&y==-1){
            camera_move_down+= game.size;
        }
        if (x==1&&y==0){
            camera_move_right+= game.size;
        }
        if (x==-1&&y==0){
            camera_move_left+= game.size;
        }
        player.move(x, y);
    }

    public void hod_end(){
        if (player.getX() == 20 && player.getY() == 0 && !flag_stop){
            flag_stop = true;
            game.theme.stop();
            game.player_lvl = player.getLvl();
            game.setScreen(new WinScreen(game));
        }
        slime_move();
        game.moves+=1;
    }

    public void slime_move(){
        for (Slime slime:slimes){
            if (slime.getHealth()>0) {
                if (Math.abs(slime.getX() - player.getX()) < 3 && Math.abs(slime.getY() - player.getY()) < 3) {
                    slime.attacking(player.getX(), player.getY());
                    player.attacked(slime.getPower());
                    is_hod = false;
                } else {
                    if (Math.abs(slime.getX() - player.getX()) < 5 && Math.abs(slime.getY() - player.getY()) < 5) {
                        slime_hod = true;
                        cages[slime.getX()][slime.getY()].set_movable(true);
                        System.out.println("I see you!");
                        if (slime_hod && slime.getX() == player.getX() && slime.getY() < player.getY()){
                            //cages[slime.getX()][slime.getY()].set_movable(true);
                            if (cages[slime.getX()][slime.getY()+1].get_movable()) slime.move(0, 1);
                            else if (cages[slime.getX()+1][slime.getY()].get_movable()) slime.move(1, 0);
                            else if (cages[slime.getX()-1][slime.getY()].get_movable()) slime.move(-1, 0);
                            slime_hod = false;
                            //cages[slime.getX()][slime.getY()].set_movable(false);
                        }
                        if (slime_hod && slime.getX() == player.getX() && slime.getY() > player.getY()){
                            //cages[slime.getX()][slime.getY()].set_movable(true);
                            if (cages[slime.getX()][slime.getY()-1].get_movable()) slime.move(0, -1);
                            else if (cages[slime.getX()+1][slime.getY()].get_movable()) slime.move(1, 0);
                            else if (cages[slime.getX()-1][slime.getY()].get_movable()) slime.move(-1, 0);
                            slime_hod = false;
                            //cages[slime.getX()][slime.getY()].set_movable(false);
                        }
                        if (slime_hod && slime.getX() > player.getX() && slime.getY() == player.getY()){
                            //cages[slime.getX()][slime.getY()].set_movable(true);
                            if (cages[slime.getX()-1][slime.getY()].get_movable()) slime.move(-1, 0);
                            else if (cages[slime.getX()][slime.getY()+1].get_movable()) slime.move(0, 1);
                            else if (cages[slime.getX()][slime.getY()-1].get_movable()) slime.move(0, -1);
                            slime_hod = false;
                            //cages[slime.getX()][slime.getY()].set_movable(false);
                        }
                        if (slime_hod && slime.getX() < player.getX() && slime.getY() == player.getY()){
                            //cages[slime.getX()][slime.getY()].set_movable(true);
                            if (cages[slime.getX()+1][slime.getY()].get_movable()) slime.move(+1, 0);
                            else if (cages[slime.getX()][slime.getY()+1].get_movable()) slime.move(0, 1);
                            else if (cages[slime.getX()][slime.getY()-1].get_movable()) slime.move(0, -1);
                            slime_hod = false;
                            //cages[slime.getX()][slime.getY()].set_movable(false);
                        }
                        if (slime_hod && slime.getX() < player.getX() && slime.getY() < player.getY()){
                            //cages[slime.getX()][slime.getY()].set_movable(true);
                            if (Math.abs(slime.getX() - player.getX()) < Math.abs(slime.getY()- player.getY())){
                                if (cages[slime.getX()][slime.getY()+1].get_movable()) slime.move(0, 1);
                                else if (cages[slime.getX()+1][slime.getY()].get_movable()) slime.move(1, 0);
                                else if (cages[slime.getX()+1][slime.getY()+1].get_movable()) slime.move(1, 1);
                            }
                            else if (Math.abs(slime.getX() - player.getX()) > Math.abs(slime.getY()- player.getY())){
                                if (cages[slime.getX()+1][slime.getY()].get_movable()) slime.move(1, 0);
                                else if (cages[slime.getX()][slime.getY()+1].get_movable()) slime.move(0, 1);
                                else if (cages[slime.getX()+1][slime.getY()+1].get_movable()) slime.move(1, 1);
                            }
                            else if (Math.abs(slime.getX() - player.getX()) == Math.abs(slime.getY()- player.getY())){
                                if (cages[slime.getX()+1][slime.getY()+1].get_movable()) slime.move(1, 1);
                                else if (cages[slime.getX()][slime.getY()+1].get_movable()) slime.move(0, 1);
                                else if (cages[slime.getX()+1][slime.getY()].get_movable()) slime.move(1, 0);
                            }
                            slime_hod = false;
                            //cages[slime.getX()][slime.getY()].set_movable(false);
                        }
                        if (slime_hod && slime.getX() > player.getX() && slime.getY() < player.getY()){
                            //cages[slime.getX()][slime.getY()].set_movable(true);
                            if (Math.abs(slime.getX() - player.getX()) < Math.abs(slime.getY()- player.getY())){
                                if (cages[slime.getX()][slime.getY()+1].get_movable()) slime.move(0, 1);
                                else if (cages[slime.getX()-1][slime.getY()].get_movable()) slime.move(-1, 0);
                                else if (cages[slime.getX()-1][slime.getY()+1].get_movable()) slime.move(-1, 1);
                            }
                            else if (Math.abs(slime.getX() - player.getX()) > Math.abs(slime.getY()- player.getY())){
                                if (cages[slime.getX()-1][slime.getY()].get_movable()) slime.move(-1, 0);
                                else if (cages[slime.getX()][slime.getY()+1].get_movable()) slime.move(0, 1);
                                else if (cages[slime.getX()-1][slime.getY()+1].get_movable()) slime.move(-1, 1);
                            }
                            else if (Math.abs(slime.getX() - player.getX()) == Math.abs(slime.getY()- player.getY())){
                                if (cages[slime.getX()-1][slime.getY()+1].get_movable()) slime.move(-1, 1);
                                else if (cages[slime.getX()][slime.getY()+1].get_movable()) slime.move(0, 1);
                                else if (cages[slime.getX()-1][slime.getY()].get_movable()) slime.move(-1, 0);
                            }
                            slime_hod = false;
                            //cages[slime.getX()][slime.getY()].set_movable(false);
                        }
                        if (slime_hod && slime.getX() < player.getX() && slime.getY() > player.getY()){
                            //cages[slime.getX()][slime.getY()].set_movable(true);
                            if (Math.abs(slime.getX() - player.getX()) < Math.abs(slime.getY()- player.getY())){
                                if (cages[slime.getX()][slime.getY()-1].get_movable()) slime.move(0, -1);
                                else if (cages[slime.getX()+1][slime.getY()].get_movable()) slime.move(1, 0);
                                else if (cages[slime.getX()+1][slime.getY()-1].get_movable()) slime.move(1, -1);
                            }
                            else if (Math.abs(slime.getX() - player.getX()) > Math.abs(slime.getY()- player.getY())){
                                if (cages[slime.getX()+1][slime.getY()].get_movable()) slime.move(1, 0);
                                else if (cages[slime.getX()][slime.getY()-1].get_movable()) slime.move(0, -1);
                                else if (cages[slime.getX()+1][slime.getY()-1].get_movable()) slime.move(1, -1);
                            }
                            else if (Math.abs(slime.getX() - player.getX()) == Math.abs(slime.getY()- player.getY())){
                                if (cages[slime.getX()+1][slime.getY()-1].get_movable()) slime.move(1, -1);
                                else if (cages[slime.getX()][slime.getY()-1].get_movable()) slime.move(0, -1);
                                else if (cages[slime.getX()+1][slime.getY()].get_movable()) slime.move(1, 0);
                            }
                            slime_hod = false;
                            //cages[slime.getX()][slime.getY()].set_movable(false);
                        }
                        if (slime_hod && slime.getX() > player.getX() && slime.getY() > player.getY()){
                            //cages[slime.getX()][slime.getY()].set_movable(true);
                            if (Math.abs(slime.getX() - player.getX()) < Math.abs(slime.getY() - player.getY())){
                                if (cages[slime.getX()][slime.getY()-1].get_movable()) slime.move(0, -1);
                                else if (cages[slime.getX()-1][slime.getY()].get_movable()) slime.move(-1, 0);
                                else if (cages[slime.getX()-1][slime.getY()-1].get_movable()) slime.move(-1, -1);
                            }
                            else if (Math.abs(slime.getX() - player.getX()) > Math.abs(slime.getY()- player.getY())){
                                if (cages[slime.getX()-1][slime.getY()].get_movable()) slime.move(-1, 0);
                                else if (cages[slime.getX()][slime.getY()-1].get_movable()) slime.move(0, -1);
                                else if (cages[slime.getX()-1][slime.getY()-1].get_movable()) slime.move(-1, -1);
                            }
                            else if (Math.abs(slime.getX() - player.getX()) == Math.abs(slime.getY()- player.getY())){
                                if (cages[slime.getX()-1][slime.getY()-1].get_movable()) slime.move(-1, -1);
                                else if (cages[slime.getX()][slime.getY()-1].get_movable()) slime.move(0, -1);
                                else if (cages[slime.getX()-1][slime.getY()].get_movable()) slime.move(-1, 0);
                            }
                            slime_hod = false;
                            //cages[slime.getX()][slime.getY()].set_movable(false);
                        }
                        cages[slime.getX()][slime.getY()].set_movable(false);
                    }
                }
            }
        }
    }

    public GameScreen(DungeonEscape game) {
        flag_stop = false;
        game.theme.setLooping(true);
        game.theme.play();
        game.theme.setVolume(0.5f);
        game.moves = 0;
        start_timer = 0.1f;
        camera = new OrthographicCamera(game.width, game.height);
        camera.setToOrtho(false, game.width, game.height);
        camera.translate(-game.size, 0);
        camera_move_up = 0;
        camera_move_down = 0;
        camera_move_left = 0;
        camera_move_right = 0;
        is_hod = true;
        is_attack = false;
        this.game = game;
        slimes = new Slime[game.slime_mass_y];
        cages = new Cage[game.cage_x][game.cage_y];
        levers = new Lever[game.lever_mass_y];
        for (int i = 0; i < game.cage_x; i++){
            for (int j = 0; j < game.cage_y; j++){
                if (game.map[i][j].contains("sf__")) cages[i][j] = new Cage(i, j, true, game.size, game.horizontal_otstup, game.vertical_otstup, game.stone_floor_texture_region, 1);
                else if (game.map[i][j].contains("nthi")) cages[i][j] = new Cage(i, j, false, game.size, game.horizontal_otstup, game.vertical_otstup, game.player_blast, 1);
                else if (game.map[i][j].contains("clmn")) cages[i][j] = new Cage(i, j, false, game.size, game.horizontal_otstup, game.vertical_otstup, game.clmn, 1);
                else if (game.map[i][j].contains("exit")) cages[i][j] = new Cage(i, j, true, game.size, game.horizontal_otstup, game.vertical_otstup, game.exit_img, 1);
                else if (game.map[i][j].contains("sfsc")) cages[i][j] = new Cage(i, j, true, game.size, game.horizontal_otstup, game.vertical_otstup, game.stone_floor_sc, 1);
                else if (game.map[i][j].contains("wdwt")) cages[i][j] = new Cage(i, j, false, game.size, game.horizontal_otstup, game.vertical_otstup, game.wdwt, 12);

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
            slimes[i] = new Slime(game.slimes_mass[i][1], game.slimes_mass[i][0], game.size, game.horizontal_otstup, game.vertical_otstup, game.green_slime_texture_region, 6, game.speed, game.slime_blast, game.green_slime_attacking, game.green_slime_attacked, game.slime_attacking_sound, game.slime_attacked_sound, game.title_text_table);
            cages[game.slimes_mass[i][1]][game.slimes_mass[i][0]].set_movable(false);
        }
        for (int i=0; i< game.lever_mass_y; i++){
            if (game.levers_mass[i][4] == 0) {
                levers[i] = new Lever(game.levers_mass[i][0], game.levers_mass[i][1], game.levers_mass[i][2], game.levers_mass[i][3], game.size, game.horizontal_otstup, game.vertical_otstup, game.activ_lever, game.passiv_lever, game.speed, game.cvd, game.ovd, game.slime_attacked_sound, game.open_doors_sound, game.closed_doors_sound);
                cages[game.levers_mass[i][0]][game.levers_mass[i][1]].set_movable(false);
                cages[game.levers_mass[i][2]][game.levers_mass[i][3]].set_movable(false);
            }
            if (game.levers_mass[i][4] == 1) {
                levers[i] = new Lever(game.levers_mass[i][0], game.levers_mass[i][1], game.levers_mass[i][2], game.levers_mass[i][3], game.size, game.horizontal_otstup, game.vertical_otstup, game.activ_lever, game.passiv_lever, game.speed, game.chd, game.ohd, game.slime_attacked_sound, game.open_doors_sound, game.closed_doors_sound);
                cages[game.levers_mass[i][0]][game.levers_mass[i][1]].set_movable(false);
                cages[game.levers_mass[i][2]][game.levers_mass[i][3]].set_movable(false);
            }
            if (game.levers_mass[i][4] == 2) {
                levers[i] = new Lever(game.levers_mass[i][0], game.levers_mass[i][1], game.levers_mass[i][2], game.levers_mass[i][3], game.size, game.horizontal_otstup, game.vertical_otstup, game.player_blast, game.player_blast, game.speed, game.exit_door, game.exit_door, game.slime_attacked_sound, game.open_doors_sound, game.closed_doors_sound);
                cages[game.levers_mass[i][0]][game.levers_mass[i][1]].set_movable(false);
                cages[game.levers_mass[i][2]][game.levers_mass[i][3]].set_movable(true);
            }
        }
        player = new Player(3, 3, game.size, game.horizontal_otstup, game.vertical_otstup,
                game.player_texture_region_right, game.player_texture_region_left,
                12,
                game.player_texture_region_mowing_right, game.player_texture_region_mowing_left,
                14,
                game.speed, game.player_blast,
                game.player_attacking_right, game.player_attacked_right,
                game.player_attacking_left, game.player_attacked_left,
                game.player_attacking_sound, game.player_attacked_sound, game.name);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            public boolean touchDown (int x, int y, int pointer, int button) {
                int touch_x;
                int touch_y;
                if ((Gdx.input.getX()-game.horizontal_otstup) / game.size / camera.zoom>=0){
                    touch_x = (int) ((Gdx.input.getX()-game.horizontal_otstup) / (game.size + game.size * (camera.zoom-1)));
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
                if (button == Input.Buttons.LEFT && !flag_stop) {
                    if (touch_x == 9 && touch_y == 1) {
                        is_attack = !is_attack;
                    }
                    if (touch_x == 9 && touch_y == 6) {
                        game.setScreen(new MainMenuScreen(game));
                    }
                    if (is_hod) {
                        if (touch_x == 9 && touch_y == 0) {
                            hod_end();
                        }
                        if (is_attack) {
                            if (touch_x == 4 && touch_y == 4) { // up
                                for (Slime slime : slimes){
                                    if (slime.getX() == player.getX() && slime.getY() == player.getY()+1) {
                                        player.attacking(player.getX(), player.getY()+1);
                                        slime.attacked(player.getPower());
                                        is_attack = false;
                                        hod_end();
                                    }
                                }
                                for (Lever lever : levers){
                                    if (lever.getX() == player.getX() && lever.getY() == player.getY()+1) {
                                        player.attacking(player.getX(), player.getY()+1);
                                        lever.click(cages);
                                        is_attack = false;
                                        hod_end();
                                    }
                                }
                            }
                            if (touch_x == 4 && touch_y == 2) { // down
                                for (Slime slime : slimes){
                                    if (slime.getX() == player.getX() && slime.getY() == player.getY()-1) {
                                        player.attacking(player.getX(), player.getY()-1);
                                        slime.attacked(player.getPower());
                                        is_attack = false;
                                        hod_end();
                                    }
                                }
                                for (Lever lever : levers){
                                    if (lever.getX() == player.getX() && lever.getY() == player.getY()-1) {
                                        player.attacking(player.getX(), player.getY()-1);
                                        lever.click(cages);
                                        is_attack = false;
                                        hod_end();
                                    }
                                }
                            }
                            if (touch_x == 5 && touch_y == 3) { // right
                                for (Slime slime : slimes){
                                    if (slime.getX() == player.getX()+1 && slime.getY() == player.getY()) {
                                        player.attacking(player.getX()+1, player.getY());
                                        slime.attacked(player.getPower());
                                        is_attack = false;
                                        hod_end();
                                    }
                                }
                                for (Lever lever : levers){
                                    if (lever.getX() == player.getX()+1 && lever.getY() == player.getY()) {
                                        player.attacking(player.getX()+1, player.getY());
                                        lever.click(cages);
                                        is_attack = false;
                                        hod_end();
                                    }
                                }
                            }
                            if (touch_x == 3 && touch_y == 3) { //left
                                for (Slime slime : slimes){
                                    if (slime.getX() == player.getX()-1 && slime.getY() == player.getY()) {
                                        player.attacking(player.getX()-1, player.getY());
                                        slime.attacked(player.getPower());
                                        is_attack = false;
                                        hod_end();
                                    }
                                }
                                for (Lever lever : levers){
                                    if (lever.getX() == player.getX()-1 && lever.getY() == player.getY()) {
                                        player.attacking(player.getX()-1, player.getY());
                                        lever.click(cages);
                                        is_attack = false;
                                        hod_end();
                                    }
                                }
                            }
                        }
                        else{
                            if (touch_x == 4 && touch_y == 4) {
                                if (cages[player.getX()][player.getY()+1].get_movable()) {
                                    Go(0, 1);
                                    hod_end();
                                }
                            }
                            if (touch_x == 4 && touch_y == 2) {
                                if (cages[player.getX()][player.getY()-1].get_movable()) {
                                    Go(0, -1);
                                    hod_end();
                                }
                            }
                            if (touch_x == 5 && touch_y == 3) {
                                if (cages[player.getX()+1][player.getY()].get_movable()) {
                                    Go(1, 0);
                                    hod_end();
                                }
                            }
                            if (touch_x == 3 && touch_y == 3) {
                                if (cages[player.getX()-1][player.getY()].get_movable()) {
                                    Go(-1, 0);
                                    hod_end();
                                }
                            }
                        }
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
        check_hod();
        for (Slime slime:slimes){
            if (slime.getHealth()<=0){
                cages[slime.getX()][slime.getY()].set_movable(true);
                slime.death();
            }
        }
        game.left_border_x = player.get_real_X()- game.size*4-game.horizontal_otstup;
        game.right_border_x = player.get_real_X()+ game.size*6;
        game.up_border_x = player.get_real_X()- game.size*4-game.horizontal_otstup;
        game.down_border_x = player.get_real_X()- game.size*4-game.horizontal_otstup;
        game.left_border_y = player.get_real_Y()- game.size*3-game.vertical_otstup;
        game.right_border_y = player.get_real_Y()- game.size*3-game.vertical_otstup;
        game.up_border_y = player.get_real_Y()+ game.size*4;
        game.down_border_y = player.get_real_Y()- game.size*3-game.vertical_otstup;
        if (camera_move_right > 0){
            camera.translate(game.speed*delta, 0);
            camera_move_right -= game.speed*delta;
            //if (camera_move_right == 0) is_hod = true;
        }
        if (game.speed*delta > camera_move_right && camera_move_right > 0){
            camera.translate(camera_move_right, 0);
            camera_move_right = 0;
            //is_hod = true;
        }
        if (camera_move_left > 0){
            camera.translate(-game.speed*delta, 0);
            camera_move_left -= game.speed*delta;
            //if (camera_move_left == 0) is_hod = true;
        }
        if (game.speed*delta > camera_move_left && camera_move_left > 0){
            camera.translate(-camera_move_left, 0);
            camera_move_left = 0;
            //is_hod = true;
        }
        if (camera_move_up > 0){
            camera.translate(0, game.speed*delta);
            camera_move_up -= game.speed*delta;
            //if (camera_move_up == 0) is_hod = true;
        }
        if (game.speed*delta > camera_move_up && camera_move_up > 0){
            camera.translate(0, camera_move_up);
            camera_move_up = 0;
            //is_hod = true;
        }
        if (camera_move_down > 0){
            camera.translate(0, -game.speed*delta);
            camera_move_down -= game.speed*delta;
            //if (camera_move_down == 0) is_hod = true;
        }
        if (game.speed*delta > camera_move_down && camera_move_down > 0){
            camera.translate(0, -camera_move_down);
            camera_move_down = 0;
            //is_hod = true;
        }

        Gdx.gl.glClearColor(0.4f, 0.4f, 0.4f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        for (int i = 0; i < game.cage_x; i++){
            for (int j = 0; j < game.cage_y; j++) {
                if (Math.abs(i - player.getX()) < 6 && Math.abs(j - player.getY()) < 5) {
                    cages[i][j].draw(game.batch, game.size, delta);
                }
            }
        }
        player.draw(game.batch, game.size, delta);
        for (Slime slime: slimes){
            if (Math.abs(slime.getX() - player.getX()) < 6 && Math.abs(slime.getY() - player.getY()) < 5) {
                slime.draw(game.batch, game.size, delta);
            }
        }
        for (Lever lever: levers){
            lever.draw(game.batch, game.size);
        }
        if (game.vertical_otstup!=0){
            game.batch.draw(game.border, game.up_border_x-game.size, game.up_border_y, game.width+2*game.size, game.vertical_otstup+game.size);
            game.batch.draw(game.border, game.down_border_x-game.size, game.down_border_y-game.size, game.width+2*game.size, game.vertical_otstup+game.size);
        }
        if (game.horizontal_otstup!=0) {
            game.batch.draw(game.border, game.left_border_x - game.size, game.left_border_y - game.size, game.horizontal_otstup + game.size, game.height + 2 * game.size);
            game.batch.draw(game.border, game.right_border_x - game.size, game.right_border_y - game.size, game.horizontal_otstup + 2*game.size, game.height + 2 * game.size);
        }
        if (is_attack) game.batch.draw(game.activ_attack_button, game.right_border_x - game.size, game.right_border_y+game.size, game.size, game.size);
        else game.batch.draw(game.passiv_attack_button, game.right_border_x - game.size, game.right_border_y+game.size, game.size, game.size);
        game.batch.draw(game.waiting_button, game.right_border_x - game.size, game.right_border_y, game.size, game.size);
        game.batch.draw(game.info_window, game.right_border_x - game.size, game.right_border_y+game.size*4, game.size, game.size*2);
        game.info_font.draw(game.batch, "Name:", game.right_border_x - game.size + game.size/10, game.right_border_y+game.size*6 - game.size / 20);
        game.info_font.draw(game.batch, player.getName(), game.right_border_x - game.size + game.size/10, game.right_border_y+game.size*6 - game.size *5 / 20);
        game.info_font.draw(game.batch, "Hp:"+player.getHealth()+"/"+player.getMaxHealth(), game.right_border_x - game.size + game.size/10, game.right_border_y+game.size*6 - game.size * 10 / 20);
        game.info_font.draw(game.batch, "Moves:"+game.moves, game.right_border_x - game.size + game.size/10, game.right_border_y+game.size*6 - game.size * 15 / 20);
        game.info_font.draw(game.batch, "Power:"+player.getPower(), game.right_border_x - game.size + game.size/10, game.right_border_y+game.size*6 - game.size * 20 / 20);
        if (start_timer>=0){
            start_timer-=delta;
            game.batch.draw(game.border, game.left_border_x, game.left_border_y, game.width+game.size*2, game.height+game.size*2);
        }
        camera.update();
        game.batch.end();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }
}