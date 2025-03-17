package io.github.some_example_name;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.math.Rectangle;


/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main implements ApplicationListener {
    Texture backgroundTexture;
    Texture catTexture;

    //platform
    Texture platformStart;
    Texture platformMiddle;
    Texture platformEnd;

    //obstacles
    Texture chainLong;
    Texture chainShort;
    Texture postSmall;
    Texture postLarge;
    Texture bladeTop;
    Texture bladeBottom;


    SpriteBatch spriteBatch;
    FitViewport viewport;

    Sprite catSprite;

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
    private float spawnTimer = 0;

    @Override
    public void create() {
        backgroundTexture = new Texture("Spooky-forest.png");
        catTexture = new Texture("Cat.png");

        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(8, 5);

        catSprite = new Sprite(catTexture);
        catSprite.setSize(1, 1);

        //platform
        platformStart = new Texture("platform-left.png");
        platformMiddle = new Texture("platform-middle.png");
        platformEnd = new Texture("platform-right.png");

        //obstacle textures
        chainLong = new Texture("long_chain.png");
        chainShort = new Texture("short_chain.png");
        postSmall = new Texture("small_post.png");
        postLarge = new Texture("large_post.png");
        bladeTop = new Texture("blade_top.png");
        bladeBottom = new Texture("blade_bottom.png");

        //initialize obstacle storage
        topObstacles = new Array<>();
        bottomObstacles = new Array<>();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render() {
        input();
        logic();
        updateObstacles();
        draw();
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

        catSprite.setX(MathUtils.clamp(catSprite.getX(), 0, worldWidth - catWidth));

        float delta = Gdx.graphics.getDeltaTime();

        verticalVelocity += delta * gravity;

        catSprite.setY(catSprite.getY() + verticalVelocity * delta);

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

            //randomize whether to use long or short chain
            boolean useLongChain = MathUtils.randomBoolean();
            Texture selectedChain = useLongChain ? chainLong : chainShort;
            Texture selectedPost = useLongChain ? postSmall : postLarge;

            float chainHeight = useLongChain ? 1.5f : 1.0f;
            float postHeight = useLongChain ? 0.8f : 1.2f;

            float bottomY = (worldHeight - chainHeight) - GAP_HEIGHT - postHeight;

            topObstacles.add(new Rectangle(8, worldHeight - chainHeight, 0.5f, 0.5f));
            bottomObstacles.add(new Rectangle(8, bottomY, 0.5f, 0.5f));

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
        catSprite.draw(spriteBatch);

        for (Rectangle obstacle : topObstacles) {
            boolean useLongChain = obstacle.y > 4.0f;
            Texture selectedChain = useLongChain ? chainLong : chainShort;
            Texture selectedPost = useLongChain ? postSmall : postLarge;

            float chainHeight = useLongChain ? 1.5f : 1.0f;
            float postHeight = useLongChain ? 0.8f : 1.2f;

            spriteBatch.draw(selectedChain, obstacle.x, obstacle.y + 0.5f, 0.5f, chainHeight);
            spriteBatch.draw(bladeTop, obstacle.x, obstacle.y, 0.5f, 0.5f);

            float bottomY = (obstacle.y - chainHeight) - GAP_HEIGHT - postHeight;
            spriteBatch.draw(selectedPost, obstacle.x, bottomY, 0.5f, postHeight);
            spriteBatch.draw(bladeBottom, obstacle.x, bottomY + postHeight, 0.5f, 0.5f);
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
        chainLong.dispose();
        chainShort.dispose();
        bladeBottom.dispose();
        postLarge.dispose();
        postSmall.dispose();
        bladeTop.dispose();
    }
}
