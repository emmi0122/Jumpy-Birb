package io.github.some_example_name;

public class Score {
    private int currentScore = 0;
    private int highScore = 0;

    public void addScore(int score){
        currentScore += score;
        if(currentScore > highScore){
            highScore = currentScore;
        }
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public int getHighScore() {
        return highScore;
    }

    public void resetScore(){
        currentScore = 0;
    }
}
