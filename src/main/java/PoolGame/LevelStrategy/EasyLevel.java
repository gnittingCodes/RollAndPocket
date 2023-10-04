package PoolGame.LevelStrategy;
import java.io.FileNotFoundException;
import PoolGame.Game;

import javafx.scene.Group;
import PoolGame.ConfigReader;
import PoolGame.ConfigReader.ConfigKeyMissingException;
import org.json.simple.parser.ParseException;
import java.io.IOException;

/** Easy Level subclass strategy */
public class EasyLevel implements Level {
    private Group group = new Group();
    private ConfigReader configReader;
    private final String path = "/config_easy.json";
    
    /** the constructor have a new config reader that reads a new path */
    public EasyLevel() {
        try {
            configReader = new ConfigReader(path, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.printf("ERROR: %s\n", e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.printf("ERROR: %s\n", e.getMessage());
            System.exit(1);
        } catch (ParseException e) {
            e.printStackTrace();
            System.err.printf("ERROR: %s\n", e.getMessage());
            System.exit(1);
        } catch (ConfigKeyMissingException e) {
            e.printStackTrace();
            System.err.printf("ERROR: %s\n", e.getMessage());
            System.exit(1);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            System.err.printf("ERROR: %s\n", e.getMessage());
            System.exit(1);
        }
    }

    /**
     * read the path: /config_easy.json
     * @param game The game instance to call the following methods.
     */
    public void setLevelStrategy(Game game) {
        group.getChildren().clear();
        game.reloadConfig(configReader);
        game.addDrawables(group);
        game.getScene().setRoot(group);
    }
        
}