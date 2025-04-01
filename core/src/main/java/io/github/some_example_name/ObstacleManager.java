package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class ObstacleManager {

    private static final float OBSTACLE_SPEED = 1.5f;
    private static final float OBSTACLE_SPAWN_TIME = 3f;

    private Texture chainTexture, postTexture, bladeTop, bladeBottom;

    private Array<Rectangle> topObstacles = new Array<>();
    private Array<Rectangle> bottomObstacles = new Array<>();
    private Array<Float> chainHeights = new Array<>();
    private Array<Float> postHeights = new Array<>();
    private float spawnTimer = 0;

    public ObstacleManager(Texture chain, Texture post, Texture top, Texture bottom) {
        this.chainTexture = chain;
        this.postTexture = post;
        this.bladeTop = top;
        this.bladeBottom = bottom;
    }

    public void update(float delta, float worldHeight) {
        spawnTimer += delta;

        for (Rectangle r : topObstacles) {
            r.x -= OBSTACLE_SPEED * delta;
        }

        for (Rectangle r : bottomObstacles) {
            r.x -= OBSTACLE_SPEED * delta;
        }

        if (spawnTimer > OBSTACLE_SPAWN_TIME) {
            spawnObstacle(worldHeight);
            spawnTimer = 0;
        }

        removeOffscreenObstacles();
    }

    private void spawnObstacle(float worldHeight) {
        float chainHeight = MathUtils.random(1.0f, 2.5f);
        float postHeight = MathUtils.random(0.8f, 2f);

        float topY = worldHeight - chainHeight;
        float bottomY = 0;

        topObstacles.add(new Rectangle(10, topY, 0.5f, 0.5f));
        bottomObstacles.add(new Rectangle(10, bottomY, 0.5f, 0.5f));
        chainHeights.add(chainHeight);
        postHeights.add(postHeight);
    }

    private void removeOffscreenObstacles() {
        for (int i = topObstacles.size - 1; i >= 0; i--) {
            if (topObstacles.get(i).x + 0.5f < 0) {
                topObstacles.removeIndex(i);
                bottomObstacles.removeIndex(i);
                chainHeights.removeIndex(i);
                postHeights.removeIndex(i);
            }
        }
    }

    public void draw(SpriteBatch batch) {
        for (int i = 0; i < topObstacles.size; i++) {
            Rectangle top = topObstacles.get(i);
            Rectangle bottom = bottomObstacles.get(i);
            float chainHeight = chainHeights.get(i);
            float postHeight = postHeights.get(i);

            batch.draw(chainTexture, top.x, top.y + 0.5f, 0.5f, chainHeight);
            batch.draw(bladeTop, top.x, top.y, 0.5f, 0.5f);

            batch.draw(postTexture, bottom.x, bottom.y, 0.5f, postHeight);
            batch.draw(bladeBottom, bottom.x, bottom.y + postHeight, 0.5f, 0.5f);
        }
    }
}
