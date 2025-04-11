package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class ObstacleManager {

    private static final float OBSTACLE_SPEED = 150f;
    private static final float OBSTACLE_SPAWN_TIME = 3f;

    private Texture chainTexture, postTexture, bladeTop, bladeBottom;

    private Array<Rectangle> topObstacles = new Array<>();
    private Array<Rectangle> bottomObstacles = new Array<>();
    private Array<Float> chainHeights = new Array<>();
    private Array<Float> postHeights = new Array<>();
    private Array<Boolean> scoredObstacles = new Array<>();
    private float spawnTimer = 0;

    public ObstacleManager(Texture chain, Texture post, Texture top, Texture bottom) {
        this.chainTexture = chain;
        this.postTexture = post;
        this.bladeTop = top;
        this.bladeBottom = bottom;
    }

    public void update(float delta, float worldHeight, boolean gameStarted) {
        if(!gameStarted) return;
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

    //method for random spawning of obstacles
    private void spawnObstacle(float worldHeight) {
        float chainHeight = MathUtils.random(100f, 250f);
        float postHeight = MathUtils.random(80f, 200f);

        float topY = worldHeight - chainHeight;
        float bottomY = 0;

        scoredObstacles.add(false);

        topObstacles.add(new Rectangle(1000, topY, 100f, chainHeight));
        bottomObstacles.add(new Rectangle(1000, bottomY, 100f, postHeight));
        chainHeights.add(chainHeight);
        postHeights.add(postHeight);
    }

    //method for removing obstacles that passed the left edge of the screen
    private void removeOffscreenObstacles() {
        for (int i = topObstacles.size - 1; i >= 0; i--) {
            if (topObstacles.get(i).x + 0.5f < 0) {
                topObstacles.removeIndex(i);
                bottomObstacles.removeIndex(i);
                chainHeights.removeIndex(i);
                postHeights.removeIndex(i);
                scoredObstacles.removeIndex(i);
            }
        }
    }

    public void draw(SpriteBatch batch) {
        for (int i = 0; i < topObstacles.size; i++) {
            Rectangle top = topObstacles.get(i);
            Rectangle bottom = bottomObstacles.get(i);
            float chainHeight = chainHeights.get(i);
            float postHeight = postHeights.get(i);

            batch.draw(chainTexture, top.x, top.y + 50f, 50f, chainHeight);
            batch.draw(bladeTop, top.x, top.y, 50f, 50f);

            batch.draw(postTexture, bottom.x, bottom.y, 50f, postHeight);
            batch.draw(bladeBottom, bottom.x, bottom.y + postHeight, 50f, 50f);
        }


    }

    //Collisionmethod
    public boolean checkCollision(Rectangle catBounds) {
        for (Rectangle topObstacle : topObstacles) {
            if (catBounds.overlaps(topObstacle)) {
                return true;
            }
        }

        for (Rectangle bottomObstacle : bottomObstacles) {
            if (catBounds.overlaps(bottomObstacle)) {
                return true;
            }
        }

        return false;
    }

    public float getNextUnscoredObstacleX() {
        for (int i = 0; i < topObstacles.size; i++) {
            if (!scoredObstacles.get(i)) {
                return topObstacles.get(i).x;
            }
        }
        return -1;
    }

    public void markObstaclesAsScored(float x){
        for(int i = 0; i < topObstacles.size; i++){
            if (!scoredObstacles.get(i) && topObstacles.get(i).x == x) {
                scoredObstacles.set(i, true);
                break;
            }
        }
    }

    public float getObstacleBounds() {
        return topObstacles.first().getX();
    }
}
