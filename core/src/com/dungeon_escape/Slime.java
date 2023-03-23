package com.dungeon_escape;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Slime {
    private boolean isAttacking, isAttacked, isMoving, isAlive;
    private int x, y, health, maxHealth, power;
    private float realX, realY, activatedTimer, speed, horizontalIndent, verticalIndent, size;
    private Animation slimeAnimation, attackingSlimeAnimation,  attackedSlimeAnimation;
    private Texture titleTextTable;
    private BitmapFont titleFont;
    private Charge charge;
    private Sound slimeAttackingSound, slimeAttackedSound;

    Slime(int x, int y, float size, float horizontalIndent, float verticalIndent,
          Texture slimeTextureRegion, int frameCount, float speed, Texture slimeCharge,
          Texture attackingSlimeTextureRegion, Texture attackedSlimeTextureRegion, Sound slimeAttackingSound,
          Sound slimeAttackedSound, Texture titleTextTable, BitmapFont slimeFont, int health, int maxHealth){
        this.speed = speed;
        this.x = x;
        this.y = y;
        realX = this.x*size+ horizontalIndent;
        realY = this.y*size+ verticalIndent;
        this.verticalIndent = verticalIndent;
        this.horizontalIndent = horizontalIndent;
        attackingSlimeAnimation = new Animation(new TextureRegion(attackingSlimeTextureRegion), frameCount, 0.5f);
        attackedSlimeAnimation = new Animation(new TextureRegion(attackedSlimeTextureRegion), frameCount, 0.5f);
        slimeAnimation = new Animation(new TextureRegion(slimeTextureRegion), frameCount, 0.5f);
        this.titleTextTable = titleTextTable;
        titleFont = slimeFont;
        this.slimeAttackingSound = slimeAttackingSound;
        this.slimeAttackedSound = slimeAttackedSound;
        isAttacking = false;
        isAttacked = false;
        activatedTimer = 0;
        charge = new Charge(x, y, size, horizontalIndent, verticalIndent, slimeCharge, 8, speed);
        this.size = size;
        isAlive = true;
        this.maxHealth = maxHealth;
        this.health = health;
        power = 15;
    }

    public void draw(SpriteBatch batch, float size, float dt) {
        if (isAlive) {
            batch.draw(titleTextTable, realX, realY + size - size / 4, size, size / 4);
            if (health>=100) titleFont.draw(batch, "  "+health+"/"+ maxHealth, realX + size / 10, realY + size - size / 15);
            else titleFont.draw(batch, "   "+health+"/"+ maxHealth, realX + size / 10, realY + size - size / 15);

            if (isAttacked) {
                batch.draw(attackedSlimeAnimation.getFrame(), realX, realY, size, size);
                if (activatedTimer > 0) {
                    activatedTimer -= dt;
                } else isAttacked = false;
            }
            if (isAttacking) {
                if (activatedTimer > 0 && charge.isActiv()) {
                    activatedTimer -= dt;
                    if (!isAttacked) batch.draw(attackingSlimeAnimation.getFrame(), realX, realY, size, size);
                }
                else{
                    isAttacking = false;
                }
            }

            if (!isAttacked && !isAttacking) {
                batch.draw(slimeAnimation.getFrame(), realX, realY, size, size);
            }

            if (isMoving) {
                if (realX < x * size + horizontalIndent) {
                    if (realX + speed * dt < x * size + horizontalIndent) {
                        realX += speed * dt;
                    } else
                        realX += speed * dt - (realX + speed * dt - (x * size + horizontalIndent));
                }
                if (realX > x * size + horizontalIndent) {
                    if (realX + speed * dt > x * size + horizontalIndent) {
                        realX -= speed * dt;
                    } else
                        realX -= speed * dt - (realX + speed * dt - (x * size + horizontalIndent));
                }
                if (realY < y * size + verticalIndent) {
                    if (realY + speed * dt < y * size + verticalIndent) {
                        realY += speed * dt;
                    } else {
                        realY += speed * dt - (realY + speed * dt - (y * size + verticalIndent));
                    }
                }
                if (realY > y * size + verticalIndent) {
                    if (realY + speed * dt > y * size + verticalIndent) {
                        realY -= speed * dt;
                    } else
                        realY -= speed * dt - (realY + speed * dt - (y * size + verticalIndent));
                }
            }
            if (realX == x * size + horizontalIndent && realY == y * size + verticalIndent) {
                isMoving = false;
            }
            slimeAnimation.update(dt);
            attackedSlimeAnimation.update(dt);
            attackingSlimeAnimation.update(dt);
        }
    }
    public void attacking(int x, int y){
        if (isAttacking == false) {
            slimeAttackingSound.play();
            isAttacking = true;
            activatedTimer = 1f;
            charge.setTarget(x, y, this.x, this.y);
        }
    }
    public void attacked(int damage){
        if (isAttacked == false) {
            slimeAttackedSound.play();
            isAttacked = true;
            activatedTimer = 1f;
            health-=damage;
        }
    }
    public void move(int x, int y){
        this.x += x;
        this.y += y;
        isMoving = true;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public int getHealth(){
        return health;
    }
    public int getMaxHealth(){
        return maxHealth;
    }
    public int getPower(){
        return power;
    }
    public void death(){
        x = 24;
        y = 24;
        realX = x*size+ horizontalIndent;
        realY = y*size+ verticalIndent;
        isAlive = false;
    }
    public boolean getMoving(){return isMoving;}
    public boolean getAttacking(){return isAttacking;}
    public boolean getAttacked(){return isAttacked;}
    public void blastDraw(SpriteBatch batch, float size, float dt){
        charge.draw(batch, size, dt);}
}
