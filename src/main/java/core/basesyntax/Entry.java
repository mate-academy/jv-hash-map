package core.basesyntax;

import java.util.Objects;

public class Entry<K,V> {
    private K key;
    private V value;

    public Entry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return this.key;
    }

    public V getValue() {
        return this.value;
    }

    public Entry<K, V> setKey(K key) {
        this.key = key;
        return this;
    }

    public Entry<K, V> setValue(V value) {
        this.value = value;
        return this;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Entry<?, ?> entry = (Entry<?, ?>) object;
        return Objects.equals(key, entry.key) && Objects.equals(value, entry.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }
}
