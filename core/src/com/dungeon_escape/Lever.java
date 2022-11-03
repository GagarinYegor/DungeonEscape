package com.dungeon_escape;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;

public class Lever {
    private int x, y, door_x, door_y;
    private float speed, real_x, real_y, door_real_x, door_real_y, vertical_otstup, horizontal_otstup;
    private boolean is_activ;
    private  Texture activ_lever, passiv_lever, closed_door, opened_door;
    Sound lever_sound, open_door_sound, close_door_sound;
    Lever (int x, int y, int door_x, int door_y, float size, float horizontal_otstup, float vertical_otstup, Texture activ_lever, Texture passiv_lever, float speed, Texture closed_door, Texture opened_door, Sound lever_sound, Sound open_door_sound, Sound close_door_sound){
        this.speed = speed;
        this.x = x;
        this.y = y;
        this.door_x = door_x;
        this.door_y = door_y;
        real_x = this.x*size+horizontal_otstup;
        real_y = this.y*size+vertical_otstup;
        door_real_x = this.door_x*size+horizontal_otstup;
        door_real_y = this.door_y*size+vertical_otstup;
        this.vertical_otstup = vertical_otstup;
        this.horizontal_otstup = horizontal_otstup;
        this.activ_lever = activ_lever;
        this.passiv_lever = passiv_lever;
        this.closed_door = closed_door;
        this.opened_door = opened_door;
        this.lever_sound = lever_sound;
        this.open_door_sound = open_door_sound;
        this.close_door_sound = close_door_sound;
        is_activ = false;
    }

    public void draw(SpriteBatch batch, float size){
        if (is_activ) {
            batch.draw(activ_lever, real_x, real_y, size, size);
            batch.draw(opened_door, door_real_x, door_real_y, size, size*2);
        }
        else {
            batch.draw(passiv_lever, real_x, real_y, size, size);
            batch.draw(closed_door, door_real_x, door_real_y, size, size*2);
        }
    }

    public void click(Cage [][] cages){
        if (!is_activ) open_door_sound.play();
        else close_door_sound.play();
        is_activ = !is_activ;
        cages[door_x][door_y].set_movable(!cages[door_x][door_y].get_movable());
    }

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
}