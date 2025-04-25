package io.github.some_example_name;

import com.badlogic.gdx.math.MathUtils;

public enum Difficulty {
    EASY(-1000, 100f, 4.5f),
    NORMAL(-1000, 150f, 3f),
    HARD(-1000, 200f, 2f);

    public final float gravity;
    public final float obstacleSpeed;
    public final float spawnTime;

    Difficulty(float gravity, float obstacleSpeed, float spawnTime) {
        this.gravity = gravity;
        this.obstacleSpeed = obstacleSpeed;
        this.spawnTime = spawnTime;
    }

}
