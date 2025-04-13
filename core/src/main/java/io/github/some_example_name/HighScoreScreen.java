package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class HighScoreScreen implements Screen {
    private Main game;
    private SpriteBatch spriteBatch;
    private FitViewport viewport;
    private BitmapFont font;
    private Texture background;

    private int currentScore;
    private int highScore;

    public HighScoreScreen(Main game, int currentScore, int highScore) {
        this.game = game;
        this.currentScore = currentScore;
        this.highScore = highScore;
    }

    @Override
    public void show() {
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(1000, 600);
        font = new BitmapFont();
        font.getData().setScale(3);
        font.setColor(Color.WHITE);
        background = new Texture("Spooky-forest.png");
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());

        font.draw(spriteBatch, "Game Over", 350, 500);
        font.draw(spriteBatch, "Score: " + currentScore, 350, 400);
        font.draw(spriteBatch, "High Score: " + highScore, 350, 330);
        font.draw(spriteBatch, "Try again: Press SPACE or CLICK", 250, 200);

        spriteBatch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            game.startGame();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        spriteBatch.dispose();
        font.dispose();
        background.dispose();
    }

}
