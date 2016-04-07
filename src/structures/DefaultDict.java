package structures;

import java.util.HashMap;
import java.util.function.Function;

/**
 * Default Dictionary Data Structure - loosely based on Python's defaultdict object.
 *
 * Created by Sidd Karamcheti on 3/7/16.
 */
public class DefaultDict<K, V> extends HashMap<K, V> {
    protected final V defaultValue;
    protected final Class<V> defaultClass;
    protected final Function<K,V> defaultFunction;


    /**
     * Instantiate a DefaultDict with a given Value.
     *
     * @param defaultValue Default Value to populate DefaultDict with.
     */
    public DefaultDict(V defaultValue) {
        this.defaultValue = defaultValue;
        this.defaultClass = null;
        this.defaultFunction = null;
    }

    /**
     * Instantiate a DefaultDict with a given Class.
     *
     * @param defaultClass Default Class to populate DefaultDict with.
     */
    public DefaultDict(Class<V> defaultClass) {
        this.defaultClass = defaultClass;
        this.defaultValue = null;
        this.defaultFunction = null;
    }

    /**
     * Instantiate a DefaultDict with a given Function.
     *
     * @param defaultFunction Default Function to populate DefaultDict with.
     */
    public DefaultDict(Function<K,V> defaultFunction) {
        this.defaultFunction = defaultFunction;
        this.defaultClass = null;
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
//                System.out.println("Defaulted");
                returnValue = this.defaultValue;
            }
            else if(this.defaultFunction != null){
//                System.out.println("Defaulted");
                returnValue = this.defaultFunction.apply((K)  key);
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
