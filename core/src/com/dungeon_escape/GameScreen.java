package com.dungeon_escape;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.Scanner;
import java.util.Vector;

public class GameScreen extends ScreenAdapter {

    DungeonEscape game;
    Slime [] slimes;
    Cage [][] cages;
    Player player;
    Lever [] levers;
    OrthographicCamera camera;
    float start_timer, camera_move_up, camera_move_down, camera_move_left, camera_move_right;
    boolean is_hod, is_attack, check_flag, slime_hod, is_map_find, is_map_activ;
    FileHandle saved_file;

    private void save() {
        saved_file.writeString("///moves///" + "\n", false);
        saved_file.writeString(game.moves + "\n", true);
        saved_file.writeString("///is_map_find, is_map_activ, is_attack///" + "\n", true);
        String text = "";
        if (is_map_find) text += "1 ";
        else text += "0 ";
        if (is_map_activ) text += "1 ";
        else text += "0 ";
        if (is_attack) text += "1" + "\n";
        else text += "0" + "\n";
        saved_file.writeString(text, true);
        saved_file.writeString("///playerX, playerY, name, health, maxHealth///" + "\n", true);
        saved_file.writeString(player.getX() + " " + player.getY() + " " + player.getName() + " " + player.getMaxHealth() + " " + player.getHealth() + "\n", true);
        saved_file.writeString("///slimes///" + "\n", true);
        for (int i = 0; i<slimes.length; i++) {
            saved_file.writeString(slimes[i].getY() + " " + slimes[i].getX() + " " + slimes[i].getHealth() + " " + slimes[i].getMaxHealth() + "\n", true);
        }
        saved_file.writeString("///levers///" + "\n", true);
        for (int i = 0; i<levers.length; i++) {
            if (levers[i].isActiv()) saved_file.writeString("1" + "\n", true);
            else saved_file.writeString("0" + "\n", true);
        }
    }

    public void check_hod(){
        if (player.getX() == 20 && player.getY() == 0){
            game.theme.stop();
            game.setScreen(new WinScreen(game));
        }
        if (player.getX() == 37 && player.getY() == 3 && !is_map_find){
            is_map_find = true;
            cages[37][3].change_Animation(game.stoneFloorTextureRegion, 1);
        }
        if (player.getHealth()>0) {
            check_flag = true;
            for (Slime slime : slimes) {
                if (slime.getHealth()>0) {
                    if (slime.getMoving()) check_flag = false;
                    if (slime.getAttack()) check_flag = false;
                    if (slime.getAttacked()) check_flag = false;
                }
            }
            if (player.isMoving()) check_flag = false;
            if (player.isAttacking()) check_flag = false;
            if (player.isAttacked()) check_flag = false;
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
        if (player.getHealth()< player.getMaxHealth()) {
            player.setHealth(player.getHealth() + 5);
            if (player.getHealth()> player.getMaxHealth()){
                player.setHealth(player.getMaxHealth());
            }
        }
        slime_move();
        game.moves+=1;
        save();
    }

    public void slime_move(){
        int damage = 0;
        for (Slime slime:slimes){
            if (slime.getHealth()>0) {
                if (Math.abs(slime.getX() - player.getX()) < 3 && Math.abs(slime.getY() - player.getY()) < 3) {
                    slime.attacking(player.getX(), player.getY());
                    damage+= slime.getPower();
                    is_hod = false;
                } else {
                    if (Math.abs(slime.getX() - player.getX()) < 5 && Math.abs(slime.getY() - player.getY()) < 5) {
                        slime_hod = true;
                        cages[slime.getX()][slime.getY()].setMovable(true);
                        //System.out.println("I see you!");
                        if (slime_hod && slime.getX() == player.getX() && slime.getY() < player.getY()){
                            //cages[slime.getX()][slime.getY()].set_movable(true);
                            if (cages[slime.getX()][slime.getY()+1].getMovable()) slime.move(0, 1);
                            else if (cages[slime.getX()+1][slime.getY()].getMovable()) slime.move(1, 0);
                            else if (cages[slime.getX()-1][slime.getY()].getMovable()) slime.move(-1, 0);
                            slime_hod = false;
                            //cages[slime.getX()][slime.getY()].set_movable(false);
                        }
                        if (slime_hod && slime.getX() == player.getX() && slime.getY() > player.getY()){
                            //cages[slime.getX()][slime.getY()].set_movable(true);
                            if (cages[slime.getX()][slime.getY()-1].getMovable()) slime.move(0, -1);
                            else if (cages[slime.getX()+1][slime.getY()].getMovable()) slime.move(1, 0);
                            else if (cages[slime.getX()-1][slime.getY()].getMovable()) slime.move(-1, 0);
                            slime_hod = false;
                            //cages[slime.getX()][slime.getY()].set_movable(false);
                        }
                        if (slime_hod && slime.getX() > player.getX() && slime.getY() == player.getY()){
                            //cages[slime.getX()][slime.getY()].set_movable(true);
                            if (cages[slime.getX()-1][slime.getY()].getMovable()) slime.move(-1, 0);
                            else if (cages[slime.getX()][slime.getY()+1].getMovable()) slime.move(0, 1);
                            else if (cages[slime.getX()][slime.getY()-1].getMovable()) slime.move(0, -1);
                            slime_hod = false;
                            //cages[slime.getX()][slime.getY()].set_movable(false);
                        }
                        if (slime_hod && slime.getX() < player.getX() && slime.getY() == player.getY()){
                            //cages[slime.getX()][slime.getY()].set_movable(true);
                            if (cages[slime.getX()+1][slime.getY()].getMovable()) slime.move(+1, 0);
                            else if (cages[slime.getX()][slime.getY()+1].getMovable()) slime.move(0, 1);
                            else if (cages[slime.getX()][slime.getY()-1].getMovable()) slime.move(0, -1);
                            slime_hod = false;
                            //cages[slime.getX()][slime.getY()].set_movable(false);
                        }
                        if (slime_hod && slime.getX() < player.getX() && slime.getY() < player.getY()){
                            //cages[slime.getX()][slime.getY()].set_movable(true);
                            if (Math.abs(slime.getX() - player.getX()) < Math.abs(slime.getY()- player.getY())){
                                if (cages[slime.getX()][slime.getY()+1].getMovable()) slime.move(0, 1);
                                else if (cages[slime.getX()+1][slime.getY()].getMovable()) slime.move(1, 0);
                                else if (cages[slime.getX()+1][slime.getY()+1].getMovable()) slime.move(1, 1);
                            }
                            else if (Math.abs(slime.getX() - player.getX()) > Math.abs(slime.getY()- player.getY())){
                                if (cages[slime.getX()+1][slime.getY()].getMovable()) slime.move(1, 0);
                                else if (cages[slime.getX()][slime.getY()+1].getMovable()) slime.move(0, 1);
                                else if (cages[slime.getX()+1][slime.getY()+1].getMovable()) slime.move(1, 1);
                            }
                            else if (Math.abs(slime.getX() - player.getX()) == Math.abs(slime.getY()- player.getY())){
                                if (cages[slime.getX()+1][slime.getY()+1].getMovable()) slime.move(1, 1);
                                else if (cages[slime.getX()][slime.getY()+1].getMovable()) slime.move(0, 1);
                                else if (cages[slime.getX()+1][slime.getY()].getMovable()) slime.move(1, 0);
                            }
                            slime_hod = false;
                            //cages[slime.getX()][slime.getY()].set_movable(false);
                        }
                        if (slime_hod && slime.getX() > player.getX() && slime.getY() < player.getY()){
                            //cages[slime.getX()][slime.getY()].set_movable(true);
                            if (Math.abs(slime.getX() - player.getX()) < Math.abs(slime.getY()- player.getY())){
                                if (cages[slime.getX()][slime.getY()+1].getMovable()) slime.move(0, 1);
                                else if (cages[slime.getX()-1][slime.getY()].getMovable()) slime.move(-1, 0);
                                else if (cages[slime.getX()-1][slime.getY()+1].getMovable()) slime.move(-1, 1);
                            }
                            else if (Math.abs(slime.getX() - player.getX()) > Math.abs(slime.getY()- player.getY())){
                                if (cages[slime.getX()-1][slime.getY()].getMovable()) slime.move(-1, 0);
                                else if (cages[slime.getX()][slime.getY()+1].getMovable()) slime.move(0, 1);
                                else if (cages[slime.getX()-1][slime.getY()+1].getMovable()) slime.move(-1, 1);
                            }
                            else if (Math.abs(slime.getX() - player.getX()) == Math.abs(slime.getY()- player.getY())){
                                if (cages[slime.getX()-1][slime.getY()+1].getMovable()) slime.move(-1, 1);
                                else if (cages[slime.getX()][slime.getY()+1].getMovable()) slime.move(0, 1);
                                else if (cages[slime.getX()-1][slime.getY()].getMovable()) slime.move(-1, 0);
                            }
                            slime_hod = false;
                            //cages[slime.getX()][slime.getY()].set_movable(false);
                        }
                        if (slime_hod && slime.getX() < player.getX() && slime.getY() > player.getY()){
                            //cages[slime.getX()][slime.getY()].set_movable(true);
                            if (Math.abs(slime.getX() - player.getX()) < Math.abs(slime.getY()- player.getY())){
                                if (cages[slime.getX()][slime.getY()-1].getMovable()) slime.move(0, -1);
                                else if (cages[slime.getX()+1][slime.getY()].getMovable()) slime.move(1, 0);
                                else if (cages[slime.getX()+1][slime.getY()-1].getMovable()) slime.move(1, -1);
                            }
                            else if (Math.abs(slime.getX() - player.getX()) > Math.abs(slime.getY()- player.getY())){
                                if (cages[slime.getX()+1][slime.getY()].getMovable()) slime.move(1, 0);
                                else if (cages[slime.getX()][slime.getY()-1].getMovable()) slime.move(0, -1);
                                else if (cages[slime.getX()+1][slime.getY()-1].getMovable()) slime.move(1, -1);
                            }
                            else if (Math.abs(slime.getX() - player.getX()) == Math.abs(slime.getY()- player.getY())){
                                if (cages[slime.getX()+1][slime.getY()-1].getMovable()) slime.move(1, -1);
                                else if (cages[slime.getX()][slime.getY()-1].getMovable()) slime.move(0, -1);
                                else if (cages[slime.getX()+1][slime.getY()].getMovable()) slime.move(1, 0);
                            }
                            slime_hod = false;
                            //cages[slime.getX()][slime.getY()].set_movable(false);
                        }
                        if (slime_hod && slime.getX() > player.getX() && slime.getY() > player.getY()){
                            //cages[slime.getX()][slime.getY()].set_movable(true);
                            if (Math.abs(slime.getX() - player.getX()) < Math.abs(slime.getY() - player.getY())){
                                if (cages[slime.getX()][slime.getY()-1].getMovable()) slime.move(0, -1);
                                else if (cages[slime.getX()-1][slime.getY()].getMovable()) slime.move(-1, 0);
                                else if (cages[slime.getX()-1][slime.getY()-1].getMovable()) slime.move(-1, -1);
                            }
                            else if (Math.abs(slime.getX() - player.getX()) > Math.abs(slime.getY()- player.getY())){
                                if (cages[slime.getX()-1][slime.getY()].getMovable()) slime.move(-1, 0);
                                else if (cages[slime.getX()][slime.getY()-1].getMovable()) slime.move(0, -1);
                                else if (cages[slime.getX()-1][slime.getY()-1].getMovable()) slime.move(-1, -1);
                            }
                            else if (Math.abs(slime.getX() - player.getX()) == Math.abs(slime.getY()- player.getY())){
                                if (cages[slime.getX()-1][slime.getY()-1].getMovable()) slime.move(-1, -1);
                                else if (cages[slime.getX()][slime.getY()-1].getMovable()) slime.move(0, -1);
                                else if (cages[slime.getX()-1][slime.getY()].getMovable()) slime.move(-1, 0);
                            }
                            slime_hod = false;
                            //cages[slime.getX()][slime.getY()].set_movable(false);
                        }
                        cages[slime.getX()][slime.getY()].setMovable(false);
                    }
                }
            }
        }
        if (damage>0) player.attacked(damage);
    }

    public GameScreen(DungeonEscape game) {
        saved_file = Gdx.files.local("text_resources/saved_records.txt");
        this.game = game;
        game.theme.setLooping(true);
        game.theme.play();
        game.theme.setVolume(0.5f);
        start_timer = 0.1f;
        camera = new OrthographicCamera(game.width, game.height);
        camera.setToOrtho(false, game.width, game.height);
        camera_move_up = 0;
        camera_move_down = 0;
        camera_move_left = 0;
        camera_move_right = 0;
        is_hod = true;
        slimes = new Slime[game.slime_mass_y];
        cages = new Cage[game.cage_x][game.cage_y];
        levers = new Lever[game.lever_mass_y];
        for (int i = 0; i < game.cage_x; i++) {
            for (int j = 0; j < game.cage_y; j++) {
                if (game.map[i][j].contains("sf__"))
                    cages[i][j] = new Cage(i, j, true, game.size, game.horizontalOtstup, game.verticalOtstup, game.stoneFloorTextureRegion, 1);
                else if (game.map[i][j].contains("sfwm"))
                    cages[i][j] = new Cage(i, j, true, game.size, game.horizontalOtstup, game.verticalOtstup, game.sfwm, 1);
                else if (game.map[i][j].contains("nthi"))
                    cages[i][j] = new Cage(i, j, false, game.size, game.horizontalOtstup, game.verticalOtstup, game.playerBlast, 1);
                else if (game.map[i][j].contains("clmn"))
                    cages[i][j] = new Cage(i, j, false, game.size, game.horizontalOtstup, game.verticalOtstup, game.clmn, 1);
                else if (game.map[i][j].contains("exit"))
                    cages[i][j] = new Cage(i, j, true, game.size, game.horizontalOtstup, game.verticalOtstup, game.exitImg, 1);
                else if (game.map[i][j].contains("sfsc"))
                    cages[i][j] = new Cage(i, j, true, game.size, game.horizontalOtstup, game.verticalOtstup, game.stoneFloorSc, 1);
                else if (game.map[i][j].contains("wdwt"))
                    cages[i][j] = new Cage(i, j, false, game.size, game.horizontalOtstup, game.verticalOtstup, game.wdwt, 12);

                else if (game.map[i][j].contains("wd__"))
                    cages[i][j] = new Cage(i, j, false, game.size, game.horizontalOtstup, game.verticalOtstup, game.wd__, 1);
                else if (game.map[i][j].contains("wu__"))
                    cages[i][j] = new Cage(i, j, false, game.size, game.horizontalOtstup, game.verticalOtstup, game.wu__, 1);
                else if (game.map[i][j].contains("wl__"))
                    cages[i][j] = new Cage(i, j, false, game.size, game.horizontalOtstup, game.verticalOtstup, game.wl__, 1);
                else if (game.map[i][j].contains("wr__"))
                    cages[i][j] = new Cage(i, j, false, game.size, game.horizontalOtstup, game.verticalOtstup, game.wr__, 1);

                else if (game.map[i][j].contains("cul_"))
                    cages[i][j] = new Cage(i, j, false, game.size, game.horizontalOtstup, game.verticalOtstup, game.cul_, 1);
                else if (game.map[i][j].contains("cur_"))
                    cages[i][j] = new Cage(i, j, false, game.size, game.horizontalOtstup, game.verticalOtstup, game.cur_, 1);
                else if (game.map[i][j].contains("cdl_"))
                    cages[i][j] = new Cage(i, j, false, game.size, game.horizontalOtstup, game.verticalOtstup, game.cdl_, 1);
                else if (game.map[i][j].contains("cdr_"))
                    cages[i][j] = new Cage(i, j, false, game.size, game.horizontalOtstup, game.verticalOtstup, game.cdr_, 1);

                else if (game.map[i][j].contains("cwul"))
                    cages[i][j] = new Cage(i, j, false, game.size, game.horizontalOtstup, game.verticalOtstup, game.cwul, 1);
                else if (game.map[i][j].contains("cwur"))
                    cages[i][j] = new Cage(i, j, false, game.size, game.horizontalOtstup, game.verticalOtstup, game.cwur, 1);
                else if (game.map[i][j].contains("cwdl"))
                    cages[i][j] = new Cage(i, j, false, game.size, game.horizontalOtstup, game.verticalOtstup, game.cwdl, 1);
                else if (game.map[i][j].contains("cwdr"))
                    cages[i][j] = new Cage(i, j, false, game.size, game.horizontalOtstup, game.verticalOtstup, game.cwdr, 1);

                else
                    cages[i][j] = new Cage(i, j, true, game.size, game.horizontalOtstup, game.verticalOtstup, game.beginButton, 1);
            }
        }

        FileHandle saved_file = Gdx.files.local("text_resources/saved_records.txt");
        if (saved_file.exists()) {
            Scanner saved_records_scan = new Scanner(saved_file.read());
            Vector<String> saved_strings = new Vector<>();
            while (saved_records_scan.hasNextLine()){
                saved_strings.add(saved_records_scan.nextLine());
            }
            for (int i = 0; i < saved_strings.size(); i++){
                System.out.println(saved_strings.get(i));
            }
            game.moves = Integer.parseInt(saved_strings.get(1));
            if (saved_strings.get(3).split(" ")[0].equals("0")) is_map_find = false;
            else is_map_find = true;
            if (saved_strings.get(3).split(" ")[1].equals("0")) is_map_activ = false;
            else is_map_activ = true;
            if (saved_strings.get(3).split(" ")[2].equals("0")) is_attack = false;
            else is_attack = true;
            player = new Player(Integer.parseInt(saved_strings.get(5).split(" ")[0]), Integer.parseInt(saved_strings.get(5).split(" ")[1]), game.size, game.horizontalOtstup, game.verticalOtstup,
                    game.playerTextureRegionRight, game.playerTextureRegionLeft,
                    12,
                    game.playerTextureRegionMowingRight, game.playerTextureRegionMowingLeft,
                    14,
                    game.speed, game.playerBlast,
                    game.playerAttackingRight, game.playerAttackedRight,
                    game.playerAttackingLeft, game.playerAttackedLeft,
                    game.playerAttackingSound, game.sound, saved_strings.get(5).split(" ")[2], Integer.parseInt(saved_strings.get(5).split(" ")[3]), Integer.parseInt(saved_strings.get(5).split(" ")[4]));
            camera.translate((player.getX()-4)*game.size, (player.getY()-3)*game.size);
            game.name = player.getName();
            for (int i = 0; i < slimes.length; i++) {
                slimes[i] = new Slime(Integer.parseInt(saved_strings.get(7+i).split(" ")[1]),
                        Integer.parseInt(saved_strings.get(7+i).split(" ")[0]), game.size,
                        game.horizontalOtstup, game.verticalOtstup, game.greenSlimeTextureRegion,
                        6, game.speed, game.slimeBlast, game.greenSlimeAttacking,
                        game.greenSlimeAttacked, game.slimeAttackingSound, game.slimeAttackedSound,
                        game.titleTextTable, game.slimeFont, Integer.parseInt(saved_strings.get(7+i).split(" ")[2]),
                        Integer.parseInt(saved_strings.get(7+i).split(" ")[3]));
                cages[slimes[i].getX()][slimes[i].getY()].setMovable(false);
            }
            System.out.println(slimes.length);
            for (int i = 0; i < game.lever_mass_y; i++) {
                if (game.levers_mass[i][4] == 0) {
                    levers[i] = new Lever(game.levers_mass[i][0], game.levers_mass[i][1], game.levers_mass[i][2], game.levers_mass[i][3], game.size, game.horizontalOtstup, game.verticalOtstup, game.activLever, game.passivLever, game.cvd, game.ovd, game.leverSound, game.openDoorsSound, game.closedDoorsSound, saved_strings.get(8+slimes.length+i).equals("1"));
                    cages[game.levers_mass[i][0]][game.levers_mass[i][1]].setMovable(false);
                    cages[game.levers_mass[i][2]][game.levers_mass[i][3]].setMovable(saved_strings.get(8+slimes.length+i).equals("1"));
                }
                if (game.levers_mass[i][4] == 1) {
                    levers[i] = new Lever(game.levers_mass[i][0], game.levers_mass[i][1], game.levers_mass[i][2], game.levers_mass[i][3], game.size, game.horizontalOtstup, game.verticalOtstup, game.activLever, game.passivLever, game.chd, game.ohd, game.slimeAttackedSound, game.openDoorsSound, game.closedDoorsSound, saved_strings.get(8+slimes.length+i).equals("1"));
                    cages[game.levers_mass[i][0]][game.levers_mass[i][1]].setMovable(false);
                    cages[game.levers_mass[i][2]][game.levers_mass[i][3]].setMovable(saved_strings.get(8+slimes.length+i).equals("1"));
                }
                if (game.levers_mass[i][4] == 2) {
                    levers[i] = new Lever(game.levers_mass[i][0], game.levers_mass[i][1], game.levers_mass[i][2], game.levers_mass[i][3], game.size, game.horizontalOtstup, game.verticalOtstup, game.playerBlast, game.playerBlast, game.exitDoor, game.exitDoor, game.slimeAttackedSound, game.openDoorsSound, game.closedDoorsSound, saved_strings.get(8+slimes.length+i).equals("1"));
                    cages[game.levers_mass[i][0]][game.levers_mass[i][1]].setMovable(true);
                    cages[game.levers_mass[i][2]][game.levers_mass[i][3]].setMovable(true);
                }
            }
        }
        else {
            game.moves = 0;
            is_map_find = false;
            is_map_activ = false;
            player = new Player(3, 3, game.size, game.horizontalOtstup, game.verticalOtstup,
                    game.playerTextureRegionRight, game.playerTextureRegionLeft,
                    12,
                    game.playerTextureRegionMowingRight, game.playerTextureRegionMowingLeft,
                    14,
                    game.speed, game.playerBlast,
                    game.playerAttackingRight, game.playerAttackedRight,
                    game.playerAttackingLeft, game.playerAttackedLeft,
                    game.playerAttackingSound, game.sound, game.name, 100, 100);
            camera.translate(-game.size, 0);
            is_attack = false;

            for (int i = 0; i < slimes.length; i++) {
                slimes[i] = new Slime(game.slimes_mass[i][1], game.slimes_mass[i][0], game.size, game.horizontalOtstup, game.verticalOtstup, game.greenSlimeTextureRegion, 6, game.speed, game.slimeBlast, game.greenSlimeAttacking, game.greenSlimeAttacked, game.slimeAttackingSound, game.slimeAttackedSound, game.titleTextTable, game.slimeFont, 100, 100);
                cages[slimes[i].getX()][slimes[i].getY()].setMovable(false);
            }

            for (int i = 0; i < game.lever_mass_y; i++) {
                if (game.levers_mass[i][4] == 0) {
                    levers[i] = new Lever(game.levers_mass[i][0], game.levers_mass[i][1], game.levers_mass[i][2], game.levers_mass[i][3], game.size, game.horizontalOtstup, game.verticalOtstup, game.activLever, game.passivLever, game.cvd, game.ovd, game.leverSound, game.openDoorsSound, game.closedDoorsSound, false);
                    cages[game.levers_mass[i][0]][game.levers_mass[i][1]].setMovable(false);
                    cages[game.levers_mass[i][2]][game.levers_mass[i][3]].setMovable(false);
                }
                if (game.levers_mass[i][4] == 1) {
                    levers[i] = new Lever(game.levers_mass[i][0], game.levers_mass[i][1], game.levers_mass[i][2], game.levers_mass[i][3], game.size, game.horizontalOtstup, game.verticalOtstup, game.activLever, game.passivLever, game.chd, game.ohd, game.slimeAttackedSound, game.openDoorsSound, game.closedDoorsSound, false);
                    cages[game.levers_mass[i][0]][game.levers_mass[i][1]].setMovable(false);
                    cages[game.levers_mass[i][2]][game.levers_mass[i][3]].setMovable(false);
                }
                if (game.levers_mass[i][4] == 2) {
                    levers[i] = new Lever(game.levers_mass[i][0], game.levers_mass[i][1], game.levers_mass[i][2], game.levers_mass[i][3], game.size, game.horizontalOtstup, game.verticalOtstup, game.playerBlast, game.playerBlast, game.exitDoor, game.exitDoor, game.slimeAttackedSound, game.openDoorsSound, game.closedDoorsSound, false);
                    cages[game.levers_mass[i][0]][game.levers_mass[i][1]].setMovable(true);
                    cages[game.levers_mass[i][2]][game.levers_mass[i][3]].setMovable(true);
                }
            }
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            public boolean touchDown (int x, int y, int pointer, int button) {
                int touch_x;
                int touch_y;
                if ((Gdx.input.getX()-game.horizontalOtstup) / game.size / camera.zoom>=0){
                    touch_x = (int) ((Gdx.input.getX()-game.horizontalOtstup) / (game.size + game.size * (camera.zoom-1)));
                }
                else{
                    touch_x = (int) ((Gdx.input.getX()-game.horizontalOtstup) / game.size / camera.zoom - 1);
                }
                if ((game.height - (game.verticalOtstup +Gdx.input.getY())) / game.size >=0){
                    touch_y= (int) ((game.height - (game.verticalOtstup +Gdx.input.getY())) / game.size);
                }
                else {
                    touch_y = (int) ((game.height - (game.verticalOtstup +Gdx.input.getY())) / game.size - 1);
                }
                if (button == Input.Buttons.LEFT) {
                    if (touch_x == 9 && touch_y == 3 && is_map_find) {
                        is_map_activ = !is_map_activ;
                    }
                    if (touch_x == 9 && touch_y == 1) {
                        is_attack = !is_attack;
                    }
                    if(!is_map_activ) {
                        if (is_hod) {
                            if (touch_x == 9 && touch_y == 2) {
                                hod_end();
                            }
                            if (is_attack) {
                                if (touch_x == 4 && touch_y == 4) { // up
                                    for (Slime slime : slimes) {
                                        if (slime.getX() == player.getX() && slime.getY() == player.getY() + 1) {
                                            player.attacking(player.getX(), player.getY() + 1);
                                            slime.attacked(player.getPower());
                                            if (game.attack_button_auto_reset) is_attack = false;
                                            hod_end();
                                        }
                                    }
                                    for (Lever lever : levers) {
                                        if (lever.getX() == player.getX() && lever.getY() == player.getY() + 1) {
                                            player.attacking(player.getX(), player.getY() + 1);
                                            lever.click(cages);
                                            if (game.attack_button_auto_reset) is_attack = false;
                                            hod_end();
                                        }
                                    }
                                }
                                if (touch_x == 4 && touch_y == 2) { // down
                                    for (Slime slime : slimes) {
                                        if (slime.getX() == player.getX() && slime.getY() == player.getY() - 1) {
                                            player.attacking(player.getX(), player.getY() - 1);
                                            slime.attacked(player.getPower());
                                            if (game.attack_button_auto_reset) is_attack = false;
                                            hod_end();
                                        }
                                    }
                                    for (Lever lever : levers) {
                                        if (lever.getX() == player.getX() && lever.getY() == player.getY() - 1) {
                                            player.attacking(player.getX(), player.getY() - 1);
                                            lever.click(cages);
                                            if (game.attack_button_auto_reset) is_attack = false;
                                            hod_end();
                                        }
                                    }
                                }
                                if (touch_x == 5 && touch_y == 3) { // right
                                    for (Slime slime : slimes) {
                                        if (slime.getX() == player.getX() + 1 && slime.getY() == player.getY()) {
                                            player.attacking(player.getX() + 1, player.getY());
                                            slime.attacked(player.getPower());
                                            if (game.attack_button_auto_reset) is_attack = false;
                                            hod_end();
                                        }
                                    }
                                    for (Lever lever : levers) {
                                        if (lever.getX() == player.getX() + 1 && lever.getY() == player.getY()) {
                                            player.attacking(player.getX() + 1, player.getY());
                                            lever.click(cages);
                                            if (game.attack_button_auto_reset) is_attack = false;
                                            hod_end();
                                        }
                                    }
                                }
                                if (touch_x == 3 && touch_y == 3) { //left
                                    for (Slime slime : slimes) {
                                        if (slime.getX() == player.getX() - 1 && slime.getY() == player.getY()) {
                                            player.attacking(player.getX() - 1, player.getY());
                                            slime.attacked(player.getPower());
                                            if (game.attack_button_auto_reset) is_attack = false;
                                            hod_end();
                                        }
                                    }
                                    for (Lever lever : levers) {
                                        if (lever.getX() == player.getX() - 1 && lever.getY() == player.getY()) {
                                            player.attacking(player.getX() - 1, player.getY());
                                            lever.click(cages);
                                            if (game.attack_button_auto_reset) is_attack = false;
                                            hod_end();
                                        }
                                    }
                                }
                            } else {
                                if (touch_x == 4 && touch_y == 4) {
                                    if (cages[player.getX()][player.getY() + 1].getMovable()) {
                                        Go(0, 1);
                                        hod_end();
                                    }
                                }
                                if (touch_x == 4 && touch_y == 2) {
                                    if (cages[player.getX()][player.getY() - 1].getMovable()) {
                                        Go(0, -1);
                                        hod_end();
                                    }
                                }
                                if (touch_x == 5 && touch_y == 3) {
                                    if (cages[player.getX() + 1][player.getY()].getMovable()) {
                                        Go(1, 0);
                                        hod_end();
                                    }
                                }
                                if (touch_x == 3 && touch_y == 3) {
                                    if (cages[player.getX() - 1][player.getY()].getMovable()) {
                                        Go(-1, 0);
                                        hod_end();
                                    }
                                }
                            }
                        }
                        return true;
                    }
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
                cages[slime.getX()][slime.getY()].setMovable(true);
                slime.death();
            }
        }
        game.left_border_x = player.getRealX()- game.size*4-game.horizontalOtstup;
        game.right_border_x = player.getRealX()+ game.size*6;
        game.up_border_x = player.getRealX()- game.size*4-game.horizontalOtstup;
        game.down_border_x = player.getRealX()- game.size*4-game.horizontalOtstup;
        game.left_border_y = player.getRealY()- game.size*3-game.verticalOtstup;
        game.right_border_y = player.getRealY()- game.size*3-game.verticalOtstup;
        game.up_border_y = player.getRealY()+ game.size*4;
        game.down_border_y = player.getRealY()- game.size*3-game.verticalOtstup;
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
        game.gameBatch.setProjectionMatrix(camera.combined);
        game.gameBatch.begin();
        for (int i = 0; i < game.cage_x; i++){
            for (int j = 0; j < game.cage_y; j++) {
                //cages[i][j].draw(game.batch, game.size, delta);
                if (Math.abs(i - player.getX()) < 6 && Math.abs(j - player.getY()) < 5) {
                    cages[i][j].draw(game.gameBatch, game.size, delta);
                }
            }
        }
        player.draw(game.gameBatch, game.size, delta);
        for (Slime slime: slimes){
            if (Math.abs(slime.getX() - player.getX()) < 6 && Math.abs(slime.getY() - player.getY()) < 5) {
                slime.draw(game.gameBatch, game.size, delta);
            }
        }
        for (Lever lever: levers){
            lever.draw(game.gameBatch, game.size);
        }
        for (Slime slime: slimes){
            if (Math.abs(slime.getX() - player.getX()) < 6 && Math.abs(slime.getY() - player.getY()) < 5) {
                slime.blast_drow(game.gameBatch, game.size, delta);
            }
        }
        if (game.verticalOtstup !=0){
            game.gameBatch.draw(game.border, game.up_border_x-game.size, game.up_border_y, game.width+2*game.size, game.verticalOtstup +game.size);
            game.gameBatch.draw(game.border, game.down_border_x-game.size, game.down_border_y-game.size, game.width+2*game.size, game.verticalOtstup +game.size);
        }
        if (game.horizontalOtstup !=0) {
            game.gameBatch.draw(game.border, game.left_border_x - game.size, game.left_border_y - game.size, game.horizontalOtstup + game.size, game.height + 2 * game.size);
            game.gameBatch.draw(game.border, game.right_border_x - game.size, game.right_border_y - game.size, game.horizontalOtstup + 2*game.size, game.height + 2 * game.size);
        }
        if (is_attack) game.gameBatch.draw(game.activAttackButton, game.right_border_x - game.size, game.verticalOtstup +game.right_border_y+game.size, game.size, game.size);
        else game.gameBatch.draw(game.passivAttackButton, game.right_border_x - game.size, game.verticalOtstup +game.right_border_y+game.size, game.size, game.size);
        game.gameBatch.draw(game.waitingButton, game.right_border_x - game.size, game.verticalOtstup +game.right_border_y+game.size*2, game.size, game.size);

        game.gameBatch.draw(game.infoWindow, game.right_border_x - game.size, game.verticalOtstup +game.right_border_y + game.size * 4, game.size, game.size * 2);
        if (!game.is_english) {
            game.infoFont.draw(game.gameBatch, "Имя:", game.right_border_x - game.size + game.size / 10, game.verticalOtstup +game.right_border_y + game.size * 6 - game.size * 3 / 20);
            game.infoNameFont.draw(game.gameBatch, player.getName(), game.right_border_x - game.size + game.size / 10, game.verticalOtstup +game.right_border_y + game.size * 6 - game.size * 7 / 20);
            game.infoFont.draw(game.gameBatch, "Здоровье:", game.right_border_x - game.size + game.size / 10, game.verticalOtstup +game.right_border_y + game.size * 6 - game.size * 10 / 20);
            game.infoFont.draw(game.gameBatch, player.getHealth() + "/" + player.getMaxHealth(), game.right_border_x - game.size + game.size / 10, game.verticalOtstup +game.right_border_y + game.size * 6 - game.size * 15 / 20);
            game.infoFont.draw(game.gameBatch, "Ход №:", game.right_border_x - game.size + game.size / 10, game.verticalOtstup +game.right_border_y + game.size * 6 - game.size * 20 / 20);
            game.infoFont.draw(game.gameBatch, "" + game.moves, game.right_border_x - game.size + game.size / 10, game.verticalOtstup +game.right_border_y + game.size * 6 - game.size * 25 / 20);
            game.infoFont.draw(game.gameBatch, "Сила:", game.right_border_x - game.size + game.size / 10, game.verticalOtstup +game.right_border_y + game.size * 6 - game.size * 30 / 20);
            game.infoFont.draw(game.gameBatch, "" + player.getPower(), game.right_border_x - game.size + game.size / 10, game.verticalOtstup +game.right_border_y + game.size * 6 - game.size * 35 / 20);
        }
        else {
            game.infoFont.draw(game.gameBatch, "Name:", game.right_border_x - game.size + game.size / 10, game.verticalOtstup +game.right_border_y + game.size * 6 - game.size * 3 / 20);
            game.infoNameFont.draw(game.gameBatch, player.getName(), game.right_border_x - game.size + game.size / 10, game.verticalOtstup +game.right_border_y + game.size * 6 - game.size * 7 / 20);
            game.infoFont.draw(game.gameBatch, "Health:", game.right_border_x - game.size + game.size / 10, game.verticalOtstup +game.right_border_y + game.size * 6 - game.size * 10 / 20);
            game.infoFont.draw(game.gameBatch, player.getHealth() + "/" + player.getMaxHealth(), game.right_border_x - game.size + game.size / 10, game.verticalOtstup +game.right_border_y + game.size * 6 - game.size * 15 / 20);
            game.infoFont.draw(game.gameBatch, "Moves:", game.right_border_x - game.size + game.size / 10, game.verticalOtstup +game.right_border_y + game.size * 6 - game.size * 20 / 20);
            game.infoFont.draw(game.gameBatch, "" + game.moves, game.right_border_x - game.size + game.size / 10, game.verticalOtstup +game.right_border_y + game.size * 6 - game.size * 25 / 20);
            game.infoFont.draw(game.gameBatch, "Power:", game.right_border_x - game.size + game.size / 10, game.verticalOtstup +game.right_border_y + game.size * 6 - game.size * 30 / 20);
            game.infoFont.draw(game.gameBatch, "" + player.getPower(), game.right_border_x - game.size + game.size / 10, game.verticalOtstup +game.right_border_y + game.size * 6 - game.size * 35 / 20);
        }
        if (is_map_find){
            if (!is_map_activ) {
                game.gameBatch.draw(game.passivMapButton, game.right_border_x - game.size, game.verticalOtstup +game.right_border_y+game.size*3, game.size, game.size);
            }
            else {
                game.gameBatch.draw(game.activMapButton, game.right_border_x - game.size, game.verticalOtstup +game.right_border_y+game.size*3, game.size, game.size);
            }
        }
        if (is_map_activ){
            game.gameBatch.draw(game.mapImg, game.left_border_x+game.horizontalOtstup +game.size, game.left_border_y+game.verticalOtstup, game.size*7, game.size*7);
        }
        if (start_timer>=0){
            start_timer-=delta;
            game.gameBatch.draw(game.border, game.left_border_x, game.left_border_y, game.width+game.size*2, game.height+game.size*2);
        }
        camera.update();
        game.gameBatch.end();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }
}