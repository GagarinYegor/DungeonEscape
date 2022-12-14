package com.dungeon_escape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player {
    private boolean is_attack, is_attacked, is_moving, is_right;
    private int x, y, health, max_health, power;
    private float real_x, real_y, attacked_timer, speed, horizontal_otstup, vertical_otstup, size;
    private Animation player_animation_right, player_animation_left, player_mowing_right, player_mowing_left;
    private Texture attacking_player_right, attacked_player_right, attacking_player_left, attacked_player_left;
    private Blast blast;
    private String name;

    Sound player_attacking_sound, player_attacked_sound;
    Player(int x, int y, float size, float horizontal_otstup, float vertical_otstup,
           Texture player_texture_region_right, Texture player_texture_region_left,
           int frameCount,
           Texture player_texture_region_mowing_right, Texture player_texture_region_mowing_left,
           int MovingframeCount,
           float speed, Texture player_blast,
           Texture attacking_player_right, Texture attacked_player_right,
           Texture attacking_player_left, Texture attacked_player_left,
           Sound player_attacking_sound, Sound player_attacked_sound, String name){
        this.speed = speed;
        this.x = x;
        this.y = y;
        real_x = this.x*size+horizontal_otstup;
        real_y = this.y*size+vertical_otstup;
        this.vertical_otstup = vertical_otstup;
        this.horizontal_otstup = horizontal_otstup;
        this.attacking_player_right = attacking_player_right;
        this.attacked_player_right = attacked_player_right;
        this.attacking_player_left = attacking_player_left;
        this.attacked_player_left = attacked_player_left;
        player_animation_right = new Animation(new TextureRegion(player_texture_region_right), frameCount, 0.5f);
        player_animation_left = new Animation(new TextureRegion(player_texture_region_left), frameCount, 0.5f);
        player_mowing_right = new Animation(new TextureRegion(player_texture_region_mowing_right), MovingframeCount, 0.3f);
        player_mowing_left = new Animation(new TextureRegion(player_texture_region_mowing_left), MovingframeCount, 0.3f);
        this.player_attacking_sound = player_attacking_sound;
        this.player_attacked_sound = player_attacked_sound;
        is_attack = false;
        is_attacked = false;
        attacked_timer = 0;
        blast = new Blast(x, y, size, horizontal_otstup, vertical_otstup, player_blast, 8, speed);
        this.size = size;
        max_health = 100;
        health = max_health;
        power = 20;
        this.name = name;
        is_right = true;
    }

    public void draw(SpriteBatch batch, float size, float dt){
        if(!is_moving) {
            if (is_attacked) {
                if (is_right) batch.draw(attacked_player_right, real_x, real_y, size, size);
                else batch.draw(attacked_player_left, real_x, real_y, size, size);
                if (attacked_timer < 0.5f) {
                    attacked_timer += dt;
                } else is_attacked = false;
            } else {
                if (is_attack) {
                    if (is_right) batch.draw(attacking_player_right, real_x, real_y, size, size);
                    else batch.draw(attacking_player_left, real_x, real_y, size, size);
                    if (!blast.get_activ()) is_attack = false;
                } else {
                    if (is_right) batch.draw(player_animation_right.getFrame(), real_x, real_y, size, size);
                    else batch.draw(player_animation_left.getFrame(), real_x, real_y, size, size);
                }
            }
        }
        else {
            if (is_right) {
                batch.draw(player_mowing_right.getFrame(), real_x, real_y, size, size);
                player_mowing_right.update(dt);
            }
            else {
                batch.draw(player_mowing_left.getFrame(), real_x, real_y, size, size);
                player_mowing_left.update(dt);
            }
            if (real_x<x*size+horizontal_otstup){
                is_right = true;
                if (real_x+speed*dt<x*size+horizontal_otstup) {
                    real_x += speed*dt;
                }
                //else real_x+=speed*dt-(real_x+speed*dt-(x*size+horizontal_otstup));
                else real_x = x*size+horizontal_otstup;
            }
            if (real_x>x*size+horizontal_otstup){
                is_right = false;
                if (real_x-speed*dt>x*size+horizontal_otstup) {
                    real_x -= speed*dt;
                }
                //else real_x-=speed*dt-(real_x+speed*dt-(x*size+horizontal_otstup));
                else real_x = x*size+horizontal_otstup;
            }
            if (real_y<y*size+vertical_otstup){
                if (real_y+speed*dt<y*size+vertical_otstup) {
                    real_y += speed*dt;
                }
                //else real_y+=speed*dt-(real_y+speed*dt-(y*size+vertical_otstup));
                else real_y = y*size+vertical_otstup;
            }
            if (real_y>y*size+vertical_otstup){
                if (real_y-speed*dt>y*size+vertical_otstup) {
                    real_y -= speed*dt;
                }
                //else real_y-=speed*dt-(real_y+speed*dt-(y*size+vertical_otstup));
                else real_y = y*size+vertical_otstup;
            }
        }
        if (real_x==x*size+horizontal_otstup&&real_y==y*size+vertical_otstup) {
            is_moving = false;
        }
        player_animation_right.update(dt);
        player_animation_left.update(dt);
        blast.draw(batch, size, dt);
    }

    public void attacking(int x, int y){
        if (is_attack == false) {
            player_attacking_sound.play();
            is_attack = true;
            blast.set_target(x, y, this.x, this.y);
        }
    }
    public void attacked(int damage){
        if (is_attacked == false) {
            health -= damage;
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
    public float get_real_X(){
        return real_x;
    }
    public float get_real_Y(){
        return real_y;
    }
    public int getHealth(){
        return health;
    }
    public int getMaxHealth(){
        return max_health;
    }
    public int getPower(){
        return power;
    }
    public boolean getMoving(){return is_moving;}
    public boolean getAttack(){return is_attack;}
    public boolean getAttacked(){return is_attacked;}
    public String getName(){return name;}
    public void setHealth(int newHealth){health = newHealth;}
}
