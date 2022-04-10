package core.basesyntax;

public interface MyMap<K, V> {
    void put(K key, V value);

    V getV(K key);

    int getSize();
}
