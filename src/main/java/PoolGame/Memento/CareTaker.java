package PoolGame.Memento;
import PoolGame.Items.Ball;
import java.util.*;

/**CareTaker for PoolTableMemento */
public class CareTaker {

    private PoolTableMemento history = null;

    /**
     * Add to save memento.
     * @param memento The memento.
     */
    public void setMemento(PoolTableMemento memento) {
        this.history = memento;
    }

    /**
     * @return Returns the memento.
     */
    public PoolTableMemento getPrevMemento() { 
        return history;
    }
    
}
