package com.dungeon_escape;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Lever {
    private int x, y, doorX, doorY;
    private float realX, realY, doorRealX, doorRealY, activatedTimer;
    private boolean isActiv, isActivated;
    private  Texture activLever, passivLever, closedDoor, openedDoor;
    private Sound lever_sound, openDoorSound, closeDoorSound;
    Lever (int x, int y, int doorX, int door_y, float size, float horizontalOtstup, float verticalOtstup, Texture activLever, Texture passivlever, Texture closedDoor, Texture openedDoor, Sound leverSound, Sound openDoorSound, Sound closeDoorSound, boolean isActiv){
        this.x = x;
        this.y = y;
        this.doorX = doorX;
        this.doorY = door_y;
        realX = this.x*size+ horizontalOtstup;
        realY = this.y*size+verticalOtstup;
        doorRealX = this.doorX *size+ horizontalOtstup;
        doorRealY = this.doorY *size+verticalOtstup;
        this.activLever = activLever;
        this.passivLever = passivlever;
        this.closedDoor = closedDoor;
        this.openedDoor = openedDoor;
        this.lever_sound = leverSound;
        this.openDoorSound = openDoorSound;
        this.closeDoorSound = closeDoorSound;
        this.isActiv = isActiv;
        activatedTimer = 0;
    }

    public void draw(SpriteBatch batch, float size, float dt){
        if (isActivated) {
            if (activatedTimer < 0.5f) {
                activatedTimer += dt;
            } else isActivated = false;
        }
        if (isActiv) {
            batch.draw(activLever, realX, realY, size, size);
            batch.draw(openedDoor, doorRealX, doorRealY, size, size*2);
        }
        else {
            batch.draw(passivLever, realX, realY, size, size);
            batch.draw(closedDoor, doorRealX, doorRealY, size, size*2);
        }
    }

    public void click(Cage [][] cages){
        if (!isActivated) {
            lever_sound.play();
            if (!isActiv) openDoorSound.play();
            else closeDoorSound.play();
            isActiv = !isActiv;
            cages[doorX][doorY].setMovable(!cages[doorX][doorY].getMovable());
            activatedTimer = 0;
            isActivated = true;
        }
    }

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public boolean getActivated() {return isActivated;}
    public boolean isActiv() {return isActiv;}
}