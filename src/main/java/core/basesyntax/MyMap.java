package core.basesyntax;

public interface MyMap<K, V> {
    void put(K key, V value);

    int hashCode(Object key);

    V getValue(K key);

    int getSize();
}
