package structures;

import java.util.HashMap;

/**
 * Default Dictionary Data Structure - loosely based on Python's defaultdict object.
 *
 * Created by Sidd Karamcheti on 3/7/16.
 */
public class DefaultDict<K, V> extends HashMap<K, V> {
    V defaultValue;
    Class<V> defaultClass;

    /**
     * Instantiate a DefaultDict with a given Value.
     *
     * @param defaultValue Default Value to populate DefaultDict with.
     */
    public DefaultDict(V defaultValue) {
        this.defaultValue = defaultValue;
        this.defaultClass = null;
    }

    /**
     * Instantiate a DefaultDict with a given Class.
     *
     * @param defaultClass Default Class to populate DefaultDict with.
     */
    public DefaultDict(Class<V> defaultClass) {
        this.defaultClass = defaultClass;
        this.defaultValue = null;
    }

    /**
     * Override default HashMap get method.
     *
     * @param key HashMap key.
     */
    @Override
    public V get(Object key) {
        V returnValue = super.get(key);
        if (returnValue == null) {
            if (this.defaultValue != null) {
                returnValue = this.defaultValue;
            }
            else {
                try {
                    returnValue = this.defaultClass.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            this.put((K) key, returnValue);
        }
        return returnValue;
    }
}
