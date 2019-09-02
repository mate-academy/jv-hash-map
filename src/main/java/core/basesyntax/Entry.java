package core.basesyntax;

public class Entry<K, V> {
    private K key;
    private V value;
    private Entry<K, V> next;

    public void setNext(Entry<K, V> next) {
        this.next = next;
    }

    public Entry() {
        this.key = null;
        this.value = null;
        this.next = null;
    }

    public Entry(K key, V value, Entry<K, V> next) {
        this.key = key;
        this.value = value;
        this.next = next;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public Entry<K, V> getNext() {
        return next;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public void setValue(V value) {
        this.value = value;
    }


}
