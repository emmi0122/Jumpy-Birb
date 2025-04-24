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

        easyButton = new Rectangle(350, 340, 300, 80);
        normalButton = new Rectangle(350, 230, 300, 80);
        hardButton = new Rectangle(350, 120, 300, 80);
    }

    @Override
    public void render(float delta){
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        spriteBatch.begin();

        spriteBatch.draw(background, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        font.draw(spriteBatch, "Select difficulty", 340, 520);
        font.draw(spriteBatch, "Easy", easyButton.x +60, easyButton.y +60);
        font.draw(spriteBatch, "Normal", normalButton.x +60, normalButton.y +60);
        font.draw(spriteBatch, "Hard", hardButton.x +60, hardButton.y +60);

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
        if (difficultyScreenMusic != null) difficultyScreenMusic.dispose();
    }
}
