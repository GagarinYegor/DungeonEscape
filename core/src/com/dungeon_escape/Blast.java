package com.dungeon_escape;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Blast {
    private int x, y;
    private float real_x, real_y, speed, vertical_otstup, horizontal_otstup, size;
    private Animation blast_animation;
    private boolean is_activ;

    Blast (int x, int y, float size, float horizontal_otstup, float vertical_otstup, Texture blast_texture_region, int frameCount, float speed){
        this.speed = speed;
        this.x = x;
        this.y = y;
        real_x = this.x*size+horizontal_otstup;
        real_y = this.y*size+vertical_otstup;
        this.vertical_otstup = vertical_otstup;
        this.horizontal_otstup = horizontal_otstup;
        blast_animation = new Animation(new TextureRegion(blast_texture_region), frameCount, 0.5f);
        this.size = size;
        is_activ = false;
    }
    public void draw(SpriteBatch batch, float size, float dt){
        if (is_activ){
            batch.draw(blast_animation.getFrame(), real_x, real_y, size, size);
            blast_animation.update(dt);
            if (real_x<x*size+horizontal_otstup){
                if (real_x+speed*dt<x*size+horizontal_otstup) {
                    real_x += speed*dt;
                }
                else real_x+=speed*dt-(real_x+speed*dt-(x*size+horizontal_otstup));
            }
            if (real_x>x*size+horizontal_otstup){
                if (real_x+speed*dt>x*size+horizontal_otstup) {
                    real_x -= speed*dt;
                }
                else real_x-=speed*dt-(real_x+speed*dt-(x*size+horizontal_otstup));
            }
            if (real_y<y*size+vertical_otstup){
                if (real_y+speed*dt<y*size+vertical_otstup) {
                    real_y += speed*dt;
                }
                else {
                    real_y+=speed*dt-(real_y+speed*dt-(y*size+vertical_otstup));
                }
            }
            if (real_y>y*size+vertical_otstup){
                if (real_y+speed*dt>y*size+vertical_otstup) {
                    real_y -= speed*dt;
                }
                else real_y-=speed*dt-(real_y+speed*dt-(y*size+vertical_otstup));
            }
        }
        if (real_x==x*size+horizontal_otstup&&real_y==y*size+vertical_otstup) {
            is_activ = false;
        }
    }

    public void set_target(int x, int y, int zero_x, int zero_y){
        this.x = x;
        this.y = y;
        real_x = zero_x*size+horizontal_otstup;
        real_y = zero_y*size+vertical_otstup;
        System.out.println(x + " " + y);
        is_activ = true;
    }

    public boolean get_activ(){
        return is_activ;
    }
}
