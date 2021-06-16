package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int MULTIPLIER = 2;
    private int size;
    private Node<K,V>[] table;
    private int threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);

    public MyHashMap() {
        this.table = new Node[INITIAL_CAPACITY];
    }

    public static class Node<K,V> {
        private final int hashCode;
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(int hashCode, K key, V value, Node<K, V> next) {
            this.hashCode = hashCode;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        int keyIndex = getPlace(key);
        Node<K,V> currentNode = table[keyIndex];
        if (currentNode == null) {
            table[keyIndex] = new Node<>(getPlace(key), key, value, null);
            if (++size > threshold) {
                resize();
            }
            return;
        }
        do {
            if (key == currentNode.key || key != null && key.equals(currentNode.key)) {
                currentNode.value = value;
                return;
            }
            currentNode = currentNode.next;
        } while (currentNode != null);
        currentNode = table[keyIndex]; // обнуление до ячейки
        if (keyIndex == 0 && key == null) {
            while (currentNode.next != null) {
                currentNode = currentNode.next;
            }
            currentNode.next = new Node<>(getPlace(key), key, value, null);
            if (++size > threshold) {
                resize();
            }
            return;
        }
        currentNode = table[keyIndex]; // обнуление до ячейки

        do {
            if (getPlace(currentNode.hashCode) == keyIndex) {
                if (currentNode.next == null) {
                    currentNode.next = new Node<>(key.hashCode(), key, value, null);
                    if (++size > threshold) {
                        resize();
                    }
                    return;
                }
            }
            currentNode = currentNode.next;
        } while (currentNode != null);
    }

    @Override
    public V getValue(K key) {
        for (Node<K, V> bucket : table) {
            if (bucket != null) {
                Node<K, V> currentNode = bucket;
                while (currentNode != null) {
                    if (key == currentNode.key || currentNode.key != null
                            && currentNode.key.equals(key)) {
                        return currentNode.value;
                    }
                    currentNode = currentNode.next;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getPlace(Object key) {
        return (key == null) ? 0 : Math.abs((key.hashCode() % table.length));
    }

    private void resize() {
        Node<K,V>[] oldTable = table;
        table = new Node[oldTable.length * MULTIPLIER];
        size = 0;
        threshold = threshold * MULTIPLIER;
        for (Node<K, V> bucket : oldTable) {
            if (bucket != null) {
                Node<K, V> currentNode = bucket;
                while (currentNode != null) {
                    put(currentNode.key, currentNode.value);
                    currentNode = currentNode.next;
                }
            }
        }
    }
}
