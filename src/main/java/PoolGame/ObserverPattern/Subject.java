package PoolGame.ObserverPattern;
import PoolGame.Game;

/** Ball implements Subject */
public interface Subject {

    /**
     * Attach the BallObserver to the ball when it is being added.
     * @param observer The ball observer that implements Observer.
     */
    public void attach(Observer observer);

    /**
     * This method is called when a ball falls into a pocket.
     * This method updates the game score when a non-white ball falls into pocket.
     * @param game The game instance. 
     */
    public void alert(Game game);

    /**
     * Retrieve the colour of the ball. 
     * @return String The colour of the ball.
     */
    public String getColour();
}

