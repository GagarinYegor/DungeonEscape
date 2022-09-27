package com.dungeon_escape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Slime {
    private boolean is_attack, is_attacked;
    private int x, y;
    private float real_x, real_y, attacked_timer, speed, horizontal_otstup, vertical_otstup, size;
    private Animation slime_animation;
    private Texture attacking_slime, title_text_table, attacked_slime;
    private BitmapFont font;
    private Blast blast;
    Sound slime_attacking_sound, slime_attacked_sound;

    Slime(int x, int y, float size, float horizontal_otstup, float vertical_otstup, Texture slime_texture_region, int frameCount, float speed, Texture slime_blast, Texture attacking_slime, Texture attacked_slime, Sound slime_attacking_sound, Sound slime_attacked_sound, Texture title_text_table){
        this.speed = speed;
        this.x = x;
        this.y = y;
        real_x = this.x*size+horizontal_otstup;
        real_y = this.y*size+vertical_otstup;
        this.vertical_otstup = vertical_otstup;
        this.horizontal_otstup = horizontal_otstup;
        this.attacking_slime = attacking_slime;
        this.attacked_slime = attacked_slime;
        slime_animation = new Animation(new TextureRegion(slime_texture_region), frameCount, 0.5f);
        this.title_text_table = title_text_table;
        font = new BitmapFont();
        font.setColor(Color.RED);
        font.getData().setScale(size/100, size/75);
        this.slime_attacking_sound = slime_attacking_sound;
        this.slime_attacked_sound = slime_attacked_sound;
        is_attack = false;
        is_attacked = false;
        attacked_timer = 0;
        blast = new Blast(x, y, size, horizontal_otstup, vertical_otstup, slime_blast, 8, speed);
        this.size = size;
    }

    public void draw(SpriteBatch batch, float size, float dt){
        batch.draw(title_text_table, real_x, real_y + size - size / 4, size, size / 4);
        font.draw(batch, "Hp:100/100", real_x + size / 10, real_y + size - size / 20);
        if (is_attacked) {
            batch.draw(attacked_slime, real_x, real_y, size, size);
            if (attacked_timer<0.5f) {
                attacked_timer+=dt;
            }
            else is_attacked = false;
        }
        else {
            batch.draw(slime_animation.getFrame(), real_x, real_y, size, size);
            if (is_attack) {
                batch.draw(attacking_slime, real_x, real_y, size, size);
                if (!blast.get_activ()) is_attack = false;
            }
        }
        slime_animation.update(dt);
        blast.draw(batch, size, dt);
    }
    public void attacking(int x, int y){
        if (is_attack == false) {
            slime_attacking_sound.play();
            is_attack = true;
            blast.set_target(x, y, this.x, this.y);
        }
    }
    public void attacked(){
        if (is_attacked == false) {
            slime_attacked_sound.play();
            is_attacked = true;
            attacked_timer = 0;
        }
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
}
