package io.github.some_example_name;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class Menu implements Screen {

    private Game game;
    private Stage stage;
    private Skin skin;

    public Menu(Game game) {

        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);


        //Bild från assets-filen
        skin = new Skin(Gdx.files.internal(""));

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        TextButton startButton = new TextButton("Start", skin);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //Skapa klass för spelskärmen?
                //Användaren trycker på startknapp -> startar spelet

                // - här anges isåfall kod för att byta till skärmklassen -
            }
        });
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

}
