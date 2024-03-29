package com.dungeonEscape;
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
    float startTimer, cameraMoveUp, cameraMoveDown, cameraMoveLeft, cameraMoveRight, clickDelay;
    boolean isMove, isAttack, checkFlag, slimeMove, isMapFind, isMapActive, isTipsActiv, isDialogOpen, close, isClickDelay;
    FileHandle savedFile;
    int currentTip;

    private void save() {
        savedFile.writeString("///moves///" + "\n", false);
        savedFile.writeString(game.moves + "\n", true);
        savedFile.writeString("///is_map_find, is_map_activ, is_attack, is_tips_activ///" + "\n", true);
        String text = "";
        if (isMapFind) text += "1 ";
        else text += "0 ";
        if (isMapActive) text += "1 ";
        else text += "0 ";
        if (isAttack) text += "1 ";
        else text += "0 ";
        if (isTipsActiv) text += "1" + "\n";
        else text += "0" + "\n";
        savedFile.writeString(text, true);
        savedFile.writeString("///playerX, playerY, name, health, maxHealth///" + "\n", true);
        savedFile.writeString(player.getX() + " " + player.getY() + " " + player.getName() + " " + player.getMaxHealth() + " " + player.getHealth() + "\n", true);
        savedFile.writeString("///slimes///" + "\n", true);
        for (int i = 0; i<slimes.length; i++) {
            savedFile.writeString(slimes[i].getY() + " " + slimes[i].getX() + " " + slimes[i].getHealth() + " " + slimes[i].getMaxHealth() + "\n", true);
        }
        savedFile.writeString("///levers///" + "\n", true);
        for (int i = 0; i<levers.length; i++) {
            if (levers[i].getActive()) savedFile.writeString("1" + "\n", true);
            else savedFile.writeString("0" + "\n", true);
        }
    }

    public void click(){
        clickDelay = 0.5f;
        isClickDelay = true;
    }

    public void check_hod(){
        if (player.getX() == 20 && player.getY() == 0){
            game.theme.stop();
            game.setScreen(new WinScreen(game));
        }
        if (player.getX() == 37 && player.getY() == 3 && !isMapFind){
            isMapFind = true;
            cages[37][3].change_Animation(game.stoneFloorTextureRegion, 1);
        }
        if (player.getHealth()>0) {
            checkFlag = true;
            for (Slime slime : slimes) {
                if (slime.getHealth()>0) {
                    if (slime.getMoving()) checkFlag = false;
                    if (slime.getAttacking()) checkFlag = false;
                    if (slime.getAttacked()) checkFlag = false;
                }
            }
            for (Lever lever : levers) {
                if (lever.getActivated()) checkFlag = false;
            }
            if (player.getMoving()) checkFlag = false;
            if (player.getAttacking()) checkFlag = false;
            if (player.getAttacked()) checkFlag = false;
            if (isClickDelay) checkFlag = false;
            if (checkFlag) isMove = true;
        }
        else{
            game.theme.stop();
            game.setScreen(new DeathScreen(game));
        }

    }

    public void Go(int x, int y){
        isMove = false;
        if (x==0&&y==1){
            cameraMoveUp += game.size;
        }
        if (x==0&&y==-1){
            cameraMoveDown += game.size;
        }
        if (x==1&&y==0){
            cameraMoveRight += game.size;
        }
        if (x==-1&&y==0){
            cameraMoveLeft += game.size;
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
                    isMove = false;
                } else {
                    if (Math.abs(slime.getX() - player.getX()) < 5 && Math.abs(slime.getY() - player.getY()) < 5) {
                        slimeMove = true;
                        cages[slime.getX()][slime.getY()].setMovable(true);
                        //System.out.println("I see you!");
                        if (slimeMove && slime.getX() == player.getX() && slime.getY() < player.getY()){
                            //cages[slime.getX()][slime.getY()].set_movable(true);
                            if (cages[slime.getX()][slime.getY()+1].getMovable()) slime.move(0, 1);
                            else if (cages[slime.getX()+1][slime.getY()].getMovable()) slime.move(1, 0);
                            else if (cages[slime.getX()-1][slime.getY()].getMovable()) slime.move(-1, 0);
                            slimeMove = false;
                            //cages[slime.getX()][slime.getY()].set_movable(false);
                        }
                        if (slimeMove && slime.getX() == player.getX() && slime.getY() > player.getY()){
                            //cages[slime.getX()][slime.getY()].set_movable(true);
                            if (cages[slime.getX()][slime.getY()-1].getMovable()) slime.move(0, -1);
                            else if (cages[slime.getX()+1][slime.getY()].getMovable()) slime.move(1, 0);
                            else if (cages[slime.getX()-1][slime.getY()].getMovable()) slime.move(-1, 0);
                            slimeMove = false;
                            //cages[slime.getX()][slime.getY()].set_movable(false);
                        }
                        if (slimeMove && slime.getX() > player.getX() && slime.getY() == player.getY()){
                            //cages[slime.getX()][slime.getY()].set_movable(true);
                            if (cages[slime.getX()-1][slime.getY()].getMovable()) slime.move(-1, 0);
                            else if (cages[slime.getX()][slime.getY()+1].getMovable()) slime.move(0, 1);
                            else if (cages[slime.getX()][slime.getY()-1].getMovable()) slime.move(0, -1);
                            slimeMove = false;
                            //cages[slime.getX()][slime.getY()].set_movable(false);
                        }
                        if (slimeMove && slime.getX() < player.getX() && slime.getY() == player.getY()){
                            //cages[slime.getX()][slime.getY()].set_movable(true);
                            if (cages[slime.getX()+1][slime.getY()].getMovable()) slime.move(+1, 0);
                            else if (cages[slime.getX()][slime.getY()+1].getMovable()) slime.move(0, 1);
                            else if (cages[slime.getX()][slime.getY()-1].getMovable()) slime.move(0, -1);
                            slimeMove = false;
                            //cages[slime.getX()][slime.getY()].set_movable(false);
                        }
                        if (slimeMove && slime.getX() < player.getX() && slime.getY() < player.getY()){
                            //cages[slime.getX()][slime.getY()].set_movable(true);
                            if (Math.abs(slime.getX() - player.getX()) < Math.abs(slime.getY()- player.getY())){
                                if (cages[slime.getX()][slime.getY()+1].getMovable()) slime.move(0, 1);
                                else if (cages[slime.getX()+1][slime.getY()].getMovable()) slime.move(1, 0);
                                else if (cages[slime.getX()+1][slime.getY()+1].getMovable()) slime.move(1, 1);
                            }
                            else if (Math.abs(slime.getX() - player.getX()) > Math.abs(slime.getY() - player.getY())){
                                if (cages[slime.getX() + 1][slime.getY()].getMovable()) slime.move(1, 0);
                                else if (cages[slime.getX()][slime.getY() + 1].getMovable()) slime.move(0, 1);
                                else if (cages[slime.getX() + 1][slime.getY() + 1].getMovable()) slime.move(1, 1);
                            }
                            else if (Math.abs(slime.getX() - player.getX()) == Math.abs(slime.getY() - player.getY())){
                                if (cages[slime.getX() + 1][slime.getY() + 1].getMovable()) slime.move(1, 1);
                                else if (cages[slime.getX()][slime.getY() + 1].getMovable()) slime.move(0, 1);
                                else if (cages[slime.getX() + 1][slime.getY()].getMovable()) slime.move(1, 0);
                            }
                            slimeMove = false;
                            //cages[slime.getX()][slime.getY()].set_movable(false);
                        }
                        if (slimeMove && slime.getX() > player.getX() && slime.getY() < player.getY()){
                            //cages[slime.getX()][slime.getY()].set_movable(true);
                            if (Math.abs(slime.getX() - player.getX()) < Math.abs(slime.getY()- player.getY())){
                                if (cages[slime.getX()][slime.getY() + 1].getMovable()) slime.move(0, 1);
                                else if (cages[slime.getX() - 1][slime.getY()].getMovable()) slime.move(-1, 0);
                                else if (cages[slime.getX() - 1][slime.getY() + 1].getMovable()) slime.move(-1, 1);
                            }
                            else if (Math.abs(slime.getX() - player.getX()) > Math.abs(slime.getY() - player.getY())){
                                if (cages[slime.getX() - 1][slime.getY()].getMovable()) slime.move(-1, 0);
                                else if (cages[slime.getX()][slime.getY() + 1].getMovable()) slime.move(0, 1);
                                else if (cages[slime.getX() - 1][slime.getY() + 1].getMovable()) slime.move(-1, 1);
                            }
                            else if (Math.abs(slime.getX() - player.getX()) == Math.abs(slime.getY()- player.getY())){
                                if (cages[slime.getX() - 1][slime.getY() + 1].getMovable()) slime.move(-1, 1);
                                else if (cages[slime.getX()][slime.getY() + 1].getMovable()) slime.move(0, 1);
                                else if (cages[slime.getX() - 1][slime.getY()].getMovable()) slime.move(-1, 0);
                            }
                            slimeMove = false;
                            //cages[slime.getX()][slime.getY()].set_movable(false);
                        }
                        if (slimeMove && slime.getX() < player.getX() && slime.getY() > player.getY()){
                            //cages[slime.getX()][slime.getY()].set_movable(true);
                            if (Math.abs(slime.getX() - player.getX()) < Math.abs(slime.getY()- player.getY())){
                                if (cages[slime.getX()][slime.getY() - 1].getMovable()) slime.move(0, -1);
                                else if (cages[slime.getX() + 1][slime.getY()].getMovable()) slime.move(1, 0);
                                else if (cages[slime.getX() + 1][slime.getY() - 1].getMovable()) slime.move(1, -1);
                            }
                            else if (Math.abs(slime.getX() - player.getX()) > Math.abs(slime.getY()- player.getY())){
                                if (cages[slime.getX() + 1][slime.getY()].getMovable()) slime.move(1, 0);
                                else if (cages[slime.getX()][slime.getY() - 1].getMovable()) slime.move(0, -1);
                                else if (cages[slime.getX() + 1][slime.getY() - 1].getMovable()) slime.move(1, -1);
                            }
                            else if (Math.abs(slime.getX() - player.getX()) == Math.abs(slime.getY()- player.getY())){
                                if (cages[slime.getX() + 1][slime.getY() - 1].getMovable()) slime.move(1, -1);
                                else if (cages[slime.getX()][slime.getY() - 1].getMovable()) slime.move(0, -1);
                                else if (cages[slime.getX() + 1][slime.getY()].getMovable()) slime.move(1, 0);
                            }
                            slimeMove = false;
                            //cages[slime.getX()][slime.getY()].set_movable(false);
                        }
                        if (slimeMove && slime.getX() > player.getX() && slime.getY() > player.getY()){
                            //cages[slime.getX()][slime.getY()].set_movable(true);
                            if (Math.abs(slime.getX() - player.getX()) < Math.abs(slime.getY() - player.getY())){
                                if (cages[slime.getX()][slime.getY() - 1].getMovable()) slime.move(0, -1);
                                else if (cages[slime.getX() - 1][slime.getY()].getMovable()) slime.move(-1, 0);
                                else if (cages[slime.getX() - 1][slime.getY() - 1].getMovable()) slime.move(-1, -1);
                            }
                            else if (Math.abs(slime.getX() - player.getX()) > Math.abs(slime.getY() - player.getY())){
                                if (cages[slime.getX() - 1][slime.getY()].getMovable()) slime.move(-1, 0);
                                else if (cages[slime.getX()][slime.getY() - 1].getMovable()) slime.move(0, -1);
                                else if (cages[slime.getX() - 1][slime.getY() - 1].getMovable()) slime.move(-1, -1);
                            }
                            else if (Math.abs(slime.getX() - player.getX()) == Math.abs(slime.getY() - player.getY())){
                                if (cages[slime.getX() - 1][slime.getY() - 1].getMovable()) slime.move(-1, -1);
                                else if (cages[slime.getX()][slime.getY() - 1].getMovable()) slime.move(0, -1);
                                else if (cages[slime.getX() - 1][slime.getY()].getMovable()) slime.move(-1, 0);
                            }
                            slimeMove = false;
                            //cages[slime.getX()][slime.getY()].set_movable(false);
                        }
                        cages[slime.getX()][slime.getY()].setMovable(false);
                    }
                }
            }
        }
        if (damage > 0) player.attacked(damage);
    }

    public GameScreen(final DungeonEscape game) {
        this.game = game;
        savedFile = Gdx.files.local("text_resources/saved_records.txt");
        game.theme.setLooping(true);
        game.theme.play();
        game.theme.setVolume(0.5f);
        isClickDelay = false;
        clickDelay = 0;
        startTimer = 0.1f;
        close = false;
        camera = new OrthographicCamera(game.width, game.height);
        camera.setToOrtho(false, game.width, game.height);
        cameraMoveUp = 0;
        cameraMoveDown = 0;
        cameraMoveLeft = 0;
        cameraMoveRight = 0;
        isMove = true;
        currentTip = 0;
        slimes = new Slime[game.slimeMassY];
        cages = new Cage[game.cageX][game.cageY];
        levers = new Lever[game.leverMassY];
        isDialogOpen = false;
        for (int i = 0; i < game.cageX; i++) {
            for (int j = 0; j < game.cageY; j++) {
                if (game.map[i][j].contains("sf__"))
                    cages[i][j] = new Cage(i, j, true, game.size, game.horizontalIndend, game.verticalIndent, game.stoneFloorTextureRegion, 1);
                else if (game.map[i][j].contains("sfwm"))
                    cages[i][j] = new Cage(i, j, true, game.size, game.horizontalIndend, game.verticalIndent, game.sfwm, 1);
                else if (game.map[i][j].contains("nthi"))
                    cages[i][j] = new Cage(i, j, false, game.size, game.horizontalIndend, game.verticalIndent, game.playerCharge, 1);
                else if (game.map[i][j].contains("clmn"))
                    cages[i][j] = new Cage(i, j, false, game.size, game.horizontalIndend, game.verticalIndent, game.clmn, 1);
                else if (game.map[i][j].contains("exit"))
                    cages[i][j] = new Cage(i, j, true, game.size, game.horizontalIndend, game.verticalIndent, game.exitImg, 1);
                else if (game.map[i][j].contains("sfsc"))
                    cages[i][j] = new Cage(i, j, true, game.size, game.horizontalIndend, game.verticalIndent, game.stoneFloorSc, 1);
                else if (game.map[i][j].contains("wdwt"))
                    cages[i][j] = new Cage(i, j, false, game.size, game.horizontalIndend, game.verticalIndent, game.wdwt, 12);

                else if (game.map[i][j].contains("wd__"))
                    cages[i][j] = new Cage(i, j, false, game.size, game.horizontalIndend, game.verticalIndent, game.wd__, 1);
                else if (game.map[i][j].contains("wu__"))
                    cages[i][j] = new Cage(i, j, false, game.size, game.horizontalIndend, game.verticalIndent, game.wu__, 1);
                else if (game.map[i][j].contains("wl__"))
                    cages[i][j] = new Cage(i, j, false, game.size, game.horizontalIndend, game.verticalIndent, game.wl__, 1);
                else if (game.map[i][j].contains("wr__"))
                    cages[i][j] = new Cage(i, j, false, game.size, game.horizontalIndend, game.verticalIndent, game.wr__, 1);

                else if (game.map[i][j].contains("cul_"))
                    cages[i][j] = new Cage(i, j, false, game.size, game.horizontalIndend, game.verticalIndent, game.cul_, 1);
                else if (game.map[i][j].contains("cur_"))
                    cages[i][j] = new Cage(i, j, false, game.size, game.horizontalIndend, game.verticalIndent, game.cur_, 1);
                else if (game.map[i][j].contains("cdl_"))
                    cages[i][j] = new Cage(i, j, false, game.size, game.horizontalIndend, game.verticalIndent, game.cdl_, 1);
                else if (game.map[i][j].contains("cdr_"))
                    cages[i][j] = new Cage(i, j, false, game.size, game.horizontalIndend, game.verticalIndent, game.cdr_, 1);

                else if (game.map[i][j].contains("cwul"))
                    cages[i][j] = new Cage(i, j, false, game.size, game.horizontalIndend, game.verticalIndent, game.cwul, 1);
                else if (game.map[i][j].contains("cwur"))
                    cages[i][j] = new Cage(i, j, false, game.size, game.horizontalIndend, game.verticalIndent, game.cwur, 1);
                else if (game.map[i][j].contains("cwdl"))
                    cages[i][j] = new Cage(i, j, false, game.size, game.horizontalIndend, game.verticalIndent, game.cwdl, 1);
                else if (game.map[i][j].contains("cwdr"))
                    cages[i][j] = new Cage(i, j, false, game.size, game.horizontalIndend, game.verticalIndent, game.cwdr, 1);

                else
                    cages[i][j] = new Cage(i, j, true, game.size, game.horizontalIndend, game.verticalIndent, game.beginButton, 1);
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
            if (saved_strings.get(3).split(" ")[0].equals("0")) isMapFind = false;
            else isMapFind = true;
            if (saved_strings.get(3).split(" ")[1].equals("0")) isMapActive = false;
            else isMapActive = true;
            if (saved_strings.get(3).split(" ")[2].equals("0")) isAttack = false;
            else isAttack = true;
            if (saved_strings.get(3).split(" ")[3].equals("0")) isTipsActiv = false;
            else isTipsActiv = true;
            player = new Player(Integer.parseInt(saved_strings.get(5).split(" ")[0]), Integer.parseInt(saved_strings.get(5).split(" ")[1]), game.size, game.horizontalIndend, game.verticalIndent,
                    game.playerTextureRegionRight, game.playerTextureRegionLeft,
                    12,
                    game.playerTextureRegionMowingRight, game.playerTextureRegionMowingLeft,
                    14,
                    game.speed, game.playerCharge,
                    game.playerAttackingRight, game.playerAttackedRight,
                    game.playerAttackingLeft, game.playerAttackedLeft,
                    game.playerAttackingSound, game.sound, saved_strings.get(5).split(" ")[2], Integer.parseInt(saved_strings.get(5).split(" ")[3]), Integer.parseInt(saved_strings.get(5).split(" ")[4]));
            camera.translate((player.getX()-4)*game.size, (player.getY()-3)*game.size);
            game.name = player.getName();
            for (int i = 0; i < slimes.length; i++) {
                slimes[i] = new Slime(Integer.parseInt(saved_strings.get(7+i).split(" ")[1]),
                        Integer.parseInt(saved_strings.get(7+i).split(" ")[0]), game.size,
                        game.horizontalIndend, game.verticalIndent, game.greenSlimeTextureRegion,
                        6, game.speed, game.slimeChargeTextureRegion, game.greenAttackingSlimeTextureRegion,
                        game.greenAttackedSlimeTextureRegion, game.slimeAttackingSound, game.slimeAttackedSound,
                        game.titleTextTable, game.slimeFont, Integer.parseInt(saved_strings.get(7+i).split(" ")[2]),
                        Integer.parseInt(saved_strings.get(7+i).split(" ")[3]));
                cages[slimes[i].getX()][slimes[i].getY()].setMovable(false);
            }
            //System.out.println(slimes.length);
            for (int i = 0; i < game.leverMassY; i++) {
                if (game.levers_mass[i][4] == 0) {
                    levers[i] = new Lever(game.levers_mass[i][0], game.levers_mass[i][1], game.levers_mass[i][2], game.levers_mass[i][3], game.size, game.horizontalIndend, game.verticalIndent, game.activeLever, game.passiveLever, game.cvd, game.ovd, game.leverSound, game.openDoorsSound, game.closedDoorsSound, saved_strings.get(8+slimes.length+i).equals("1"));
                    cages[game.levers_mass[i][0]][game.levers_mass[i][1]].setMovable(false);
                    cages[game.levers_mass[i][2]][game.levers_mass[i][3]].setMovable(saved_strings.get(8+slimes.length+i).equals("1"));
                }
                if (game.levers_mass[i][4] == 1) {
                    levers[i] = new Lever(game.levers_mass[i][0], game.levers_mass[i][1], game.levers_mass[i][2], game.levers_mass[i][3], game.size, game.horizontalIndend, game.verticalIndent, game.activeLever, game.passiveLever, game.chd, game.ohd, game.slimeAttackedSound, game.openDoorsSound, game.closedDoorsSound, saved_strings.get(8+slimes.length+i).equals("1"));
                    cages[game.levers_mass[i][0]][game.levers_mass[i][1]].setMovable(false);
                    cages[game.levers_mass[i][2]][game.levers_mass[i][3]].setMovable(saved_strings.get(8+slimes.length+i).equals("1"));
                }
                if (game.levers_mass[i][4] == 2) {
                    levers[i] = new Lever(game.levers_mass[i][0], game.levers_mass[i][1], game.levers_mass[i][2], game.levers_mass[i][3], game.size, game.horizontalIndend, game.verticalIndent, game.playerCharge, game.playerCharge, game.exitDoor, game.exitDoor, game.slimeAttackedSound, game.openDoorsSound, game.closedDoorsSound, saved_strings.get(8+slimes.length+i).equals("1"));
                    cages[game.levers_mass[i][0]][game.levers_mass[i][1]].setMovable(true);
                    cages[game.levers_mass[i][2]][game.levers_mass[i][3]].setMovable(true);
                }
            }
            if (isMapFind) cages[37][3].change_Animation(game.stoneFloorTextureRegion, 1);
        }
        else {
            game.moves = 0;
            isMapFind = false;
            isMapActive = false;
            isTipsActiv = true;
            player = new Player(3, 3, game.size, game.horizontalIndend, game.verticalIndent,
                    game.playerTextureRegionRight, game.playerTextureRegionLeft, 12,
                    game.playerTextureRegionMowingRight, game.playerTextureRegionMowingLeft, 14,
                    game.speed, game.playerCharge,
                    game.playerAttackingRight, game.playerAttackedRight,
                    game.playerAttackingLeft, game.playerAttackedLeft,
                    game.playerAttackingSound, game.sound, game.name, 100, 100);
            camera.translate(-game.size, 0);
            isAttack = false;

            for (int i = 0; i < slimes.length; i++) {
                slimes[i] = new Slime(game.slimes_mass[i][1], game.slimes_mass[i][0], game.size, game.horizontalIndend, game.verticalIndent, game.greenSlimeTextureRegion, 6, game.speed, game.slimeChargeTextureRegion, game.greenAttackingSlimeTextureRegion, game.greenAttackedSlimeTextureRegion, game.slimeAttackingSound, game.slimeAttackedSound, game.titleTextTable, game.slimeFont, 100, 100);
                cages[slimes[i].getX()][slimes[i].getY()].setMovable(false);
            }

            for (int i = 0; i < game.leverMassY; i++) {
                if (game.levers_mass[i][4] == 0) {
                    levers[i] = new Lever(game.levers_mass[i][0], game.levers_mass[i][1], game.levers_mass[i][2], game.levers_mass[i][3], game.size, game.horizontalIndend, game.verticalIndent, game.activeLever, game.passiveLever, game.cvd, game.ovd, game.leverSound, game.openDoorsSound, game.closedDoorsSound, false);
                    cages[game.levers_mass[i][0]][game.levers_mass[i][1]].setMovable(false);
                    cages[game.levers_mass[i][2]][game.levers_mass[i][3]].setMovable(false);
                }
                if (game.levers_mass[i][4] == 1) {
                    levers[i] = new Lever(game.levers_mass[i][0], game.levers_mass[i][1], game.levers_mass[i][2], game.levers_mass[i][3], game.size, game.horizontalIndend, game.verticalIndent, game.activeLever, game.passiveLever, game.chd, game.ohd, game.slimeAttackedSound, game.openDoorsSound, game.closedDoorsSound, false);
                    cages[game.levers_mass[i][0]][game.levers_mass[i][1]].setMovable(false);
                    cages[game.levers_mass[i][2]][game.levers_mass[i][3]].setMovable(false);
                }
                if (game.levers_mass[i][4] == 2) {
                    levers[i] = new Lever(game.levers_mass[i][0], game.levers_mass[i][1], game.levers_mass[i][2], game.levers_mass[i][3], game.size, game.horizontalIndend, game.verticalIndent, game.playerCharge, game.playerCharge, game.exitDoor, game.exitDoor, game.slimeAttackedSound, game.openDoorsSound, game.closedDoorsSound, false);
                    cages[game.levers_mass[i][0]][game.levers_mass[i][1]].setMovable(true);
                    cages[game.levers_mass[i][2]][game.levers_mass[i][3]].setMovable(true);
                }
            }
        }
        game.gameListener = new Input.TextInputListener() {
            @Override
            public void input(String s) {
                //Gdx.input.setOnscreenKeyboardVisible(true);
                if ((!game.isEnglish && s.equals("Да")) || (game.isEnglish && s.equals("Yes"))) {
                    close = true;
                }
                else {
                    if (!game.isEnglish) {
                        isDialogOpen = true;
                        Gdx.input.getTextInput(game.gameListener, "Вы уверены что хотите отменить игру?", "", "Введите \"Да\" в это поле");
                    } else {
                        isDialogOpen = true;
                        Gdx.input.getTextInput(game.gameListener, "Are you sure you want to cancel the game?", "", "Enter \"Yes\" here");
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
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            public boolean keyDown(int keycode){
                if (!isClickDelay && keycode == Input.Keys.M && isMapFind && !isTipsActiv && !isDialogOpen) {
                    click();
                    isMapActive = !isMapActive;
                    save();
                }
                if (!isClickDelay && keycode == Input.Keys.N && isTipsActiv && !isDialogOpen) {
                    click();
                    if (currentTip + 1 <= 6) {
                        currentTip += 1;
                    }
                    else {
                        currentTip = 0;
                    }
                    save();
                }
                if (!isClickDelay && keycode == Input.Keys.A && !isMapActive && !isTipsActiv && !isDialogOpen) {
                    click();
                    isAttack = !isAttack;
                    save();
                }
                if (!isClickDelay && keycode == Input.Keys.T && !isMapActive && !isDialogOpen) {
                    click();
                    isTipsActiv = !isTipsActiv;
                    save();
                }
                if (!isClickDelay && keycode == Input.Keys.E && !isMapActive && !isTipsActiv) {
                    click();
                    if (!isDialogOpen) {
                        if (!game.isEnglish) {
                            isDialogOpen = true;
                            Gdx.input.getTextInput(game.gameListener, "Вы уверены что хотите отменить игру?", "", "Введите \"Да\" в это поле");
                        } else {
                            isDialogOpen = true;
                            Gdx.input.getTextInput(game.gameListener, "Are you sure you want to cancel the game?", "", "Enter \"Yes\" here");
                        }
                    }
                }
                if(!isClickDelay && !isMapActive && !isTipsActiv && !isDialogOpen) {
                    if (isMove) {
                        if (keycode == Input.Keys.P) {
                            click();
                            hod_end();
                        }
                        if (isAttack) {
                                if (keycode == Input.Keys.UP) { // up
                                    for (Slime slime : slimes) {
                                        if (slime.getX() == player.getX() && slime.getY() == player.getY() + 1) {
                                            player.attacking(player.getX(), player.getY() + 1);
                                            slime.attacked(player.getPower());
                                            if (game.attackButtonAutoReset) isAttack = false;
                                            click();
                                            hod_end();
                                        }
                                    }
                                    for (Lever lever : levers) {
                                        if (lever.getX() == player.getX() && lever.getY() == player.getY() + 1) {
                                            player.attacking(player.getX(), player.getY() + 1);
                                            lever.click(cages);
                                            if (game.attackButtonAutoReset) isAttack = false;
                                            click();
                                            hod_end();
                                        }
                                    }
                                }
                                if (keycode == Input.Keys.DOWN) { // down
                                    for (Slime slime : slimes) {
                                        if (slime.getX() == player.getX() && slime.getY() == player.getY() - 1) {
                                            player.attacking(player.getX(), player.getY() - 1);
                                            slime.attacked(player.getPower());
                                            if (game.attackButtonAutoReset) isAttack = false;
                                            click();
                                            hod_end();
                                        }
                                    }
                                    for (Lever lever : levers) {
                                        if (lever.getX() == player.getX() && lever.getY() == player.getY() - 1) {
                                            player.attacking(player.getX(), player.getY() - 1);
                                            lever.click(cages);
                                            if (game.attackButtonAutoReset) isAttack = false;
                                            click();
                                            hod_end();
                                        }
                                    }
                                }
                                if (keycode == Input.Keys.RIGHT) { // right
                                    for (Slime slime : slimes) {
                                        if (slime.getX() == player.getX() + 1 && slime.getY() == player.getY()) {
                                            player.attacking(player.getX() + 1, player.getY());
                                            slime.attacked(player.getPower());
                                            if (game.attackButtonAutoReset) isAttack = false;
                                            click();
                                            hod_end();
                                        }
                                    }
                                    for (Lever lever : levers) {
                                        if (lever.getX() == player.getX() + 1 && lever.getY() == player.getY()) {
                                            player.attacking(player.getX() + 1, player.getY());
                                            lever.click(cages);
                                            if (game.attackButtonAutoReset) isAttack = false;
                                            click();
                                            hod_end();
                                        }
                                    }
                                }
                                if (keycode == Input.Keys.LEFT) { //left
                                    for (Slime slime : slimes) {
                                        if (slime.getX() == player.getX() - 1 && slime.getY() == player.getY()) {
                                            player.attacking(player.getX() - 1, player.getY());
                                            slime.attacked(player.getPower());
                                            if (game.attackButtonAutoReset) isAttack = false;
                                            click();
                                            hod_end();
                                        }
                                    }
                                    for (Lever lever : levers) {
                                        if (lever.getX() == player.getX() - 1 && lever.getY() == player.getY()) {
                                            player.attacking(player.getX() - 1, player.getY());
                                            lever.click(cages);
                                            if (game.attackButtonAutoReset) isAttack = false;
                                            click();
                                            hod_end();
                                        }
                                    }
                                }
                            } else {
                            if (keycode == Input.Keys.UP) {
                                    if (cages[player.getX()][player.getY() + 1].getMovable()) {
                                        Go(0, 1);
                                        click();
                                        hod_end();
                                    }
                                }
                            if (keycode == Input.Keys.DOWN) {
                                    if (cages[player.getX()][player.getY() - 1].getMovable()) {
                                        Go(0, -1);
                                        click();
                                        hod_end();
                                    }
                                }
                            if (keycode == Input.Keys.RIGHT) {
                                    if (cages[player.getX() + 1][player.getY()].getMovable()) {
                                        Go(1, 0);
                                        click();
                                        hod_end();
                                    }
                                }
                            if (keycode == Input.Keys.LEFT) {
                                    if (cages[player.getX() - 1][player.getY()].getMovable()) {
                                        Go(-1, 0);
                                        click();
                                        hod_end();
                                    }
                                }
                        }
                    }
                    return true;
                }
                return false;
            }

            public boolean touchDown (int x, int y, int pointer, int button) {
                System.out.println(isClickDelay);
                int touchX;
                int touchY;
                if ((Gdx.input.getX() - game.horizontalIndend) / game.size / camera.zoom >= 0){
                    touchX = (int)((Gdx.input.getX() - game.horizontalIndend) / (game.size + game.size * (camera.zoom - 1)));
                }
                else{
                    touchX = (int) ((Gdx.input.getX()-game.horizontalIndend) / game.size / camera.zoom - 1);
                }
                if ((game.height - (game.verticalIndent +Gdx.input.getY())) / game.size >=0){
                    touchY= (int) ((game.height - (game.verticalIndent +Gdx.input.getY())) / game.size);
                }
                else {
                    touchY = (int) ((game.height - (game.verticalIndent +Gdx.input.getY())) / game.size - 1);
                }
                if (button == Input.Buttons.LEFT) {
                    if (!isClickDelay && touchX == 9 && touchY == 3 && isMapFind && !isTipsActiv && !isDialogOpen) {
                        isMapActive = !isMapActive;
                        click();
                        save();
                    }
                    if (!isClickDelay && touchX == 0 && touchY == 6 && isTipsActiv && !isDialogOpen) {
                        if (currentTip + 1 <= 6) {
                            currentTip += 1;
                        }
                        else {
                            currentTip = 0;
                        }
                        click();
                        save();
                    }
                    if (!isClickDelay && touchX == 9 && touchY == 1 && !isMapActive && !isTipsActiv && !isDialogOpen) {
                        isAttack = !isAttack;
                        click();
                        save();
                    }
                    if (!isClickDelay && touchX == 9 && touchY == 6 && !isMapActive && !isDialogOpen) {
                        isTipsActiv = !isTipsActiv;
                        click();
                        save();
                    }
                    if (!isClickDelay && touchX == 9 && touchY == 0 && !isMapActive && !isTipsActiv) {
                        if (!isDialogOpen) {
                            if (!game.isEnglish) {
                                isDialogOpen = true;
                                click();
                                Gdx.input.getTextInput(game.gameListener, "Вы уверены что хотите отменить игру?", "", "Введите \"Да\" в это поле");
                            } else {
                                isDialogOpen = true;
                                click();
                                Gdx.input.getTextInput(game.gameListener, "Are you sure you want to cancel the game?", "", "Enter \"Yes\" here");
                            }
                        }
                    }
                    if(!isClickDelay && !isMapActive && !isTipsActiv && !isDialogOpen) {
                        if (isMove) {
                            if (touchX == 9 && touchY == 2) {
                                hod_end();
                                click();
                            }
                            if (isAttack) {
                                if (touchX == 4 && touchY == 4) { // up
                                    for (Slime slime : slimes) {
                                        if (slime.getX() == player.getX() && slime.getY() == player.getY() + 1) {
                                            player.attacking(player.getX(), player.getY() + 1);
                                            slime.attacked(player.getPower());
                                            if (game.attackButtonAutoReset) isAttack = false;
                                            hod_end();
                                            click();
                                        }
                                    }
                                    for (Lever lever : levers) {
                                        if (lever.getX() == player.getX() && lever.getY() == player.getY() + 1) {
                                            player.attacking(player.getX(), player.getY() + 1);
                                            lever.click(cages);
                                            if (game.attackButtonAutoReset) isAttack = false;
                                            hod_end();
                                            click();
                                        }
                                    }
                                }
                                if (touchX == 4 && touchY == 2) { // down
                                    for (Slime slime : slimes) {
                                        if (slime.getX() == player.getX() && slime.getY() == player.getY() - 1) {
                                            player.attacking(player.getX(), player.getY() - 1);
                                            slime.attacked(player.getPower());
                                            if (game.attackButtonAutoReset) isAttack = false;
                                            hod_end();
                                            click();
                                        }
                                    }
                                    for (Lever lever : levers) {
                                        if (lever.getX() == player.getX() && lever.getY() == player.getY() - 1) {
                                            player.attacking(player.getX(), player.getY() - 1);
                                            lever.click(cages);
                                            if (game.attackButtonAutoReset) isAttack = false;
                                            hod_end();
                                            click();
                                        }
                                    }
                                }
                                if (touchX == 5 && touchY == 3) { // right
                                    for (Slime slime : slimes) {
                                        if (slime.getX() == player.getX() + 1 && slime.getY() == player.getY()) {
                                            player.attacking(player.getX() + 1, player.getY());
                                            slime.attacked(player.getPower());
                                            if (game.attackButtonAutoReset) isAttack = false;
                                            hod_end();
                                            click();
                                        }
                                    }
                                    for (Lever lever : levers) {
                                        if (lever.getX() == player.getX() + 1 && lever.getY() == player.getY()) {
                                            player.attacking(player.getX() + 1, player.getY());
                                            lever.click(cages);
                                            if (game.attackButtonAutoReset) isAttack = false;
                                            hod_end();
                                            click();
                                        }
                                    }
                                }
                                if (touchX == 3 && touchY == 3) { //left
                                    for (Slime slime : slimes) {
                                        if (slime.getX() == player.getX() - 1 && slime.getY() == player.getY()) {
                                            player.attacking(player.getX() - 1, player.getY());
                                            slime.attacked(player.getPower());
                                            if (game.attackButtonAutoReset) isAttack = false;
                                            hod_end();click();
                                        }
                                    }
                                    for (Lever lever : levers) {
                                        if (lever.getX() == player.getX() - 1 && lever.getY() == player.getY()) {
                                            player.attacking(player.getX() - 1, player.getY());
                                            lever.click(cages);
                                            if (game.attackButtonAutoReset) isAttack = false;
                                            hod_end();
                                            click();
                                        }
                                    }
                                }
                            } else {
                                if (touchX == 4 && touchY == 4) {
                                    if (cages[player.getX()][player.getY() + 1].getMovable()) {
                                        Go(0, 1);
                                        hod_end();
                                        click();
                                    }
                                }
                                if (touchX == 4 && touchY == 2) {
                                    if (cages[player.getX()][player.getY() - 1].getMovable()) {
                                        Go(0, -1);
                                        hod_end();
                                        click();
                                    }
                                }
                                if (touchX == 5 && touchY == 3) {
                                    if (cages[player.getX() + 1][player.getY()].getMovable()) {
                                        Go(1, 0);
                                        hod_end();
                                        click();
                                    }
                                }
                                if (touchX == 3 && touchY == 3) {
                                    if (cages[player.getX() - 1][player.getY()].getMovable()) {
                                        Go(-1, 0);
                                        hod_end();
                                        click();
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
        if (isClickDelay){
            clickDelay -= delta;
            if (clickDelay < 0){
                isClickDelay = false;
            }
        }

        if (close) {
            game.theme.stop();

            game.setScreen(new DeathScreen(game));
        }
        check_hod();
        for (Slime slime:slimes){
            if (slime.getHealth()<=0){
                cages[slime.getX()][slime.getY()].setMovable(true);
                slime.death();
            }
        }
        game.leftBorderX = player.getRealX() - game.size * 4 - game.horizontalIndend;
        game.rightBorderX = player.getRealX() + game.size * 6;
        game.upBorderX = player.getRealX() - game.size * 4 - game.horizontalIndend;
        game.downBorderX = player.getRealX() - game.size * 4 - game.horizontalIndend;
        game.leftBorderY = player.getRealY() - game.size * 3 - game.verticalIndent;
        game.rightBorderY = player.getRealY() - game.size * 3 - game.verticalIndent;
        game.upBorderY = player.getRealY() + game.size * 4;
        game.downBorderY = player.getRealY() - game.size * 3 - game.verticalIndent;
        if (cameraMoveRight > 0){
            camera.translate(game.speed*delta, 0);
            cameraMoveRight -= game.speed*delta;
            //if (camera_move_right == 0) is_hod = true;
        }
        if (game.speed*delta > cameraMoveRight && cameraMoveRight > 0){
            camera.translate(cameraMoveRight, 0);
            cameraMoveRight = 0;
            //is_hod = true;
        }
        if (cameraMoveLeft > 0){
            camera.translate(-game.speed*delta, 0);
            cameraMoveLeft -= game.speed*delta;
            //if (camera_move_left == 0) is_hod = true;
        }
        if (game.speed*delta > cameraMoveLeft && cameraMoveLeft > 0){
            camera.translate(-cameraMoveLeft, 0);
            cameraMoveLeft = 0;
            //is_hod = true;
        }
        if (cameraMoveUp > 0){
            camera.translate(0, game.speed*delta);
            cameraMoveUp -= game.speed*delta;
            //if (camera_move_up == 0) is_hod = true;
        }
        if (game.speed*delta > cameraMoveUp && cameraMoveUp > 0){
            camera.translate(0, cameraMoveUp);
            cameraMoveUp = 0;
            //is_hod = true;
        }
        if (cameraMoveDown > 0){
            camera.translate(0, -game.speed*delta);
            cameraMoveDown -= game.speed*delta;
            //if (camera_move_down == 0) is_hod = true;
        }
        if (game.speed*delta > cameraMoveDown && cameraMoveDown > 0){
            camera.translate(0, -cameraMoveDown);
            cameraMoveDown = 0;
            //is_hod = true;
        }

        Gdx.gl.glClearColor(0.4f, 0.4f, 0.4f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.gameBatch.setProjectionMatrix(camera.combined);
        game.gameBatch.begin();
        for (int i = 0; i < game.cageX; i++){
            for (int j = 0; j < game.cageY; j++) {
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
            lever.draw(game.gameBatch, game.size, delta);
        }
        for (Slime slime: slimes){
            if (Math.abs(slime.getX() - player.getX()) < 6 && Math.abs(slime.getY() - player.getY()) < 5) {
                slime.blastDraw(game.gameBatch, game.size, delta);
            }
        }
        if (game.verticalIndent !=0){
            game.gameBatch.draw(game.border, game.upBorderX -game.size, game.upBorderY, game.width+2*game.size, game.verticalIndent +game.size);
            game.gameBatch.draw(game.border, game.downBorderX -game.size, game.downBorderY -game.size, game.width+2*game.size, game.verticalIndent +game.size);
        }
        if (game.horizontalIndend !=0) {
            game.gameBatch.draw(game.border, game.leftBorderX - game.size, game.leftBorderY - game.size, game.horizontalIndend + game.size, game.height + 2 * game.size);
            game.gameBatch.draw(game.border, game.rightBorderX - game.size, game.rightBorderY - game.size, game.horizontalIndend + 2*game.size, game.height + 2 * game.size);
        }
        if (isAttack) game.gameBatch.draw(game.activeAttackButton, game.rightBorderX - game.size, game.verticalIndent +game.rightBorderY +game.size, game.size, game.size);
        else game.gameBatch.draw(game.passiveAttackButton, game.rightBorderX - game.size, game.verticalIndent +game.rightBorderY +game.size, game.size, game.size);
        if (isTipsActiv) game.gameBatch.draw(game.activeTipsButton, game.rightBorderX - game.size, game.verticalIndent +game.rightBorderY +game.size*6, game.size, game.size);
        else game.gameBatch.draw(game.passiveTipsButton, game.rightBorderX - game.size, game.verticalIndent +game.rightBorderY +game.size*6, game.size, game.size);
        game.gameBatch.draw(game.closeButton, game.rightBorderX - game.size, game.verticalIndent +game.rightBorderY +game.size*0, game.size, game.size);
        game.gameBatch.draw(game.waitingButton, game.rightBorderX - game.size, game.verticalIndent +game.rightBorderY +game.size*2, game.size, game.size);

        game.gameBatch.draw(game.infoWindow, game.rightBorderX - game.size, game.verticalIndent +game.rightBorderY + game.size * 4, game.size, game.size * 2);
        if (!game.isEnglish) {
            game.infoFont.draw(game.gameBatch, "Имя:", game.rightBorderX - game.size + game.size / 10, game.verticalIndent +game.rightBorderY + game.size * 6 - game.size * 3 / 20);
            game.infoNameFont.draw(game.gameBatch, player.getName(), game.rightBorderX - game.size + game.size / 10, game.verticalIndent +game.rightBorderY + game.size * 6 - game.size * 7 / 20);
            game.infoFont.draw(game.gameBatch, "Здоровье:", game.rightBorderX - game.size + game.size / 10, game.verticalIndent +game.rightBorderY + game.size * 6 - game.size * 10 / 20);
            game.infoFont.draw(game.gameBatch, player.getHealth() + "/" + player.getMaxHealth(), game.rightBorderX - game.size + game.size / 10, game.verticalIndent +game.rightBorderY + game.size * 6 - game.size * 15 / 20);
            game.infoFont.draw(game.gameBatch, "Ход №:", game.rightBorderX - game.size + game.size / 10, game.verticalIndent +game.rightBorderY + game.size * 6 - game.size * 20 / 20);
            game.infoFont.draw(game.gameBatch, "" + game.moves, game.rightBorderX - game.size + game.size / 10, game.verticalIndent +game.rightBorderY + game.size * 6 - game.size * 25 / 20);
            game.infoFont.draw(game.gameBatch, "Сила:", game.rightBorderX - game.size + game.size / 10, game.verticalIndent +game.rightBorderY + game.size * 6 - game.size * 30 / 20);
            game.infoFont.draw(game.gameBatch, "" + player.getPower(), game.rightBorderX - game.size + game.size / 10, game.verticalIndent +game.rightBorderY + game.size * 6 - game.size * 35 / 20);
        }
        else {
            game.infoFont.draw(game.gameBatch, "Name:", game.rightBorderX - game.size + game.size / 10, game.verticalIndent +game.rightBorderY + game.size * 6 - game.size * 3 / 20);
            game.infoNameFont.draw(game.gameBatch, player.getName(), game.rightBorderX - game.size + game.size / 10, game.verticalIndent +game.rightBorderY + game.size * 6 - game.size * 7 / 20);
            game.infoFont.draw(game.gameBatch, "Health:", game.rightBorderX - game.size + game.size / 10, game.verticalIndent +game.rightBorderY + game.size * 6 - game.size * 10 / 20);
            game.infoFont.draw(game.gameBatch, player.getHealth() + "/" + player.getMaxHealth(), game.rightBorderX - game.size + game.size / 10, game.verticalIndent +game.rightBorderY + game.size * 6 - game.size * 15 / 20);
            game.infoFont.draw(game.gameBatch, "Moves:", game.rightBorderX - game.size + game.size / 10, game.verticalIndent +game.rightBorderY + game.size * 6 - game.size * 20 / 20);
            game.infoFont.draw(game.gameBatch, "" + game.moves, game.rightBorderX - game.size + game.size / 10, game.verticalIndent +game.rightBorderY + game.size * 6 - game.size * 25 / 20);
            game.infoFont.draw(game.gameBatch, "Power:", game.rightBorderX - game.size + game.size / 10, game.verticalIndent +game.rightBorderY + game.size * 6 - game.size * 30 / 20);
            game.infoFont.draw(game.gameBatch, "" + player.getPower(), game.rightBorderX - game.size + game.size / 10, game.verticalIndent +game.rightBorderY + game.size * 6 - game.size * 35 / 20);
        }
        if (isMapFind){
            if (!isMapActive) {
                game.gameBatch.draw(game.passivMapButton, game.rightBorderX - game.size, game.verticalIndent +game.rightBorderY +game.size*3, game.size, game.size);
            }
            else {
                game.gameBatch.draw(game.activeMapButton, game.rightBorderX - game.size, game.verticalIndent +game.rightBorderY +game.size*3, game.size, game.size);
            }
        }
        if (isMapActive){
            game.gameBatch.draw(game.mapImg, game.leftBorderX +game.horizontalIndend +game.size, game.leftBorderY +game.verticalIndent, game.size*7, game.size*7);
        }

        if (isTipsActiv){
            switch (currentTip) {
                case 0:
                    game.gameBatch.draw(game.tip0, game.leftBorderX + game.horizontalIndend, game.leftBorderY + game.verticalIndent, game.size * 10, game.size * 7);
                    if (game.isEnglish){
                        game.tipsFont.draw(game.gameBatch, "Welcome to the DungeonEscape game!"+"\n"+"The goal of the game is to find a way out of the dungeon"+"\n"+"in the minimum number of moves."+"\n"+"Slimes can get in the way that will attack the player."+"\n"+"The player can also attack slimes.", game.horizontalIndend +game.leftBorderX + game.size * 1.1f, game.verticalIndent + game.leftBorderY + game.size * 6.6f);

                    }
                    else {
                        game.tipsFont.draw(game.gameBatch, "Добро пожаловать в игру DungeonEscape!"+"\n"+"Цель игры - найти выход из подземелья за минимальное число ходов."+"\n"+"На пути могут попадаться слаймы, которые будут атаковать игрока."+"\n"+"Игрок также может атаковать слаймов.", game.horizontalIndend + game.leftBorderX + game.size * 1.1f, game.verticalIndent + game.leftBorderY + game.size * 6.6f);
                    }
                    break;
                case 1:
                    game.gameBatch.draw(game.tip1, game.leftBorderX + game.horizontalIndend, game.leftBorderY + game.verticalIndent, game.size * 10, game.size * 7);
                    if (game.isEnglish){
                        game.tipsFont.draw(game.gameBatch, "Next tip", game.horizontalIndend +game.leftBorderX + game.size * 1.1f, game.verticalIndent + game.leftBorderY + game.size * 6.6f);
                        game.tipsFont.draw(game.gameBatch, "Open/close tips", game.horizontalIndend +game.leftBorderX + game.size * 7.3f, game.verticalIndent + game.leftBorderY + game.size * 6.6f);
                        game.tipsFont.draw(game.gameBatch, "Stats"+"\n"+"window", game.horizontalIndend +game.leftBorderX + game.size * 9.1f, game.verticalIndent + game.leftBorderY + game.size * 5.9f);
                        game.tipsFont.draw(game.gameBatch, "Map"+"\n"+"button", game.horizontalIndend +game.leftBorderX + game.size * 8f, game.verticalIndent + game.leftBorderY + game.size * 3.7f);
                        game.tipsFont.draw(game.gameBatch, "Wait"+"\n"+"button", game.horizontalIndend +game.leftBorderX + game.size * 8f, game.verticalIndent + game.leftBorderY + game.size * 2.7f);
                        game.tipsFont.draw(game.gameBatch, "Attack"+"\n"+"button", game.horizontalIndend +game.leftBorderX + game.size * 8f, game.verticalIndent + game.leftBorderY + game.size * 1.7f);
                        game.tipsFont.draw(game.gameBatch, "Pass"+"\n"+"button", game.horizontalIndend +game.leftBorderX + game.size * 8f, game.verticalIndent + game.leftBorderY + game.size * 0.7f);
                        game.tipsFont.draw(game.gameBatch, "Hero", game.horizontalIndend +game.leftBorderX + game.size * 4.2f, game.verticalIndent + game.leftBorderY + game.size * 4.2f);

                    }
                    else {
                        game.tipsFont.draw(game.gameBatch, "Следующая подсказка", game.horizontalIndend +game.leftBorderX + game.size * 1.1f, game.verticalIndent + game.leftBorderY + game.size * 6.6f);
                        game.tipsFont.draw(game.gameBatch, "Открыть/закрыть подсказки", game.horizontalIndend +game.leftBorderX + game.size * 5.9f, game.verticalIndent + game.leftBorderY + game.size * 6.6f);
                        game.tipsFont.draw(game.gameBatch, "Окно"+"\n"+"харак-"+"\n"+"терис-"+"\n"+"тик", game.horizontalIndend +game.leftBorderX + game.size * 9.1f, game.verticalIndent + game.leftBorderY + game.size * 5.9f);
                        game.tipsFont.draw(game.gameBatch, "Кнопка"+"\n"+"карты", game.horizontalIndend +game.leftBorderX + game.size * 8f, game.verticalIndent + game.leftBorderY + game.size * 3.7f);
                        game.tipsFont.draw(game.gameBatch, "Кнопка"+"\n"+"пропуска"+"\n"+"хода", game.horizontalIndend +game.leftBorderX + game.size * 8f, game.verticalIndent + game.leftBorderY + game.size * 2.9f);
                        game.tipsFont.draw(game.gameBatch, "Кнопка"+"\n"+"атаки", game.horizontalIndend +game.leftBorderX + game.size * 8f, game.verticalIndent + game.leftBorderY + game.size * 1.7f);
                        game.tipsFont.draw(game.gameBatch, "Сдаться", game.horizontalIndend +game.leftBorderX + game.size * 8f, game.verticalIndent + game.leftBorderY + game.size * 0.6f);
                        game.tipsFont.draw(game.gameBatch, "Персонаж", game.horizontalIndend +game.leftBorderX + game.size * 4f, game.verticalIndent + game.leftBorderY + game.size * 4.2f);
                    }
                    break;
                case 2:
                    game.gameBatch.draw(game.tip2, game.leftBorderX + game.horizontalIndend, game.leftBorderY + game.verticalIndent, game.size * 10, game.size * 7);
                    if (game.isEnglish){
                        game.tipsFont.draw(game.gameBatch, "      Unactive"+"\n"+"attack button", game.horizontalIndend +game.leftBorderX + game.size * 0.7f, game.verticalIndent + game.leftBorderY + game.size * 5f);
                        game.tipsFont.draw(game.gameBatch, "       Active"+"\n"+"attack button", game.horizontalIndend +game.leftBorderX + game.size * 2.8f, game.verticalIndent + game.leftBorderY + game.size * 5f);
                        game.tipsFont.draw(game.gameBatch, "   Unactive"+"\n"+"map button", game.horizontalIndend +game.leftBorderX + game.size * 0.85f, game.verticalIndent + game.leftBorderY + game.size * 1f);
                        game.tipsFont.draw(game.gameBatch, "      Active"+"\n"+"map button", game.horizontalIndend +game.leftBorderX + game.size * 2.85f, game.verticalIndent + game.leftBorderY + game.size * 1f);
                        game.tipsFont.draw(game.gameBatch, "   Unactive"+"\n"+" tips button", game.horizontalIndend +game.leftBorderX + game.size * 5.85f, game.verticalIndent + game.leftBorderY + game.size * 1f);
                        game.tipsFont.draw(game.gameBatch, "      Active"+"\n"+" tips button", game.horizontalIndend +game.leftBorderX + game.size * 7.85f, game.verticalIndent + game.leftBorderY + game.size * 1f);

                    }
                    else {
                        game.tipsFont.draw(game.gameBatch, " Неактивная"+"\n"+"кнопка атаки", game.horizontalIndend +game.leftBorderX + game.size * 0.8f, game.verticalIndent + game.leftBorderY + game.size * 5f);
                        game.tipsFont.draw(game.gameBatch, "    Активная"+"\n"+"кнопка атаки", game.horizontalIndend +game.leftBorderX + game.size * 2.8f, game.verticalIndent + game.leftBorderY + game.size * 5f);
                        game.tipsFont.draw(game.gameBatch, " Неактивная"+"\n"+"кнопка карты", game.horizontalIndend +game.leftBorderX + game.size * 0.8f, game.verticalIndent + game.leftBorderY + game.size * 1f);
                        game.tipsFont.draw(game.gameBatch, "    Активная"+"\n"+"кнопка карты", game.horizontalIndend +game.leftBorderX + game.size * 2.8f, game.verticalIndent + game.leftBorderY + game.size * 1f);
                        game.tipsFont.draw(game.gameBatch, "      Неактивная"+"\n"+"кнопка подсказок", game.horizontalIndend +game.leftBorderX + game.size * 5.5f, game.verticalIndent + game.leftBorderY + game.size * 1f);
                        game.tipsFont.draw(game.gameBatch, "         Активная"+"\n"+"кнопка подсказок", game.horizontalIndend +game.leftBorderX + game.size * 7.5f, game.verticalIndent + game.leftBorderY + game.size * 1f);

                    }
                    break;
                case 3:

                    game.gameBatch.draw(game.tip3, game.leftBorderX + game.horizontalIndend, game.leftBorderY + game.verticalIndent, game.size * 10, game.size * 7);
                    if (game.isEnglish){
                        game.tipsFont.draw(game.gameBatch, "A hero in a move can move to one of the neighbors of the cages."+"\n"+"To do this, you need to click on the cage to which you want to move.", game.horizontalIndend +game.leftBorderX + game.size * 0, game.verticalIndent + game.leftBorderY + game.size * 0.8f);
                    }
                    else {
                        game.tipsFont.draw(game.gameBatch, "Персонаж за ход может переместиться на одну из соседник клеток."+"\n"+"Для этого нужно нажать на клетку на которую вы хотите переместиться.", game.horizontalIndend +game.leftBorderX + game.size * 0, game.verticalIndent + game.leftBorderY + game.size * 0.8f);
                    }
                    break;
                case 4:
                    game.gameBatch.draw(game.tip4, game.leftBorderX + game.horizontalIndend, game.leftBorderY + game.verticalIndent, game.size * 10, game.size * 7);
                    if (game.isEnglish){
                        game.tipsFont.draw(game.gameBatch, "A hero in a move can attack one of the neighbors of the cages. This requires"+"\n"+"press the attack button and then the cage you want to attack."+"\n"+"If there is no enemy or lever in it, nothing will happen and the move will not be spent.", game.horizontalIndend +game.leftBorderX + game.size * 0, game.verticalIndent + game.leftBorderY + game.size * 1f);
                    }
                    else {
                        game.tipsFont.draw(game.gameBatch, "Персонаж за ход может атаковать одну из соседник клеток. Для этого нужно"+"\n"+"нажать кнопку атаки, а затем на клетку, которую вы хотите атаковать."+"\n"+"Если в ней нет врага или рычага ничего не произойдет и ход не потратится.", game.horizontalIndend +game.leftBorderX + game.size * 0, game.verticalIndent + game.leftBorderY + game.size * 1f);
                    }
                    break;
                case 5:
                    game.gameBatch.draw(game.tip5, game.leftBorderX + game.horizontalIndend, game.leftBorderY + game.verticalIndent, game.size * 10, game.size * 7);
                    if (game.isEnglish){
                        game.tipsFont.draw(game.gameBatch, "A slime in a move can move to one of the neighbors of the cages."+"\n"+"Unlike the player, he can also move diagonally.", game.horizontalIndend +game.leftBorderX + game.size * 0, game.verticalIndent + game.leftBorderY + game.size * 0.8f);
                    }
                    else {
                        game.tipsFont.draw(game.gameBatch, "Слайм за ход может переместиться на одну из соседник клеток."+"\n"+"В отличии от игрока он также может перемещаться по диагонали.", game.horizontalIndend +game.leftBorderX + game.size * 0, game.verticalIndent + game.leftBorderY + game.size * 0.8f);
                    }
                    break;
                case 6:
                    game.gameBatch.draw(game.tip6, game.leftBorderX + game.horizontalIndend, game.leftBorderY + game.verticalIndent, game.size * 10, game.size * 7);
                    if (game.isEnglish){
                        game.tipsFont.draw(game.gameBatch, "Slime in a move can attack within a radius of two cages."+"\n"+"Also, the slime charge of the slime can fly over the door.", game.horizontalIndend +game.leftBorderX + game.size * 0, game.verticalIndent + game.leftBorderY + game.size * 0.8f);
                    }
                    else {
                        game.tipsFont.draw(game.gameBatch, "Слайм за ход может атаковать в радиусе двух клеток."+"\n"+"Также слизь слайма может перелетать над дверями.", game.horizontalIndend +game.leftBorderX + game.size * 0, game.verticalIndent + game.leftBorderY + game.size * 0.8f);
                    }
                    break;
            }
            game.gameBatch.draw(game.arrowNext, game.leftBorderX + game.horizontalIndend, game.leftBorderY + game.verticalIndent + game.size*6, game.size, game.size);
        }

        if (startTimer >=0){
            startTimer -=delta;
            game.gameBatch.draw(game.border, game.leftBorderX, game.leftBorderY, game.width+game.size*2, game.height+game.size*2);
        }
        camera.update();
        game.gameBatch.end();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }
}