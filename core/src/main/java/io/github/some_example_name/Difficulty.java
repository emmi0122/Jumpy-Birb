package io.github.some_example_name;

import com.badlogic.gdx.math.MathUtils;

public enum Difficulty {
    EASY(-1000, 100f, 4.5f, MathUtils.random(100f, 190f), MathUtils.random(80f, 200f)),
    NORMAL(-1000, 150f, 3f, MathUtils.random(100f, 190f), MathUtils.random(80f, 200f)),
    HARD(-1000, 200f, 2f, MathUtils.random(100f, 205f), MathUtils.random(80f, 210f));

    public final float gravity;
    public final float obstacleSpeed;
    public final float spawnTime;
    public final float chainHeight;
    public final float postHeight;

    Difficulty(float gravity, float obstacleSpeed, float spawnTime, float chainHeight, float postHeight) {
        this.gravity = gravity;
        this.obstacleSpeed = obstacleSpeed;
        this.spawnTime = spawnTime;
        this.chainHeight = chainHeight;
        this.postHeight = postHeight;
    }

}
