package src;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Abstract class representing a class that can be serialized towards a json
 * Author: Francesco Nieri
 * Date: 17/02/2023
 */

public interface Serializable {

    JSONObject asJson() throws JSONException;
}
