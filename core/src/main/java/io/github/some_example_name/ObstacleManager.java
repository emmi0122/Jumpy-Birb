package io.github.some_example_name;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class ObstacleManager {

    private Difficulty difficulty;

    private float obstacleSpeed;
    private float obstacleSpawnTime;

    private Texture chainTexture, postTexture, bladeTop, bladeBottom;

    private Array<Rectangle> topObstacles = new Array<>();
    private Array<Rectangle> bottomObstacles = new Array<>();
    private Array<Float> chainHeights = new Array<>();
    private Array<Float> postHeights = new Array<>();
    private Array<Boolean> scoredObstacles = new Array<>();
    private float spawnTimer = 0;

    private Array<Float> originalTopY = new Array<>();
    private Array<Float> originalBottomHeight = new Array<>();
    private Array<Float> oscillationOffset = new Array<>();
    private Array<Boolean> movingUp = new Array<>();

    public ObstacleManager(Texture chain, Texture post, Texture top, Texture bottom,
                           float obstacleSpeed, float obstacleSpawnTime, Difficulty difficulty) {
        this.chainTexture = chain;
        this.postTexture = post;
        this.bladeTop = top;
        this.bladeBottom = bottom;
        this.obstacleSpeed = obstacleSpeed;
        this.obstacleSpawnTime = obstacleSpawnTime;
        this.difficulty = difficulty;
    }

    public void update(float delta, float worldHeight, boolean gameStarted) {
        if (!gameStarted) return;
        spawnTimer += delta;
        Boolean rising = true;

        for (Rectangle r : topObstacles) {
            r.x -= obstacleSpeed * delta;
        }

        for (Rectangle r : bottomObstacles) {
            r.x -= obstacleSpeed * delta;
        }

        for (int i = 0; i < topObstacles.size; i++) {
            Rectangle top = topObstacles.get(i);
            Rectangle bottom = bottomObstacles.get(i);

            float offset = oscillationOffset.get(i);
            boolean up = movingUp.get(i);
            float moveAmount = 20f * delta;

            if (up) {
                offset += moveAmount;
                if (offset >= 10f) {
                    offset = 10f;
                    movingUp.set(i, false);
                }
            } else {
                offset -= moveAmount;
                if (offset <= -10f) {
                    offset = -10f;
                    movingUp.set(i, true);
                }
            }

            // Uppdatera offset
            oscillationOffset.set(i, offset);

            float newTopY = originalTopY.get(i) + offset;
            top.y = newTopY;

            float originalHeight = originalBottomHeight.get(i);
            float newBottomY = offset;
            bottom.setPosition(bottom.x, newBottomY);
            bottom.setSize(bottom.width, originalHeight + 40f);
        }


        if (spawnTimer > obstacleSpawnTime) {
            spawnObstacle(worldHeight);
            spawnTimer = 0;
        }

        removeOffscreenObstacles();
    }

    //method for random spawning of obstacles
    private void spawnObstacle(float worldHeight) {
        float chainHeight, postHeight;
        switch (difficulty) {
            case EASY:
                chainHeight = MathUtils.random(100f, 160f);
                postHeight = MathUtils.random(80f, 140f);
                break;
            case NORMAL:
                chainHeight = MathUtils.random(120f, 190f);
                postHeight = MathUtils.random(90f, 180f);
                break;
            case HARD:
                chainHeight = MathUtils.random(150f, 220f);
                postHeight = MathUtils.random(100f, 200f);
                break;
            default:
                chainHeight = MathUtils.random(100f, 160f);
                postHeight = MathUtils.random(80f, 140f);
        }

        System.out.println(chainHeight);
        float visualHeight = chainHeight - 10f;
        float topY = worldHeight - visualHeight;
        float bottomY = 0;

        scoredObstacles.add(false);

        topObstacles.add(new Rectangle(1000, topY, 40f, visualHeight));
        bottomObstacles.add(new Rectangle(1000, bottomY, 40f, postHeight + 40f));
        chainHeights.add(visualHeight);
        postHeights.add(postHeight);

        originalTopY.add(worldHeight - chainHeight + 10f);
        originalBottomHeight.add(postHeight);
        oscillationOffset.add(0f);
        movingUp.add(true);
    }

    //method for removing obstacles that passed the left edge of the screen
    private void removeOffscreenObstacles() {
        for (int i = topObstacles.size - 1; i >= 0; i--) {
            if (topObstacles.get(i).x + 100f < 0) {
                topObstacles.removeIndex(i);
                bottomObstacles.removeIndex(i);
                chainHeights.removeIndex(i);
                postHeights.removeIndex(i);
                scoredObstacles.removeIndex(i);
                originalTopY.removeIndex(i);
                originalBottomHeight.removeIndex(i);
                oscillationOffset.removeIndex(i);
                movingUp.removeIndex(i);
            }
        }
    }

    public void draw(SpriteBatch batch) {
        for (int i = 0; i < topObstacles.size; i++) {
            Rectangle top = topObstacles.get(i);
            Rectangle bottom = bottomObstacles.get(i);
            float chainHeight = chainHeights.get(i);
            float postHeight = postHeights.get(i);


            batch.draw(chainTexture, top.x, top.y, 50f, chainHeight + 50f);
            batch.draw(bladeTop, top.x, top.y - 10f, 50f, 50f);

            batch.draw(postTexture, bottom.x, bottom.y - 50f, 50f, postHeight + 50f);
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

    public void markObstaclesAsScored(float x) {
        for (int i = 0; i < topObstacles.size; i++) {
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
