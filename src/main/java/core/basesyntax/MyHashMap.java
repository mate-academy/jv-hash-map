package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int entriesCount = 0;
    private Node<K, V> nullBucket = new Node(null, null, null, null);
    private Node[] buckets = new Node[INITIAL_CAPACITY];

    @Override
    public void put(K key, V value) {
        if (nullKeyCheck(key)) {
            if (nullBucket.getValue() == null && value != null) {
                entriesCount++;
            }
            nullBucket.setValue(value);
            return;
        }
        int index = Math.abs(key.hashCode() % buckets.length);
        if (collisionCheck(index)) {
            Node<K, V> search = buckets[index];
            if (search.getKey().equals(key)) {
                search.setValue(value);
                return;
            }
            while (search.hasNext()) {
                if (search.getKey().equals(key)) {
                    search.setValue(value);
                    return;
                }
                search = search.next;
            }
            search.setNext(new Node<K, V>(key, value, search, null));
            entriesCount++;
        } else {
            buckets[index] = new Node<K, V>(key, value, null, null);
            entriesCount++;
        }
    }

    @Override
    public V getValue(K key) {
        if (nullKeyCheck(key)) {
            return nullBucket.getValue();
        }
        int index = Math.abs(key.hashCode() % buckets.length);
        if (collisionCheck(index)) {
            Node<K, V> search = buckets[index];
            if (search.getKey().equals(key)) {
                return search.getValue();
            }
            while (search.hasNext()) {
                if (search.getNext().getKey().equals(key)) {
                    return (V) search.getNext().getValue();
                }
                search = search.getNext();
            }
            System.out.println("Invalid key input");
            return null;
        }
        return null;
    }

    @Override
    public int getSize() {
        return entriesCount;
    }

    private boolean collisionCheck(int index) {
        return buckets[index] != null;
    }

    private boolean nullKeyCheck(K key) {
        return key == null;
    }

    private void resize() {
        if (entriesCount / buckets.length >= LOAD_FACTOR) {
            Node[] temp = new Node[buckets.length];
            for (int i = 0; i < buckets.length; i++) {
                if (collisionCheck(i)) {
                    temp[i] = buckets[i];
                }
            }
            buckets = new Node[buckets.length * 2];
            for (int i = 0; i < buckets.length; i++) {
                if (collisionCheck(i)) {
                    this.put((K) temp[i].getKey(), (V) temp[i].getValue());
                    Node<K, V> search = temp[i];
                    while (search.hasNext()) {
                        this.put((K) search.getNext().getKey(), (V) search.getNext().getValue());
                    }
                }
            }

        }
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> previous;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> previous, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.previous = previous;
            this.next = next;
        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        private Node<K, V> getPrevious() {
            return this.previous;
        }

        private void setPrevious(Node<K, V> previous) {
            this.previous = previous;
        }

        private Node<K, V> getNext() {
            return this.next;
        }

        private void setNext(Node<K, V> next) {
            this.next = next;
        }

        private boolean hasNext() {
            return this.next != null;
        }

        private boolean hasPrevious() {
            return this.previous != null;
        }

    }
}
