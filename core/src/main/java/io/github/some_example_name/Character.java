package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
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

    public Character(Texture texture) {
        this.sprite = new Sprite(texture);
        this.sprite.setSize(80, 80);
        this.sprite.setPosition(270, 260);
    }

    public void update(float delta, float worldHeight, Rectangle platformBounds) {
        handleInput();
        applyGravity(delta);
        clampPosition(worldHeight);
        handlePlatformCollision(platformBounds);
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            verticalVelocity = jumpSpeed;
            hasJumped = true;
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

    public Rectangle getBounds() {
        return new Rectangle(sprite.getX(), sprite.getY(), 0.58f, 0.58f);
    }

    public void draw(com.badlogic.gdx.graphics.g2d.SpriteBatch batch) {
        sprite.draw(batch);
    }

    public Sprite getSprite() {
        return sprite;
    }
}
