package PoolGame.LevelStrategy;
import PoolGame.Game;

/** The difficulty level main function is to reset the level */
public interface Level {
    /**
     * Function to set the level 
     * @param game The instance of the game
     */
    public void setLevelStrategy(Game game);
}