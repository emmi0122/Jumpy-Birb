package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Character {

    private Sprite sprite;
    private float gravity = -1000;
    private float jumpSpeed = 500;
    private float verticalVelocity = 0f;
    private float groundLevel = 0f;
    private boolean hasJumped = false;
    private boolean isOnSolidSurface = false;
    private Sound jumpSound;

    private Texture catTexture;
    private Texture catJumpTexture;

    private float textureTimer = 0f;
    private final float JUMP_TEXTURE_DURATION = 0.3f;

    public Character(Texture texture, float gravity) {
        this.catTexture = new Texture("Cat.png");
        this.catJumpTexture = new Texture("katt2.png");
        this.sprite = new Sprite(catTexture);
        this.sprite.setSize(80, 80);
        this.sprite.setPosition(270, 260);
        this.gravity = gravity;

        jumpSound = Gdx.audio.newSound(Gdx.files.internal("jump-sound.mp3"));
    }

    public void update(float delta, float worldHeight, Rectangle platformBounds) {
        handleInput();
        applyGravity(delta);
        clampPosition(worldHeight);
        handlePlatformCollision(platformBounds);

        if (textureTimer > 0) {
            textureTimer -= delta;
            if (textureTimer <= 0) {
                sprite.setTexture(catTexture);
            }
        }
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            verticalVelocity = jumpSpeed;
            hasJumped = true;

            jumpSound.play(0.5f);

            sprite.setTexture(catJumpTexture);
            textureTimer = JUMP_TEXTURE_DURATION;
        }
    }

    public boolean hasJumpedOnce() {
        return hasJumped;
    }

    private void applyGravity(float delta) {
        verticalVelocity += gravity * delta;
        sprite.setY(sprite.getY() + verticalVelocity * delta);

        if (sprite.getY() <= groundLevel) {
            sprite.setY(groundLevel);
            verticalVelocity = 0;
        }
    }

    private void clampPosition(float worldHeight) {
        sprite.setY(MathUtils.clamp(sprite.getY(), 0, worldHeight - sprite.getHeight()));
    }

    private void handlePlatformCollision(Rectangle platformBounds) {
        Rectangle bounds = getBounds();
        isOnSolidSurface = false;
        if (bounds.overlaps(platformBounds)) {
            sprite.setY(platformBounds.y + platformBounds.height);
            verticalVelocity = 0;
            isOnSolidSurface = true;
        }
        else {
            isOnSolidSurface = false;
        }
    }

    public boolean isOnGround() {
        return sprite.getY() <= groundLevel && !isOnSolidSurface;
    }

    public Rectangle getBounds() {
        return new Rectangle(sprite.getX(), sprite.getY(), 60f, 60f);
    }

    public void draw(com.badlogic.gdx.graphics.g2d.SpriteBatch batch) {
        sprite.draw(batch);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void dispose() {
        catTexture.dispose();
        catJumpTexture.dispose();
        jumpSound.dispose();
    }
}
