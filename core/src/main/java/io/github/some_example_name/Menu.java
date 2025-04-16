package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Menu implements Screen {

    private Texture cat;
    private Texture platform;
    private Main game;
    private SpriteBatch spriteBatch;
    private FitViewport viewport;
    private Texture backgroundTexture;
    private Texture startTextTexture;
    private Music menuMusic;
    private float alpha = 0f;

    public Menu(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(1000, 600);
        backgroundTexture = new Texture("Spooky-forest.png");
        startTextTexture = new Texture("main-menu.png");
        cat = new Texture("Cat.png");
        platform = new Texture("plat.png");

        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("Darkest-Hour.mp3"));
        menuMusic.setLooping(true);
        menuMusic.setVolume(0.5f);
        menuMusic.play();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        alpha += Gdx.graphics.getDeltaTime() * 0.5f;

        if(alpha > 1f) alpha = 1f;
        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());

        spriteBatch.draw(platform, 200, 200, 200, 100);
        spriteBatch.draw(cat, 270, 264, 80, 80);
        spriteBatch.setColor(1, 1, 1, alpha);
        spriteBatch.draw(startTextTexture, 400, 100, 450, 400);


        spriteBatch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            menuMusic.stop();
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
        cat.dispose();
        if (menuMusic != null) menuMusic.dispose();
    }
}
