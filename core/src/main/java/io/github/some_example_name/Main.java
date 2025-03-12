package io.github.some_example_name;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.math.Rectangle;


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main implements ApplicationListener {
    Texture backgroundTexture;
    Texture catTexture;
    Texture pillarTexture;

    SpriteBatch spriteBatch;
    FitViewport viewport;

    Sprite catSprite;

    float gravity = -10f;
    float jumpSpeed = 5f;
    float verticalVelocity = 0f;
    float groundLevel = 0f;

    @Override
    public void create() {
        backgroundTexture = new Texture("Spooky-forest.png");
        catTexture = new Texture("Cat.png");

        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(8, 5);

        catSprite = new Sprite(catTexture);
        catSprite.setSize(1, 1);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render() {
        input();
        logic();
        draw();
    }

    private void input() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && catSprite.getY() <= groundLevel) {
            verticalVelocity = jumpSpeed;
        }
    }

    private void logic() {
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        float catWidth = catSprite.getWidth();
        float catHeight = catSprite.getHeight();

        catSprite.setX(MathUtils.clamp(catSprite.getX(), 0, worldWidth - catWidth));

        float delta = Gdx.graphics.getDeltaTime();

        verticalVelocity += delta * gravity;

        catSprite.setY(catSprite.getY() + verticalVelocity * delta);

        if (catSprite.getY() <= groundLevel) {
            catSprite.setY(groundLevel);
            verticalVelocity = 0;
        }
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();

        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        spriteBatch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight);
        catSprite.draw(spriteBatch);

        spriteBatch.end();
    }
    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void dispose() {
        // Destroy application's resources here.
    }
}
