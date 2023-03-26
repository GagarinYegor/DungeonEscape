package com.dungeonEscape;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Lever {
    private int x, y, doorX, doorY;
    private float realX, realY, doorRealX, doorRealY, activatedTimer;
    private boolean isActive, isActivated;
    private Texture activeLever, passiveLever, closedDoor, openedDoor;
    private Sound lever_sound, openDoorSound, closeDoorSound;

    Lever (int x, int y, int doorX, int door_y, float size, float horizontalIndent, float verticalIndent,
           Texture activeLever, Texture passiveLever, Texture closedDoor, Texture openedDoor,
           Sound leverSound, Sound openDoorSound, Sound closeDoorSound, boolean isActive){
        this.x = x;
        this.y = y;
        this.doorX = doorX;
        this.doorY = door_y;
        realX = this.x*size+ horizontalIndent;
        realY = this.y*size+verticalIndent;
        doorRealX = this.doorX *size+ horizontalIndent;
        doorRealY = this.doorY *size+verticalIndent;
        this.activeLever = activeLever;
        this.passiveLever = passiveLever;
        this.closedDoor = closedDoor;
        this.openedDoor = openedDoor;
        this.lever_sound = leverSound;
        this.openDoorSound = openDoorSound;
        this.closeDoorSound = closeDoorSound;
        this.isActive = isActive;
        activatedTimer = 0;
    }

    public void draw(SpriteBatch batch, float size, float dt){
        if (isActivated) {
            if (activatedTimer > 0 ) {
                activatedTimer -= dt;
            } else isActivated = false;
        }
        if (isActive) {
            batch.draw(activeLever, realX, realY, size, size);
            batch.draw(openedDoor, doorRealX, doorRealY, size, size*2);
        }
        else {
            batch.draw(passiveLever, realX, realY, size, size);
            batch.draw(closedDoor, doorRealX, doorRealY, size, size*2);
        }
    }

    public void click(Cage [][] cages){
        if (!isActivated) {
            lever_sound.play();
            if (!isActive) openDoorSound.play();
            else closeDoorSound.play();
            isActive = !isActive;
            cages[doorX][doorY].setMovable(!cages[doorX][doorY].getMovable());
            activatedTimer = 0.5f;
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
    public boolean getActive() {return isActive;}
}