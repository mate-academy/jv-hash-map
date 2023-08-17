package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size = 0;

    public MyHashMap() {
        this.table = (Node<K,V>[]) new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = getElementPosition(key);
        Node<K, V> currentElement = table[index];

        if (currentElement == null) {
            Node<K, V> newNode = new Node<>(key, value);
            table[index] = newNode;
            size++;
        } else {
            while (true) {
                if (currentElement.key == key
                        || (currentElement.key != null && currentElement.key.equals(key))) {
                    currentElement.value = value;
                    break;
                }
                if (currentElement.next == null) {
                    currentElement.next = new Node<>(key, value);
                    size++;
                    break;
                }
                currentElement = currentElement.next;
            }
        }

        if (size > (table.length * LOAD_FACTOR)) {
            increaseArray();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getElementPosition(key);
        Node<K, V> currentElement = table[index];
        while (currentElement != null) {
            if (currentElement.key == key
                    || (currentElement.key != null && currentElement.key.equals(key))) {
                return currentElement.value;
            }

            currentElement = currentElement.next;
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getElementPosition(K element) {
        return element == null ? 0 : Math.abs(element.hashCode() % table.length);
    }

    private void increaseArray() {
        Node<K, V>[] oldTab = table;
        table = (Node<K,V>[])new Node[oldTab.length * 2];

        for (int j = 0; j < oldTab.length; j++) {
            Node<K, V> oldCurElem = oldTab[j];

            while (oldCurElem != null) {
                int newIndex = getElementPosition(oldCurElem.key);
                Node<K, V> currentElement = table[newIndex];

                if (currentElement == null) {
                    table[newIndex] = new Node<>(oldCurElem.key, oldCurElem.value);
                } else {
                    while (true) {
                        if (currentElement.next == null) {
                            currentElement.next = new Node<>(oldCurElem.key, oldCurElem.value);
                            break;
                        }
                        currentElement = currentElement.next;
                    }
                }

                oldCurElem = oldCurElem.next;
            }
        }
    }

    public static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
