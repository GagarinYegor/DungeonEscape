package com.dungeon_escape;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

public class FontString {
    String font_string;
    float real_x, real_y;

    FontString(String font_string, int x, int y, float size, float horizontal_otstup, float vertical_otstup){
        System.out.println(font_string);
        this.font_string=font_string;
        real_x = x*size+horizontal_otstup;
        real_y = y*size+vertical_otstup;
    }

    public void draw(SpriteBatch batch, float size, HashMap<String, TextureRegion> font_map){
        for (int i=0; i<font_string.length(); i++){
            if (font_map.containsKey(font_string.toUpperCase().toCharArray()[i])) batch.draw(font_map.get(font_string.toUpperCase().toCharArray()[i]), real_x+size*i, real_y, size, size);
            else batch.draw(font_map.get("А"), real_x+size*i, real_y, size, size);
        }
    }
}
