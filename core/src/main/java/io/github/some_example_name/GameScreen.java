package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameScreen implements Screen {

    private Main game;
    private SpriteBatch spriteBatch;
    private FitViewport viewport;

    private Texture background;
    private float backgroundX;
    private float backgroundSpeed;

    private Texture catTexture, platformTexture;
    private Sprite platformSprite;

    private Texture chainTexture, postTexture, bladeTop, bladeBottom;

    private Character cat;
    private ObstacleManager obstacleManager;

    private boolean gameStarted = false;

    public GameScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(10, 6);

        background = new Texture("Spooky-forest.png");
        catTexture = new Texture("Cat.png");
        platformTexture = new Texture("plat.png");
        chainTexture = new Texture("long_chain.png");
        postTexture = new Texture("large_post.png");
        bladeTop = new Texture("blade_top.png");
        bladeBottom = new Texture("blade_bottom.png");

        platformSprite = new Sprite(platformTexture);
        platformSprite.setSize(2, 1);
        platformSprite.setPosition(2, 2);

        backgroundX = 0;
        backgroundSpeed = 1.5f;

        cat = new Character(catTexture);
        obstacleManager = new ObstacleManager(chainTexture, postTexture, bladeTop, bladeBottom);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        if(!gameStarted && cat.hasJumpedOnce()) {
            gameStarted = true;
        }

        if(gameStarted) {
            platformSprite.setX(platformSprite.getX() - backgroundSpeed*Gdx.graphics.getDeltaTime());
        }

        if(platformSprite.getX() + platformSprite.getWidth() < 0) {
            platformSprite.setAlpha(0);
        }

        Rectangle platformBounds = new Rectangle(
            platformSprite.getX(),
            platformSprite.getY(),
            platformSprite.getWidth(),
            0.65f
        );

        cat.update(delta, viewport.getWorldHeight(), platformBounds);
        obstacleManager.update(delta, viewport.getWorldHeight());

        //Check to see if Cat collides
        if (obstacleManager.checkCollision(cat.getBounds())) {
            gameOver();
        }

        spriteBatch.begin();
        drawBackground();
        platformSprite.draw(spriteBatch);
        cat.draw(spriteBatch);
        obstacleManager.draw(spriteBatch);
        spriteBatch.end();
    }

    private void drawBackground() {
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        backgroundX -= backgroundSpeed * Gdx.graphics.getDeltaTime();
        if (backgroundX <= -worldWidth) {
            backgroundX = 0;
        }

        spriteBatch.draw(background, backgroundX, 0, worldWidth, worldHeight);
        spriteBatch.draw(background, backgroundX + worldWidth, 0, worldWidth, worldHeight);
    }

//Sends player back to menu when losing
    private void gameOver() {
        game.setScreen(new Menu(game));
    }

    @Override public void resize(int width, int height) { viewport.update(width, height, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        spriteBatch.dispose();
        background.dispose();
        catTexture.dispose();
        platformTexture.dispose();
        chainTexture.dispose();
        postTexture.dispose();
        bladeTop.dispose();
        bladeBottom.dispose();
    }
}
