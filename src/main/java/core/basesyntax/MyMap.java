package core.basesyntax;

public interface MyMap<K, V> {
    int DEFAULT_INITIAL_CAPACITY = 16;
    float DEFAULT_LOAD_FACTOR = 0.75f;

    void put(K key, V value);

    V getValue(K key);

    int getSize();
}
