package PoolGame.Factory;

import PoolGame.Config.Configurable;
import PoolGame.Config.TableConfig;
import org.json.simple.JSONObject;

/** A config factory that will handle the `table` section of the config */
public class TableConfigFactory implements ConfigFactory {
    public Configurable make(Object jSONObject) {
        return new TableConfig(jSONObject);
    }
}
