package io.github.some_example_name;

import com.badlogic.gdx.Game;

public class Main extends Game {

    @Override
    public void create() {
        setScreen(new Menu(this));
    }

    public void startGame() {
        setScreen(new GameScreen(this));
    }

}
