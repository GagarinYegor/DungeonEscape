package com.dungeon_escape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player {
    private boolean is_attack, is_attacked, is_moving;
    private int x, y;
    private float real_x, real_y, attacked_timer, speed, horizontal_otstup, vertical_otstup, size;
    private Animation player_animation;
    private Texture attacking_player, attacked_player;
    private Blast blast;

    Sound player_attacking_sound, player_attacked_sound;
    Player(int x, int y, float size, float horizontal_otstup, float vertical_otstup, Texture player_texture_region, int frameCount, float speed, Texture player_blast, Texture attacking_player, Texture attacked_player, Sound player_attacking_sound, Sound player_attacked_sound){
        this.speed = speed;
        this.x = x;
        this.y = y;
        real_x = this.x*size+horizontal_otstup;
        real_y = this.y*size+vertical_otstup;
        this.vertical_otstup = vertical_otstup;
        this.horizontal_otstup = horizontal_otstup;
        this.attacking_player = attacking_player;
        this.attacked_player = attacked_player;
        player_animation = new Animation(new TextureRegion(player_texture_region), frameCount, 0.5f);
        this.player_attacking_sound = player_attacking_sound;
        this.player_attacked_sound = player_attacked_sound;
        is_attack = false;
        is_attacked = false;
        attacked_timer = 0;
        blast = new Blast(x, y, size, horizontal_otstup, vertical_otstup, player_blast, 8, speed);
        this.size = size;
    }

    public void draw(SpriteBatch batch, float size, float dt){
        if(!is_moving) {
            if (is_attacked) {
                batch.draw(attacked_player, real_x, real_y, size, size);
                if (attacked_timer < 0.5f) {
                    attacked_timer += dt;
                } else is_attacked = false;
            } else {
                if (is_attack) {
                    batch.draw(attacking_player, real_x, real_y, size, size);
                    if (!blast.get_activ()) is_attack = false;
                } else batch.draw(player_animation.getFrame(), real_x, real_y, size, size);
            }
        }
        else {
            batch.draw(player_animation.getFrame(), real_x, real_y, size, size);
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
            is_moving = false;
        }
        player_animation.update(dt);
        blast.draw(batch, size, dt);
    }

    public void attacking(int x, int y){
        if (is_attack == false) {
           player_attacking_sound.play();
            is_attack = true;
            blast.set_target(x, y, this.x, this.y);
        }
    }
    public void attacked(){
        if (is_attacked == false) {
            player_attacked_sound.play();
            is_attacked = true;
            attacked_timer = 0;
        }
    }
    public void move(int x, int y){
        this.x += x;
        this.y += y;
        is_moving = true;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public boolean getMoving(){return is_moving;}
}
