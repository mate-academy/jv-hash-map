package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 1 << 4;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
        threshold = (int) (LOAD_FACTOR * INITIAL_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        int hash = key == null ? 0 : key.hashCode();
        Node<K, V> element = table[getIndex(key)];
        if (element == null) {
            table[getIndex(key)] = new Node<>(hash, key, value, null);
        } else {
            do {
                if (hash == element.hash && (key == element.key
                        || (key != null && key.equals(element.key)))) {
                    element.value = value;
                    return;
                } else if (element.next == null) {
                    element.next = new Node<>(hash, key, value, null);
                    element = null;
                } else {
                    element = element.next;
                }
            } while (element != null);
        }
        if (++size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int hash = key == null ? 0 : key.hashCode();
        Node<K, V> element = table[getIndex(key)];
        while (element != null) {
            if (element.hash == hash && (element.key == key
                    || (key != null && key.equals(element.key)))) {
                return element.value;
            }
            element = element.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int oldCapacity = table.length;
        int newCapacity = oldCapacity << 1;
        transfer(oldCapacity, newCapacity);
        threshold = threshold << 1;
    }

    private void transfer(int oldCap, int newCap) {
        Node<K, V>[] newTab = new Node[newCap];
        Node<K, V> elementOldTab;
        Node<K, V> next;
        for (int i = 0; i < oldCap; i++) {
            if ((elementOldTab = table[i]) != null) {
                do {
                    next = elementOldTab.next;
                    elementOldTab.next = null;
                    if (newTab[elementOldTab.hash & (newCap - 1)] != null) {
                        Node<K, V> elementNewTab = newTab[elementOldTab.hash & (newCap - 1)];
                        while (elementNewTab.next != null) {
                            elementNewTab = elementNewTab.next;
                        }
                        elementNewTab.next = elementOldTab;
                    } else {
                        newTab[elementOldTab.hash & (newCap - 1)] = elementOldTab;
                    }
                    elementOldTab = next;
                } while (next != null);
            }
        }
        table = newTab;
    }

    private int getIndex(K key) {
        return key == null ? 0 : key.hashCode() & (table.length - 1);
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
