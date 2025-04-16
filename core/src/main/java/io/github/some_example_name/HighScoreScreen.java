package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class HighScoreScreen extends ScreenAdapter {
    private Main game;
    private SpriteBatch spriteBatch;
    private FitViewport viewport;
    private BitmapFont font;
    private BitmapFont headerFont;
    private Texture background;
    private Texture header;
    private Texture bottomHeader;

    private float inputDelay = 2f;
    private float timeSinceShown = 0f;
    private float alpha = 0f;

    private int currentScore;
    private Score score;

    public HighScoreScreen(Main game, int currentScore, int highScore) {
        this.game = game;
        this.currentScore = currentScore;
        this.score = new Score();
    }

    @Override
    public void show() {
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(1000, 600);

        background = new Texture("Spooky-forest.png");
        header = new Texture("header3.png");
        bottomHeader = new Texture("header4.png");

        headerFont = new BitmapFont();
        headerFont.getData().setScale(4);
        headerFont.setColor(Color.FIREBRICK);

        font = new BitmapFont();
        font.getData().setScale(4);
        font.setColor(Color.WHITE);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        timeSinceShown += delta;

        spriteBatch.begin();


        spriteBatch.draw(background, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        spriteBatch.draw(header, 290, 240, 420, 370);

        //headerFont.draw(spriteBatch, "Game Over", 350, 500);
        font.draw(spriteBatch, "" + currentScore, 570, 425);
        font.draw(spriteBatch, "" + score.getHighScore(), 610, 330);
        //font.draw(spriteBatch, "Try again: Press SPACE or CLICK", 250, 200);

        if (timeSinceShown >= inputDelay) {
            alpha += Gdx.graphics.getDeltaTime() * 0.5f;
            if(alpha > 1f) alpha = 1f;
            spriteBatch.setColor(1f, 1f, 1f, alpha);
            spriteBatch.draw(bottomHeader, 200, 25, 580, 180);
            spriteBatch.setColor(1f, 1f, 1f, 1f);
            //font.draw(spriteBatch, "Try again: Press SPACE or CLICK", 250, 200);
        }

        spriteBatch.end();

        if (timeSinceShown >= inputDelay &&
            (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))) {
            game.setScreen(new GameScreen(game));
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
