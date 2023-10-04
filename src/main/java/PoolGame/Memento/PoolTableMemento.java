package PoolGame.Memento;
import java.util.*;
import PoolGame.Items.Ball;
import javafx.scene.text.Text;

/** Memento for undo functionality. */
public class PoolTableMemento {
    private int score;
    private int time;
    private List<Ball> state;

    /**
     * @param state The list of the balls. 
     * @param time The time when the undo function is called.
     * @param score The game score.
     */
    public PoolTableMemento(List<Ball> state, int time, int score) {
        this.state = state;
        this.time = time;
        this.score = score;
    }

    /**
     * @return Returns the state.
     */
    public List<Ball> getState() {
        return this.state;
    }

    /**
     * @return Returns the time.
     */
    public int getTime() {
        return this.time;
    }

    /**
     * @return Returns the score.
     */
    public int getScore() {
        return this.score;
    }

}