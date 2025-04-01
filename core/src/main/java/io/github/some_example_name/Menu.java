package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Menu implements Screen {

    private Main game;
    private SpriteBatch spriteBatch;
    private FitViewport viewport;
    private Texture backgroundTexture;
    private Texture startTextTexture;

    public Menu(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(10, 6);
        backgroundTexture = new Texture("Spooky-forest.png");
        startTextTexture = new Texture("start-text.png");
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());

        spriteBatch.draw(startTextTexture, 2, 2, 6, 3);

        spriteBatch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            game.startGame();
        }
    }

    @Override public void resize(int width, int height) { viewport.update(width, height, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        spriteBatch.dispose();
        backgroundTexture.dispose();
        startTextTexture.dispose();
    }
}
