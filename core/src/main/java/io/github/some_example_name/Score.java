package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Score {
    private int currentScore = 0;
    private int highScore = 0;
    private Preferences prefs;

    public Score() {
        prefs = Gdx.app.getPreferences("GamePref");
        highScore = prefs.getInteger("highscore", 0);
    }

    public void addScore(int score) {
        currentScore += score;
        if (currentScore > highScore) {
            highScore = currentScore;
            prefs.putInteger("highscore", highScore);
            prefs.flush();
        }
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public int getHighScore() {
        return highScore;
    }

    public void resetScore() {
        currentScore = 0;
    }
}
