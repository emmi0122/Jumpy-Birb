package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameScreen extends ScreenAdapter {
    private Main game;
    private SpriteBatch spriteBatch;
    private FitViewport viewport;

    private Music gameMusic;

    private Score score;
    private float lastObstacleX = -1;
    private Difficulty difficulty;

    private ParallaxBackground  parallaxBackground;
    private Texture background;
    private float backgroundX;
    private float backgroundSpeed;

    private Texture catTexture, platformTexture;
    private Sprite platformSprite;

    private Texture chainTexture, postTexture, bladeTop, bladeBottom;

    private Character cat;
    private Sound catCollisionSound;
    private ObstacleManager obstacleManager;

    private BitmapFont font;
    private int points = 0;

    private boolean gameStarted = false;

    public GameScreen(Main game, Difficulty difficulty) {
        this.game = game;
        this.difficulty= difficulty;
    }

    @Override
    public void show() {
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(1000, 600);

        background = new Texture("Spooky-forest.png");
        catTexture = new Texture("Cat.png");
        platformTexture = new Texture("plat.png");
        chainTexture = new Texture("long_chain.png");
        postTexture = new Texture("large_post.png");
        bladeTop = new Texture("blade_top.png");
        bladeBottom = new Texture("blade_bottom.png");

        Texture bg1 = new Texture("spooky-forest1.png");
        Texture bg2 = new Texture("spooky-forest2.png");
        Texture bg3 = new Texture("spooky-forest3.png");

        backgroundSpeed = difficulty.obstacleSpeed * 0.9f;
        parallaxBackground = new ParallaxBackground(
            viewport.getWorldWidth(),
            backgroundSpeed,
            new TextureRegion(bg1),
            new TextureRegion(bg2),
            new TextureRegion(bg3));

        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("Night-of-the-Streets.mp3"));
        gameMusic.setLooping(true);
        gameMusic.setVolume(0.8f);
        gameMusic.play();

        catCollisionSound = Gdx.audio.newSound(Gdx.files.internal("Meow.mp3"));

        platformSprite = new Sprite(platformTexture);
        platformSprite.setSize(200, 100);
        platformSprite.setPosition(200, 200);

        backgroundX = 0;

        cat = new Character(catTexture, difficulty.gravity);
        obstacleManager = new ObstacleManager(chainTexture, postTexture, bladeTop, bladeBottom, difficulty.obstacleSpeed, difficulty.spawnTime, difficulty);

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.setUseIntegerPositions(false);
        font.getData().setScale(2);

        score = new Score();
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
            100,
            64
        );

        cat.update(delta, viewport.getWorldHeight(), platformBounds);
        obstacleManager.update(delta, viewport.getWorldHeight(), gameStarted);

        float nextObstacleX = obstacleManager.getNextUnscoredObstacleX();

        if(gameStarted && nextObstacleX != -1 && cat.getBounds().x > nextObstacleX) {
            score.addScore(1);
            obstacleManager.markObstaclesAsScored(nextObstacleX);
        }

        //Check to see if Cat collides
        if (obstacleManager.checkCollision(cat.getBounds())) {
            catCollisionSound.play(0.5f);
            gameOver();
        }

        if (cat.isOnGround()) {
            catCollisionSound.play(0.5f);
            gameOver();
        }

        spriteBatch.begin();
        drawBackground(delta);
        platformSprite.draw(spriteBatch);
        cat.draw(spriteBatch);

        if(gameStarted) {
            obstacleManager.draw(spriteBatch);
        }

        font.draw(spriteBatch, "Score: " + score.getCurrentScore(), 10, 590);

        spriteBatch.end();
    }

    private void addPoints(float x, float y) {
        if(cat.getBounds().x == obstacleManager.getObstacleBounds()) {
            points++;
        }
    }

    private void drawBackground(float delta) {
        if (gameStarted) {
            parallaxBackground.update(delta);
        }
        parallaxBackground.render(spriteBatch);
    }

    //Sends player back to menu when losing
    private void gameOver() {
        if (gameMusic != null) gameMusic.stop();
        game.setScreen(new HighScoreScreen(game, score.getCurrentScore(), score.getHighScore()));
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

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
        gameMusic.dispose();
        catCollisionSound.dispose();
    }
}
