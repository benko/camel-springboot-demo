package com.redhat.training.camel.bookstore.registry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Registry<K,V> {
    private final Logger LOG = LoggerFactory.getLogger(Registry.class.getName());

    protected Map<K,V> registry = new HashMap<>();

    public Collection<V> getItems() {
        return registry.values();
    }

    // throw VNotFoundException or return V
    public abstract V get(K key);

    // extract key from V and use add(K, V)
    public abstract void simpleAdd(V val);

    public void add(K key, V item) {
        LOG.info("Adding " + item.getClass().getSimpleName() + " to registry.");
        if (this.has(key)) {
            throw new IllegalArgumentException("Key already exists in registry: " + key.toString());
        }
        registry.put(key, item);
    }

    public void remove(K key) {
        registry.remove(key);
    }

    public boolean has(K key) {
        return registry.containsKey(key);
    }
}
