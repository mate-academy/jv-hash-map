package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int index = findIndexByKey(key);
        Node<K, V> node = table[index];
        if (node == null) {   //если в table нет нод, кладём новую ноду и завершаем метод
            table[index] = new Node<>(key, value, null);
            size++;
            return;
        } else if (key == node.key
                || (key != null && key.equals(node.key))) { //если в table уже была нода
            table[index].value = value; //и ее ключ равен введённому ключу, то просто
            return; // сеттим новое значение и также завершаем метод
        }
        while (node.next != null) { //проходимся по следущим нодам в связном списке;
            if (key == node.next.key
                    || (key != null && key.equals(node.next.key))) { //если у одной из них ключ
                node.next.value = value; //совпадает с введённым,
                break; //сеттим новое значение и выходим из цикла
            }
            node = node.next;
        }
        if (node.next == null) { //если в цикле мы ни разу не зашли в блок if (т. е. в списке
            node.next = new Node<>(key, value, null); //не оказалось совпадающих ключей),
            size++; //кладём ноду в конец списка.
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[findIndexByKey(key)];
        while (node != null) {
            if (key == node.key || (key != null && key.equals(node.key))) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] oldTab = table;
        int newCap = table.length << 1;
        table = new Node[newCap];
        size = 0;
        transfer(oldTab);
        threshold <<= 1;
    }

    private void transfer(Node<K, V>[] oldTab) {
        for (Node<K, V> node : oldTab) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int findIndexByKey(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
