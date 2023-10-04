package PoolGame.ObserverPattern;
import PoolGame.Game;

/** The Observer interface specifying the update method */
public interface Observer {
    /**
     * Updates game score function.
     * @param game The game instance.
     */
    public void update(Game game);
}