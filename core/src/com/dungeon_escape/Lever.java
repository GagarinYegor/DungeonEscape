package com.dungeon_escape;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Lever {
    private int x, y, doorX, doorY;
    private float speed, realX, realY, doorRealX, doorRealY;
    private boolean is_activ;
    private  Texture activ_lever, passivLever, closedDoor, openedDoor;
    Sound lever_sound, openDoorSound, closeDoorSound;
    Lever (int x, int y, int doorX, int door_y, float size, float horizontalOtstup, float verticalOtstup, Texture activLever, Texture passivlever, float speed, Texture closedDoor, Texture openedDoor, Sound leverSound, Sound openDoorSound, Sound closeDoorSound){
        this.speed = speed;
        this.x = x;
        this.y = y;
        this.doorX = doorX;
        this.doorY = door_y;
        realX = this.x*size+ horizontalOtstup;
        realY = this.y*size+verticalOtstup;
        doorRealX = this.doorX *size+ horizontalOtstup;
        doorRealY = this.doorY *size+verticalOtstup;
        this.activ_lever = activLever;
        this.passivLever = passivlever;
        this.closedDoor = closedDoor;
        this.openedDoor = openedDoor;
        this.lever_sound = leverSound;
        this.openDoorSound = openDoorSound;
        this.closeDoorSound = closeDoorSound;
        is_activ = false;
    }

    public void draw(SpriteBatch batch, float size){
        if (is_activ) {
            batch.draw(activ_lever, realX, realY, size, size);
            batch.draw(openedDoor, doorRealX, doorRealY, size, size*2);
        }
        else {
            batch.draw(passivLever, realX, realY, size, size);
            batch.draw(closedDoor, doorRealX, doorRealY, size, size*2);
        }
    }

    public void click(Cage [][] cages){
        lever_sound.play();
        if (!is_activ) openDoorSound.play();
        else closeDoorSound.play();
        is_activ = !is_activ;
        cages[doorX][doorY].setMovable(!cages[doorX][doorY].getMovable());
    }

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
}