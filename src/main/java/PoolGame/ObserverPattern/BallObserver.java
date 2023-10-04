package PoolGame.ObserverPattern;
import PoolGame.ObserverPattern.Subject;
import PoolGame.Game;
/** Concrete observer implements Observer interface */
public class BallObserver implements Observer {
    
    private Subject subject; // concrete subject 
    private String colour; 

    /**
     * Constructor only needs subject reference and the colour of the ball. 
     * @param subject The subject instance.
     */
    public BallObserver(Subject subject) {
        this.subject = subject; // the ball that implements Subject
        this.colour = subject.getColour(); // get colour function 
    }

    /**
     * Updates the game's current score. 
     * This method is called when a non-white ball falls into pocket.
     * @param game The game instance.
     */
    public void update(Game game) {
        
        if (this.colour.toString().equals("red")) {
            // 1
            int curScore = game.getScore();
            int newScore = curScore + 1;
            game.setScore(newScore);

        } else if (this.colour.toString().equals("yellow")) {
            // 2
            int curScore = game.getScore();
            int newScore = curScore + 2;
            game.setScore(newScore);

        } else if (this.colour.toString().equals("green")) {
            // 3
            int curScore = game.getScore();
            int newScore = curScore + 3;
            game.setScore(newScore);

        } else if (this.colour.toString().equals("brown")) {
            // 4
            int curScore = game.getScore();
            int newScore = curScore + 4;
            game.setScore(newScore);

        } else if (this.colour.toString().equals("blue")) {
            // 5
            int curScore = game.getScore();
            int newScore = curScore + 5;
            game.setScore(newScore);

        } else if (this.colour.toString().equals("purple")) {
            // 6
            int curScore = game.getScore();
            int newScore = curScore + 6;
            game.setScore(newScore);

        } else if (this.colour.toString().equals("black")) {
            // 7
            int curScore = game.getScore();
            int newScore = curScore + 7;
            game.setScore(newScore);

        } else if (this.colour.toString().equals("orange")) {
            // 8
            int curScore = game.getScore();
            int newScore = curScore + 8;
            game.setScore(newScore);
        }  
    }




}