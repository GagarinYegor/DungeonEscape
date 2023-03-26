package com.dungeonEscape;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Charge {
    private int x, y;
    private float realX, realY, speed, verticalOtstup, horizontalOtstup, size;
    private Animation blastAnimation;
    private boolean isActiv;

    public Charge(int x, int y, float size, float horizontalOtstup, float verticalOtstup, Texture blastTextureRegion, int frameCount, float speed){
        this.speed = speed;
        this.x = x;
        this.y = y;
        realX = this.x*size+ horizontalOtstup;
        realY = this.y*size+ verticalOtstup;
        this.verticalOtstup = verticalOtstup;
        this.horizontalOtstup = horizontalOtstup;
        blastAnimation = new Animation(new TextureRegion(blastTextureRegion), frameCount, 0.5f);
        this.size = size;
        isActiv = false;
    }
    public void draw(SpriteBatch batch, float size, float dt){
        if (isActiv){
            batch.draw(blastAnimation.getFrame(), realX, realY, size, size);
            blastAnimation.update(dt);
            if (realX <x*size+ horizontalOtstup){
                if (realX +speed*dt<x*size+ horizontalOtstup) {
                    realX += speed*dt;
                }
                else realX +=speed*dt-(realX +speed*dt-(x*size+ horizontalOtstup));
            }
            if (realX >x*size+ horizontalOtstup){
                if (realX +speed*dt>x*size+ horizontalOtstup) {
                    realX -= speed*dt;
                }
                else realX -=speed*dt-(realX +speed*dt-(x*size+ horizontalOtstup));
            }
            if (realY <y*size+ verticalOtstup){
                if (realY +speed*dt<y*size+ verticalOtstup) {
                    realY += speed*dt;
                }
                else {
                    realY +=speed*dt-(realY +speed*dt-(y*size+ verticalOtstup));
                }
            }
            if (realY >y*size+ verticalOtstup){
                if (realY +speed*dt>y*size+ verticalOtstup) {
                    realY -= speed*dt;
                }
                else realY -=speed*dt-(realY +speed*dt-(y*size+ verticalOtstup));
            }
        }
        if (realX ==x*size+ horizontalOtstup && realY ==y*size+ verticalOtstup) {
            isActiv = false;
        }
    }

    public void setTarget(int x, int y, int zeroX, int zeroY){
        this.x = x;
        this.y = y;
        realX = zeroX*size+ horizontalOtstup;
        realY = zeroY*size+ verticalOtstup;
        System.out.println(x + " " + y);
        isActiv = true;
    }

    public boolean isActiv(){
        return isActiv;
    }
}
