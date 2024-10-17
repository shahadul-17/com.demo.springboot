package com.demo.springboot.core;

import java.util.HashMap;
import java.util.Map;

public final class MapBuilder<KeyType, ValueType> {

    private boolean built = false;
    private final Map<KeyType, ValueType> map = new HashMap<>();

    private MapBuilder() { }

    public MapBuilder<KeyType, ValueType> put(final KeyType key, final ValueType value) {
        if (built) { throw new RuntimeException("Cannot put key-value pair on an already built map."); }

        map.put(key, value);

        return this;
    }

    public MapBuilder<KeyType, ValueType> put(final Map<? extends KeyType, ? extends ValueType> map) {
        if (built) { throw new RuntimeException("Cannot put key-value pairs on an already built map."); }

        this.map.putAll(map);

        return this;
    }

    public MapBuilder<KeyType, ValueType> remove(final KeyType key) {
        if (built) { throw new RuntimeException("Cannot put key-value pair on an already built map."); }

        map.remove(key);

        return this;
    }

    public Map<KeyType, ValueType> build() {
        built = true;

        return map;
    }

    public static <KeyType, ValueType> MapBuilder<KeyType, ValueType> create(
            final Class<KeyType> classOfKeyType,
            final Class<ValueType> classOfValueType) {
        return new MapBuilder<>();
    }
}
