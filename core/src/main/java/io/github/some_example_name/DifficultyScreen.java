package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class DifficultyScreen extends ScreenAdapter {

    private Main game;
    private SpriteBatch spriteBatch;
    private BitmapFont font;
    private FitViewport viewport;
    private Texture background;
    private Texture select;
    private Texture easy;
    private Texture medium;
    private Texture hard;
    private Music difficultyScreenMusic;

    private Rectangle easyButton, normalButton, hardButton;

    public DifficultyScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show(){
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(1000, 600);
        font = new BitmapFont();
        font.getData().setScale(3f);
        font.setColor(Color.WHITE);
        difficultyScreenMusic = Gdx.audio.newMusic(Gdx.files.internal("Darkest-Hour.mp3"));
        difficultyScreenMusic.setLooping(true);
        difficultyScreenMusic.setVolume(0.5f);
        difficultyScreenMusic.play();

        background = new Texture("Spooky-forest.png");
        select = new Texture("select-difficulty.png");
        easy = new Texture("easy.png");
        medium = new Texture("medium.png");
        hard = new Texture("hard.png");

        easyButton = new Rectangle(400, 300, 200, 80);
        normalButton = new Rectangle(400, 200, 200, 80);
        hardButton = new Rectangle(400, 100, 200, 80);
    }

    @Override
    public void render(float delta){
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        spriteBatch.begin();

        spriteBatch.draw(background, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        spriteBatch.draw(select, 300, 350, 350f, 250f);
        spriteBatch.draw(easy, easyButton.x, easyButton.y, 150f, 70f);
        spriteBatch.draw(medium, normalButton.x + 10f, normalButton.y, 150f, 70f);
        spriteBatch.draw(hard, hardButton.x + 20f, hardButton.y, 120f, 50f);

        spriteBatch.end();

        handleInput();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            float x = Gdx.input.getX() * viewport.getWorldWidth() / Gdx.graphics.getWidth();
            float y = viewport.getWorldHeight() -  Gdx.input.getY() * viewport.getWorldHeight() / Gdx.graphics.getHeight();

            if (easyButton.contains(x, y)) {
                difficultyScreenMusic.stop();
                game.setDifficulty(Difficulty.EASY);
                game.startGame();
            } else if (normalButton.contains(x, y)) {
                difficultyScreenMusic.stop();
                game.setDifficulty(Difficulty.NORMAL);
                game.startGame();
            }  else if (hardButton.contains(x, y)) {
                difficultyScreenMusic.stop();
                game.setDifficulty(Difficulty.HARD);
                game.startGame();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        font.dispose();
        background.dispose();
        select.dispose();
        if (difficultyScreenMusic != null) difficultyScreenMusic.dispose();
    }
}
