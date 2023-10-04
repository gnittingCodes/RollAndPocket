package PoolGame.Config;

import org.json.simple.JSONObject;
import java.util.*;

import org.json.simple.JSONArray;

/** A config class for the table configuration */
public class TableConfig implements Configurable {
    private String colour;
    private double friction;
    private SizeConfig size;
    private List<PocketsConfig> pockets;

    /**
     * Initialise a config for table using a JSONObject
     * @param obj A JSONObject containing required keys for table
     */
    public TableConfig(Object obj) {
        this.parseJSON(obj);
    }

    /**
     * Initialise a config for velocity using the provided values
     * @param colour The colour of the table as String
     * @param friction The friction of the table
     * @param sizeConf A size config instance for the size of the table
     */
    public TableConfig(String colour, double friction, SizeConfig sizeConf) {
        this.init(colour, friction, sizeConf);
    }

    private void init(String colour, double friction, SizeConfig sizeConf) {
        if (!ConfigChecker.colourChecker(colour)) {
            throw new IllegalArgumentException(String.format("\"%s\" is not a valid colour.", colour));
        } else if (friction <= 0) {
            throw new IllegalArgumentException("Table friction must be at least 0.");
        } else if (friction >= 1) {
            throw new IllegalArgumentException("Table friction must be smaller than 1.");
        }
        this.colour = colour;
        this.friction = friction;
        this.size = sizeConf;
    }

    public Configurable parseJSON(Object obj) {
        JSONObject json = (JSONObject) obj;
        String colour = (String)json.get("colour");
        double friction = (double)json.get("friction");
        SizeConfig szConf = new SizeConfig(json.get("size"));
        // pockets 
        parseJSONPockets(json.get("pockets"));
        this.init(colour, friction, szConf);
        return this;
    }

    /** 
     * parse json for pockets under table section 
     * @param obj The object to be casted to JSONArray. 
    */
    private void parseJSONPockets(Object obj) {
        List<PocketsConfig> list = new ArrayList<>();
        JSONArray json = (JSONArray) obj;
        for (Object b : json) {
            list.add(new PocketsConfig(b));
        }
        this.pockets = list;
    }

    /**
     * Get the colour of the table as defined in the config.
     * @return The colour of the table
     */
    public String getColour() {
        return this.colour;
    }

    /**
     * Get the friction of the table as defined in the config.
     * @return The friction of the table
     */
    public double getFriction() {
        return this.friction;
    }

    /**
     * Get the table size config instance.
     * @return The table size config instance
     */
    public SizeConfig getSizeConfig() {
        return this.size;
    }

    /** 
     * Get the list of PocketsConfig.
     * @return The list of PocketsConfig
     */
    public List<PocketsConfig> getPocketsConfig() {
        return this.pockets;
    }
}
