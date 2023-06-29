package core.basesyntax;

public interface MyMap<K, V> {
    void put(K key, V value);

    V getValue(K key);

    boolean isEmpty();

    int getSize();
}
