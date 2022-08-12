package com.Game;

import java.util.Hashtable;

public class BaseClass {
    public Hashtable<String, Object> references;

    public BaseClass() {
        references = new Hashtable<String, Object>();
    }

    public void pushReference(String key, Object value) {
        references.put(key, value);
    }

    public <T> T getReference(String key) {
        try {
            return (T) references.get(key);
        } catch (ClassCastException e) {
            System.err.println("Reference " + key + " has not been cast to the correct type.");
            e.printStackTrace();
            return null;
        }
    }

    public boolean hasReference(String key) {
        return references.containsKey(key);
    }
}
