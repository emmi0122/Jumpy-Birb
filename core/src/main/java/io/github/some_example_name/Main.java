package io.github.some_example_name;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.math.Rectangle;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main implements ApplicationListener {

    public enum GameState {MENU, PLAYING}

    private GameState gameState = GameState.MENU;

    Texture backgroundTexture;
    Texture catTexture;
    Texture menuTexture;

    //platform
    Texture platform;

    //obstacles
    Texture chainTexture;
    Texture postTexture;
    Texture bladeTop;
    Texture bladeBottom;

    SpriteBatch spriteBatch;
    FitViewport viewport;

    Sprite catSprite;
    Sprite platformSprite;

    float gravity = -10f;
    float jumpSpeed = 5f;
    float verticalVelocity = 0f;
    float groundLevel = 0f;

    //Obstacle system
    private static final float OBSTACLE_SPEED = 200f;
    private static final float OBSTACLE_SPAWN_TIME = 4.5f;
    private static final float GAP_HEIGHT = 2f;
    private Array<Rectangle> topObstacles;
    private Array<Rectangle> bottomObstacles;
    private Array<Float> chainHeights;
    private Array<Float> postHeights;
    private float spawnTimer = 0;

    @Override
    public void create() {

        /*bild för menybakgrund
        menuTexture = new Texture("menybild");
        */

        backgroundTexture = new Texture("Spooky-forest.png");

        //platform texture
        platform = new Texture("plat.png");

        //character texture
        catTexture = new Texture("Cat.png");

        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(10, 6);

        //platform sprite and size
        platformSprite = new Sprite(platform);
        platformSprite.setSize(2, 1);

        //character sprite and size
        catSprite = new Sprite(catTexture);
        catSprite.setSize(1, 1);

        //obstacle textures
        chainTexture = new Texture("long_chain.png");
        postTexture = new Texture("large_post.png");
        bladeTop = new Texture("blade_top.png");
        bladeBottom = new Texture("blade_bottom.png");

        //initialize obstacle storage
        topObstacles = new Array<>();
        bottomObstacles = new Array<>();
        chainHeights = new Array<>();
        postHeights = new Array<>();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render() {
        /*
        if (gameState == GameState.MENU) {
            //render vad som syns i menyn
            spriteBatch.draw(menuTexture, 2, 4, viewport.getWorldWidth(), viewport.getWorldHeight()); // <-- NY KOD
            handleMenuInput();
        }
        else if (gameState == GameState.PLAYING) {
            //render vad som syns i spelet
            input();
            logic();
            updateObstacles();
            draw();
        }
        */
        input();
        logic();
        updateObstacles();
        draw();
    }


    public void handleMenuInput() {
        //trycker spelaren Space i menyn startas spelet
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            gameState = GameState.PLAYING;
        }
    }

    private void input() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && catSprite.getY() >= groundLevel) {
            verticalVelocity = jumpSpeed;
        }
    }

    private void logic() {
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        float catWidth = catSprite.getWidth();
        float catHeight = catSprite.getHeight();

        float platformWidth = platformSprite.getWidth();
        float platformHeight = platformSprite.getHeight();

        platformSprite.setY(2);
        platformSprite.setX(2);

        catSprite.setY(MathUtils.clamp(catSprite.getY(), 0, worldHeight - catWidth));

        float delta = Gdx.graphics.getDeltaTime();

        verticalVelocity += delta * gravity;

        catSprite.setY(catSprite.getY() + verticalVelocity * delta);

        //Jumping mechanism
        if (catSprite.getY() <= groundLevel) {
            catSprite.setY(groundLevel);
            verticalVelocity = 0;
        }

    }

    private void updateObstacles() {
        float delta = Gdx.graphics.getDeltaTime();
        spawnTimer += delta;

        if (spawnTimer > OBSTACLE_SPAWN_TIME) {

            float worldHeight = viewport.getWorldHeight();

            float chainHeight = MathUtils.random(1.0f, 1.5f);
            float postHeight = MathUtils.random(0.8f, 1.2f);

            float bottomY = 0;
            float topY = worldHeight - chainHeight;

            bottomObstacles.add(new Rectangle(10, bottomY, 0.5f, 0.5f));
            topObstacles.add(new Rectangle(10, topY, 0.5f, 0.5f));
            chainHeights.add(chainHeight);
            postHeights.add(postHeight);

            spawnTimer = 0;

            for (Rectangle obstacle : topObstacles) {
                obstacle.x -= OBSTACLE_SPEED * delta;
            }

            for (Rectangle obstacle : bottomObstacles) {
                obstacle.x -= OBSTACLE_SPEED * delta;
            }

            for (int i = topObstacles.size - 1; i >= 0; i--) {
                if (topObstacles.get(i).x + 0.5f < 0) {
                    topObstacles.removeIndex(i);
                    bottomObstacles.removeIndex(i);
                    chainHeights.removeIndex(i);
                    postHeights.removeIndex(i);
                }
            }

            for (int i = bottomObstacles.size - 1; i >= 0; i--) {
                if (bottomObstacles.get(i).x + 0.5f < 0) {
                    bottomObstacles.removeIndex(i);
                }
            }
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
        platformSprite.draw(spriteBatch);
        catSprite.draw(spriteBatch);

        for (int i = 0; i < topObstacles.size; i++) {
            Rectangle top = topObstacles.get(i);
            Rectangle bottom = bottomObstacles.get(i);

            float chainHeight = chainHeights.get(i);
            float postHeight = postHeights.get(i);

            spriteBatch.draw(chainTexture, top.x, top.y + 0.5f, 0.5f, chainHeight);
            spriteBatch.draw(bladeTop, top.x, top.y, 0.5f, 0.5f);

            spriteBatch.draw(postTexture, bottom.x, bottom.y, 0.5f, postHeight);
            spriteBatch.draw(bladeBottom, bottom.x, bottom.y + postHeight, 0.5f, 0.5f);
        }


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
        spriteBatch.dispose();
        backgroundTexture.dispose();
        catTexture.dispose();
        chainTexture.dispose();
        bladeBottom.dispose();
        postTexture.dispose();
        bladeTop.dispose();

        // lägg till dispose för menuTexture
    }
}
