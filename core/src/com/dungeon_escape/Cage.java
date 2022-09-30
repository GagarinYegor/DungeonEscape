package com.dungeon_escape;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Cage {
    private boolean moveable;
    private Animation cage_animation;
    private int x, y;
    private float real_x, real_y;
    BitmapFont font;

    Cage(int x, int y, boolean moveable, float size, float horizontal_otstup, float vertical_otstup, Texture cage_texture_region, int frameCount){
        this.x = x;
        this.y = y;
        real_x = this.x*size+horizontal_otstup;
        real_y = this.y*size+vertical_otstup;
        this.moveable = moveable;
        this.cage_animation = new Animation(new TextureRegion(cage_texture_region), frameCount, 0.5f);
        font = new BitmapFont();
        font.setColor(Color.RED);
    }
    void change_Animation(Texture cage_texture_region, int frameCount){
        this.cage_animation = new Animation(new TextureRegion(cage_texture_region), frameCount, 0.5f);
    }
    public void draw(SpriteBatch batch, float size, float dt){
        batch.draw(cage_animation.getFrame(), real_x, real_y, size, size);
        font.draw(batch, x+":"+(10-y), real_x+size/2, real_y+size/2);
        cage_animation.update(dt);
    }
    public boolean get_movable(){
        return moveable;
    }

    public void change_movable(){moveable=!moveable;}
}
