package io.github.some_example_name;

import com.badlogic.gdx.Game;

public class Main extends Game {

    private Difficulty difficulty = Difficulty.NORMAL;

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    @Override
    public void create() {
        setScreen(new Menu(this));
    }

    public void startGame() {
        setScreen(new GameScreen(this, difficulty));
    }

}
