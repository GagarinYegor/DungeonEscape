package com.dungeonEscape;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Cage {
    private boolean moveable;
    private Animation cageAnimation;
    private int x, y;
    private float realX, realY;
    //BitmapFont font;

    Cage(int x, int y, boolean moveable, float size, float horizontal_otstup, float vertical_otstup, Texture cage_texture_region, int frameCount){
        this.x = x;
        this.y = y;
        realX = this.x*size+horizontal_otstup;
        realY = this.y*size+vertical_otstup;
        this.moveable = moveable;
        this.cageAnimation = new Animation(new TextureRegion(cage_texture_region), frameCount, 0.5f);
        //font = new BitmapFont();
        //font.setColor(Color.RED);
    }
    void change_Animation(Texture cage_texture_region, int frameCount){
        this.cageAnimation = new Animation(new TextureRegion(cage_texture_region), frameCount, 0.5f);
    }
    public void draw(SpriteBatch batch, float size, float dt){
        batch.draw(cageAnimation.getFrame(), realX, realY, size, size);
        //font.draw(batch, x+":"+(y), real_x+size/2, real_y+size/2);
        cageAnimation.update(dt);
    }
    public boolean getMovable(){
        return moveable;
    }

    public void setMovable(boolean new_movable){moveable=new_movable;}
}
