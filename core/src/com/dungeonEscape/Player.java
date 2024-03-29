package com.dungeonEscape;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player {
    private boolean isAttacking, isAttacked, isMoving, isRight;
    private int x, y, health, maxHealth, power;
    private float realX, realY, activatedTimer, speed, horizontalOtstup, verticalOtstup;
    private Animation playerAnimationRight, playerAnimationLeft, playerMowingRight, playerMowingLeft;
    private Texture attackingPlayerRight, attackedPlayerRight, attackingPlayerLeft, attackedPlayerLeft;
    private Charge charge;
    private String name;

    Sound playerAttackingSound, playerAttackedSound;
    public Player(int x, int y, float size, float horizontalOtstup, float verticalOtstup,
           Texture playerTextureRegionRight, Texture playerTextureRegionLeft,
           int frameCount,
           Texture playerTextureRegionMowingRight, Texture playerTextureRegionMowingLeft,
           int MovingframeCount,
           float speed, Texture player_blast,
           Texture attackingPlayerRight, Texture attackedPlayerRight,
           Texture attackingPlayerLeft, Texture attackedPlayerLeft,
           Sound playerAttackingSound, Sound playerAttackedSound, String name, int maxHealth, int health){
        this.speed = speed;
        this.x = x;
        this.y = y;
        realX = this.x*size+ horizontalOtstup;
        realY = this.y*size+ verticalOtstup;
        this.verticalOtstup = verticalOtstup;
        this.horizontalOtstup = horizontalOtstup;
        this.attackingPlayerRight = attackingPlayerRight;
        this.attackedPlayerRight = attackedPlayerRight;
        this.attackingPlayerLeft = attackingPlayerLeft;
        this.attackedPlayerLeft = attackedPlayerLeft;
        playerAnimationRight = new Animation(new TextureRegion(playerTextureRegionRight), frameCount, 0.5f);
        playerAnimationLeft = new Animation(new TextureRegion(playerTextureRegionLeft), frameCount, 0.5f);
        playerMowingRight = new Animation(new TextureRegion(playerTextureRegionMowingRight), MovingframeCount, 0.3f);
        playerMowingLeft = new Animation(new TextureRegion(playerTextureRegionMowingLeft), MovingframeCount, 0.3f);
        this.playerAttackingSound = playerAttackingSound;
        this.playerAttackedSound = playerAttackedSound;
        isAttacking = false;
        isAttacked = false;
        activatedTimer = 0;
        charge = new Charge(x, y, size, horizontalOtstup, verticalOtstup, player_blast, 8, speed);
        this.maxHealth = maxHealth;
        this.health = health;
        power = 20;
        this.name = name;
        isRight = true;
    }

    public void draw(SpriteBatch batch, float size, float dt){
        if (!isMoving) {
            if (isAttacked) {
                if (isRight) batch.draw(attackedPlayerRight, realX, realY, size, size);
                else batch.draw(attackedPlayerLeft, realX, realY, size, size);
                if (activatedTimer > 0) {
                    activatedTimer -= dt;
                } else isAttacked = false;
            }
            if (isAttacking) {
                if (activatedTimer > 0 && charge.isActiv()) {
                    activatedTimer -= dt;
                    if (!isAttacked) {
                        if (isRight) batch.draw(attackingPlayerRight, realX, realY, size, size);
                        else batch.draw(attackingPlayerLeft, realX, realY, size, size);
                    }
                }
                else{
                    isAttacking = false;
                }
            }
            if (!isAttacked && !isAttacking) {
                if (isRight) batch.draw(playerAnimationRight.getFrame(), realX, realY, size, size);
                else batch.draw(playerAnimationLeft.getFrame(), realX, realY, size, size);
            }
        }
        else {
            if (isRight) {
                batch.draw(playerMowingRight.getFrame(), realX, realY, size, size);
                playerMowingRight.update(dt);
            }
            else {
                batch.draw(playerMowingLeft.getFrame(), realX, realY, size, size);
                playerMowingLeft.update(dt);
            }
            if (realX < x * size + horizontalOtstup){
                isRight = true;
                if (realX + speed * dt < x * size + horizontalOtstup) {
                    realX += speed * dt;
                }
                else realX = x * size + horizontalOtstup;
            }
            if (realX > x * size + horizontalOtstup){
                isRight = false;
                if (realX -speed * dt > x * size + horizontalOtstup) {
                    realX -= speed*dt;
                }
                else realX = x * size + horizontalOtstup;
            }
            if (realY < y * size + verticalOtstup){
                if (realY + speed * dt < y * size + verticalOtstup) {
                    realY += speed * dt;
                }
                else realY = y * size+ verticalOtstup;
            }
            if (realY > y * size+ verticalOtstup){
                if (realY -speed * dt > y * size + verticalOtstup) {
                    realY -= speed * dt;
                }
                else realY = y * size + verticalOtstup;
            }
        }
        if (realX == x * size + horizontalOtstup && realY == y * size + verticalOtstup) {
            isMoving = false;
        }
        playerAnimationRight.update(dt);
        playerAnimationLeft.update(dt);
        charge.draw(batch, size, dt);
    }

    public void attacking(int x, int y){
        playerAttackingSound.play();
        isAttacking = true;
        activatedTimer = 1f;
        charge.setTarget(x, y, this.x, this.y);
    }
    public void attacked(int damage){
        playerAttackedSound.play();
        isAttacked = true;
        activatedTimer = 1f;
        health-=damage;
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
    public float getRealX(){
        return realX;
    }
    public float getRealY(){
        return realY;
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
    public boolean getMoving(){return isMoving;}
    public boolean getAttacking(){return isAttacking;}
    public boolean getAttacked(){return isAttacked;}
    public String getName(){return name;}
    public void setHealth(int newHealth){health = newHealth;}
}
