package PoolGame;

import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import java.util.HashSet;

import PoolGame.Builder.BallBuilderDirector;
import PoolGame.Config.BallConfig;
import PoolGame.Items.Ball;
import PoolGame.Items.Pocket;
import PoolGame.Items.PoolTable;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.scene.effect.DropShadow;
import javafx.scene.text.FontWeight;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import PoolGame.LevelStrategy.Level;
import PoolGame.LevelStrategy.EasyLevel;
import PoolGame.LevelStrategy.NormalLevel;
import PoolGame.LevelStrategy.HardLevel;

import PoolGame.ObserverPattern.BallObserver;
import PoolGame.Memento.CareTaker;
import PoolGame.Memento.PoolTableMemento;
import javafx.scene.shape.Rectangle;
import PoolGame.Config.PocketsConfig;

/** The game class that runs the game */
public class Game {
    private PoolTable table;
    private boolean shownWonText = false;
    private final Text winText = new Text(50, 50, "You Won!");
    
    private Text scoreText = new Text(60, 50, "Score: 0");
    private Scene scene;
    private int score = 0;

    private Text timerText = new Text(60, 20, "Time: 0:00");
    private int time = 0;

    private boolean stopTime = false;

    private CareTaker careTaker = new CareTaker();
    /**
     * Initialise the game with the provided config.
     * @param config The config parser to load the config from.
     * @param scene The scene.
     */
    public Game(ConfigReader config, Scene scene) {
        this.setup(config);
        this.scene = scene;
    }

    private void setup(ConfigReader config) {
        this.table = new PoolTable(config.getConfig().getTableConfig());
        List<BallConfig> ballsConf = config.getConfig().getBallsConfig().getBallConfigs();
        List<PocketsConfig> pocketsConf = config.getConfig().getTableConfig().getPocketsConfig();

        List<Ball> balls = new ArrayList<>();
        List<Pocket> pockets = new ArrayList<>();   

        BallBuilderDirector builder = new BallBuilderDirector();
        builder.registerDefault();
        for (BallConfig ballConf: ballsConf) {
            Ball ball = builder.construct(ballConf);
            if (ball == null) {
                System.err.println("WARNING: Unknown ball, skipping...");
            } else {
                // attach 
                ball.attach(new BallObserver(ball)); // 
                balls.add(ball);
            }
        }

        for (PocketsConfig pc: pocketsConf) {
            Pocket p = new Pocket(pc.getPositionConfig().getX(), pc.getPositionConfig().getY(), pc.getRadius());
            pockets.add(p);
        }

        this.table.setupBalls(balls);
        this.table.setupPockets(pockets);
        
        this.winText.setVisible(false);
        this.winText.setX(table.getDimX() / 2);
        this.winText.setY(table.getDimY() / 2);

        scoreText.setText(String.format("Score: %d", this.score));
        // scoreText.setY(table.getDimY()-10);
        // scoreText.setX(table.getDimX()-100);
        scoreText.setY(40);
        scoreText.setX(60);
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        scoreText.setEffect(dropShadow);
        scoreText.setCache(true);
        scoreText.setFont(Font.font(null, FontWeight.BOLD, 17));
        this.scoreText.setVisible(true);
        this.timerText.setVisible(true);
        timerText.setEffect(dropShadow);

        careTaker.setMemento(saveCurrentState()); // set initial 
    }

    /**
     * Reloads/sets up the config reader with the according path and boolena isResourceDir
     * @param configReader The configReader to reload 
     */
    public void reloadConfig(ConfigReader configReader) {
        setup(configReader);
        setScore(0);
        resetTime();
    }

    /**
     * Returns the private attribute scene which was set in the Game constructor.
     * @return The private attribute scene.
     */
    public Scene getScene() {
        return this.scene;
    }

    /**
     * Get the window dimension in the x-axis
     * @return The x-axis size of the window dimension
     */
    public double getWindowDimX() {
        return this.table.getDimX();
    }

    /**
     * Get the window dimension in the y-axis
     * @return The y-axis size of the window dimension
     */
    public double getWindowDimY() {
        return this.table.getDimY();
    }

    /**
     * Get the pool table associated with the game
     * @return The pool table instance of the game
     */
    public PoolTable getPoolTable() {
        return this.table;
    }

    /** 
     * Add all drawable object to the JavaFX group
     * @param root The JavaFX `Group` instance
    */
    public void addDrawables(Group root) {
        ObservableList<Node> groupChildren = root.getChildren();
        table.addToGroup(groupChildren);

        // background to display time and score.
        Rectangle rectBackground = new Rectangle(55, 0, 85, 50);
        rectBackground.setFill(Color.LIGHTBLUE);
        rectBackground.setOpacity(0.5); 
        groupChildren.add(rectBackground);
    
        groupChildren.add(this.winText);
        groupChildren.add(this.scoreText);
        groupChildren.add(this.timerText);
    }

    /**
     * Reset the game:
     * resets score and time.
     */
    public void reset() {
        this.winText.setVisible(false);
        this.shownWonText = false;
        this.table.reset();
        setScore(0); // reset score
        stopTime = false; // start the time 
        resetTime(); // reset time 
    }

    /** 
     * Check memento if the memento's state and the current Balls are the same 
     * @param memento The PoolTableMemento instance
     * @param currentBalls The pool table's list of balls.
     * @return Returns if the memento's state and the current Balls are the same 
     */
    public boolean checkMemento(PoolTableMemento memento, List<Ball> currentBalls) {
        List<Ball> mementoBalls = memento.getState();
        // List<Ball> currentBalls = currentMemento.getState();

        List<Pair<Double, Double>> listOfCoordsMemento = new ArrayList<>();
        List<Pair<Double, Double>> listOfCoordsCurrent = new ArrayList<>();
        
        for (int i =0; i < mementoBalls.size(); i++) {
            Ball b = mementoBalls.get(i);
            Pair<Double, Double> coord = new Pair<>(b.getXPos(), b.getYPos());
            listOfCoordsMemento.add(coord);
        }

        for (int i =0; i < currentBalls.size(); i++) {
            Ball b = currentBalls.get(i);
            Pair<Double, Double> coord = new Pair<>(b.getXPos(), b.getYPos());
            listOfCoordsCurrent.add(coord);
        }

        boolean value = new HashSet<>(listOfCoordsCurrent).equals(new HashSet<>(listOfCoordsMemento));
        // boolean sametime = memento.getTime() == currentMemento.getTime();
        // boolean sameScore = memento.getScore() == currentMemento.getTime();

        boolean sametime = memento.getTime() == this.time;
        boolean sameScore = memento.getScore() == this.score;
        return !(value && sameScore);
    }

    /** Code to execute every tick. */
    public void tick() { 
        if (table.hasWon() && !this.shownWonText) {
            System.out.println(this.winText.getText());
            this.winText.setVisible(true);
            this.shownWonText = true;
            this.stopTime = true; // stop the time 
        } 
        if (!this.stopTime) {
            renderTime(); // timer 
        }
        table.checkPocket(this);
        table.handleCollision();
        this.table.applyFrictionToBalls();
        for (Ball ball : this.table.getBalls()) {
            ball.move();
        }
        if (checkAllAtRest()) {
            PoolTableMemento p = saveCurrentState();
            // if (checkMemento(careTaker.getPrevMemento(), table.getBalls())) {
                // System.out.println("hi");
                careTaker.setMemento(p);
            // }
        }
    }

    /** 
     * sets up pressing of keyboard keys action that does:
     * Reset the scene to change level 
     * and implement undo function 
     * and removing of balls (Cheating)
     */
    public void setUpKeyboardFunctions() {
        this.scene.setOnKeyPressed(
            k -> {
                if (k.getCode() == KeyCode.E) {
                    System.out.println("E key was pressed, switched to Easy level.");
                    this.table.setLevel(new EasyLevel());
                    this.table.gameLevelFunction(this);
                
                } else if (k.getCode() == KeyCode.N) {
                    System.out.println("N key was pressed, switched to Normal level.");
                    this.table.setLevel(new NormalLevel());
                    this.table.gameLevelFunction(this);

                } else if (k.getCode() == KeyCode.H) {
                    System.out.println("H key was pressed, switched to Hard level.");
                    this.table.setLevel(new HardLevel());
                    this.table.gameLevelFunction(this);
                
                    // undo function
                } else if (k.getCode() == KeyCode.U) {
                    if (careTaker.getPrevMemento()==null) {
                        System.out.println("No previous state saved, saving current state to memento.");
                    }
                    System.out.println("U key was pressed, UNDO."); 

                    PoolTableMemento memento = saveCurrentState();  
                    if (careTaker.getPrevMemento() != null) {
                        setStateFromMemento(careTaker.getPrevMemento()); 
                    }
                    careTaker.setMemento(memento);

                    // cheating 
                } else if (k.getCode() == KeyCode.R) {
                    // remove all red balls 
                    System.out.println("Removing red balls");
                    removeBallsFromColour("red");
                    
                } else if (k.getCode() == KeyCode.Y) {
                    // remove all yellow balls 
                    System.out.println("Removing yellow balls");
                    removeBallsFromColour("yellow");
                    
                } else if (k.getCode() == KeyCode.G) {
                    // remove all green balls 
                    System.out.println("Removing green balls");
                    removeBallsFromColour("green");
                    
                } else if (k.getCode() == KeyCode.W) {
                    // remove all BROWN balls 
                    System.out.println("Removing brown balls");
                    removeBallsFromColour("brown");
                    
                } else if (k.getCode() == KeyCode.B) {
                    // remove all blue balls 
                    System.out.println("Removing blue balls");
                    removeBallsFromColour("blue");

                } else if (k.getCode() == KeyCode.P) {
                    // remove all purple balls 
                    System.out.println("Removing purple balls");
                    removeBallsFromColour("purple");

                } else if (k.getCode() == KeyCode.K) {
                    // remove all BLACK balls 
                    System.out.println("Removing black balls");
                    removeBallsFromColour("black");

                } else if (k.getCode() == KeyCode.O) {
                    // remove all orange balls 
                    System.out.println("Removing orange balls");
                    removeBallsFromColour("orange");
                }

            }
        );
    }


    /**
     * Cheating function. Disables all the balls of the specified colour. 
     * Updates the score as well.
     * @param colour The colour of the ball(s) to remove. 
     */
    public void removeBallsFromColour(String colour) {
        for (int i = 0; i < this.table.getBalls().size(); i++) {
            if (this.table.getBalls().get(i).getColour().equals(colour)) {
                if (this.table.getBalls().get(i).isDisabled()==false)
                    this.table.getBalls().get(i).alert(this); // increment score if havent disabled yet
                this.table.getBalls().get(i).disable();
            }
        }
    }


    /**
     * Check if the balls are at rest
     * @return Return true if all balls are at rest. 
     */
    public boolean checkAllAtRest() {
        int count = 0;
        for (Ball b: this.table.getBalls()) {
            if (b.hasStopped()) {
                count++;
            }
        }
        if (count == this.table.getBalls().size()) {
            return true;
        }
        return false;
    }

    /**
     * Resets the current balls, time and score. 
     @param memento The memento to set the state to. 
     */
    public void setStateFromMemento(PoolTableMemento memento) {
        if (memento != null) { 
            this.time = memento.getTime();
            setScore(memento.getScore());
            for (int i =0; i < this.table.getBalls().size(); i++) {
                this.table.getBalls().get(i).setXVel(0);
                this.table.getBalls().get(i).setYVel(0);
                this.table.getBalls().get(i).setXPos(memento.getState().get(i).getXPos());
                this.table.getBalls().get(i).setYPos(memento.getState().get(i).getYPos());
                this.table.getBalls().get(i).setFallCounter(memento.getState().get(i).getFallCounter());

                if (!memento.getState().get(i).isDisabled() == true) {
                    this.table.getBalls().get(i).enable();
                    this.table.getBalls().get(i).setDisabled(false);
                }
            }

        }
    }


    /**
     * @return Saves and returns current PoolTableMemento. 
     */
    public PoolTableMemento saveCurrentState() {
        List<Ball> newState = new ArrayList<>();
        for (Ball ball: this.table.getBalls()) {
            Ball newBall = ball.clone();
            newBall.attach(new BallObserver(newBall));
            newState.add(newBall);
        }
        return new PoolTableMemento(newState, this.time, this.score);
    }


    /** 
     * In the ball observer class, when ball falls, get score here.
     * @return Returns the current game score.
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Sets the current score and reset the Score Text. 
     * @param score The Game's curent score;
     */
    public void setScore(int score) {
        this.score = score;
        scoreText.setText(String.format("Score: %d", score));
    }

    /**
     * Renders the timer.
     * If seconds reach 60 seconds, increase seconds and reset seconds to 0.
     */
    public void renderTime() {
        this.time+=1; // milliseconds
        int seconds = (this.time/60) % 60;
        int minutes = (this.time/3600) % 3600;

        // if seconds is not 2 digits, print 0 infront, when it reaches 2 digits, make 0 disappear
        String s = ((seconds <  10) ? "0" : ""); 
        this.timerText.setText(String.format("Time: %d:%s%d", minutes, s, seconds));
    }

    /**
     * Sets the time/milliseconds to 0.
     */
    public void resetTime() {
        this.time = 0;
        this.stopTime = false;
    }


}
