package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] buckets;
    private int count;

    public MyHashMap() {
        buckets = new Node[DEFAULT_CAPACITY];
        count = 0;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        Node<K, V> newNode = new Node<>(key, value, null);

        if (buckets[index] == null) {
            buckets[index] = newNode;
        } else {
            Node<K, V> current = buckets[index];
            while (current != null) {
                // Додаємо перевірку на null перед викликом equals()
                if ((current.key == null && key == null)
                        ||
                        (current.key != null && current.key.equals(key))) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    current.next = newNode;
                    break;
                }
                current = current.next;
            }
        }
        count++;

        if ((float) count / buckets.length > LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> current = buckets[index];

        while (current != null) {
            // Додаємо перевірку на null перед викликом equals()
            if ((current.key == null && key == null)
                    || (current.key != null && current.key.equals(key))) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return count;
    }

    private void resize() {
        Node<K, V>[] oldBuckets = buckets;
        buckets = new Node[oldBuckets.length * 2];

        count = 0; // Скидаємо лічильник перед перезаписом вузлів

        for (Node<K, V> node : oldBuckets) {
            while (node != null) {
                int newIndex = getIndex(node.key);
                Node<K, V> nextNode = node.next;
                node.next = buckets[newIndex];
                buckets[newIndex] = node;
                node = nextNode;
                count++; // Підраховуємо кількість елементів заново
            }
        }
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % buckets.length);
    }
}

