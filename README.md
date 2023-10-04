# Roll And Pocket

**Note 1:** The friction value in the config file is used as a multiplier in the
implementation. As the friction approaches 0, the friction decreases. As friction
approach 1, the friction increases. A high value of friction will make it 
impossible for a ball to move. Range of valid friction is: `0 < friction < 1`.

**Note 2:** While the forces applied to the cue ball is variable based on the
length of the line shown when dragging from the cue ball, there is a maximum cap
on the force.

**Note 3:** The center of the ball needs to be in the pocket for the code to 
consider it as "in the pocket" instead of its rectangular bound just intersecting
with the pocket's rectangular bound.

## Commands

* Run: `gradle run` to load default config from resources folder or 
`gradle run --args="'insert_config_file_path'"` to load custom config.

* Generate documentation: `gradle javadoc`

## Instructions 

### Start Game
To hit the cue ball, press on the white cue ball and a violet-red/dark pink circle will appear. 
Press and Hold that handle/circle and drag to any distance anywhere and release to hit the cue ball.

### Selecting difficulty level (click on keyboard keys):
Press the key 'E' to switch to Easy level.
Press the key 'N' to switch to Normal level.
Press the key 'H' to switch to Hard level.

###  Undo (click on keyboard keys):
Press the key "U" to undo.


### Cheat (by clicking on keyboard keys):
Press keyboard keys:

    "R" -> to remove all Red balls
    "Y" -> to remove all Yellow balls
    "G" -> to remove all Green balls
    "W" -> to remove all Brown balls
    "B" -> to remove all Blue balls
    "P" -> to remove all Purple balls
    "K" -> to remove all Black balls
    "O" -> to remove all Orange balls 

## Features 

-  Implemented a visible player-controlled cue stick which can hit the cue ball with variable velocity.

- Difficulty levels:
    -> press keyboard keys "E" to easy level, "N" to normal level, and "H" to hard level.
    -> default level is easy.

-  Time and Score

    - In my implementation, throughout the game, the duration of game is clocked. When the game wins, the time will pause. And when the game loses, the time will restart from 0:00. The game displays the time on the screen a continually updating time (initially at 0:00).

    - The score is calculated when a ball falls into a pocket. The game must display on the screen an updating score (initially at 0) when a ball falls into a pocket during the level. 
    Increment scores accordingly from the colour of the ball (when it falls into pocket).

    - Score system:
    red-> 1, yellow-> 2, green-> 3, brown -> 4, blue-> 5, purple -> 6, black -> 7, orange-> 8

- Undo and Cheat featuere
    - Player can reset the game to an earlier state (including score, time, ball positions) 
    so that a shot can be undo through pressing a keyboard key 'U' to undo a shot. 
    This must be a single state that is not written to disk, and the state reaching by the subsequent 
    undo function overwrites the existing saved state.

    - Player can cheat by pressing on the following keys:
        "R" -> to remove Red balls
        "Y" -> to remove Yellow balls
        "G" -> to remove Green balls
        "W" -> to remove Brown balls
        "B" -> to remove Blue balls
        "P" -> to remove Purple balls
        "K" -> to remove Black balls
        "O" -> to remove Orange balls   
    where this action immediately remove all the balls of a single colour at the cheating moment and add corresponding scores. 

- Javadoc is under the folder: build/docs/javadoc. 

## Explanation of Code & Design Patterns

- Implemented Difficulty level using Strategy design pattern.
    - Corresponding classes:
        - Under LevelStrategy folder/package: PoolGame.LevelStrategy
        - Classes: Level.java (interface in which the concrete classes implements to)
            EasyLevel.java (concrete class)
            NormalLevel.java (concrete class)
            HardLevel.java (concrete class)

- Implemented Updating of scores using Observer pattern.
corresponding classes:
    - Under ObserverPattern folder/package: PoolGame.ObserverPattern
    - Classes: BallObserver.java (BallObserver that implements Observer)
    Observer.java (Observer) (interface)
    Subject.java (Subject) (interface)

- Implemented the undo functionality using Memento pattern.
    - Under the Memento folder/package: PoolGame.Memento 
    - Classes: CareTaker.java (CareTaker)
    PoolTableMemento.java (Memento)


